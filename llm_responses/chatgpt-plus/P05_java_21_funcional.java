// P05: Transaction Pipeline — Java 21
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Pattern Matching para switch (Java 21, JEP 441):
//    Permite reconocer tipos numéricos mediante patrones.
//
// 2. record (Java 16, disponible en Java 21):
//    Define un modelo de transacción inmutable.
//
// 3. Stream API y expresiones lambda (Java 8):
//    Implementan filtrado, transformación y reducción.
//
// 4. OptionalDouble (Java 8):
//    Representa una suma que puede no tener elementos.
//
// 5. List.of() (Java 9):
//    Permite construir listas inmutables para las pruebas.
//
// 6. Math.round():
//    Redondea el resultado a dos decimales.
//
// NOTA:
// Pattern Matching para switch es una característica
// estable de Java 21. Las demás características
// indicadas se introdujeron en versiones anteriores.
//
// Biblioteca estándar únicamente.
// Sin bucles for/while, mutaciones ni recursión.

import java.util.List;
import java.util.OptionalDouble;

public class P05TransactionPipeline_v21 {

    // Modelo inmutable de una transacción.
    public record Transaction(
            int id,
            Number amount,
            String status,
            String category
    ) {}

    // Conversión funcional mediante pattern matching.
    private static double toDouble(Number amount) {
        return switch (amount) {
            case Integer value -> value.doubleValue();
            case Long value -> value.doubleValue();
            case Float value -> value.doubleValue();
            case Double value -> value.doubleValue();
            case null -> 0.0;
            default -> amount.doubleValue();
        };
    }

    // Procesamiento estrictamente funcional.
    public static double processTransactions(
            List<Transaction> transactions) {

        // Pipeline funcional: filtrar, transformar y reducir.
        OptionalDouble total = transactions.stream()

                // 1. Mantener solo transacciones aprobadas.
                .filter(t -> "approved".equals(t.status()))

                // 2. Mantener solo importes positivos.
                .filter(t -> toDouble(t.amount()) > 0)

                // 3. Aplicar una comisión del 10 %.
                .mapToDouble(t -> toDouble(t.amount()) * 0.9)

                // 4. Sumar mediante reducción funcional.
                .reduce(Double::sum);

        // 5. Redondear o devolver 0.0 si no hay valores.
        return total.stream()
                .map(value -> Math.round(value * 100.0) / 100.0)
                .findFirst()
                .orElse(0.0);
    }

    public static void main(String[] args) {

        // Test 1: Una aprobada y otra rechazada.
        assert processTransactions(List.of(
                new Transaction(1, 100.0, "approved", "sales"),
                new Transaction(2, 200.0, "rejected", "sales")
        )) == 90.0;

        // Test 2: Dos aprobadas y una con importe negativo.
        assert processTransactions(List.of(
                new Transaction(1, 100.0, "approved", "sales"),
                new Transaction(2, 50.0, "approved", "services"),
                new Transaction(3, -20.0, "approved", "refund")
        )) == 135.0;

        // Test 3: Lista vacía.
        assert processTransactions(List.of()) == 0.0;

        System.out.println("All tests passed");
    }
}