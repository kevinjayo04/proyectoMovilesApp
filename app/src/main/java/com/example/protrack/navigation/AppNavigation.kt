package com.example.protrack.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.protrack.screens.FirstScreen
import com.example.protrack.screens.FiveScreen
import com.example.protrack.screens.FourScreen
import com.example.protrack.screens.SecondScreen
import com.example.protrack.screens.SixScreen
import com.example.protrack.screens.ThridScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(){
    //Creo el controlador de navegacion
    val navController = rememberNavController()

    //Configuro el contenedor de la navegacion
    NavHost(navController = navController, startDestination = AppScreen.FirstScreen.ruta) {

        //Configuro de la primera pantalla
        composable(route = AppScreen.FirstScreen.ruta) {
            FirstScreen(navController)  // Primera pantalla
        }

        composable(route = AppScreen.SecondScreen.ruta){
            SecondScreen(navController) //segunda pantalla
        }

        composable(route = AppScreen.ThirdScreen.ruta){
            ThridScreen(navController) //tercera ventana
        }

        composable(route = AppScreen.FourScreen.ruta) {
            FourScreen(navController) //cuarta pantalla
        }

        composable(route = AppScreen.FiveScreen.ruta) {
            FiveScreen(navController) //quinta pantalla
        }

        composable(route = AppScreen.SixScreen.ruta){
            SixScreen(navController) //sexta pantalla
        }

    }
}