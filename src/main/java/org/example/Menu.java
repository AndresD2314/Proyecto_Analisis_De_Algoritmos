package org.example;

import java.util.Scanner;


public class Menu {
    private final Tablero tablero;

    public Menu(Tablero tablero) {
        this.tablero = tablero;
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Usted desea jugar o quiere que el juego se resuelva de manera sintetica? oprima s para jugar, n para que se resuelva de manera" +
                " sintetica");

        String opc = scanner.next();

        if (opc.equalsIgnoreCase("S")) {
            int opcion;
            do {
                System.out.println("--- Menú ---");
                System.out.println("1. Ver tablero actual");
                System.out.println("2. Agregar puente");
                System.out.println("3. Eliminar puente");
                System.out.println("4. Resolver juego");
                System.out.println("5. Mostrar informacion de nodos");
                System.out.println("0. Salir");
                System.out.print("Ingrese una opción: ");
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        tablero.mostrarInterfaz();
                        break;
                    case 2:
                        System.out.print("Ingrese la posición del primer nodo (fila, columna): ");
                        int fila1 = scanner.nextInt();
                        int columna1 = scanner.nextInt();
                        System.out.print("Ingrese la posición del segundo nodo (fila, columna): ");
                        int fila2 = scanner.nextInt();
                        int columna2 = scanner.nextInt();
                        tablero.agregarPuente(fila1 - 1, columna1 - 1, fila2 - 1, columna2 - 1);
                        break;
                    case 3:
                        System.out.print("Ingrese la posición del primer nodo (fila, columna): ");
                        fila1 = scanner.nextInt();
                        columna1 = scanner.nextInt();
                        System.out.print("Ingrese la posición del segundo nodo (fila, columna): ");
                        fila2 = scanner.nextInt();
                        columna2 = scanner.nextInt();
                        if (tablero.eliminarPuente(fila1 - 1, columna1 - 1, fila2 - 1, columna2 - 1))
                            System.out.println("Se eliminó un puente entre los nodos.");
                        break;
                    case 4:
                        if (tablero.estaResuelto())
                            System.out.println("¡Felicitaciones, ha resuelto el juego!");
                        else
                            System.out.println("No se puede resolver el juego en el estado actual.");
                        break;
                    case 5:
                        tablero.imprimirInfoNodos();
                        break;
                    case 0:
                        System.out.println("Gracias por jugar.");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } while (opcion != 0);
        } else
            System.out.println("En progreso....");
    }
}
