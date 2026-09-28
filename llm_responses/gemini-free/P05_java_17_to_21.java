// CARACTERÍSTICAS DE JAVA 21 UTILIZADAS Y RAZONES:
// 1. Record Patterns (JEP 440): Desestructuración directa de Java Records para extraer campos (id, amount, status, category)
//    de forma expresiva y segura en el pipeline.
// 2. Pattern Matching para switch con Guardas 'when' (JEP 441): Evaluación concisa de cadenas e inspección de valores nulos
//    o variaciones de estados (ej. "approved") eliminando if/else o casteos.
// 3. Sequenced Collections (JEP 431): Interfaz estandarizada para colecciones con orden de encuentro definido.
// 4. Java Records (JEP 395 - LTS desde Java 17): Modelo inmutable tipado que reemplaza estructuras opacas basadas en Map.
// 5. Unnamed Variables '_' (JEP 443 - Preview en Java 21 / Final en Java 22): Captura explícita de campos no utilizados en destructuring.
// 6. Map.of() y List.of(): Creación de colecciones inmutables optimizadas.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class P05TransactionPipeline_v21 {

    // Modelo de datos inmutable utilizando Java Records
    public record Transaction(int id, double amount, String status, String category) {

        // Método de conversión que desestructura el mapa usando Pattern Matching para instanceof
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
                .filter(P05TransactionPipeline_v21::isValidTransaction)
                .mapToDouble(tx -> switch (tx) {
                    // Record Pattern con destructuring directo (JEP 440) y variable no usada id/category
                    case Transaction(int _, double amount, String _, String _) -> amount * 0.9;
                })
                .reduce(Double::sum)
                .map(total -> BigDecimal.valueOf(total)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue())
                .orElse(0.0);
    }

    /**
     * Validación mediante Pattern Matching para switch con guardas 'when' (Java 21 LTS)
     */
    private static boolean isValidTransaction(Transaction transaction) {
        return switch (transaction) {
            case Transaction(int _, double amount, String status, String _) 
                when amount > 0.0 && isApprovedStatus(status) -> true;
            case null, default -> false;
        };
    }

    private static boolean isApprovedStatus(String status) {
        return switch (status) {
            case String s when "approved".equalsIgnoreCase(s) -> true;
            case null, default -> false;
        };
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
 RESUMEN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21:
 ====================================================================================
 1. Pattern Matching para Switch y Record Patterns (Java 21 LTS):
    En lugar de acceder a las propiedades con invocaciones estándar de métodos (`tx.amount()`), 
    Java 21 permite desestructurar el Record directamente en la firma de coincidencia:
    `case Transaction(int _, double amount, String status, String _)`
    Esto descompone el objeto e inyecta las componentes directamente en el ámbito local.

 2. Guardas con la palabra clave `when`:
    Permite combinar la estructura del patrón con predicados booleanos directamente en el switch:
    `when amount > 0.0 && isApprovedStatus(status)`
    Esto centraliza las reglas de filtrado en expresiones idiomáticas y libres de efectos secundarios.

 3. Unnamed Variables (`_`):
    Al desestructurar un Record, indicamos de forma explícita que ciertos campos no son relevantes 
    para la operación (ejemplo: `id` o `category`), previniendo advertencias de variables no usadas 
    y aclarando la intención del código.

 4. Manejo Seguro de Nulos en Switch:
    Java 21 integra el manejo de `null` directamente dentro de las expresiones switch (`case null, default -> false;`),
    lo que reemplaza las comprobaciones defensivas tradicionales `if (status == null)`.

 5. Redondeo Financiero Preciso:
    Se mantiene el uso de `BigDecimal.setScale(2, RoundingMode.HALF_UP)` para evitar los problemas
    de precisión flotante de `Math.round(total * 100.0) / 100.0`.
 ====================================================================================
*/