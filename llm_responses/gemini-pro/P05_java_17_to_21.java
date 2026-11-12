// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Records (JEP 395): Se erradica por completo el uso frágil de `Map<String, Object>` para representar entidades, sustituyéndolo por un `record`. Esto provee tipado estático fuerte, inmutabilidad por defecto y elimina los riesgosos casteos en tiempo de ejecución.
// 2. Pattern Matching for switch (JEP 441) y Record Patterns (JEP 440): En Java 21, el `switch` deja de ser un simple selector de constantes. Ahora desestructura el `record` `Transaction` extrayendo sus variables internas (`amount`, `status`) directamente en el nivel de enrutamiento.
// 3. Guardas con 'when': Permite incorporar la lógica de negocio (status aprobado y monto positivo) directamente en el `case` del switch, fusionando el control de flujo estructural y la validación matemática en una sola operación atómica.
// 4. Inferencia de Tipos (var) y List.of(): Para limpiar el código cliente y las pruebas, usando colecciones inmutables compactas.

import java.util.List;

// Java 21: Definición de la entidad como un Record
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v21 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Transaction> transactions) {
        var total = 0.0;
        
        for (var tx : transactions) {
            // Java 21: Pattern Matching avanzado en switch
            switch (tx) {
                // Desestructuración del objeto y evaluación de la guarda 'when'
                case Transaction(int id, double amount, String status, String cat) 
                     when "approved".equals(status) && amount > 0 -> {
                    
                    total += amount * 0.9;
                }
                // Manejo de objetos nulos y transacciones que no coinciden con el patrón/guarda
                case null, default -> {
                    // No-op: la iteración continúa
                }
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