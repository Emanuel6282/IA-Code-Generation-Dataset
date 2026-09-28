// Java 8 - Paradigma Iterativo Estricto
// Solución sin Streams, Lambdas ni Recursión. Usa bucles imperativos con acumulación explícita.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionPipeline {

    /**
     * Procesa una lista de transacciones usando iteración imperativa.
     * 
     * @param transactions Lista de transacciones representadas como Map<String, Object>
     * @return Suma acumulada de las transacciones válidas con un 10% de descuento, redondeada a 2 decimales.
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return 0.0;
        }

        double totalAmount = 0.0;

        // Iteración explícita con bucle for-each tradicional
        for (Map<String, Object> tx : transactions) {
            if (tx == null) {
                continue;
            }

            // Extracción segura de valores
            Object statusObj = tx.get("status");
            Object amountObj = tx.get("amount");

            if (statusObj instanceof String && amountObj instanceof Number) {
                String status = (String) statusObj;
                double amount = ((Number) amountObj).doubleValue();

                // 1. Filtrar: status == "approved" Y amount > 0
                if ("approved".equals(status) && amount > 0) {
                    // 2. Aplicar: comisión del 10% (monto * 0.9)
                    double discountedAmount = amount * 0.9;

                    // Acumulación imperativa con estado mutable
                    totalAmount = totalAmount + discountedAmount;
                }
            }
        }

        // 3. Retornar total redondeado a 2 decimales mediante BigDecimal (Java 8 standard)
        BigDecimal bd = new BigDecimal(Double.toString(totalAmount));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void main(String[] args) {
        // Ejecutar con la opción '-ea' habilitada (java -ea TransactionPipeline)

        // Caso 1: Transacción aprobada vs rechazada (Ejemplo del enunciado)
        List<Map<String, Object>> t1 = new ArrayList<>();
        
        Map<String, Object> tx1 = new HashMap<>();
        tx1.put("id", 1);
        tx1.put("amount", 100.0);
        tx1.put("status", "approved");
        tx1.put("category", "tech");
        t1.add(tx1);

        Map<String, Object> tx2 = new HashMap<>();
        tx2.put("id", 2);
        tx2.put("amount", 200.0);
        tx2.put("status", "rejected");
        tx2.put("category", "food");
        t1.add(tx2);

        assert processTransactions(t1) == 90.0 : "Falló Caso 1: Debería ser 90.0";

        // Caso 2: Lista vacía o sin transacciones válidas
        List<Map<String, Object>> t2Vacia = new ArrayList<>();
        assert processTransactions(t2Vacia) == 0.0 : "Falló Caso 2a: Lista vacía debe retornar 0.0";

        List<Map<String, Object>> t2Invalidas = new ArrayList<>();
        Map<String, Object> tx3 = new HashMap<>();
        tx3.put("id", 3);
        tx3.put("amount", -50.0);
        tx3.put("status", "approved");
        tx3.put("category", "tech");
        t2Invalidas.add(tx3);

        Map<String, Object> tx4 = new HashMap<>();
        tx4.put("id", 4);
        tx4.put("amount", 150.0);
        tx4.put("status", "rejected");
        tx4.put("category", "books");
        t2Invalidas.add(tx4);

        assert processTransactions(t2Invalidas) == 0.0 : "Falló Caso 2b: Sin transacciones válidas debe retornar 0.0";

        // Caso 3: Múltiples transacciones válidas y redondeo
        List<Map<String, Object>> t3 = new ArrayList<>();
        
        Map<String, Object> tx5 = new HashMap<>();
        tx5.put("id", 5);
        tx5.put("amount", 100.0);
        tx5.put("status", "approved");
        tx5.put("category", "tech");
        t3.add(tx5);

        Map<String, Object> tx6 = new HashMap<>();
        tx6.put("id", 6);
        tx6.put("amount", 50.55);
        tx6.put("status", "approved");
        tx6.put("category", "services");
        t3.add(tx6);

        // Cálculo esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> 135.5
        assert processTransactions(t3) == 135.5 : "Falló Caso 3: Debería ser 135.5";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}