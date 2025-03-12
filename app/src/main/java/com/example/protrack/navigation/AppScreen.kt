package com.example.protrack.navigation

//clase sellada que representa las pantallas de las aplicaciones
sealed class AppScreen(val ruta: String) {
    //Primera pantalla, asociada a la ruta
    object FirstScreen: AppScreen("first_screen")
    //Segunda pantalla, asociada a la ruta
    object SecondScreen: AppScreen("second_screen")
    //Tercera pantalla, asociada a la ruta
    object ThirdScreen: AppScreen("third_screen")
    //Cuarta pantalla, asociada a la ruta
    object FourScreen: AppScreen("four_screen")
    //Quinta pantalla, asociada a la ruta
    object FiveScreen: AppScreen("five_screen")
    //Sexta pantalla, asociada a la ruta
    object SixScreen: AppScreen("six_screen")

}