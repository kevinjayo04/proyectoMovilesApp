package com.example.protrack.screens


import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FiveScreen(navController: NavController) {
    //variables de estado
    val selectedDate = remember { mutableStateOf("") }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val hourlyRate = remember { mutableStateOf("") }
    val totalEarnings = remember { mutableStateOf("Sin calcular") }
    val firebaseData = remember { mutableStateOf("Sin datos") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffc8c8af))
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(color = Color(0xffd9d9d9))
        ) {
            Text(
                text = "CALENDARIO",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido Principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = 100.dp,
                        start = 16.dp,
                        end = 16.dp)
        ) {
            // Calendario
            AndroidView(
                factory = { CalendarView(context) }, //Crea una vista del calendario
                modifier = Modifier.fillMaxWidth(), // modificador para ocupar todo el espacio
                update = { calendarView -> //Comportamiento de actualizacion en el calendario
                    calendarView.setOnDateChangeListener { _, year, month, day -> //Cambio de fecha
                        val selected = "$day/${month + 1}/$year" //actualiza la fecha selecciona
                        selectedDate.value = selected //se guarda la fecha seleccionada en una variable reactiva

                        // Consultar Firestore
                        FirebaseFirestore.getInstance()
                            .collection("trabajo") //Accedo a la coleccion "Trabajo" en firebase
                            .whereEqualTo("Fecha", selected) //filtro por fecha
                            .get() //realizo la consulta
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    val data = documents.first().data //obtengo datos
                                    startTime.value = data["HoraInicio"].toString() //Asigna los datos obtenido al valor reactivo
                                    endTime.value = data["HoraFin"].toString()
                                    hourlyRate.value = data["SalarioPorHora"].toString()

                                    // Calcular el total ganado
                                    val startHour = startTime.value.split(":")[0].toInt()
                                    val endHour = endTime.value.split(":")[0].toInt()
                                    val hoursWorked = endHour - startHour //calculo las horas trabaja
                                    val earnings = hoursWorked * hourlyRate.value.toInt() //ganacicas

                                    totalEarnings.value = "$earnings €" //asigno el valor obtenido
                                    firebaseData.value = "Datos cargados correctamente"
                                } else {
                                    //si no hay datos
                                    firebaseData.value = "No hay datos para esta fecha"
                                    startTime.value = ""
                                    endTime.value = ""
                                    hourlyRate.value = ""
                                    totalEarnings.value = "Sin calcular"
                                }
                            }
                            //mensaje de error
                            .addOnFailureListener {
                                firebaseData.value = "Error al cargar los datos"
                            }
                    }
                }
            )

            // Mostrar datos
            Text("Fecha seleccionada: ${selectedDate.value}")
            Text("Hora de inicio: ${startTime.value}")
            Text("Hora de fin: ${endTime.value}")
            Text("Salario por hora: ${hourlyRate.value} €")
            Text("Total ganado: ${totalEarnings.value}")

            // Mensaje de estado
            Text(
                text = firebaseData.value,//mostrar mensaje de estado
                color = if (firebaseData.value.contains("Error")) Color.Red else Color.Black, //mostrar mensaje en rojo de error
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}