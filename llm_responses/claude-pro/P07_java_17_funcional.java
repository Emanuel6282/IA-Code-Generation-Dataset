import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Java 17 — paradigma funcional (Stream API, lambdas, Optional; sin for/while).
//
// Características de Java 17 (incluidas las estabilizadas entre Java 9 y 17) que se usan:
// 1. Records (Java 16): Cell, Direction, Level y TestCase son datos inmutables con
//    equals/hashCode generados; Cell puede usarse directamente como elemento de un Set.
// 2. Stream.iterate(semilla, condición, siguiente) (Java 9): genera los niveles del BFS
//    y se detiene en el primer nivel vacío. En Java 8 hacía falta acotar con limit().
// 3. Optional.stream() (Java 9): convierte un Optional en un stream de 0 o 1 elementos;
//    descarta los casos de prueba que pasan con flatMap(Optional::stream).
// 4. Collectors.toUnmodifiableSet (Java 10) y Stream.toList() (Java 16): resultados
//    inmutables sin envolverlos a mano.
// 5. List.of (Java 9), `var` (Java 10) y String.formatted (Java 15).
//
// Inmutabilidad: floodFill NO modifica la matriz recibida; construye y retorna una
// nueva. Todos los conjuntos y listas intermedios son no modificables.
//
// Rendimiento: O(filas * columnas), pero cada celda se representa con un objeto Cell en
// conjuntos inmutables, así que es mucho más lento que la versión iterativa con la
// matriz mutable: con 1000 x 1000 de un solo color, ~2-3 s frente a ~0,05 s. Ver
// también el hashCode de Cell, sin el cual el rendimiento se degradaba por colisiones.
public class FloodFill {

    /** Celda de la matriz (fila, columna). */
    private record Cell(int row, int col) {
        Cell move(Direction direction) {
            return new Cell(row + direction.dRow(), col + direction.dCol());
        }

        /**
         * Hash bien distribuido. El que genera el record (31 * row + col) colisiona en
         * matrices de más de 31 columnas: (r, c) y (r + 1, c - 31) dan el mismo valor. Los
         * conjuntos inmutables de Set.of / toUnmodifiableSet usan sondeo lineal, y con esas
         * colisiones cada búsqueda recorría largas cadenas: una matriz de 100 x 100 no
         * terminaba en minutos. Se mezclan los bits con la constante de Fibonacci
         * (0x9E3779B9) para repartir los valores.
         */
        @Override
        public int hashCode() {
            int h = row * 0x9E3779B9 + col;
            return h ^ (h >>> 16);
        }
    }

    /** Desplazamiento a una celda vecina. */
    private record Direction(int dRow, int dCol) {}

    /** Arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
    private static final List<Direction> DIRECTIONS = List.of(
            new Direction(-1, 0), new Direction(1, 0), new Direction(0, -1), new Direction(0, 1));

    /**
     * Estado inmutable del BFS: nivel anterior y nivel actual. En un grafo no dirigido,
     * los vecinos del nivel k solo pueden estar en los niveles k-1, k o k+1, así que basta
     * con recordar esos dos para no volver atrás.
     */
    private record Level(Set<Cell> previous, Set<Cell> current) {}

    /** Color de la celda, o Optional.empty() si no existe (admite filas de distinto largo). */
    private static Optional<Integer> colorAt(int[][] grid, Cell cell) {
        return cell.row() >= 0 && cell.row() < grid.length
                && cell.col() >= 0 && cell.col() < grid[cell.row()].length
                ? Optional.of(grid[cell.row()][cell.col()])
                : Optional.empty();
    }

