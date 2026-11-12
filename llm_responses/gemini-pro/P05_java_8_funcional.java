// CARACTERISTICAS DE JAVA 8 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Stream API (java.util.stream): Transforma el procesamiento de colecciones de un modelo de iteración externa (bucles) a un modelo de iteración interna declarativo y perezozo (lazy).
// 2. Expresiones Lambda (->): Funciones anónimas que permiten pasar comportamiento como parámetro a las operaciones de orden superior (filter, map).
// 3. Optional<T> y Reducción Funcional: Se reemplaza la acumulación en variables mutables usando `reduce()`, el cual retorna un `Optional<Double>`. La mónada `Optional` nos permite encadenar el redondeo con `map()` de forma segura y proveer un valor por defecto puro con `orElse(0.0)` si el flujo termina vacío, eliminando verificaciones de nulidad imperativas.
// 4. Ausencia de Estado Mutable: Sin variables 'total' ni asignaciones incrementales iterativas, garantizando la pureza referencial.

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Optional;

// Estructura inmutable del dato (Data Record equivalente pre-Java 14)
class Transaction {
    final int id;
    final double amount;
    final String status;
    final String category;

    public Transaction(int id, double amount, String status, String category) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.category = category;
    }
}

public class P05TransactionPipeline_v8_Functional {

    /**
     * Procesa una lista de transacciones usando el paradigma funcional de Java 8.
     */
    public static double processTransactions(List<Transaction> transactions) {
        // Validación para evitar NullPointerException si la lista es nula 
        // (comportamiento defensivo funcional usando Optional inicial)
        return Optional.ofNullable(transactions)
            // Convertimos la lista segura a su Stream
            .map(List::stream)
            // Si la lista era nula, retornamos un Stream vacío
            .orElseGet(java.util.stream.Stream::empty)
            
            // 1. Filter: Predicado puro evaluando estatus y monto
            .filter(tx -> "approved".equals(tx.status) && tx.amount > 0)
            
            // 2. Map: Transformación del objeto transacción a su valor comisionado (Double)
            .map(tx -> tx.amount * 0.9)
            
            // 3. Reduce: Combinador asociativo sin estado (fold)
            .reduce(Double::sum) // Retorna un Optional<Double>
            
            // Operaciones sobre la mónada Optional:
            // Mapeamos el resultado acumulado (si existe) para redondear a 2 decimales
            .map(sum -> Math.round(sum * 100.0) / 100.0)
            
            // Extraemos el valor o retornamos 0.0 si el flujo original fue filtrado completamente
            .orElse(0.0);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: filtrado correcto (descarta rejected) y comisión
        assert processTransactions(Arrays.asList(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        )) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista vacía, lista nula y montos negativos/nulos
        assert processTransactions(new ArrayList<>()) == 0.0 : "Error: Lista vacía esperado 0.0";
        assert processTransactions(null) == 0.0 : "Error: Manejo funcional de nulos falló";
        assert processTransactions(Arrays.asList(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        )) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión y acumulación múltiple funcional
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma reducida = 27.594 -> Redondeo Optional map = 27.59
        assert processTransactions(Arrays.asList(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        )) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}