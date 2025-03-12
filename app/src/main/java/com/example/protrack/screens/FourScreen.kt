package com.example.protrack.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.protrack.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FourScreen(navController: NavController) {
    // Variables de estado para las selecciones
    val selectedDate = remember { mutableStateOf("Sin seleccionar") }
    val startTime = remember { mutableStateOf("Sin seleccionar") }
    val endTime = remember { mutableStateOf("Sin seleccionar") }
    val hourlyRate = remember { mutableStateOf("") } // Salario por hora
    val context = LocalContext.current

    //Variables para los mensajes
    val mensajeAlerta = remember { mutableStateOf(false) }
    val valido = remember { mutableStateOf(false) }
    val fallido = remember { mutableStateOf(false) }

    //Intancia de firebase
    val firestore = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffc8c8af))
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .background(color = Color(0xffd9d9d9))
        ) {
            Text(
                text = "INSERTAR DATOS",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido Principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp)
        ) {
            // Botón para Selección de Día
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Seleccione día: ${selectedDate.value}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = {
                    showDatePickerDialog(context) { date ->
                        selectedDate.value = date
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_calend),
                        contentDescription = "Seleccionar día",
                        tint = Color(0xff49454f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Botón para Selección de Hora de Inicio
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Hora de inicio: ${startTime.value}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = {
                    showTimePickerDialog(context) { time ->
                        startTime.value = time
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_clock),
                        contentDescription = "Seleccionar hora de inicio",
                        tint = Color(0xff49454f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Boton para Seleccion de Hora de Fin
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Hora de fin: ${endTime.value}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = {
                    showTimePickerDialog(context) { time ->
                        endTime.value = time
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_clock),
                        contentDescription = "Seleccionar hora de fin",
                        tint = Color(0xff49454f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Entrada de Salario por Hora
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Salario por hora (€):",
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = hourlyRate.value,
                    onValueChange = { value ->
                        // Permitir solo números en el campo
                        if (value.all { it.isDigit() }) {
                            hourlyRate.value = value
                        }
                    },
                    label = { Text("€ / hora") },
                    placeholder = { Text("Introduce un número") },
                    modifier = Modifier.width(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(170.dp))


            // Botones Limpiar y Guardar en línea
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón Limpiar
                TextButton(
                    onClick = {
                        selectedDate.value = "Sin seleccionar"
                        startTime.value = "Sin seleccionar"
                        endTime.value = "Sin seleccionar"
                        hourlyRate.value = ""
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = Color(0xffe6e0e9))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Limpiar", color = Color(0xff65558f))
                }

                // Botón Guardar
                TextButton(
                    onClick = {
                        //Validar todos loa campos que no esten vacios 
                        if (selectedDate.value == "Sin seleccionar" ||
                            startTime.value == "Sin seleccionar" ||
                            endTime.value == "Sin seleccionar" ||
                            hourlyRate.value.isEmpty()
                            ){
                                mensajeAlerta.value = true

                        } else {
                            //Guardar en Fireabase los datos
                            val data = mapOf(
                                "Fecha" to selectedDate.value,
                                "HoraInicio" to startTime.value,
                                "HoraFin" to endTime.value,
                                "SalarioPorHora" to hourlyRate.value

                            )
                            firestore.collection("trabajo")
                                .add(data)
                                .addOnSuccessListener {
                                    valido.value = true
                                }
                                .addOnFailureListener {
                                    fallido.value = true
                                }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = Color(0xffe6e0e9))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Guardar", color = Color(0xff65558f))
                }
            }

            //mensaje para campos vacios por rellenar
            if (mensajeAlerta.value){
                AlertDialog(
                    onDismissRequest = {mensajeAlerta.value = false},
                    title = { Text(text = "Alerta") },
                    text = { Text(text = "Por favor rellene todos los campos.") },
                    confirmButton = {
                        TextButton(onClick = {mensajeAlerta.value = false}) {
                            Text("OK")
                        }
                    }
                )
            }

            //mensaje de confirmacion de guardado en firebase
            if (valido.value) {
                AlertDialog(
                    onDismissRequest = { valido.value = false },
                    title = { Text(text = "Exito") },
                    text = { Text(text = "Datos guardados correctamente.") },
                    confirmButton = {
                        TextButton(onClick = { valido.value = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            //mensaje si falla al guardar en firebase
            if (fallido.value) {
                AlertDialog(
                    onDismissRequest = { fallido.value = false },
                    title = { Text(text = "Error") },
                    text = { Text(text = "Error al guardar los datos.") },
                    confirmButton = {
                        TextButton(onClick = { fallido.value = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

// Funcion para mostrar el Calendario
fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
    //Obtengo una intancia del calendario con la fecha actual
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR) //Año actual
    val month = calendar.get(Calendar.MONTH) // mes actual
    val day = calendar.get(Calendar.DAY_OF_MONTH) //Dia del actual

    //creo y muestro selector de fecha
    DatePickerDialog(
        context, //mostrar diagolo
        { _, selectedYear, selectedMonth, selectedDay ->
            //CallBack que se ejecuata cuando el usuario selecciona la fecha
            //Restablece la fecha seleccionada
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)//llamo a la funcion Callback con la fecha seleccionada
        },
        year, //año que selecciona, pero por defecto estara el actual
        month, //mes que selecciona, pero por defecto estara el actual
        day //dia que selecciona, pero por defecto estara el actual
    ).show() //Muestra el dialogo
}

// Funcion para mostrar el Reloj de tiempo
fun showTimePickerDialog(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    ).show()
}