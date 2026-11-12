import java.util.ArrayList;
import java.util.Arrays;

// CARACTERISTICAS DE JAVA 8 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Iterativo Estricto (DFS con Pila Explícita): Se elimina completamente la recursión. En su lugar, se utiliza un bucle `while` y un `ArrayList` estándar operando como una pila (Stack) LIFO (Last-In, First-Out). Esto traslada la carga de memoria de la pila de llamadas (Call Stack) al Heap.
// 2. Ausencia de Abstracciones Funcionales: Se evitan deliberadamente la API de Streams y las lambdas introducidas en Java 8. Todo el control de flujo se realiza mediante saltos condicionales `if` y bucles `for`/`while` primitivos, garantizando transparencia absoluta sobre el costo computacional de la iteración.
// 3. Mutación de Estado In-Place: La matriz original se modifica de forma destructiva directamente a través de sus índices, lo cual es la marca registrada del rendimiento iterativo puro en arreglos bidimensionales en la JVM.

public class P08FloodFill_Java8_Iterative {

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Manejo defensivo contra matrices nulas o vacías
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        int originalColor = grid[row][col];

        // Cláusula de guarda: si el color no cambia, abortamos para evitar bucles infinitos
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Se emplea ArrayList operando como una Pila (Stack) para emular el recorrido DFS
        ArrayList<int[]> stack = new ArrayList<>();
        stack.add(new int[]{row, col});

        // Arreglo de desplazamientos para las 4 direcciones (arriba, abajo, izquierda, derecha)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Bucle iterativo de bajo nivel
        while (!stack.isEmpty()) {
            // Extracción destructiva del último elemento (LIFO) para tiempo O(1)
            int[] current = stack.remove(stack.size() - 1);
            int r = current[0];
            int c = current[1];

            // Verificación y mutación in-place
            if (grid[r][c] == originalColor) {
                grid[r][c] = newColor;

                // Exploración iterativa de vecinos
                for (int i = 0; i < directions.length; i++) {
                    int nr = r + directions[i][0];
                    int nc = c + directions[i][1];

                    // Si el vecino está dentro de los límites, se apila para futura evaluación
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        stack.add(new int[]{nr, nc});
                    }
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P08FloodFill_Java8_Iterative.java

        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: reemplazo de unos conectados a partir de (1,1)
        int[][] grid1 = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };
        int[][] expected1 = {
            {2, 2, 2},
            {2, 2, 0},
            {2, 0, 1}
        };
        int[][] res1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(res1, expected1) : "Error en el caso base 1";

        // 2. Caso sin cambios: el color inicial ya es igual al nuevo color
        int[][] grid2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] expected2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] res2 = floodFill(grid2, 0, 0, 0);
        assert Arrays.deepEquals(res2, expected2) : "Error en el caso de no reemplazo";

        // 3. Caso de aislamiento: solo cambia el centro, los otros 1s no están conectados en 4 direcciones
        int[][] grid3 = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };
        int[][] expected3 = {
            {1, 0, 1},
            {0, 3, 0},
            {1, 0, 1}
        };
        int[][] res3 = floodFill(grid3, 1, 1, 3);
        assert Arrays.deepEquals(res3, expected3) : "Error en el caso de aislamiento";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}