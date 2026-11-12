import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionPipeline {

    /** Transacción financiera como record: un portador de datos inmutable. */
    public record Transaction(int id, double amount, String status, String category) {}

    private static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.9");  // comisión del 10 %

    /**
     * Pasos 1 y 2 en una sola función pura: si la transacción es válida devuelve su monto
     * tras la comisión; si no, Optional.empty().
     *
     * <p>Pattern matching en switch con patrón de record y guarda `when` (JEP 440 +
     * JEP 441, estándar en Java 21): desestructura la transacción y expresa el filtro en el
     * propio patrón. El compilador verifica que el switch sea exhaustivo.
     */
    private static Optional<BigDecimal> netAmount(Transaction transaction) {
        return switch (transaction) {
            // BigDecimal.valueOf(double) parte de la representación decimal más corta del
            // double (1.15 -> 1.15), no de su valor binario exacto.
            case Transaction(var id, var amount, var status, var category)
                    when "approved".equals(status) && amount > 0 ->
                    Optional.of(BigDecimal.valueOf(amount).multiply(COMMISSION_FACTOR));
            case Transaction other -> Optional.empty();   // rechazada o con monto <= 0
        };
    }

    /**
     * Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
     * una comisión del 10 %, y retorna el total redondeado a 2 decimales. Si la lista es
     * null, se trata como vacía.
     *
     * <p>Los cálculos se hacen con BigDecimal para evitar los errores de representación de
     * double: por ejemplo, 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con double daría
     * 1.03.
     */
    public static double processTransactions(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)
                .stream()                                        // null -> stream vacío
                .flatMap(List::stream)
                .map(TransactionPipeline::netAmount)             // pasos 1 y 2: Optional<BigDecimal>
                .flatMap(Optional::stream)                       // descarta las no válidas
                .reduce(BigDecimal.ZERO, BigDecimal::add)        // paso 3; identidad ZERO: vacía -> 0
                .setScale(2, RoundingMode.HALF_UP)               // redondeo comercial (1.035 -> 1.04)
                .doubleValue();
    }

    /** Caso de prueba como record. */
    record TestCase(String name, List<Transaction> transactions, double expected) {}

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    static Optional<String> failure(TestCase testCase) {
        return switch (testCase) {
            case TestCase(var name, var transactions, var expected) -> Optional.of(processTransactions(transactions))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s': esperado %.2f, obtenido %.2f".formatted(name, expected, actual));
        };
    }

    // Ejecutar con assertions habilitadas: java -ea TransactionPipeline.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        assert processTransactions(List.of()) == 0.0 : "lista vacía -> 0.0";

        var failures = Stream.of(
                        // Caso 1: ejemplo del enunciado (solo la primera está aprobada)
                        new TestCase("ejemplo del enunciado", List.of(
                                new Transaction(1, 100.0, "approved", "food"),
                                new Transaction(2, 200.0, "rejected", "travel")), 90.0),
                        // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
                        new TestCase("sin transacciones válidas", List.of(
                                new Transaction(1, 50.0, "rejected", "food"),
                                new Transaction(2, 0.0, "approved", "food"),
                                new Transaction(3, -30.0, "approved", "refund")), 0.0),
                        // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
                        new TestCase("redondeo de 1.035", List.of(
                                new Transaction(1, 1.15, "approved", "fees")), 1.04),
                        new TestCase("suma con decimales", List.of(
                                new Transaction(1, 0.3, "approved", "food"),
                                new Transaction(2, 0.2, "approved", "food"),
                                new Transaction(3, 99.99, "rejected", "food")), 0.45))
                .map(TransactionPipeline::failure)
                .flatMap(Optional::stream)   // descarta los casos que pasan
                .toList();                   // lista inmutable

        // getFirst() (sequenced collections, Java 21) sobre los fallos; solo se evalúa
        // si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}