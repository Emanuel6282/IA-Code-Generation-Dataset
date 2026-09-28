// Java 17 - Paradigma Funcional con Records y Stream API
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class TransactionPipeline {

    // Modelo inmutable utilizando Java Records
    public record Transaction(int id, double amount, String status, String category) {}

    /**
     * Procesa una lista de transacciones filtrando por 'approved' y monto positivo,
     * aplicando un 10% de comisión y retornando el total redondeado a 2 decimales.
     */
    public static double processTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 0.0;
        }

        double total = transactions.stream()
                // 1. Filtrar: status == "approved" Y amount > 0
                .filter(t -> "approved".equalsIgnoreCase(t.status()) && t.amount() > 0)
                // 2. Aplicar: comisión del 10% (monto * 0.9)
                .mapToDouble(t -> t.amount() * 0.9)
                // 3. Sumar valores resultantes
                .sum();

        // Redondear a 2 decimales usando BigDecimal para precisión numérica
        return BigDecimal.valueOf(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con la opción '-ea' (java -ea TransactionPipeline)

        // Caso 1: Ejemplo base del enunciado (Aprobada vs Rechazada)
        List<Transaction> t1 = List.of(
            new Transaction(1, 100.0, "approved", "tech"),
            new Transaction(2, 200.0, "rejected", "food")
        );
        assert processTransactions(t1) == 90.0 : "Falló Caso 1";

        // Caso 2: Lista vacía o sin transacciones válidas
        assert processTransactions(Collections.emptyList()) == 0.0 : "Falló Caso 2: Lista vacía";
        List<Transaction> t2 = List.of(
            new Transaction(3, -50.0, "approved", "tech"),
            new Transaction(4, 150.0, "rejected", "books")
        );
        assert processTransactions(t2) == 0.0 : "Falló Caso 2: Sin válidas";

        // Caso 3: Múltiples válidas y precisión de redondeo
        List<Transaction> t3 = List.of(
            new Transaction(5, 100.0, "approved", "tech"),
            new Transaction(6, 50.55, "approved", "services"),
            new Transaction(7, 0.0, "approved", "other")
        );
        // Esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> 135.5
        assert processTransactions(t3) == 135.5 : "Falló Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}