import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// CARACTERISTICAS DE JAVA 17 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Records (JEP 395): Se define `record Coord(int r, int c) {}`. Esto reemplaza por completo la verbosa clase interna inmutable de Java 8, proveyendo automáticamente constructores, inmutabilidad garantizada, `equals()` y `hashCode()`, esenciales para la evaluación en colecciones `Set`.
// 2. Inferencia de Tipos de Variables Locales (var - JEP 286): Reduce significativamente el ruido visual en la definición de flujos, variables temporales y acumuladores inmutables.
// 3. Paradigma Funcional Puro: Se utiliza `Optional` para el flujo de ejecución principal, recursión pura con `Stream.reduce()` para acumular el estado espacial (similar a un `foldLeft` en Scala) e `IntStream` para proyectar una matriz enteramente nueva sin causar mutaciones destructivas.

public class P11FloodFill_Java17_Functional {

    // Estructura de datos nominal inmutable nativa de Java 17
    record Coord(int r, int c) {}

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Enfoque funcional defensivo: el flujo fluye a través del pipeline Optional
        return Optional.ofNullable(grid)
            .filter(g -> g.length > 0 && g[0].length > 0)
            .map(g -> {
                var originalColor = g[row][col];
                
                // Clausura de guarda inmutable
                if (originalColor == newColor) {
                    return g;
                }

                // 1. Calcular de forma pura el conjunto de coordenadas que deben cambiar
                // Usamos Set.of() (introducido post-Java 8) para inicializar el acumulador vacío inmutable
                var pixelsToChange = getConnected(
                    g, g.length, g[0].length, originalColor, 
                    Set.of(), new Coord(row, col)
                );

                // 2. Construir la nueva matriz mediante mapeos funcionales
                return IntStream.range(0, g.length)
                    .mapToObj(r -> IntStream.range(0, g[0].length)
                        .map(c -> pixelsToChange.contains(new Coord(r, c)) ? newColor : g[r][c])
                        .toArray()
                    ).toArray(int[][]::new);
            })
            .orElse(grid);
    }

    // Función recursiva pura que arrastra el estado inmutable sin alterar referencias globales
    private static Set<Coord> getConnected(int[][] grid, int rows, int cols, int originalColor, Set<Coord> visited, Coord current) {
        // Condiciones de poda (fuera de límites, color distinto, o ciclo detectado)
        if (current.r() < 0 || current.r() >= rows || current.c() < 0 || current.c() >= cols 
            || grid[current.r()][current.c()] != originalColor 
            || visited.contains(current)) {
            return visited;
        }

        // Generar el siguiente estado inmutable
        var nextVisited = new HashSet<>(visited);
        nextVisited.add(current);
        var immutableVisited = Collections.unmodifiableSet(nextVisited);

        // Reducción espacial funcional sobre los 4 nodos adyacentes
        return Stream.of(
            new Coord(current.r() - 1, current.c()),
            new Coord(current.r() + 1, current.c()),
            new Coord(current.r(), current.c() - 1),
            new Coord(current.r(), current.c() + 1)
        ).reduce(
            immutableVisited,
            (acc, next) -> getConnected(grid, rows, cols, originalColor, acc, next),
            (set1, set2) -> {
                var combined = new HashSet<>(set1);
                combined.addAll(set2);
                return Collections.unmodifiableSet(combined);
            }
        );
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P11FloodFill_Java17_Functional.java

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

        System.out.println("Todos los tests funcionales en Java 17 pasaron correctamente.");
    }
}