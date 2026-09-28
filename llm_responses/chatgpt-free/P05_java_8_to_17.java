// P05: Transaction Pipeline — Java 17
//
// Características utilizadas:
//
// 1. var (Java 10+):
//    Se utiliza para variables locales cuando el tipo es evidente,
//    reduciendo código repetitivo sin cambiar el comportamiento.
//
// 2. Map.of() (Java 9+):
//    Disponible en Java 17 y utilizado para construir mapas pequeños
//    de forma concisa en los casos de prueba.
//
// 3. Collections.emptyList() y Collections.singletonList():
//    APIs inmutables disponibles en Java 17 que permiten expresar
//    de forma clara los casos de lista vacía y lista de un elemento.
//
// Nota:
// Java 17 no introduce una característica exclusiva que mejore
// materialmente este algoritmo iterativo. Por ello, se conserva el
// bucle for-each y el acumulador mutable para mantener el mismo
// paradigma y comportamiento del código original.

import java.util.*;

public class P05TransactionPipeline_v17 {

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
 * 1. Se cambió el nombre de la clase de P05TransactionPipeline_v8
 *    a P05TransactionPipeline_v17 para identificar la versión objetivo.
 *
 * 2. Se utiliza var para las variables locales total, tx, status,
 *    amount y txs1/txs3. Esto reduce declaraciones repetitivas y
 *    es apropiado cuando el tipo puede inferirse claramente.
 *
 * 3. El método tx() ahora utiliza Map.of(), disponible desde Java 9
 *    y plenamente compatible con Java 17. Esto simplifica la creación
 *    de mapas pequeños utilizados exclusivamente como datos de prueba.
 *
 * 4. Se mantiene el bucle for-each porque el código original utiliza
 *    un paradigma iterativo. No se introducen Streams ni lambdas,
 *    evitando modificar innecesariamente el estilo del algoritmo.
 *
 * 5. Se mantiene el acumulador mutable total porque forma parte del
 *    procesamiento iterativo original.
 *
 * 6. Se conserva exactamente la misma lógica de filtrado:
 *       "approved".equals(status) && amount > 0
 *
 * 7. Se conserva exactamente la misma comisión:
 *       amount * 0.9
 *
 * 8. Se conserva exactamente el mismo redondeo:
 *       Math.round(total * 100.0) / 100.0
 *
 * 9. Se conservaron los tres casos de prueba originales:
 *       TC01: filtrado y aplicación de comisión
 *       TC02: lista vacía
 *       TC03: una transacción approved
 *
 * 10. Los assert mantienen las mismas condiciones y tolerancias
 *     del código Java 8 original.
 *
 * 11. No se utilizan características artificialmente introducidas
 *     solo para "usar Java 17". Java 17 no proporciona una nueva
 *     característica específica que mejore materialmente este
 *     algoritmo sencillo sin cambiar su diseño.
 */