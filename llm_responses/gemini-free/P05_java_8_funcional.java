// Java 8 - Paradigma Funcional Estricto
// Solución declarativa sin bucles for/while imperativos.
// Emplea Stream API, expresiones lambda, referencias a métodos, Optional y BigDecimal para precisión de redondeo.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionPipeline {

    /**
     * Procesa una lista de transacciones utilizando un pipeline declarativo puro con Stream API.
     *
     * @param transactions Lista de mapas que representan las transacciones financieras.
     * @return Suma de montos aprobados con comisión del 10% aplicada, redondeada a 2 decimales.
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        return Optional.ofNullable(transactions)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(tx -> Optional.ofNullable(tx)
                        .map(m -> "approved".equalsIgnoreCase(String.valueOf(m.get("status"))))
                        .orElse(false))
                .map(tx -> Optional.ofNullable(tx.get("amount"))
                        .filter(amt -> amt instanceof Number)
                        .map(amt -> ((Number) amt).doubleValue())
                        .orElse(0.0))
                .filter(amount -> amount > 0.0)
                .map(amount -> amount * 0.9)
                .reduce(Double::sum)
                .map(total -> BigDecimal.valueOf(total)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue())
                .orElse(0.0);
    }

    public static void main(String[] args) {
        // Ejecutar con la opción '-ea' de la JVM para habilitar las aserciones (java -ea TransactionPipeline)

        // Caso 1: Ejemplo base del enunciado (Aprobada vs Rechazada)
        Map<String, Object> tx1 = new HashMap<>();
        tx1.put("id", 1);
        tx1.put("amount", 100.0);
        tx1.put("status", "approved");
        tx1.put("category", "tech");

        Map<String, Object> tx2 = new HashMap<>();
        tx2.put("id", 2);
        tx2.put("amount", 200.0);
        tx2.put("status", "rejected");
        tx2.put("category", "food");

        List<Map<String, Object>> t1 = Arrays.asList(tx1, tx2);
        assert processTransactions(t1) == 90.0 : "Falló Caso 1: Debe retornar 90.0";

        // Caso 2: Lista vacía o sin transacciones válidas
        assert processTransactions(Collections.emptyList()) == 0.0 : "Falló Caso 2a: Lista vacía debe ser 0.0";
        assert processTransactions(null) == 0.0 : "Falló Caso 2b: Lista nula debe ser 0.0";

        Map<String, Object> tx3 = new HashMap<>();
        tx3.put("id", 3);
        tx3.put("amount", -50.0);
        tx3.put("status", "approved");
        tx3.put("category", "tech");

        Map<String, Object> tx4 = new HashMap<>();
        tx4.put("id", 4);
        tx4.put("amount", 150.0);
        tx4.put("status", "rejected");
        tx4.put("category", "books");

        List<Map<String, Object>> t2 = Arrays.asList(tx3, tx4);
        assert processTransactions(t2) == 0.0 : "Falló Caso 2c: Sin válidas debe retornar 0.0";

        // Caso 3: Múltiples transacciones válidas y redondeo
        Map<String, Object> tx5 = new HashMap<>();
        tx5.put("id", 5);
        tx5.put("amount", 100.0);
        tx5.put("status", "approved");
        tx5.put("category", "tech");

        Map<String, Object> tx6 = new HashMap<>();
        tx6.put("id", 6);
        tx6.put("amount", 50.55);
        tx6.put("status", "approved");
        tx6.put("category", "services");

        Map<String, Object> tx7 = new HashMap<>();
        tx7.put("id", 7);
        tx7.put("amount", 0.0);
        tx7.put("status", "approved");
        tx7.put("category", "other");

        List<Map<String, Object>> t3 = Arrays.asList(tx5, tx6, tx7);
        // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
        assert processTransactions(t3) == 135.5 : "Falló Caso 3: Debe retornar 135.5";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}