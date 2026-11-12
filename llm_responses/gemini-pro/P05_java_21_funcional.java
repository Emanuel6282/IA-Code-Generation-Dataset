// CARACTERISTICAS DE JAVA 21 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Records (Data-Oriented Programming): Se utiliza `record` para garantizar la inmutabilidad de la estructura. En Java 21, los records son ciudadanos de primera clase y la base de la programación orientada a datos.
// 2. Estabilidad del Stream API (Evaluación Académica): A diferencia del paradigma iterativo (donde Java 21 introdujo el revolucionario Pattern Matching en los bucles y switches), el pipeline funcional puro (Stream API) permanece sintácticamente estable respecto a Java 17. Esto se debe a que Java 21 aún no soporta Record Patterns directamente en los argumentos de las expresiones lambda (ej. `txs.stream().filter(Transaction(_, amount, status, _) -> ...)` no es válido todavía).
// 3. SequencedCollections (JEP 431): Aunque aquí usamos el genérico `List`, en Java 21 hereda orgánicamente de `SequencedCollection`. El polimorfismo subyacente de la colección procesada es ahora más robusto bajo el capó.
// 4. Paradigma Funcional Estricto: Ausencia total de sentencias `for`, `while` o variables mutables. Construcción perezosa declarativa usando `Optional`, `filter`, `map` y `reduce`.

import java.util.List;
import java.util.Optional;

// Estructura inmutable nativa sin setters, ideal para flujos puros
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v21_Functional {

    /**
     * Procesa una lista de transacciones usando un pipeline funcional estricto en Java 21.
     */
    public static double processTransactions(List<Transaction> transactions) {
        // Envolvemos la colección en una mónada Optional para evitar NullPointerException
        return Optional.ofNullable(transactions)
            // Si la lista es null, inyectamos una lista inmutable vacía
            .orElseGet(List::of)
            .stream()
            
            // 1. Filtrar: Predicado puro (inmutabilidad referencial)
            .filter(tx -> "approved".equals(tx.status()) && tx.amount() > 0)
            
            // 2. Aplicar: Transformación matemática (Map)
            .map(tx -> tx.amount() * 0.9)
            
            // 3. Reduce: Plegado del stream a un solo valor (Combinador sin estado)
            // Esto retorna un Optional<Double> que nos protege de flujos que queden vacíos
            .reduce(Double::sum)
            
            // Operación monádica: Mapeamos el Optional resultante para aplicar el redondeo
            // Esto solo se ejecuta si el reduce encontró elementos
            .map(sum -> Math.round(sum * 100.0) / 100.0)
            
            // Extracción final o identidad matemática por defecto
            .orElse(0.0);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: filtrado correcto (descarta rejected) y aplicación de comisión
        var test1 = List.of(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        );
        assert processTransactions(test1) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista nula, lista vacía y montos negativos/nulos
        assert processTransactions(null) == 0.0 : "Error: Fallo en manejo funcional de nulos";
        assert processTransactions(List.of()) == 0.0 : "Error: Lista vacía esperado 0.0";
        
        var test2 = List.of(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        );
        assert processTransactions(test2) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión matemática y acumulación múltiple funcional
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma reducida = 27.594 -> Redondeo en map = 27.59
        var test3 = List.of(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        );
        assert processTransactions(test3) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}