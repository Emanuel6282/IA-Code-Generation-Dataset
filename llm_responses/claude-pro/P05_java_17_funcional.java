import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionPipeline {

    private static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.9");  // comisión del 10 %

    /**
     * Transacción financiera como record (estándar desde Java 16): un portador de datos
     * inmutable. Los pasos 1 y 2 del pipeline son métodos del propio record, así que el
     * stream los usa como referencias a método.
     */
    public record Transaction(int id, double amount, String status, String category) {

        /** Paso 1 (filtro): aprobada y con monto positivo. */
        boolean isValid() {
            return "approved".equals(status) && amount > 0;
        }

        /**
         * Paso 2 (transformación): monto tras la comisión del 10 %. BigDecimal.valueOf
         * parte de la representación decimal más corta del double (1.15 -> 1.15), no de
         * su valor binario exacto.
         */
        BigDecimal netAmount() {
            return BigDecimal.valueOf(amount).multiply(COMMISSION_FACTOR);
        }
    }

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
        return Optional.ofNullable(transactions)
                .stream()                                        // Optional.stream() (Java 9): null -> vacío
                .flatMap(List::stream)
                .filter(Transaction::isValid)
                .map(Transaction::netAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)        // identidad ZERO: vacía -> 0
                .setScale(2, RoundingMode.HALF_UP)               // redondeo comercial (1.035 -> 1.04)
                .doubleValue();
    }

    /** Caso de prueba como record. */
    record TestCase(String name, List<Transaction> transactions, double expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            return Optional.of(processTransactions(transactions))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s': esperado %.2f, obtenido %.2f".formatted(name, expected, actual));
        }
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
                .map(TestCase::failure)
                .flatMap(Optional::stream)   // Optional.stream() (Java 9): descarta los que pasan
                .toList();                   // Stream.toList() (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}