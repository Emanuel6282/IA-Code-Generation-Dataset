import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    /**
     * Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
     * una comisión del 10 %, y retorna el total redondeado a 2 decimales.
     *
     * <p>Los cálculos se hacen con BigDecimal para evitar los errores de representación
     * de double: por ejemplo, 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con double
     * el producto vale un poco menos y Math.round(x * 100) / 100.0 devuelve 1.03.
     */
    public static double processTransactions(List<Transaction> transactions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            // 1. Filtrar ("approved".equals(...) evita un NullPointerException si status es null)
            if (!"approved".equals(t.status) || t.amount <= 0) {
                continue;
            }
            // 2. Aplicar la comisión. BigDecimal.valueOf(double) parte de la representación
            //    decimal más corta del double (1.15 -> 1.15), no de su valor binario exacto.
            BigDecimal net = BigDecimal.valueOf(t.amount).multiply(COMMISSION_FACTOR);
            // 3. Acumular
            total = total.add(net);
        }
        // Redondeo comercial: los .5 se redondean hacia arriba (1.035 -> 1.04).
        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final List<Transaction> transactions;
        final double expected;

        TestCase(String name, List<Transaction> transactions, double expected) {
            this.name = name;
            this.transactions = transactions;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac TransactionPipeline.java && java -ea TransactionPipeline
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: ejemplo del enunciado (solo la primera está aprobada)
        testCases.add(new TestCase("ejemplo del enunciado", Arrays.asList(
                new Transaction(1, 100.0, "approved", "food"),
                new Transaction(2, 200.0, "rejected", "travel")), 90.0));
        // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        testCases.add(new TestCase("sin transacciones válidas", Arrays.asList(
                new Transaction(1, 50.0, "rejected", "food"),
                new Transaction(2, 0.0, "approved", "food"),
                new Transaction(3, -30.0, "approved", "refund")), 0.0));
        // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
        testCases.add(new TestCase("redondeo de 1.035", Arrays.asList(
                new Transaction(1, 1.15, "approved", "fees")), 1.04));
        testCases.add(new TestCase("suma con decimales", Arrays.asList(
                new Transaction(1, 0.3, "approved", "food"),
                new Transaction(2, 0.2, "approved", "food"),
                new Transaction(3, 99.99, "rejected", "food")), 0.45));

        assert processTransactions(Collections.<Transaction>emptyList()) == 0.0 : "lista vacía -> 0.0";

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            double result = processTransactions(testCase.transactions);
            if (result != testCase.expected) {
                failures.add("Caso '" + testCase.name + "': esperado " + testCase.expected
                        + ", obtenido " + result);
            }
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}