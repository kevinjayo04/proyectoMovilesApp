package com.example.protrack.screens


import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.protrack.navigation.AppScreen
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale



@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ThridScreen(navController: NavController) {
    val currentMonth = getCurrentMonth() // Mes actual en formato MM/YYYY
    val earnings = remember { mutableStateOf(0f) } // Ganancias totales del mes



    // Cargo las ganancias del mes actual desde Firebase
    LaunchedEffect(Unit) {
        //Ejecuto el bloque composable
        fetchEarningsForCurrentMonth(currentMonth) { total ->
            //se ingresa y se se calcula o recupera y se actualiza el estado.
            earnings.value = total
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffc8c8af))
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PROTRACK",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Mostrar Ganancias Totales
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
        ) {
            Text(
                text = "Ganancias del Mes: $currentMonth",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Text(
                text = "Total: €${earnings.value}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
        }

        // Botones para navegar
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Button(
                onClick = { navController.navigate(AppScreen.FourScreen.ruta) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Ingresar Datos")
            }
            Button(
                onClick = { navController.navigate(AppScreen.FiveScreen.ruta) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Calendario")
            }
            Button(
                onClick = { navController.navigate(AppScreen.SixScreen.ruta) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Modificar")
            }
        }
    }
}

// Funcion para obtener el mes actual
@RequiresApi(Build.VERSION_CODES.N)
fun getCurrentMonth(): String {
    val calendar = Calendar.getInstance()
    val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val year = calendar.get(Calendar.YEAR).toString()
    return "$month/$year"
}

// Funcion para obtener las ganancias del mes actual desde Firebase
@RequiresApi(Build.VERSION_CODES.N)
fun fetchEarningsForCurrentMonth(
    currentMonth: String, //Mes actual
    onEarningsFetched: (Float) -> Unit //Recibe el ingreso total
) {
    val db = FirebaseFirestore.getInstance() //conexion a la base de datos
    db.collection("trabajo") //consulta
        .get()// recupero todos los datos
        .addOnSuccessListener { documents -> //Consulta exitosa
            var totalEarnings = 0f //Variable para almacenar los ingresos totales

            //recorro los datos de la base datos
            for (document in documents) {
                val date = document.getString("Fecha") //fechas en formato DD/MM/YYYY
                val salary = document.getString("SalarioPorHora")?.toFloatOrNull() ?: 0f
                val horaInicio = document.getString("HoraInicio")
                val horaFin = document.getString("HoraFin")

                //verifico si la fecha corresponde al mes actual
                if (date != null && date.endsWith(currentMonth)) {

                    //calculo las hora trabajas
                    val hoursWorked = calculateHoursWorked(horaInicio, horaFin)
                    //Sumo los ingresos al total
                    totalEarnings += hoursWorked * salary
                }
            }
            //llamo al callBack con los ingresos totales calculados
            onEarningsFetched(totalEarnings)
        }
        .addOnFailureListener {
            //Si hay un error, devuelvo el valor 0
            onEarningsFetched(0f)
        }
}

// Funcion para calcular las horas trabajadas a partir de dos cadenas de tiempo
@RequiresApi(Build.VERSION_CODES.N)
fun calculateHoursWorked(startTime: String?, endTime: String?): Float {
    if (startTime == null || endTime == null) return 0f

    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return try {
        val start = format.parse(startTime)
        val end = format.parse(endTime)
        val difference = end.time - start.time
        (difference / (1000 * 60 * 60)).toFloat() // Convertir a horas
    } catch (e: Exception) {
        0f
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun defaultView(){
    var navController = rememberNavController()
    ThridScreen(navController)

}

