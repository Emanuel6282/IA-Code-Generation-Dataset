import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaxSubarray {

    /**
     * Resumen inmutable de un tramo contiguo del arreglo. Con estos cuatro valores se
     * puede calcular el resumen de dos tramos consecutivos sin volver a mirar sus
     * elementos, que es lo que permite resolver el problema con un reduce.
     */
    private static final class Segment {
        final long total;   // suma de todo el tramo
        final long prefix;  // mejor suma de un prefijo no vacío del tramo
        final long suffix;  // mejor suma de un sufijo no vacío del tramo
        final long best;    // mejor suma de un subarreglo no vacío dentro del tramo

        Segment(long total, long prefix, long suffix, long best) {
            this.total = total;
            this.prefix = prefix;
            this.suffix = suffix;
            this.best = best;
        }

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
     * <p>Cada elemento se convierte en un Segment y reduce los combina de izquierda a
     * derecha. Como no hay elemento identidad (no existe el subarreglo vacío), se usa el
     * reduce sin valor inicial, que devuelve un Optional: vacío si el arreglo lo es.
     * Las sumas se acumulan en long para no desbordar el rango de int. O(n) en tiempo.
     *
     * @throws IllegalArgumentException si {@code nums} está vacío
     */
    public static long maxSubarraySum(int[] nums) {
        return Arrays.stream(nums)
                .mapToObj(Segment::of)
                .reduce(Segment::concat)                       // Optional<Segment>
                .map(segment -> segment.best)
                .orElseThrow(() -> new IllegalArgumentException("el arreglo debe tener al menos un elemento"));
    }

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    private static Optional<String> failure(String name, int[] nums, long expected) {
        long result = maxSubarraySum(nums);
        return Optional.of(result)
                .filter(actual -> actual != expected)
                .map(actual -> "Caso '" + name + "' " + Arrays.toString(nums) + ": esperado "
                        + expected + ", obtenido " + actual);
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

    // Compilar y ejecutar con assertions habilitadas:
    //   javac MaxSubarray.java && java -ea MaxSubarray
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<String> failures = Stream.of(
                        // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
                        failure("ejemplo del enunciado", new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6),
                        // Caso 2: todos negativos -> el menos negativo
                        failure("todos negativos", new int[] {-3, -1, -2}, -1),
                        failure("un solo elemento negativo", new int[] {-5}, -5),
                        // Caso 3: suma que supera el rango de int (con int se desbordaría a -2)
                        failure("suma mayor que int",
                                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 2L * Integer.MAX_VALUE),
                        // Arreglo vacío -> IllegalArgumentException
                        emptyArrayFailure())
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}