# Proyecto Final: Solucionador de Laberintos 

## 📌 Información General
- **Título:** Comparativa para Resolver Laberintos  
- **Asignatura:** Estructura de Datos  
- **Carrera:** Computación  
- **Integrantes:** Michael Yumbla, Erick Bermeo, Mateo Cordero, Ariel Badillo  
- **Fecha:** 28/07/2025  
- **Profesor:** Ing. Pablo Torres  

---

## 🧩 Descripción 

Implementación de 6 algoritmos con diferentes enfoques para resolver laberintos en Java, con visualización paso a paso en una interfaz gráfica.

### 1. MazeSolverRecursivo (Simple)
- **Tipo:** DFS Recursivo básico  
- **Características:**
  - Solo explora derecha y abajo  
  - Sin retroceso visible  
  - Eficiente en memoria  

### 2. MazeSolverRecursivoCompleto
- **Tipo:** DFS Recursivo completo  
- **Características:**
  - Explora en las 4 direcciones (arriba, abajo, izquierda, derecha)  
  - Muestra recorrido en **amarillo**  
  - Camino final correcto en **verde**

### 3. MazeSolverRecursivoCompletoBT
- **Tipo:** DFS con Backtracking visual  
- **Características:**
  - Retroceso visual en el laberinto  
  - Estados de exploración, retroceso y solución  
  - Didáctico para entender el proceso  

### 4. MazeSolverDFS (Iterativo)
- **Tipo:** DFS con Stack (iterativo)  
- **Características:**
  - Implementación no recursiva  
  - Evita desbordamiento de pila  
  - Sigue el orden LIFO (último en entrar, primero en salir)  

### 5. MazeSolverBFS
- **Tipo:** BFS con Queue  
- **Características:**
  - Encuentra el camino más corto  
  - Exploración en **anchura**  
  - Orden FIFO (primero en entrar, primero en salir)  

### 6. MazeSolverBackTracking (Avanzado)
- **Tipo:** Backtracking optimizado  
- **Características:**
  - Control paso a paso con `Semaphore`  
  - Visualización de retroceso, exploración y camino final  
  - Tres estados: **amarillo** (explorado), **rojo** (retroceso), **verde** (solución)

---

## 🚀 Ejecución

Para ejecutar el proyecto:

1. Compila el código:
    ```bash
    javac App.java
    ```
2. Ejecuta la aplicación:
    ```bash
    java App
    ```


## 📊 Comparativa de Algoritmos
| Algoritmo             | Complejidad | Camino Óptimo | Memoria | Visualización         |
| --------------------- | ----------- | ------------- | ------- | --------------------- |
| Recursivo Simple      | O(n)        | ❌             | Baja    | Básica                |
| Recursivo Completo    | O(n)        | ❌             | Media   | Intermedia            |
| Recursivo+BT          | O(n)        | ❌             | Alta    | Completa              |
| DFS Iterativo         | O(n)        | ❌             | Media   | Intermedia            |
| BFS                   | O(n)        | ✅             | Alta    | Radial (amplio)       |
| BackTracking Avanzado | O(n)        | Depende       | Alta    | Paso a paso didáctico |


## 📚 Estructura del Código
```plaintext
src/
├── controllers/
│   └── MazeController.java
├── dao/
│   └── (pendiente/uso interno)
├── models/
│   ├── AlgorithmResult.java
│   ├── Cell.java
│   ├── CellState.java
│   ├── Maze.java
│   └── SolveResults.java
├── solver/
│   ├── MazeSolver.java
│   └── solverImpl/
│       ├── MazeSolverRecursivo.java
│       ├── MazeSolverRecursivoCompleto.java
│       ├── MazeSolverRecursivoCompletoBT.java
│       ├── MazeSolverDFS.java
│       ├── MazeSolverBFS.java
│       └── MazeSolverBackTracking.java
├── views/
│   ├── MazePanel.java
│   ├── MazeFrame.java
│   ├── ResultadosDialog.java
│   └── GraficaResultadosDialog.java
└── App.java

```

## 📸 Capturas de Pantalla
