package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Tablero {
    private Nodos[][] nodos;
    private int f;
    private int c;

    public Tablero() {
    }

    public Nodos getNodo(int fila, int columna) {
        return nodos[fila][columna];
    }


    public boolean estaResuelto() {
        // Verifica cada nodo en el tablero
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                Nodos nodo = nodos[i][j];
                int puentesConectados = nodo.getNodosConectados().size();
                if (puentesConectados != nodo.getPuentesRequeridos()) {
                    // Si el número de puentes conectados no es igual al número de puentes requeridos,
                    // el juego no está resuelto
                    return false;
                }
            }
        }
        // Verificar si hay algún nodo que no tiene todos sus puentes requeridos conectados
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                Nodos nodo = nodos[i][j];
                int puentesConectados = nodo.getNodosConectados().size();
                if (puentesConectados < nodo.getPuentesRequeridos()) {
                    // Si hay algún nodo que no tiene todos sus puentes requeridos conectados,
                    // el juego aún no está resuelto
                    return false;
                }
            }
        }
        // Si llegamos a este punto, todos los nodos tienen el número correcto de puentes conectados
        // y no hay nodos que aún no tienen todos sus puentes requeridos conectados, entonces el juego está resuelto
        return true;
    }


    public void agregarPuente(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Nodos origen = getNodo(filaOrigen, columnaOrigen);
        Nodos destino = getNodo(filaDestino, columnaDestino);

        // Verificar que los nodos existan
        if (origen == null || destino == null) {
            System.out.println("No se pudo agregar el puente: nodos no existen");
            return;
        }


        // Verificar que los nodos no estén en diagonal
        if (filaOrigen != filaDestino && columnaDestino != columnaOrigen) {
            System.out.println("No se pudo agregar el puente: nodos están en diagonal");
            return;
        }


        // Agregar el puente en ambos nodos
        origen.agregarPuente(destino);
    }


    public boolean eliminarPuente(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Nodos origen = getNodo(filaOrigen, columnaOrigen);
        Nodos destino = getNodo(filaDestino, columnaDestino);

        // Verificar que los nodos existan
        if (origen == null || destino == null) {
            return false;
        }

        // Buscar el puente y eliminarlo en ambos nodos
        if (origen.tienePuente(destino)) {
            origen.eliminarPuente(destino);
            destino.eliminarPuente(origen);
            return true;
        } else {
            return false;
        }
    }

    public void cargarArchivoTablero(String nombreArchivo) {
        try (Stream<String> lineas = Files.lines(Paths.get(nombreArchivo))) {
            Iterator<String> iteradorLineas = lineas.iterator();

            String[] contenido = iteradorLineas.next().split(",");
            int filas = Integer.parseInt(contenido[0]);
            int columnas = Integer.parseInt(contenido[1]);
            System.out.println("Filas: " + filas + " Columnas: " + columnas);
            this.f = filas;
            this.c = columnas;

            // Creamos un nuevo tablero con las dimensiones especificadas
            nodos = new Nodos[filas][columnas];

            // Leemos el resto del archivo para llenar la matriz de nodos
            int i = 0;
            while (iteradorLineas.hasNext()) {
                String linea = iteradorLineas.next();
                for (int j = 0; j < columnas; j++) {
                    int valor = Character.getNumericValue(linea.charAt(j));
                    nodos[i][j] = new Nodos(i, j , valor);
                }
                i++;
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo! " + e.getMessage());
        }
    }

    public void mostrarInterfaz() {
        String[][] caracteres = new String[f*2+1][c*2+1];
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                Nodos nodoActual = nodos[i][j];
                if (nodoActual.getPuentesRequeridos() != 0) {
                    // Si el nodo tiene un puente requerido, usa un punto como caracter
                    caracteres[i*2+1][j*2+1] = "•";

                    // Verifica si el nodo tiene nodos conectados
                    Map<Nodos, Integer> nodosConectados = nodoActual.getNodosConectados();
                    if (!nodosConectados.isEmpty()) {
                        // Si el nodo tiene conexiones, usa los caracteres correspondientes
                        for (Nodos nodoConectado : nodosConectados.keySet()) {

                            // Verificar la dirección de la conexión
                            if (nodoConectado.getFila() < i) { //Arriba
                                caracteres[i*2][j*2+1] = "|";
                            } else if (nodoConectado.getFila() > i) { //Abajo
                                caracteres[i*2+2][j*2+1] = "|";
                            } else if (nodoConectado.getColumna() < j) { //Izquierda
                                caracteres[i*2+1][j*2] = "-";
                            } else if (nodoConectado.getColumna() > j) { //Derecha
                                caracteres[i*2+1][j*2+2] = "-";
                            }
                        }
                    } else {
                        // Si el nodo no tiene conexiones, usa espacios vacíos para representar los caracteres
                        caracteres[i*2][j*2+1] = " ";
                        caracteres[i*2+1][j*2] = " ";
                        caracteres[i*2+1][j*2+2] = " ";
                        caracteres[i*2+2][j*2+1] = " ";
                    }
                }
            }
        }

        // Imprime los caracteres en la consola
        for (String[] caracter : caracteres) {
            for (String s : caracter) {
                if (s == null) {
                    System.out.print("  ");
                } else {
                    System.out.print(s + " ");
                }
            }
            System.out.println();
        }
    }

    public void imprimirInfoNodos() {
        System.out.println("Información de los nodos:\n");
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                Nodos nodo = nodos[i][j];
                System.out.printf("Nodo (%d,%d):\n", i + 1, j + 1);
                System.out.printf("  Puentes requeridos: %d\n", nodo.getPuentesRequeridos());
                System.out.printf("  Puentes construidos: %d\n", nodo.getPuentesConstruidos());
                List<Nodos> conexiones = nodo.getConexiones();
                if (conexiones.isEmpty()) {
                    System.out.println("  Conexiones: Ninguna");
                } else {
                    System.out.println("  Conexiones:");
                    for (Nodos conexion : conexiones) {
                        System.out.printf("    - Nodo (%d,%d)\n", (conexion.getFila() + 1), (conexion.getColumna() + 1));
                    }
                }
                System.out.println();
            }
        }
    }

}
