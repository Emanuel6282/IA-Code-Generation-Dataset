import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class MaxSubarray {

    /**
     * Resumen inmutable de un tramo contiguo del arreglo, como record (estándar desde
     * Java 16). Con estos cuatro valores se puede calcular el resumen de dos tramos
     * consecutivos sin volver a mirar sus elementos, que es lo que permite resolver el
     * problema con un reduce.
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

        /**
         * Resumen del tramo `this` seguido de `next`. El mejor subarreglo está entero en
         * uno de los dos tramos, o cruza la frontera (sufijo de `this` + prefijo de
         * `next`). Esta operación es asociativa, como exige Stream.reduce.
         */
        Segment concat(Segment next) {
            return new Segment(
                    total + next.total,
                    Math.max(prefix, total + next.prefix),
                    Math.max(next.suffix, next.total + suffix),
                    Math.max(Math.max(best, next.best), suffix + next.prefix));
        }
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
                .reduce(Segment::concat)                       // Optional<Segment>
                .map(Segment::best)                            // accesor generado por el record
                .orElseThrow(() -> new IllegalArgumentException("el arreglo debe tener al menos un elemento"));
    }

    /** Caso de prueba como record. */
    record TestCase(String name, int[] nums, long expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            return Optional.of(maxSubarraySum(nums))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s' %s: esperado %d, obtenido %d"
                            .formatted(name, Arrays.toString(nums), expected, actual));
        }
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
                                .map(TestCase::failure),
                        // Arreglo vacío -> IllegalArgumentException
                        Stream.of(emptyArrayFailure()))
                .flatMap(Optional::stream)   // Optional.stream() (Java 9): descarta los que pasan
                .toList();                   // Stream.toList() (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}