package com.example.protrack.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.protrack.R
import com.example.protrack.navigation.AppScreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navController: NavController){
    //Variables de estado
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    //variable para la instacia a Firebase
    val auth = FirebaseAuth.getInstance()

    Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 412.dp)
                .requiredHeight(height = 917.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondooriginal),
                contentDescription = "fondo original",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = -5.dp,
                        y = -10.dp)
                    .requiredWidth(width = 553.dp)
                    .requiredHeight(height = 917.dp))
            Image(
                painter = painterResource(id = R.drawable.iconavatar),
                contentDescription = "iconAvatar",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 114.dp,
                        y = 208.dp)
                    .requiredWidth(width = 190.dp)
                    .requiredHeight(height = 177.dp)
                    .clip(shape = RoundedCornerShape(100.dp)))

            //boton para el registro de usuarios
            Button(
                onClick = {
                        navController.navigate(route = AppScreen.SecondScreen.ruta)
                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffef7ff).copy(alpha = 0.75f)),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 35.dp,
                            y = 769.dp)
                    .requiredWidth(width = 152.dp)
                    .requiredHeight(height = 36.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .requiredWidth(width = 152.dp)
                        .requiredHeight(height = 36.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "REGISTRARSE",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically))
                    }
                }
            }

            //boton para el inicio de sesion
            Button(
                onClick = {
                    if (email.isNotEmpty() && clave.isNotEmpty()){
                        //autenticador de Firebase
                        auth.signInWithEmailAndPassword(email, clave).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                //nageva a la siguiente ventana
                                navController.navigate(route = AppScreen.ThirdScreen.ruta)
                            } else{
                                mostrarDialogo = true
                            }
                        }

                    } else{
                        mostrarDialogo = true
                    }

                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffef7ff).copy(alpha = 0.75f)),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 228.dp,
                        y = 769.dp)
                    .requiredWidth(width = 152.dp)
                    .requiredHeight(height = 36.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .requiredWidth(width = 152.dp)
                        .requiredHeight(height = 36.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "INICIAR SESION",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically))
                    }
                    if (mostrarDialogo){
                        mensajeError(titulo = "Error", mensaje = "Error en el Gmail o Contraseña", onDismiss = {mostrarDialogo = false})

                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.iconperson),
                contentDescription = "iconperson",
                colorFilter = ColorFilter.tint(Color(0xff1d1b20)),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 59.dp,
                        y = 510.dp)
                    .requiredWidth(width = 61.dp)
                    .requiredHeight(height = 59.dp))

            //Texto Contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                placeholder = { Text("Introduzca contraseña") }, // Mensaje de fondo
                visualTransformation = PasswordVisualTransformation(), // Transforma el texto a formato de contraseña
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 126.dp, y = 620.dp)
                    .requiredWidth(width = 217.dp)
                    .requiredHeight(height = 52.dp)
                    .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            )

            //texto del Gmail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Introduzca el Gmail") }, // Mensaje de fondo
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 126.dp, y = 515.dp)
                    .requiredWidth(width = 217.dp)
                    .requiredHeight(height = 51.dp)
                    .clip(shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            )
             Image(
                painter = painterResource(id = R.drawable.lock),
                contentDescription = "lock",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 65.dp,
                        y = 620.dp)
                    .requiredWidth(width = 49.dp)
                    .requiredHeight(height = 52.dp)
             )
        }
    }

}
//funcion de mensaje de errores
@Composable
fun mensajeError(titulo : String, mensaje : String, onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {onDismiss() }) {
                Text("Aceptar")
            }
        },
        title = { Text(titulo) },
        text = { Text(mensaje) }
    )

}
