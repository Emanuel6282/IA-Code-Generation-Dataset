import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// CARACTERISTICAS DE JAVA 21 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Inmutabilidad Moderna (`Set.copyOf`): Reemplaza al antiguo y verboso `Collections.unmodifiableSet()` de versiones previas. `Set.copyOf()` garantiza una copia verdaderamente inmutable y altamente optimizada en memoria para acompañar la propagación de nuestro estado DFS funcional.
// 2. Records Locales (JEP 395): La declaración `record Coord(int r, int c) {}` se mantiene como la forma idiomática y definitiva en Java 21 para definir tuplas inmutables nativas, proveyendo igualdad estructural por defecto, lo cual es crítico para colecciones `Set`.
// 3. Paradigma Funcional Puro: Cero mutaciones destructivas (`grid[r][c] = ...`) y cero bucles `for`/`while`. Todo el control de flujo recae en proyecciones (`map`, `IntStream`) y reducciones matemáticas (`Stream.reduce()`), simulando perfectamente la transparencia referencial de lenguajes como Scala o Haskell.

public class P13FloodFill_Java21_Functional {

    // Tupla inmutable nativa de Java, ideal para el paradigma declarativo
    record Coord(int r, int c) {}

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Enfoque funcional defensivo: orquestación de estado a través de Optional
        return Optional.ofNullable(grid)
            .filter(g -> g.length > 0 && g[0].length > 0)
            .map(g -> {
                var originalColor = g[row][col];
                
                // Clausura de guarda: retorna la referencia original si no hay efecto deseado
                if (originalColor == newColor) {
                    return g;
                }

                // 1. Pliegue (Fold) espacial puro para obtener los píxeles a modificar
                var pixelsToChange = getConnected(
                    g, g.length, g[0].length, originalColor, 
                    Set.of(), new Coord(row, col)
                );

                // 2. Proyección inmutable: se construye una NUEVA matriz bidimensional
                return IntStream.range(0, g.length)
                    .mapToObj(r -> IntStream.range(0, g[0].length)
                        .map(c -> pixelsToChange.contains(new Coord(r, c)) ? newColor : g[r][c])
                        .toArray()
                    ).toArray(int[][]::new);
            })
            .orElse(grid); // Retorno base ante matrices nulas o inválidas
    }

    // Función recursiva pura: arrastra el conjunto inmutable de visitados sin efectos secundarios
    private static Set<Coord> getConnected(int[][] grid, int rows, int cols, int originalColor, Set<Coord> visited, Coord current) {
        // Condiciones de poda (límites, color diferente, o ya procesado)
        if (current.r() < 0 || current.r() >= rows || current.c() < 0 || current.c() >= cols 
            || grid[current.r()][current.c()] != originalColor 
            || visited.contains(current)) {
            return visited;
        }

        // Creación del nuevo estado inmutable utilizando APIs modernas de Java
        var nextVisited = new HashSet<>(visited);
        nextVisited.add(current);
        var immutableVisited = Set.copyOf(nextVisited); // Inmutabilidad fuerte O(N) optimizada

        // Propagación espacial utilizando reducción (reduce) sobre un Stream
        return Stream.of(
            new Coord(current.r() - 1, current.c()),
            new Coord(current.r() + 1, current.c()),
            new Coord(current.r(), current.c() - 1),
            new Coord(current.r(), current.c() + 1)
        ).reduce(
            immutableVisited,
            (acc, next) -> getConnected(grid, rows, cols, originalColor, acc, next),
            (set1, set2) -> {
                // Función combinadora para reconciliación del Stream
                var combined = new HashSet<Coord>(set1);
                combined.addAll(set2);
                return Set.copyOf(combined);
            }
        );
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P13FloodFill_Java21_Functional.java

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

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}