// Java 17 - Paradigma Iterativo Estricto
// Solución basada exclusivamente en control de flujo imperativo (bucles for-each, variables mutables)
// e Inferencia de Tipos con 'var' y Pattern Matching para 'instanceof' (características modernas de Java 10+ y 16+).

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionPipeline {

    /**
     * Procesa una lista de transacciones utilizando iteración imperativa y estado mutable.
     * 
     * @param transactions Lista de mapas que representan las transacciones.
     * @return Suma acumulada con un 10% de comisión aplicada, redondeada a 2 decimales.
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 0.0;
        }

        var totalAmount = 0.0; // Inferencia de tipo local (Java 10+)

        // Bucle for-each imperativo tradicional (sin Streams ni Lambdas)
        for (var tx : transactions) {
            if (tx == null) {
                continue;
            }

            var statusObj = tx.get("status");
            var amountObj = tx.get("amount");

            // Pattern Matching para instanceof (Java 16+ / estándar en Java 17)
            if (statusObj instanceof String status && amountObj instanceof Number number) {
                var amount = number.doubleValue();

                // 1. Filtrar: status == "approved" Y amount > 0
                if ("approved".equalsIgnoreCase(status) && amount > 0.0) {
                    // 2. Aplicar: comisión del 10% (monto * 0.9)
                    var discountedAmount = amount * 0.9;

                    // Acumulación de estado explícita
                    totalAmount += discountedAmount;
                }
            }
        }

        // 3. Redondear a 2 decimales usando BigDecimal
        return BigDecimal.valueOf(totalAmount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static void main(String[] args) {
        // Ejecutar con la opción '-ea' de la JVM para habilitar las aserciones (java -ea TransactionPipeline)

        // Caso 1: Ejemplo del enunciado (Aprobada vs Rechazada)
        var t1 = new ArrayList<Map<String, Object>>();

        var tx1 = new HashMap<String, Object>();
        tx1.put("id", 1);
        tx1.put("amount", 100.0);
        tx1.put("status", "approved");
        tx1.put("category", "tech");
        t1.add(tx1);

        var tx2 = new HashMap<String, Object>();
        tx2.put("id", 2);
        tx2.put("amount", 200.0);
        tx2.put("status", "rejected");
        tx2.put("category", "food");
        t1.add(tx2);

        assert processTransactions(t1) == 90.0 : "Falló Caso 1: Debería ser 90.0";

        // Caso 2: Lista vacía, nula o sin transacciones válidas
        assert processTransactions(List.of()) == 0.0 : "Falló Caso 2a: Lista vacía";
        assert processTransactions(null) == 0.0 : "Falló Caso 2b: Lista nula";

        var t2Invalidas = new ArrayList<Map<String, Object>>();
        var tx3 = new HashMap<String, Object>();
        tx3.put("id", 3);
        tx3.put("amount", -50.0);
        tx3.put("status", "approved");
        tx3.put("category", "tech");
        t2Invalidas.add(tx3);

        var tx4 = new HashMap<String, Object>();
        tx4.put("id", 4);
        tx4.put("amount", 150.0);
        tx4.put("status", "rejected");
        tx4.put("category", "books");
        t2Invalidas.add(tx4);

        assert processTransactions(t2Invalidas) == 0.0 : "Falló Caso 2c: Sin válidas";

        // Caso 3: Múltiples válidas y precisión de redondeo HALF_UP
        var t3 = List.of(
            Map.<String, Object>of("id", 5, "amount", 100.0, "status", "approved", "category", "tech"),
            Map.<String, Object>of("id", 6, "amount", 50.55, "status", "approved", "category", "services"),
            Map.<String, Object>of("id", 7, "amount", 0.0, "status", "approved", "category", "other")
        );
        // Esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
        assert processTransactions(t3) == 135.5 : "Falló Caso 3: Debería ser 135.5";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}