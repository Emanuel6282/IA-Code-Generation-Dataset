import java.util.ArrayList;
import java.util.Arrays;

// CARACTERISTICAS DE JAVA 21 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Colecciones Secuenciadas (Sequenced Collections - JEP 431): Esta es la gran novedad de Java 21 para estructuras iterativas. Se introducen interfaces unificadas que añaden métodos como `addLast()`, `removeLast()`, `getFirst()`, etc. Ahora podemos operar un `ArrayList` como una Pila (Stack) LIFO de forma explícita y semánticamente limpia, eliminando el clásico y verboso `stack.remove(stack.size() - 1)`.
// 2. Records Locales consolidados (JEP 395): Mantenemos la declaración inmutable nominal `record Coord(int r, int c) {}` a nivel local para empaquetar las coordenadas sin ensuciar la clase exterior.
// 3. Paradigma Iterativo Puro: Bucle `while` apoyado en una pila explícita en el Heap, evadiendo la API de Streams y garantizando que las mutaciones espaciales (`grid[r][c] = newColor`) ocurran en tiempo constante O(1) con cero sobrecarga funcional.

public class P12FloodFill_Java21_Iterative {

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Manejo defensivo estándar
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        int originalColor = grid[row][col];

        // Cláusula de guarda para evitar bucles infinitos
        if (originalColor == newColor) {
            return grid;
        }

        // Estructura nominal inmutable local
        record Coord(int r, int c) {}

        // Inicialización usando inferencia de tipos (var)
        var stack = new ArrayList<Coord>();
        
        // Uso de Colecciones Secuenciadas (Java 21): inserción clara al final
        stack.addLast(new Coord(row, col));

        var rows = grid.length;
        var cols = grid[0].length;
        var directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Bucle iterativo basado en Heap (Depth-First Search)
        while (!stack.isEmpty()) {
            // Uso de Colecciones Secuenciadas (Java 21): extracción LIFO O(1) sin calcular el índice
            var current = stack.removeLast();
            var r = current.r();
            var c = current.c();

            // Verificación y mutación destructiva in-place
            if (grid[r][c] == originalColor) {
                grid[r][c] = newColor;

                // Exploración iterativa
                for (var dir : directions) {
                    var nr = r + dir[0];
                    var nc = c + dir[1];

                    // Si está en rango, se añade a la pila iterativa
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        stack.addLast(new Coord(nr, nc)); // Apilado secuencial moderno
                    }
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P12FloodFill_Java21_Iterative.java

        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo
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
        assert Arrays.deepEquals(floodFill(grid1, 1, 1, 2), expected1) : "Error en el caso base 1";

        // 2. Caso sin cambios (color igual)
        int[][] grid2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] expected2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 0), expected2) : "Error en el caso de no reemplazo";

        // 3. Caso de aislamiento
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
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 3), expected3) : "Error en el caso de aislamiento";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}