// P05: Transaction Pipeline — Java 17
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. record (Java 16, disponible en Java 17):
//    Define transacciones con campos finales y
//    accesores generados automáticamente.
//
// 2. Stream API (Java 8):
//    Permite procesar transacciones declarativamente.
//
// 3. Expresiones lambda (Java 8):
//    Implementan filtrado y transformación sin bucles.
//
// 4. filter() y mapToDouble():
//    Seleccionan transacciones válidas y aplican
//    la comisión del 10 %.
//
// 5. OptionalDouble (Java 8):
//    Representa el resultado de una reducción
//    que puede no contener valores.
//
// 6. Optional (Java 8):
//    Permite transformar el resultado y proporcionar
//    un valor predeterminado con orElse().
//
// 7. List.of() (Java 9):
//    Construye listas inmutables para las pruebas.
//
// NOTA:
// record es una característica moderna disponible
// en Java 17. Streams, lambdas y Optional son de Java 8.
//
// Biblioteca estándar únicamente.
// Sin for/while, estado mutable ni recursión.

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class P05TransactionPipeline_v17 {

    // Modelo inmutable de una transacción.
    public record Transaction(
            int id,
            double amount,
            String status,
            String category
    ) {}

    // Función de procesamiento estrictamente funcional.
    public static double processTransactions(
            List<Transaction> transactions) {

        // Pipeline funcional: filtrar, transformar y reducir.
        OptionalDouble total = transactions.stream()

                // 1. Filtrar transacciones aprobadas.
                .filter(t -> "approved".equals(t.status()))

                // 2. Filtrar importes positivos.
                .filter(t -> t.amount() > 0)

                // 3. Aplicar comisión del 10 %.
                .mapToDouble(t -> t.amount() * 0.9)

                // 4. Reducir sin valor inicial.
                .reduce(Double::sum);

        // 5. Convertir OptionalDouble a Optional<Double>,
        //    redondear y manejar el caso vacío.
        return Optional.of(total)
                .filter(OptionalDouble::isPresent)
                .map(OptionalDouble::getAsDouble)
                .map(value -> Math.round(value * 100.0) / 100.0)
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