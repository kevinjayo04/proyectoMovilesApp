package com.example.protrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.protrack.R
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SixScreen(navController: NavController) {
    //variables de estado
    val selectedDate = remember { mutableStateOf("Sin seleccionar") }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val hourlyRate = remember { mutableStateOf("") }
    val context = LocalContext.current

    //Variables de alertas
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }

    //variables de confirmacion y acciones
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val actionType = remember { mutableStateOf("") } // "Modificar" o "Eliminar"

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
                text = "MODIFICAR",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp)
        ) {
            // Seleccion de fecha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Fecha seleccionada: ${selectedDate.value}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = {
                    showDatePickerDialog(context) { date ->
                        selectedDate.value = date
                        cargarDatosDesdeFirebase(date, startTime, endTime, hourlyRate, showAlert, alertMessage)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_calend),
                        contentDescription = "Seleccionar fecha",
                        tint = Color(0xff49454f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Mostrar datos si existen
            if (startTime.value.isNotEmpty()) {
                // Hora de inicio
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Hora de inicio: ${startTime.value}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = {
                        showTimePickerDialog(context) { time -> startTime.value = time }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_clock),
                            contentDescription = "Seleccionar hora de inicio",
                            tint = Color(0xff49454f),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Hora de fin
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Hora de fin: ${endTime.value}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = {
                        showTimePickerDialog(context) { time -> endTime.value = time }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_clock),
                            contentDescription = "Seleccionar hora de fin",
                            tint = Color(0xff49454f),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Salario por hora
                OutlinedTextField(
                    value = hourlyRate.value,
                    onValueChange = { value -> if (value.all { it.isDigit() }) hourlyRate.value = value },
                    label = { Text("Salario por hora (€)") },
                    placeholder = { Text("Introduce un número") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones Modificar y Eliminar
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            actionType.value = "Modificar"
                            showConfirmationDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe6e0e9))
                    ) {
                        Text("Modificar", color = Color.Black)
                    }

                    Button(
                        onClick = {
                            actionType.value = "Eliminar"
                            showConfirmationDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe6e0e9))
                    ) {
                        Text("Eliminar", color = Color.Black)
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showConfirmationDialog.value) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog.value = false },
                confirmButton = {
                    Button(onClick = {
                        showConfirmationDialog.value = false
                        if (actionType.value == "Modificar") {
                            modificarDatosEnFirebase(selectedDate.value, startTime.value, endTime.value, hourlyRate.value, showAlert, alertMessage)
                        } else if (actionType.value == "Eliminar") {
                            eliminarDatosEnFirebase(selectedDate, startTime, endTime, hourlyRate, showAlert, alertMessage)
                        }
                    }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmationDialog.value = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Confirmación") },
                text = { Text("¿Estás seguro de que quieres ${actionType.value.lowercase()} los datos?") }
            )
        }

        // Alerta
        if (showAlert.value) {
            AlertDialog(
                onDismissRequest = { showAlert.value = false },
                confirmButton = {
                    Button(onClick = { showAlert.value = false }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Aviso") },
                text = { Text(alertMessage.value) }
            )
        }
    }
}

// Funcion para cargar datos desde Firebase
fun cargarDatosDesdeFirebase(

    //almacenar parametros de la funcion
    fecha: String,
    startTime: MutableState<String>,
    endTime: MutableState<String>,
    hourlyRate: MutableState<String>,
    showAlert: MutableState<Boolean>,
    alertMessage: MutableState<String>
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("trabajo")
        .whereEqualTo("Fecha", fecha)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val doc = documents.first()
                startTime.value = doc.getString("HoraInicio") ?: ""
                endTime.value = doc.getString("HoraFin") ?: ""
                hourlyRate.value = doc.getString("SalarioPorHora") ?: ""
            } else {
                alertMessage.value = "No hay datos para esta fecha."
                showAlert.value = true
            }
        }
}

// Función para modificar datos en Firebase
fun modificarDatosEnFirebase(
    fecha: String,
    horaInicio: String,
    horaFin: String,
    salarioPorHora: String,
    showAlert: MutableState<Boolean>,
    alertMessage: MutableState<String>
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("trabajo")
        .whereEqualTo("Fecha", fecha)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                db.collection("trabajo").document(document.id)
                    .update(
                        mapOf(
                            "HoraInicio" to horaInicio,
                            "HoraFin" to horaFin,
                            "SalarioPorHora" to salarioPorHora
                        )
                    )
                alertMessage.value = "Datos modificados correctamente."
                showAlert.value = true
            }
        }
}

// Función para eliminar datos en Firebase
fun eliminarDatosEnFirebase(
    selectedDate: MutableState<String>,
    startTime: MutableState<String>,
    endTime: MutableState<String>,
    hourlyRate: MutableState<String>,
    showAlert: MutableState<Boolean>,
    alertMessage: MutableState<String>
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("trabajo")
        .whereEqualTo("Fecha", selectedDate.value)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                db.collection("trabajo").document(document.id).delete()
            }
            // Resetear los campos al estado inicial
            selectedDate.value = "Sin seleccionar"
            startTime.value = ""
            endTime.value = ""
            hourlyRate.value = ""
            alertMessage.value = "Datos eliminados correctamente."
            showAlert.value = true
        }
        .addOnFailureListener {
            alertMessage.value = "Error al eliminar los datos."
            showAlert.value = true
        }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SixScreenPreview() {
    var navController = rememberNavController()
    SixScreen(navController)
}