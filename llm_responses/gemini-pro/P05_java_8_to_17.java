// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Records (JEP 395): Se reemplazó el uso frágil y verboso de `Map<String, Object>` por un `record`. En Java 17, los records son la forma estándar e idiomática de representar "diccionarios de datos" u objetos inmutables. Esto elimina la necesidad de casteos inseguros (como `(String)` o `((Number)...).doubleValue()`) y mejora drásticamente el rendimiento y la legibilidad.
// 2. Inferencia de Tipos de Variables Locales (var - JEP 286): Se utiliza `var` para reducir el ruido visual en la declaración de las listas de prueba y el bucle for-each, permitiendo que el compilador infiera el tipo exacto de los datos.
// 3. Fábricas de Colecciones Inmutables (List.of - Java 9+): Se reemplazó la verbosidad de `Arrays.asList()`, `Collections.emptyList()` y `Collections.singletonList()` por `List.of()`, creando colecciones inmutables de forma mucho más directa y segura.

import java.util.List;

// Java 17: Estructura de datos inmutable nativa para reemplazar el Map
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v17 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Transaction> transactions) {
        var total = 0.0;
        
        for (var tx : transactions) {
            // Acceso fuertemente tipado mediante los métodos generados por el Record
            if ("approved".equals(tx.status()) && tx.amount() > 0) {
                total += tx.amount() * 0.9;
            }
        }
        
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar con el flag -ea (enable assertions)
        // ==========================================

        // TC01: filtrar negativo y rechazado, aplicar fee
        var txs1 = List.of(
            new Transaction(1, 100.0, "approved", "income"),
            new Transaction(2, -50.0, "approved", "income"),
            new Transaction(3, 200.0, "rejected", "income"),
            new Transaction(4, 150.0, "approved", "expense")
        );
        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01 : "TC01 fallo";

        // TC02: lista vacia
        assert processTransactions(List.of()) == 0.0 : "TC02 fallo";

        // TC03: un approved
        var txs3 = List.of(new Transaction(1, 1000.0, "approved", "x"));
        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01 : "TC03 fallo";

        System.out.println("All tests passed");
    }
}