// P05: Transaction Pipeline — Java 21
//
// Características utilizadas:
//
// 1. var:
//    Se utiliza para variables locales cuyo tipo es evidente, mejorando
//    la legibilidad. Está disponible desde Java 10 y es totalmente
//    compatible con Java 21.
//
// 2. Map.of():
//    Se utiliza para construir mapas pequeños de manera concisa e
//    inmutable. Está disponible desde Java 9 y es idiomático en Java 21.
//
// 3. for-each:
//    Se conserva para mantener el paradigma iterativo del código original.
//
// Nota:
// Java 21 incorpora características como pattern matching para switch,
// record patterns y Sequenced Collections. Sin embargo, ninguna mejora
// materialmente este algoritmo porque las transacciones siguen siendo
// representadas como Map<String, Object>. Introducirlas artificialmente
// cambiaría la estructura del problema sin aportar una mejora real.
//
// Por tanto, se prioriza código idiomático de Java 21 sin modificar
// innecesariamente la lógica original.

import java.util.*;

public class P05TransactionPipeline_v21 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comisión del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(
            List<Map<String, Object>> transactions) {

        var total = 0.0;

        for (var tx : transactions) {
            var status = (String) tx.get("status");
            var amount = ((Number) tx.get("amount")).doubleValue();

            if ("approved".equals(status) && amount > 0) {
                total = total + amount * 0.9;
            }
        }

        return Math.round(total * 100.0) / 100.0;
    }

    private static Map<String, Object> tx(
            int id,
            double amount,
            String status,
            String cat) {

        return Map.of(
                "id", id,
                "amount", amount,
                "status", status,
                "category", cat
        );
    }

    public static void main(String[] args) {

        // TC01: filtrar negativo y rechazado, aplicar fee
        var txs1 = Arrays.asList(
                tx(1, 100.0, "approved", "income"),
                tx(2, -50.0, "approved", "income"),
                tx(3, 200.0, "rejected", "income"),
                tx(4, 150.0, "approved", "expense")
        );

        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01
                : "TC01 fallo";


        // TC02: lista vacía
        assert processTransactions(Collections.emptyList()) == 0.0
                : "TC02 fallo";


        // TC03: un approved
        var txs3 = Collections.singletonList(
                tx(1, 1000.0, "approved", "x")
        );

        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01
                : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
 * Cambios realizados:
 *
 * 1. Se actualizó la clase de P05TransactionPipeline_v8 a
 *    P05TransactionPipeline_v21 para identificar la versión objetivo.
 *
 * 2. Se utilizaron declaraciones var para las variables locales.
 *    Esto reduce código repetitivo cuando el tipo puede inferirse
 *    claramente y es compatible con Java 21.
 *
 * 3. El método tx() se simplificó utilizando Map.of(), evitando la
 *    creación manual de un HashMap y múltiples llamadas a put().
 *
 * 4. Se mantiene List<Map<String, Object>> para preservar exactamente
 *    la representación de las transacciones del código original.
 *
 * 5. Se conserva el bucle for-each y el acumulador total, manteniendo
 *    el mismo paradigma iterativo y la misma lógica de procesamiento.
 *
 * 6. El filtrado permanece idéntico:
 *       "approved".equals(status) && amount > 0
 *
 * 7. La comisión permanece idéntica:
 *       amount * 0.9
 *
 * 8. El redondeo permanece idéntico:
 *       Math.round(total * 100.0) / 100.0
 *
 * 9. Se conservaron exactamente los tres tests originales:
 *       TC01: filtrado de transacciones y comisión
 *       TC02: lista vacía
 *       TC03: una transacción aprobada
 *
 * 10. Se mantuvieron las mismas tolerancias de los tests TC01 y TC03.
 *
 * 11. No se introdujeron Streams, lambdas ni recursión, ya que el código
 *     original utiliza un enfoque iterativo.
 *
 * 12. No se forzaron características exclusivas de Java 21. Aunque Java 21
 *     incorpora pattern matching para switch, record patterns y Sequenced
 *     Collections, ninguna resulta apropiada para esta representación
 *     basada en Map<String, Object>. Evitarlas hace que la migración sea
 *     más idiomática y mantenga el diseño original.
 */