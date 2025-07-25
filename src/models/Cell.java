package models;

// Representa una celda del laberinto
public class Cell {
    public boolean wall = false;      // Si es un muro
    public boolean visited = false;   // Si ha sido visitada por el algoritmo
    public boolean solution = false;  // Si forma parte del camino solución
}
