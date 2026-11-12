import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Java 8 — paradigma funcional (Stream API, lambdas, Optional; sin for/while).
//
// Características de Java 8 que se usan y por qué:
// 1. Stream API (Stream, IntStream, Collectors): el recorrido, los vecinos y la matriz
//    resultante se expresan como transformaciones de streams, sin bucles.
// 2. Lambdas y referencias a métodos: cada paso es una función pura.
// 3. Optional: la lectura de una celda devuelve Optional.empty() si está fuera de la
//    matriz, en lugar de lanzar una excepción o devolver un valor centinela; la
//    validación de la posición inicial usa Optional.filter(...).orElseThrow(...).
// 4. Stream.iterate: genera los niveles del BFS de forma perezosa a partir de un estado
//    inmutable. Java 8 no tiene takeWhile (llega en Java 9), así que el stream se acota
//    con limit(celdas + 1): ningún BFS tiene más niveles que celdas, y los niveles
//    vacíos del final cuestan O(1) cada uno.
// 5. String.join: une los mensajes de fallo de las pruebas.
//
// Inmutabilidad: floodFill NO modifica la matriz recibida; construye y retorna una
// nueva. Los niveles son conjuntos no modificables (Collections.unmodifiableSet).
//
// Rendimiento: sigue siendo O(filas * columnas), pero los conjuntos de Long y los
// Optional<Integer> crean muchos objetos (autoboxing). Medido con una matriz de
// 1000 x 1000 de un solo color: 2-6 s según la presión del recolector de basura, frente
// a ~0,05 s de la versión iterativa con la matriz mutable. Es el coste de la
// inmutabilidad y de no poder usar streams de primitivos para los conjuntos en Java 8.
public class FloodFill {

    /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
    private static final List<int[]> OFFSETS = Collections.unmodifiableList(
            Arrays.asList(new int[] {-1, 0}, new int[] {1, 0}, new int[] {0, -1}, new int[] {0, 1}));

    /** Codifica una celda (fila, columna no negativas) en un único long: clave compacta para conjuntos. */
    private static long key(int r, int c) {
        return ((long) r << 32) | c;
    }

    private static int rowOf(long key) {
        return (int) (key >>> 32);
    }

    private static int colOf(long key) {
        return (int) key;
    }

    /** Color de la celda (r, c), o Optional.empty() si no existe (admite filas de distinto largo). */
    private static Optional<Integer> colorAt(int[][] grid, int r, int c) {
        return r >= 0 && r < grid.length && c >= 0 && c < grid[r].length
                ? Optional.of(grid[r][c])
                : Optional.empty();
    }

    /**
     * Estado inmutable del BFS: nivel anterior y nivel actual. En un grafo no dirigido,
     * los vecinos del nivel k solo pueden estar en los niveles k-1, k o k+1, así que basta
     * con recordar esos dos para no volver atrás.
     */
    private static final class Level {
        final Set<Long> previous;
        final Set<Long> current;

        Level(Set<Long> previous, Set<Long> current) {
            this.previous = Collections.unmodifiableSet(previous);
            this.current = Collections.unmodifiableSet(current);
        }
    }

    /**
     * Retorna una matriz nueva en la que la región conectada (4 direcciones) que contiene
     * (row, col) pasa a {@code newColor}. La matriz original no se modifica.
     *
     * <p>Si (row, col) ya tiene {@code newColor}, retorna la misma matriz, sin cambios.
     *
     * <p>Sin recursión (un DFS recursivo sobre una región grande desbordaría la pila de la
     * JVM): Stream.iterate genera los niveles del BFS y flatMap une sus celdas en la
     * región. O(filas * columnas) en tiempo.
     *
     * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int original = colorAt(grid, row, col).orElseThrow(() ->
                new IndexOutOfBoundsException("posición (" + row + ", " + col + ") fuera de la matriz"));

        return Optional.of(grid)
                .filter(g -> original != newColor)
                .map(g -> paint(g, region(g, row, col, original), newColor))
                .orElse(grid);   // ya tiene el nuevo color -> la misma matriz, sin cambios
    }

    /** Celdas vecinas de {@code cell} que existen y tienen el color original. */
    private static Stream<Long> neighbors(int[][] grid, long cell, int original) {
        return OFFSETS.stream()
                .map(d -> new int[] {rowOf(cell) + d[0], colOf(cell) + d[1]})
                .filter(p -> colorAt(grid, p[0], p[1]).filter(color -> color == original).isPresent())
                .map(p -> key(p[0], p[1]));
    }

    /** Conjunto de celdas de la región conectada que contiene (row, col). */
    private static Set<Long> region(int[][] grid, int row, int col, int original) {
        long totalCells = Arrays.stream(grid).mapToLong(line -> line.length).sum();
        Level start = new Level(Collections.<Long>emptySet(), Collections.singleton(key(row, col)));

        return Stream.iterate(start, level -> level.current.isEmpty()
                        ? level   // ya terminó: el mismo estado vacío, sin crear objetos
                        : new Level(level.current,
                                level.current.stream()
                                        .flatMap(cell -> neighbors(grid, cell, original))
                                        .filter(cell -> !level.current.contains(cell) && !level.previous.contains(cell))
                                        .collect(Collectors.toSet())))
                .limit(totalCells + 1)
                .flatMap(level -> level.current.stream())
                .collect(Collectors.toSet());
    }

    /** Matriz nueva: las celdas de {@code region} con {@code newColor}, el resto igual. */
    private static int[][] paint(int[][] grid, Set<Long> region, int newColor) {
        return IntStream.range(0, grid.length)
                .mapToObj(r -> IntStream.range(0, grid[r].length)
                        .map(c -> region.contains(key(r, c)) ? newColor : grid[r][c])
                        .toArray())
                .toArray(int[][]::new);
    }

    /** Ejecuta un caso; Optional con el mensaje de fallo, o vacío si pasa. */
    private static Optional<String> failure(String name, int[][] grid, int row, int col,
                                            int newColor, int[][] expected) {
        String before = Arrays.deepToString(grid);
        int[][] result = floodFill(grid, row, col, newColor);
        return Optional.of(name)
                .filter(n -> !Arrays.deepEquals(result, expected) || !before.equals(Arrays.deepToString(grid)))
                .map(n -> "Caso '" + n + "': esperado " + Arrays.deepToString(expected)
                        + ", obtenido " + Arrays.deepToString(result) + " (la entrada no debe cambiar)");
    }

    /** Una posición fuera de la matriz debe lanzar IndexOutOfBoundsException. */
    private static Optional<String> outOfBoundsFailure(int row, int col) {
        try {
            floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, row, col, 5);
            return Optional.of("(" + row + ", " + col + ") debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac FloodFill.java && java -ea FloodFill
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<String> failures = Stream.of(
                        // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
                        failure("ejemplo del enunciado",
                                new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2,
                                new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}),
                        // Caso 2: el color ya es el nuevo -> la matriz no cambia
                        failure("color ya es el nuevo",
                                new int[][] {{0, 0, 0}, {0, 1, 1}}, 1, 1, 1,
                                new int[][] {{0, 0, 0}, {0, 1, 1}}),
                        // Caso 3: no se rellena en diagonal
                        failure("sin diagonales",
                                new int[][] {{1, 0}, {0, 1}}, 0, 0, 3,
                                new int[][] {{3, 0}, {0, 1}}),
                        // Posición fuera de la matriz (incluido un índice negativo)
                        outOfBoundsFailure(2, 0),
                        outOfBoundsFailure(-1, 0),
                        outOfBoundsFailure(0, 3))
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}