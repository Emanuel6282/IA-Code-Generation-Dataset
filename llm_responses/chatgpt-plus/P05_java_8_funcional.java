// P05: Transaction Pipeline — Java 8
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS DE JAVA 8 UTILIZADAS:
//
// 1. Stream API:
//    Procesa las transacciones mediante un pipeline funcional.
//
// 2. Expresiones lambda:
//    Definen operaciones de filtrado y transformación.
//
// 3. filter():
//    Selecciona transacciones aprobadas con importe positivo.
//
// 4. mapToDouble():
//    Aplica una comisión del 10 % a cada importe válido.
//
// 5. sum():
//    Reduce los importes netos a un único resultado.
//
// 6. Optional:
//    Representa de forma explícita la presencia o ausencia
//    de un resultado y permite utilizar orElse(0.0).
//
// 7. Referencias a métodos:
//    Utiliza Double::doubleValue.
//
// 8. Inmutabilidad:
//    No se modifican las transacciones originales.
//
// Biblioteca estándar únicamente.
// Sin bucles for/while, acumuladores mutables ni recursión.

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class P05TransactionPipeline_v8 {

    // Modelo inmutable de una transacción.
    static final class Transaction {

        private final int id;
        private final double amount;
        private final String status;
        private final String category;

        Transaction(int id, double amount, String status, String category) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.category = category;
        }

        public double getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }
    }

    // Procesamiento estrictamente funcional.
    public static double processTransactions(
            List<Transaction> transactions) {

        // Pipeline funcional: filtrar, transformar y sumar.
        double total = transactions.stream()

                // 1. Mantener solo transacciones válidas.
                .filter(t -> "approved".equals(t.getStatus()))
                .filter(t -> t.getAmount() > 0)

                // 2. Aplicar comisión del 10 %.
                .mapToDouble(t -> t.getAmount() * 0.9)

                // 3. Sumar los importes netos.
                .sum();

        // 4. Usar Optional para representar el resultado.
        return Optional.of(total)

                // Referencia a método de Java 8.
                .map(Double::doubleValue)

                // 5. Redondear a dos decimales.
                .map(value -> Math.round(value * 100.0) / 100.0)

                // 6. Valor predeterminado.
                .orElse(0.0);
    }

    public static void main(String[] args) {

        // Test 1: Una transacción aprobada y otra rechazada.
        assert processTransactions(Arrays.asList(
                new Transaction(1, 100.0, "approved", "sales"),
                new Transaction(2, 200.0, "rejected", "sales")
        )) == 90.0;

        // Test 2: Dos aprobadas y una con importe negativo.
        assert processTransactions(Arrays.asList(
                new Transaction(1, 100.0, "approved", "sales"),
                new Transaction(2, 50.0, "approved", "services"),
                new Transaction(3, -20.0, "approved", "refund")
        )) == 135.0;

        // Test 3: Lista vacía.
        assert processTransactions(
                Collections.<Transaction>emptyList()
        ) == 0.0;

        System.out.println("All tests passed");
    }
}