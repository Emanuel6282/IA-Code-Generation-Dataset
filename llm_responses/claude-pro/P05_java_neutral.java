import java.math.BigDecimal;
import java.math.RoundingMode;
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
            //    decimal más corta del double (0.95 -> 0.95), no de su valor binario exacto.
            BigDecimal net = BigDecimal.valueOf(t.amount).multiply(COMMISSION_FACTOR);
            // 3. Acumular
            total = total.add(net);
        }
        // Redondeo comercial: los .5 se redondean hacia arriba (0.855 -> 0.86).
        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Ejecutar con assertions habilitadas: java -ea TransactionPipeline.java
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado (solo la primera está aprobada)
        assert processTransactions(List.of(
                new Transaction(1, 100.0, "approved", "food"),
                new Transaction(2, 200.0, "rejected", "travel"))) == 90.0;

        // Caso 2: lista vacía y lista sin transacciones válidas (rechazada, cero, negativa)
        assert processTransactions(List.of()) == 0.0;
        assert processTransactions(List.of(
                new Transaction(1, 50.0, "rejected", "food"),
                new Transaction(2, 0.0, "approved", "food"),
                new Transaction(3, -30.0, "approved", "refund"))) == 0.0;

        // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
        assert processTransactions(List.of(new Transaction(1, 1.15, "approved", "fees"))) == 1.04;
        assert processTransactions(List.of(
                new Transaction(1, 0.3, "approved", "food"),
                new Transaction(2, 0.2, "approved", "food"),
                new Transaction(3, 99.99, "rejected", "food"))) == 0.45;

        System.out.println("Todas las pruebas pasaron.");
    }
}