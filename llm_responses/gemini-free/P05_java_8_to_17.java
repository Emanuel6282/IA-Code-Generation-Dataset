// CARACTERÍSTICAS DE JAVA 17 UTILIZADAS Y RAZONES:
// 1. Records (Java 16 LTS): Reemplaza el Map<String, Object> genérico por un tipo fuertemente tipado e inmutable,
//    lo que elimina la necesidad de castings manuales ((String), (Number)) y previene errores en tiempo de ejecución.
// 2. Pattern Matching para instanceof (Java 16 LTS): Permite verificar y extraer el tipo de objeto en una sola operación sin casteos inseguros.
// 3. Stream API y Expresiones Lambda (Evolucionado en Java 16 con métodos más limpios):
//    Procesamiento declarativo sin bucles for ni variables mutables de estado.
// 4. Inferencia de tipos con 'var' (Java 10 LTS): Reduce el código repetitivo en la declaración de variables locales.
// 5. List.of() (Java 9): Fábrica de colecciones inmutables más concisa que Arrays.asList() o Collections.singletonList().
// 6. BigDecimal para Redondeo Seguro: Soluciona imprecisiones de coma flotante inherentes a Math.round(total * 100.0) / 100.0.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class P05TransactionPipeline_v17 {

    // Record inmutable para modelar la transacción de forma idiomática en Java 17
    public record Transaction(int id, double amount, String status, String category) {
        
        // Factory method auxiliar para mantener compatibilidad con mapas (Map<String, Object>) si provienen de APIs legacy
        public static Transaction fromMap(Map<String, Object> map) {
            if (map == null) return null;
            
            int id = map.get("id") instanceof Number n ? n.intValue() : 0;
            double amount = map.get("amount") instanceof Number n ? n.doubleValue() : 0.0;
            String status = map.get("status") instanceof String s ? s : "";
            String category = map.get("category") instanceof String c ? c : "";
            
            return new Transaction(id, amount, status, category);
        }
    }

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comisión del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        return Optional.ofNullable(transactions)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(map -> map != null)
                .map(Transaction::fromMap)
                .filter(tx -> "approved".equalsIgnoreCase(tx.status()) && tx.amount() > 0.0)
                .mapToDouble(tx -> tx.amount() * 0.9)
                .reduce(Double::sum)
                .map(total -> BigDecimal.valueOf(total)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue())
                .orElse(0.0);
    }

    private static Map<String, Object> tx(int id, double amount, String status, String cat) {
        return Map.of(
            "id", id,
            "amount", amount,
            "status", status,
            "category", cat
        );
    }

    public static void main(String[] args) {
        // TC01: filtrar negativo y rechazado, aplicar fee
        var txs1 = List.of(
            tx(1, 100.0, "approved", "income"),
            tx(2, -50.0, "approved", "income"),
            tx(3, 200.0, "rejected", "income"),
            tx(4, 150.0, "approved", "expense")
        );
        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01 : "TC01 fallo";

        // TC02: lista vacia
        assert processTransactions(Collections.emptyList()) == 0.0 : "TC02 fallo";

        // TC03: un approved
        var txs3 = List.of(tx(1, 1000.0, "approved", "x"));
        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01 : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
 ====================================================================================
 RESUMEN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17:
 ====================================================================================
 1. Modelado con Records (Java 16+):
    En Java 8 era común usar Map<String, Object> para representar estructuras de datos
    desestructuradas. En Java 17, el uso de 'record' proporciona inmutabilidad,
    tipado fuerte y constructores/getters limpios de forma nativa.

 2. Reemplazo de casting imperativo por Pattern Matching para instanceof (Java 16+):
    Se reemplaza el estilo inseguro:
       ((Number) tx.get("amount")).doubleValue()
    por:
       map.get("amount") instanceof Number n ? n.doubleValue() : 0.0
    Esto evita Runtime Exceptions (ClassCastException o NullPointerException).

 3. Pipeline de Stream API vs Bucle for-each:
    Se convierte el procesamiento imperativo con estados mutables (`double total = 0.0;`) 
    a un pipeline de funciones puras (`filter`, `mapToDouble`, `reduce`). Esto mejora la
    legibilidad y facilita un posible paralelismo seguro con `.parallelStream()`.

 4. List.of() en reemplazo de Arrays.asList() / Collections.singletonList():
    `List.of()` crea colecciones inmutables de forma más clara, segura y ligera
    en memoria que las factorías de Java 8.

 5. Redondeo preciso con BigDecimal:
    `Math.round(total * 100.0) / 100.0` puede sufrir de errores de coma flotante (IEEE 754).
    El uso de `BigDecimal.setScale(2, RoundingMode.HALF_UP)` garantiza precisión financiera.
 ====================================================================================
*/