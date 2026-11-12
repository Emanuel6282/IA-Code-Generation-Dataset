import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// CARACTERISTICAS DE JAVA 8 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Optional para flujos defensivos: Se evita el clásico `if (grid == null)` empaquetando la matriz en un `Optional` y utilizando `filter` y `map` para encadenar las operaciones lógicas de forma declarativa.
// 2. Stream API para reconstrucción sin mutación: En lugar de usar bucles `for` anidados para modificar la matriz original (mutación in-place), se utiliza `IntStream.range` para proyectar matemáticamente una NUEVA matriz bidimensional.
// 3. Reducción Pura (`Stream.reduce`): Se eliminan completamente los bucles iterativos (`while`) del algoritmo de búsqueda. El recorrido del grafo en 4 direcciones se modela como un plegado funcional (fold) donde el estado inmutable (el conjunto de píxeles visitados) se propaga recursivamente.
// 4. Inmutabilidad Explícita: Se emplea `Collections.unmodifiableSet` para emular colecciones puramente funcionales (como el `frozenset` de Python), garantizando la ausencia de efectos secundarios en la evaluación de la clausura.

public class P09FloodFill_Java8_Functional {

    // Clase auxiliar inmutable para representar tuplas de coordenadas, 
    // necesaria en Java 8 al carecer de 'Records' o Tuplas nativas (como en Scala).
    private static final class Coord {
        final int r;
        final int c;

        Coord(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return r == coord.r && c == coord.c;
        }

        @Override
        public int hashCode() {
            return 31 * r + c;
        }
    }

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Enfoque funcional defensivo: el flujo completo se orquesta a través de un Optional
        return Optional.ofNullable(grid)
            .filter(g -> g.length > 0 && g[0].length > 0)
            .map(g -> {
                int originalColor = g[row][col];
                
                // Clausura de guarda: si el color no cambia, se devuelve la matriz tal cual
                if (originalColor == newColor) {
                    return g;
                }

                // 1. Recolectar puramente el conjunto inmutable de coordenadas afectadas
                Set<Coord> pixelsToChange = getConnected(
                    g, g.length, g[0].length, originalColor, 
                    Collections.emptySet(), new Coord(row, col)
                );

                // 2. Proyectar funcionalmente una NUEVA matriz
                return IntStream.range(0, g.length)
                    .mapToObj(r -> IntStream.range(0, g[0].length)
                        .map(c -> pixelsToChange.contains(new Coord(r, c)) ? newColor : g[r][c])
                        .toArray()
                    ).toArray(int[][]::new);
            })
            .orElse(grid); // Retorno seguro si la matriz original era nula o vacía
    }

    // Función recursiva pura que acumula coordenadas sin mutar referencias externas
    private static Set<Coord> getConnected(int[][] grid, int rows, int cols, int originalColor, Set<Coord> visited, Coord current) {
        // Condiciones de poda (límites, color distinto o ya visitado)
        if (current.r < 0 || current.r >= rows || current.c < 0 || current.c >= cols 
            || grid[current.r][current.c] != originalColor 
            || visited.contains(current)) {
            return visited;
        }

        // Generar un nuevo estado inmutable para propagar
        Set<Coord> nextVisited = new HashSet<>(visited);
        nextVisited.add(current);
        Set<Coord> immutableVisited = Collections.unmodifiableSet(nextVisited);

        // Plegado funcional (Fold) sobre las 4 direcciones espaciales.
        // El acumulador propaga el `immutableVisited` a través del recorrido DFS funcional.
        return Stream.of(
            new Coord(current.r - 1, current.c), // Arriba
            new Coord(current.r + 1, current.c), // Abajo
            new Coord(current.r, current.c - 1), // Izquierda
            new Coord(current.r, current.c + 1)  // Derecha
        ).reduce(
            immutableVisited,
            (acc, next) -> getConnected(grid, rows, cols, originalColor, acc, next),
            (set1, set2) -> {
                // Combinador para operaciones en paralelo (requerido por la firma de Stream.reduce)
                Set<Coord> combined = new HashSet<>(set1);
                combined.addAll(set2);
                return Collections.unmodifiableSet(combined);
            }
        );
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P09FloodFill_Java8_Functional.java

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
        assert Arrays.deepEquals(floodFill(grid1, 1, 1, 2), expected1) : "Error en caso 1";

        // 2. Caso sin cambios (color igual)
        int[][] grid2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] expected2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 0), expected2) : "Error en caso 2";

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
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 3), expected3) : "Error en caso 3";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}