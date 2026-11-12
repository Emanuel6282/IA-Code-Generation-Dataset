import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionPipeline {

    /** Transacción financiera inmutable. */
    public static final class Transaction {
        final int id;
        final double amount;
        final String status;     // "approved" o "rejected"
        final String category;

        public Transaction(int id, double amount, String status, String category) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.category = category;
        }
    }

    private static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.9");  // comisión del 10 %

    /** Paso 1 (filtro): aprobada y con monto positivo. */
    private static final Predicate<Transaction> IS_VALID =
            t -> "approved".equals(t.status) && t.amount > 0;

    /**
     * Paso 2 (transformación): monto tras la comisión del 10 %. BigDecimal.valueOf(double)
     * parte de la representación decimal más corta del double (1.15 -> 1.15), no de su
     * valor binario exacto.
     */
    private static final Function<Transaction, BigDecimal> NET_AMOUNT =
            t -> BigDecimal.valueOf(t.amount).multiply(COMMISSION_FACTOR);

    /**
     * Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
     * una comisión del 10 %, y retorna el total redondeado a 2 decimales. Si la lista es
     * null, se trata como vacía.
     *
     * <p>Es un pipeline que corresponde uno a uno con el enunciado:
     * filter (paso 1) -> map (paso 2) -> reduce (paso 3). Los cálculos se hacen con
     * BigDecimal para evitar los errores de representación de double: por ejemplo,
     * 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con double daría 1.03.
     */
    public static double processTransactions(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)                 // Optional: lista nula -> vacía
                .map(List::stream)
                .orElseGet(Stream::empty)
                .filter(IS_VALID)
                .map(NET_AMOUNT)
                .reduce(BigDecimal.ZERO, BigDecimal::add)        // identidad ZERO: vacía -> 0
                .setScale(2, RoundingMode.HALF_UP)               // redondeo comercial (1.035 -> 1.04)
                .doubleValue();
    }

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    private static Optional<String> failure(String name, List<Transaction> transactions, double expected) {
        double result = processTransactions(transactions);
        return Optional.of(result)
                .filter(actual -> actual != expected)
                .map(actual -> "Caso '" + name + "': esperado " + expected + ", obtenido " + actual);
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac TransactionPipeline.java && java -ea TransactionPipeline
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        assert processTransactions(Collections.<Transaction>emptyList()) == 0.0 : "lista vacía -> 0.0";

        List<String> failures = Stream.of(
                        // Caso 1: ejemplo del enunciado (solo la primera está aprobada)
                        failure("ejemplo del enunciado", Arrays.asList(
                                new Transaction(1, 100.0, "approved", "food"),
                                new Transaction(2, 200.0, "rejected", "travel")), 90.0),
                        // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
                        failure("sin transacciones válidas", Arrays.asList(
                                new Transaction(1, 50.0, "rejected", "food"),
                                new Transaction(2, 0.0, "approved", "food"),
                                new Transaction(3, -30.0, "approved", "refund")), 0.0),
                        // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
                        failure("redondeo de 1.035", Arrays.asList(
                                new Transaction(1, 1.15, "approved", "fees")), 1.04),
                        failure("suma con decimales", Arrays.asList(
                                new Transaction(1, 0.3, "approved", "food"),
                                new Transaction(2, 0.2, "approved", "food"),
                                new Transaction(3, 99.99, "rejected", "food")), 0.45))
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}