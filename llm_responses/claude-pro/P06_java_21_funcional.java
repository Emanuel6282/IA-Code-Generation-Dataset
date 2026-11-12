import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class MaxSubarray {

    /**
     * Resumen inmutable de un tramo contiguo del arreglo. Con estos cuatro valores se
     * puede calcular el resumen de dos tramos consecutivos sin volver a mirar sus
     * elementos, que es lo que permite resolver el problema con un reduce.
     *
     * @param total  suma de todo el tramo
     * @param prefix mejor suma de un prefijo no vacío del tramo
     * @param suffix mejor suma de un sufijo no vacío del tramo
     * @param best   mejor suma de un subarreglo no vacío dentro del tramo
     */
    private record Segment(long total, long prefix, long suffix, long best) {

        /** Tramo de un solo elemento: todo coincide con el propio valor. */
        static Segment of(int x) {
            return new Segment(x, x, x, x);
        }
    }

    /** Dos tramos consecutivos: el izquierdo seguido del derecho. */
    private record Adjacent(Segment left, Segment right) {}

    /**
     * Resumen de dos tramos consecutivos. Operación asociativa, como exige Stream.reduce.
     *
     * <p>Patrones de record anidados en switch (JEP 440 + JEP 441, estándar en Java 21):
     * un solo patrón desestructura a la vez el par y los dos tramos, y da nombre a sus
     * ocho componentes. La fórmula se lee igual que su definición matemática: el mejor
     * subarreglo está entero en uno de los dos tramos, o cruza la frontera (sufijo
     * izquierdo + prefijo derecho).
     */
    private static Segment concat(Segment left, Segment right) {
        return switch (new Adjacent(left, right)) {
            case Adjacent(Segment(var lTotal, var lPrefix, var lSuffix, var lBest),
                          Segment(var rTotal, var rPrefix, var rSuffix, var rBest)) -> new Segment(
                    lTotal + rTotal,
                    Math.max(lPrefix, lTotal + rPrefix),
                    Math.max(rSuffix, rTotal + lSuffix),
                    Math.max(Math.max(lBest, rBest), lSuffix + rPrefix));
        };
    }

    /**
     * Retorna la suma máxima de un subarreglo contiguo no vacío de {@code nums}.
     *
     * <p>Cada elemento se convierte en un Segment y reduce los combina. Como no hay
     * elemento identidad (no existe el subarreglo vacío), se usa el reduce sin valor
     * inicial, que devuelve un Optional: vacío si el arreglo lo es. Las sumas se acumulan
     * en long para no desbordar el rango de int. O(n) en tiempo.
     *
     * @throws IllegalArgumentException si {@code nums} está vacío
     */
    public static long maxSubarraySum(int[] nums) {
        return Arrays.stream(nums)
                .mapToObj(Segment::of)
                .reduce(MaxSubarray::concat)                   // Optional<Segment>
                .map(Segment::best)
                .orElseThrow(() -> new IllegalArgumentException("el arreglo debe tener al menos un elemento"));
    }

    /** Caso de prueba como record. */
    record TestCase(String name, int[] nums, long expected) {}

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    static Optional<String> failure(TestCase testCase) {
        return switch (testCase) {
            case TestCase(var name, var nums, var expected) -> Optional.of(maxSubarraySum(nums))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s' %s: esperado %d, obtenido %d"
                            .formatted(name, Arrays.toString(nums), expected, actual));
        };
    }

    /** Un arreglo vacío debe lanzar IllegalArgumentException; si no, devuelve el mensaje. */
    private static Optional<String> emptyArrayFailure() {
        try {
            maxSubarraySum(new int[] {});
            return Optional.of("un arreglo vacío debería lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    // Ejecutar con assertions habilitadas: java -ea MaxSubarray.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var failures = Stream.concat(
                        Stream.of(
                                // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
                                new TestCase("ejemplo del enunciado", new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6),
                                // Caso 2: todos negativos -> el menos negativo
                                new TestCase("todos negativos", new int[] {-3, -1, -2}, -1),
                                new TestCase("un solo elemento negativo", new int[] {-5}, -5),
                                // Caso 3: suma que supera el rango de int (con int se desbordaría a -2)
                                new TestCase("suma mayor que int",
                                        new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 2L * Integer.MAX_VALUE))
                                .map(MaxSubarray::failure),
                        // Arreglo vacío -> IllegalArgumentException
                        Stream.of(emptyArrayFailure()))
                .flatMap(Optional::stream)   // descarta los casos que pasan
                .toList();                   // lista inmutable

        // getFirst() (sequenced collections, Java 21) sobre los fallos; solo se evalúa
        // si el assert falla.
        assert failures.isEmpty()
                : "%d fallo(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}