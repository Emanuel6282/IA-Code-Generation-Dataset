import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TransactionPipeline {

    /** Transacción financiera como record: un portador de datos inmutable. */
    public record Transaction(int id, double amount, String status, String category) {}

    private static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.9");  // comisión del 10 %

    /**
     * Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
     * una comisión del 10 %, y retorna el total redondeado a 2 decimales.
     *
     * <p>Los cálculos se hacen con BigDecimal para evitar los errores de representación
     * de double: por ejemplo, 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con double
     * el producto vale un poco menos y Math.round(x * 100) / 100.0 devuelve 1.03.
     */
    public static double processTransactions(List<Transaction> transactions) {
        var total = BigDecimal.ZERO;
        for (var transaction : transactions) {
            // Patrón de record con guarda `when` en switch (JEP 440 + JEP 441, estándar en
            // Java 21): desestructura la transacción y expresa el filtro (paso 1) en el
            // propio patrón. La rama `case Transaction other` descarta las demás.
            switch (transaction) {
                case Transaction(var id, var amount, var status, var category)
                        when "approved".equals(status) && amount > 0 -> {
                    // 2. Aplicar la comisión. BigDecimal.valueOf(double) parte de la
                    //    representación decimal más corta del double (1.15 -> 1.15).
                    var net = BigDecimal.valueOf(amount).multiply(COMMISSION_FACTOR);
                    // 3. Acumular
                    total = total.add(net);
                }
                case Transaction other -> { }   // rechazada o con monto <= 0: se ignora
            }
        }
        // Redondeo comercial: los .5 se redondean hacia arriba (1.035 -> 1.04).
        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /** Caso de prueba como record. */
    record TestCase(String name, List<Transaction> transactions, double expected) {}

    // Ejecutar con assertions habilitadas: java -ea TransactionPipeline.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = new ArrayList<TestCase>();
        // Caso 1: ejemplo del enunciado (solo la primera está aprobada)
        testCases.add(new TestCase("ejemplo del enunciado", List.of(
                new Transaction(1, 100.0, "approved", "food"),
                new Transaction(2, 200.0, "rejected", "travel")), 90.0));
        // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        testCases.add(new TestCase("sin transacciones válidas", List.of(
                new Transaction(1, 50.0, "rejected", "food"),
                new Transaction(2, 0.0, "approved", "food"),
                new Transaction(3, -30.0, "approved", "refund")), 0.0));
        // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
        testCases.add(new TestCase("redondeo de 1.035", List.of(
                new Transaction(1, 1.15, "approved", "fees")), 1.04));
        testCases.add(new TestCase("suma con decimales", List.of(
                new Transaction(1, 0.3, "approved", "food"),
                new Transaction(2, 0.2, "approved", "food"),
                new Transaction(3, 99.99, "rejected", "food")), 0.45));

        assert processTransactions(List.of()) == 0.0 : "lista vacía -> 0.0";

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            // Record pattern en switch: desestructura el caso sin llamar a los getters.
            switch (testCase) {
                case TestCase(var name, var transactions, var expected) -> {
                    var result = processTransactions(transactions);
                    if (result != expected) {
                        failures.add("Caso '%s': esperado %.2f, obtenido %.2f".formatted(name, expected, result));
                    }
                }
            }
        }

        // Sequenced collections (JEP 431, nuevo en Java 21): getFirst() en lugar de
        // failures.get(0); solo se evalúa si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}