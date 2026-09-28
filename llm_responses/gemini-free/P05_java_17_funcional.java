// Java 17 - Paradigma Funcional Estricto
// Aprovecha Java Records (LTS standard desde Java 16), Stream API declarativo, 
// expresiones lambda y la clase Optional para un procesamiento inmutable y seguro.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransactionPipeline {

    // Modelo inmutable de transacciones utilizando Java Records (Java 16+)
    public record Transaction(int id, double amount, String status, String category) {}

    /**
     * Procesa una lista de transacciones mediante una canalización funcional (Stream API).
     *
     * @param transactions Lista inmutable/mutable de objetos Transaction.
     * @return Suma acumulada de los montos aprobados con el 10% de comisión aplicada, redondeada a 2 decimales.
     */
    public static double processTransactions(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(t -> Optional.ofNullable(t)
                        .map(tx -> "approved".equalsIgnoreCase(tx.status()) && tx.amount() > 0.0)
                        .orElse(false))
                .mapToDouble(t -> t.amount() * 0.9)
                .reduce(Double::sum)
                .map(total -> BigDecimal.valueOf(total)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue())
                .orElse(0.0);
    }

    public static void main(String[] args) {
        // Ejecutar con la opción '-ea' de la JVM para habilitar las aserciones (java -ea TransactionPipeline)

        // Caso 1: Ejemplo base del enunciado (Transacción aprobada vs rechazada)
        List<Transaction> t1 = List.of(
            new Transaction(1, 100.0, "approved", "tech"),
            new Transaction(2, 200.0, "rejected", "food")
        );
        assert processTransactions(t1) == 90.0 : "Falló Caso 1: Debería ser 90.0";

        // Caso 2: Lista vacía, nula o sin transacciones válidas
        assert processTransactions(Collections.emptyList()) == 0.0 : "Falló Caso 2a: Lista vacía";
        assert processTransactions(null) == 0.0 : "Falló Caso 2b: Lista nula";

        List<Transaction> t2 = List.of(
            new Transaction(3, -50.0, "approved", "tech"),
            new Transaction(4, 150.0, "rejected", "books")
        );
        assert processTransactions(t2) == 0.0 : "Falló Caso 2c: Sin transacciones válidas";

        // Caso 3: Múltiples transacciones válidas y precisión de redondeo (HALF_UP)
        List<Transaction> t3 = List.of(
            new Transaction(5, 100.0, "approved", "tech"),
            new Transaction(6, 50.55, "approved", "services"),
            new Transaction(7, 0.0, "approved", "other")
        );
        // Cálculo esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
        assert processTransactions(t3) == 135.5 : "Falló Caso 3: Debería ser 135.5";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}