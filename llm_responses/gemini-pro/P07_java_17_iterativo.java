import java.util.ArrayList;
import java.util.Arrays;

// CARACTERISTICAS DE JAVA 17 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Records Locales (Local Records - JEP 395): Java 17 permite declarar estructuras de datos inmutables (`record`) dentro del propio método. Esto elimina la necesidad de crear arreglos anónimos `new int[]{r, c}` o clases auxiliares verbosas, mejorando drásticamente la legibilidad y seguridad de tipos en el empaquetado de coordenadas para nuestra pila iterativa.
// 2. Inferencia de Tipos de Variables Locales (`var` - JEP 286): Ampliamente consolidado en Java 17, el uso de `var` reduce el "boilerplate" clásico de Java (`ArrayList<Coord> stack = new ArrayList<Coord>()`), haciendo que el código iterativo se vea mucho más limpio y cercano a lenguajes como Python o Scala.
// 3. Paradigma Iterativo Puro: Se utiliza un `ArrayList` como una pila (Stack) LIFO para gestionar la Búsqueda en Profundidad (DFS). Todo se controla mediante bucles `while` y `for-each` tradicionales, alterando la matriz in-place sin recurrir a la API de Streams, lambdas o recursión.

public class P10FloodFill_Java17_Iterative {

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

        // Característica clave de Java 17: Record local definido dentro del método
        // Actúa como un portador de datos transparente y nominal para las coordenadas
        record Coord(int r, int c) {}

        // Uso de 'var' para reducir el ruido visual en la instanciación
        var stack = new ArrayList<Coord>();
        stack.add(new Coord(row, col));

        var rows = grid.length;
        var cols = grid[0].length;
        var directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Bucle iterativo (DFS explícito en Heap)
        while (!stack.isEmpty()) {
            // Extracción destructiva del último elemento (LIFO) para tiempo O(1)
            var current = stack.remove(stack.size() - 1);
            var r = current.r();
            var c = current.c();

            // Verificación y mutación in-place de la matriz
            if (grid[r][c] == originalColor) {
                grid[r][c] = newColor;

                // Exploración iterativa de vecinos usando bucle for-each mejorado
                for (var dir : directions) {
                    var nr = r + dir[0];
                    var nc = c + dir[1];

                    // Verificación de límites
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        stack.add(new Coord(nr, nc));
                    }
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P10FloodFill_Java17_Iterative.java

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

        System.out.println("Todos los tests iterativos en Java 17 pasaron correctamente.");
    }
}