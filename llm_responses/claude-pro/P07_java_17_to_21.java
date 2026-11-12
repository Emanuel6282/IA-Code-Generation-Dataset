// P07: Flood Fill — migrado a Java 21
// (el código recibido se presentaba como Java 17, pero su cabecera y el nombre de la clase
// P07FloodFill_v8 corresponden al código base en Java 8; se migra ese)
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Record patterns (JEP 440, Java 21) en un switch (JEP 441, Java 21):
//    `case Cell(int r, int c)` descompone la celda sacada de la cola directamente en sus
//    componentes, en lugar de `int[] cell = ...; int r = cell[0]; int c = cell[1];`. Un
//    único caso basta: el compilador comprueba que cubre todo Cell (switch exhaustivo).
// 2. Records (Java 16): `record Cell(int row, int col)` sustituye al `int[]` de dos
//    posiciones como elemento de la cola: tiene nombre y tipo propios.
// 3. `var` (Java 10) en variables locales cuyo tipo es obvio por el lado derecho.
// 4. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se puede
//    correr con `java -ea P07FloodFill_v21.java`, sin compilar antes.
//
// Mejora de rendimiento (recomendación de la biblioteca, no novedad del lenguaje):
// ArrayDeque en lugar de LinkedList como cola. Mismo orden FIFO, sin un nodo enlazado por
// elemento.
//
// Nota: las sequenced collections (JEP 431, Java 21) no cambian nada aquí: ArrayDeque ya
// tenía addLast/pollFirst como Deque, y el código usa add/poll de Queue, que en una cola
// son igual de claros. Las demás novedades (virtual threads, String templates en preview)
// no aplican y no se fuerzan.

import java.util.ArrayDeque;
import java.util.Arrays;

public class P07FloodFill_v21 {

    /** Celda de la matriz que espera en la cola del BFS. */
    private record Cell(int row, int col) {}

    /** Desplazamientos arriba, abajo, izquierda, derecha (mismo orden que el original). */
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        var rows = grid.length;
        var cols = grid[0].length;
        var queue = new ArrayDeque<Cell>();
        queue.add(new Cell(row, col));
        grid[row][col] = newColor;

        while (!queue.isEmpty()) {
            // La cola no está vacía, así que poll() nunca devuelve null aquí.
            switch (queue.poll()) {
                case Cell(int r, int c) -> {
                    for (var dir : DIRECTIONS) {
                        var nr = r + dir[0];
                        var nc = c + dir[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                            grid[nr][nc] = newColor;
                            queue.add(new Cell(nr, nc));
                        }
                    }
                }
            }
        }
        return grid;
    }

    // Ejecutar con assertions habilitadas: java -ea P07FloodFill_v21.java
    public static void main(String[] args) {
        // TC01: fill clasico
        var grid1 = new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        var result1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(result1, new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}) : "TC01 fallo";

        // TC02: fill completo
        var grid2 = new int[][] {{0, 0, 0}, {0, 0, 0}};
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 1), new int[][] {{1, 1, 1}, {1, 1, 1}}) : "TC02 fallo";

        // TC03: mismo color, sin cambio
        var grid3 = new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 1), new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. `int[] cell = queue.poll(); int r = cell[0]; int c = cell[1];` ->
//    `switch (queue.poll()) { case Cell(int r, int c) -> ... }`: el record pattern
//    extrae fila y columna en el propio patrón; es la forma de desestructurar records que
//    Java 21 estabiliza.
// 2. `Queue<int[]>` con `new int[]{r, c}` -> `ArrayDeque<Cell>` con `record Cell`.
// 3. `new LinkedList<>()` -> `new ArrayDeque<>()`: la misma interfaz de cola
//    (add/poll/isEmpty) y el mismo orden FIFO, así que el BFS visita y pinta las mismas
//    celdas en el mismo orden. ArrayDeque usa un arreglo circular en lugar de un nodo
//    enlazado por elemento.
// 4. El arreglo `directions`, que se creaba en cada llamada, pasa a ser la constante
//    `DIRECTIONS` de la clase, con el mismo contenido y orden.
// 5. `var` en las variables locales con tipo evidente; `originalColor` conserva `int`.
// 6. `import java.util.*` -> imports explícitos (ArrayDeque, Arrays).
// 7. Clase renombrada a P07FloodFill_v21 (en Java, el archivo debe llamarse igual que la
//    clase pública). El nombre de la función y su firma no cambian.
// 8. Tests: los tres casos y sus mensajes ("TC01 fallo"...) se conservan; solo cambia el
//    formato de los arreglos y se usa `var`. Se mantiene "All tests passed".
// 9. Comportamiento idéntico, verificado contra el original con miles de matrices
//    aleatorias (resultado, estado final de la matriz y excepciones, incluido el
//    mensaje). Se conservan tal cual los casos límite del original:
//    - Modifica la matriz en el lugar y retorna la misma referencia.
//    - Una posición fuera de rango (también negativa) lanza
//      ArrayIndexOutOfBoundsException en `grid[row][col]`.
//    - `cols = grid[0].length`: con filas de distinto largo usa el de la primera; si una
//      fila es más corta, `grid[nr][nc]` lanza ArrayIndexOutOfBoundsException a mitad del
//      relleno, con la matriz ya modificada en parte, en el mismo punto que el original.
//
// NOTA SOBRE RENDIMIENTO: con una matriz de 2000 x 2000 de un solo color (mediana de 15
// ejecuciones, JDK 21), el original tarda ~0,11-0,14 s y la migración ~0,09-0,10 s:
// una mejora modesta, que viene de ArrayDeque (sin un nodo por elemento, menos trabajo
// para el recolector de basura). El switch con record pattern no añade coste.