    /**
     * Retorna una matriz nueva en la que la región conectada (4 direcciones) que contiene
     * (row, col) pasa a {@code newColor}. La matriz original no se modifica.
     *
     * <p>Si (row, col) ya tiene {@code newColor}, retorna la misma matriz, sin cambios.
     *
     * <p>Sin recursión (un DFS recursivo sobre una región grande desbordaría la pila de la
     * JVM): Stream.iterate genera los niveles del BFS hasta el primero vacío y flatMap une
     * sus celdas en la región. O(filas * columnas) en tiempo.
     *
     * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        var start = new Cell(row, col);
        int original = colorAt(grid, start).orElseThrow(() ->
                new IndexOutOfBoundsException("posición (%d, %d) fuera de la matriz".formatted(row, col)));

        return Optional.of(grid)
                .filter(g -> original != newColor)
                .map(g -> paint(g, region(g, start, original), newColor))
                .orElse(grid);   // ya tiene el nuevo color -> la misma matriz, sin cambios
    }

    /** Celdas vecinas de {@code cell} que existen y tienen el color original. */
    private static Stream<Cell> neighbors(int[][] grid, Cell cell, int original) {
        return DIRECTIONS.stream()
                .map(cell::move)
                .filter(next -> colorAt(grid, next).filter(color -> color == original).isPresent());
    }

    /** Siguiente nivel del BFS: vecinos del nivel actual que no están en él ni en el anterior. */
    private static Level next(int[][] grid, Level level, int original) {
        return new Level(level.current(), level.current().stream()
                .flatMap(cell -> neighbors(grid, cell, original))
                .filter(cell -> !level.current().contains(cell) && !level.previous().contains(cell))
                .collect(Collectors.toUnmodifiableSet()));
    }

    /** Conjunto de celdas de la región conectada que contiene {@code start}. */
    private static Set<Cell> region(int[][] grid, Cell start, int original) {
        return Stream.iterate(new Level(Set.of(), Set.of(start)),
                        level -> !level.current().isEmpty(),
                        level -> next(grid, level, original))
                .flatMap(level -> level.current().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    /** Matriz nueva: las celdas de {@code region} con {@code newColor}, el resto igual. */
    private static int[][] paint(int[][] grid, Set<Cell> region, int newColor) {
        return IntStream.range(0, grid.length)
                .mapToObj(r -> IntStream.range(0, grid[r].length)
                        .map(c -> region.contains(new Cell(r, c)) ? newColor : grid[r][c])
                        .toArray())
                .toArray(int[][]::new);
    }

    /** Caso de prueba como record. */
    private record TestCase(String name, int[][] grid, int row, int col, int newColor, int[][] expected) {

        /** Ejecuta el caso: Optional con el mensaje de fallo, o vacío si pasa. */
        Optional<String> failure() {
            var before = Arrays.deepToString(grid);
            var result = floodFill(grid, row, col, newColor);
            return Optional.of(name)
                    .filter(n -> !Arrays.deepEquals(result, expected) || !before.equals(Arrays.deepToString(grid)))
                    .map(n -> "Caso '%s': esperado %s, obtenido %s (la entrada no debe cambiar)".formatted(
                            n, Arrays.deepToString(expected), Arrays.deepToString(result)));
        }
    }

    /** Una posición fuera de la matriz debe lanzar IndexOutOfBoundsException. */
    private static Optional<String> outOfBoundsFailure(Cell position) {
        try {
            floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, position.row(), position.col(), 5);
            return Optional.of("%s debería lanzar IndexOutOfBoundsException".formatted(position));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac FloodFill.java && java -ea FloodFill
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = List.of(
                // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
                new TestCase("ejemplo del enunciado",
                        new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2,
                        new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}),
                // Caso 2: el color ya es el nuevo -> la matriz no cambia
                new TestCase("color ya es el nuevo",
                        new int[][] {{0, 0, 0}, {0, 1, 1}}, 1, 1, 1,
                        new int[][] {{0, 0, 0}, {0, 1, 1}}),
                // Caso 3: no se rellena en diagonal
                new TestCase("sin diagonales",
                        new int[][] {{1, 0}, {0, 1}}, 0, 0, 3,
                        new int[][] {{3, 0}, {0, 1}}));

        // Posición fuera de la matriz (incluido un índice negativo)
        var badPositions = List.of(new Cell(2, 0), new Cell(-1, 0), new Cell(0, 3));

        var failures = Stream.concat(
                        testCases.stream().map(TestCase::failure),
                        badPositions.stream().map(FloodFill::outOfBoundsFailure))
                .flatMap(Optional::stream)   // Optional.stream (Java 9): descarta los que pasan
                .toList();                   // Stream.toList (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}