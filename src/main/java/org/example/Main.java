package org.example;


public class Main {
    public static void main(String[] args) {
        Tablero tablero = new Tablero();
        String nombreArchivo = "C:/Users/Andres Duarte/OneDrive - Pontificia Universidad Javeriana/Documents/Sexto Semestre/Análisis de algoritmos/Proyecto/pruebaProyecto.txt";
        tablero.cargarArchivoTablero(nombreArchivo);
        Menu menu = new Menu(tablero);
        menu.mostrarMenu();
    }
}