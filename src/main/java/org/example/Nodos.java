package org.example;

import java.util.*;

public class Nodos {
    private final int fila;
    private final int columna;
    private final int puentesRequeridos;
    private final Map<Nodos, Integer> nodosConectados;

    public Nodos(int fila, int columna, int puentesRequeridos) {
        this.fila = fila;
        this.columna = columna;
        this.puentesRequeridos = puentesRequeridos;
        this.nodosConectados = new HashMap<>();
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getPuentesRequeridos() {
        return puentesRequeridos;
    }

    public Map<Nodos, Integer> getNodosConectados() {
        return nodosConectados;
    }

    private void conectarNodo(Nodos otroNodo) {
        int puentesConstruidos = nodosConectados.getOrDefault(otroNodo, 0);
        nodosConectados.put(otroNodo, puentesConstruidos + 1);
    }


    public void eliminarPuente(Nodos nodoDestino) {
        // Verificar si hay una conexión con el nodo destino
        if (!nodosConectados.containsKey(nodoDestino)) {
            return; // No hay conexión para eliminar
        }

        // Reducir el número de conexiones en el mapa correspondiente al nodo actual
        nodosConectados.computeIfPresent(nodoDestino, (k, v) -> v - 1);

        // Eliminar la entrada del mapa si el número de conexiones es cero
        nodosConectados.entrySet().removeIf(entry -> entry.getValue() == 0);

        // Reducir el número de conexiones en el mapa correspondiente al nodo destino
        nodoDestino.getNodosConectados().computeIfPresent(this, (k, v) -> v - 1);

        // Eliminar la entrada del mapa si el número de conexiones es cero
        nodoDestino.getNodosConectados().entrySet().removeIf(entry -> entry.getValue() == 0);
    }

    public boolean agregarPuente(Nodos otroNodo) {


        // Verificar si los nodos ya están conectados
        if (nodosConectados.containsKey(otroNodo)) {
            // Sumar la cantidad de puentes construidos en ambos nodos
            int puentesConstruidos = nodosConectados.get(otroNodo);
            if (puentesConstruidos <= otroNodo.nodosConectados.get(this)) {
                nodosConectados.put(otroNodo, puentesConstruidos + 1);
                otroNodo.nodosConectados.put(this, puentesConstruidos + 1);
                System.out.println("Puente agregado correctamente!");
                return true;
            }
            return false;
        }

        // Asegurarse de que los nodos sean adyacentes y que no tengan un puente ya conectado
        if (!tienePuente(otroNodo)) {
            // Verificar que los nodos no estén conectados de forma diagonal
            if ((this.fila == otroNodo.getFila()) || (this.columna == otroNodo.getColumna())) {
                if (this.getPuentesConstruidos() < this.puentesRequeridos && otroNodo.getPuentesConstruidos() < otroNodo.puentesRequeridos
                        && this.nodosConectados.size() < this.puentesRequeridos) {
                    // Agregar el puente en ambos nodos
                    conectarNodo(otroNodo);
                    otroNodo.conectarNodo(this);
                    System.out.println("Puente agregado correctamente!");
                    return true;
                } else {
                    System.out.println("No se pudo crear el puente ya que sobre pasa la cantidad de puentes requeridos!");
                }
            }
        }
        return false;
    }


    public boolean tienePuente(Nodos nodo) {
        // Verifica si el nodo dado está en el mapa de nodos conectados
        return nodosConectados.containsKey(nodo);
    }

    public int getPuentesConstruidos() {
        int puentesConstruidos = 0;
        for (Map.Entry<Nodos, Integer> entry : nodosConectados.entrySet()) {
            Nodos nodo = entry.getKey();
            int numPuentes = entry.getValue();
            // Solo contar los puentes construidos en este nodo
            if (nodo.getNodosConectados().containsKey(this) && nodo.getNodosConectados().get(this) >= numPuentes) {
                puentesConstruidos += numPuentes;
            }
        }
        return puentesConstruidos;
    }


    public List<Nodos> getConexiones() {
        List<Nodos> conexiones = new ArrayList<>();
        Nodos primerNodo = null;

        for (Map.Entry<Nodos, Integer> entry : nodosConectados.entrySet()) {
            Nodos nodo = entry.getKey();
            int numPuentes = entry.getValue();
            if (numPuentes >= 0) { // agregamos todos los nodos cuyo número de puentes requeridos sea mayor o igual que cero
                if (primerNodo == null)
                    primerNodo = nodo;
                else
                    conexiones.add(nodo);
            }
        }

        if (primerNodo != null)
            conexiones.add(0, primerNodo);

        conexiones.sort(Comparator.comparing(Object::toString));
        return conexiones;
    }


}