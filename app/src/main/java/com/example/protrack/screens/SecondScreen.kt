package com.example.protrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(navController: NavController) {

    //Variables de estado para los campos de texto
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }

    //Estados para mostrar dialogos
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    //conector a firebase
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .requiredWidth(width = 412.dp)
            .requiredHeight(height = 917.dp)
            .background(color = Color(0xffc8c8af))
    ) {
        //Campo usuario
        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
            },
            placeholder = { Text("Usuario") },
            supportingText = {
                Text(
                    text = "Usuario",
                    color = Color(0xff65558f),
                    lineHeight = 1.33.em,
                    style = MaterialTheme.typography.bodySmall)
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp,
                    y = 347.dp)
                .requiredWidth(width = 364.dp)
                .requiredHeight(height = 72.dp)
                .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))

        //campo contraseña
        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
            },
            placeholder = { Text("Contraseña") },
            supportingText = {
                Text(
                    text = "Contraseña",
                    color = Color(0xff65558f),
                    style = MaterialTheme.typography.bodySmall)
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp,
                    y = 470.dp)
                .requiredWidth(width = 364.dp)
                .requiredHeight(height = 70.dp)
                .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))

        //campo Email
        OutlinedTextField(
            value = email,
            onValueChange = {
            email = it
            },
            placeholder = { Text("Gmail") },
            supportingText = {
                Text(
                    text = "Gmail",
                    color = Color(0xff65558f),
                    lineHeight = 1.33.em,
                    style = MaterialTheme.typography.bodySmall)
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp,
                        y = 225.dp)
                .requiredWidth(width = 364.dp)
                .requiredHeight(height = 71.dp)
                .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))

        //boton Registrarse
        ElevatedButton(
            onClick = {
                // Validacion para campos vacios
                if (email.isEmpty() || clave.isEmpty() || usuario.isEmpty()) {
                    mensajeError = "Todos los campos son obligatorios."
                    mostrarDialogoError = true
                } else {
                    // Creacion de cuenta en Firebase
                    auth.createUserWithEmailAndPassword(email, clave)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                mostrarDialogoExito = true
                            } else {
                                mensajeError = "Error al crear la cuenta"
                                mostrarDialogoError = true
                            }
                        }
                }
            },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffef7ff).copy(alpha = 0.75f)),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 30.dp, y = 700.dp)
                .requiredWidth(width = 134.dp) // Ajusta el ancho al igual que "Cancelar"
                .requiredHeight(height = 40.dp)
        ) {
            Text(
                text = "REGISTRARSE",
                color = Color(0xff4a4459),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
            }


        //boton Cancelar
    ElevatedButton(
        onClick = {
            navController.popBackStack()
        },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffef7ff).copy(alpha = 0.75f)),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .offset(x = 200.dp, y = 643.dp)
            .requiredWidth(152.dp)
            .requiredHeight(40.dp)
    ) {
        Text(
            text = "CANCELAR",
            color = Color(0xff4a4459),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }



    //Alerta de error
    if (mostrarDialogoError){
        AlertDialog(
            onDismissRequest = {mostrarDialogoError = false},
            confirmButton = {
                Button(onClick = {mostrarDialogoError = false}) {
                    Text("Acpetar")
                }
            },
            title = { Text("Error") },
            text = { Text(mensajeError) }
        )
    }

    // Alerta de exito
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoExito = false
                navController.popBackStack() // Volver a FirstScreen
            },
            confirmButton = {
                Button(onClick = {
                    mostrarDialogoExito = false
                    navController.popBackStack() // Volver a FirstScreen
                }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Exito") },
            text = { Text("Cuenta creada correctamente.") }
        )
    }
}


