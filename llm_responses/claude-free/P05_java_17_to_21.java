// P05: Transaction Pipeline — Java 21 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (desde Java 10, vigente en 21):
//    usada en "total", el elemento del for-each ("tx"), "status",
//    "amount" y el mapa auxiliar "m" en el metodo helper, donde el tipo es
//    evidente por el contexto o por el cast explicito ya presente.
// 2. Nota de honestidad sobre el tipo de dato: el codigo base modela cada
//    transaccion como "Map<String, Object>" (con casts explicitos a
//    "String" y "Number"), en lugar de un record tipado. Introducir aqui
//    un "record Transaction(...)" cambiaria la firma publica del metodo y
//    la forma de construir los casos de prueba, excediendo el alcance de
//    una migracion de version con "comportamiento identico". Por eso se
//    conserva intencionalmente "Map<String, Object>" con los mismos casts.
// 3. Nota de honestidad sobre las novedades especificas de Java 21: revise
//    virtual threads (JEP 444), pattern matching for switch (JEP 441),
//    record patterns (JEP 440) y sequenced collections (JEP 431). Ninguna
//    aplica sin exceder el alcance de esta migracion puntual:
//    - Virtual threads: el problema es computo puro y secuencial, sin I/O
//      bloqueante.
//    - Pattern matching for switch / record patterns: estas
//      caracteristicas desestructuran instancias de records o jerarquias
//      selladas; el dato de entrada aqui es un "Map<String, Object>" sin
//      esa estructura, por lo que no hay nada que "record pattern" pueda
//      desestructurar sin primero cambiar el modelo de datos (lo cual
//      excederia el alcance). Los casts actuales tampoco usan
//      "instanceof" previo que un pattern matching pudiera simplificar sin
//      alterar el comportamiento de ClassCastException implicito del
//      codigo original ante un tipo incorrecto.
//    - Sequenced collections: los "List" usados (Arrays.asList,
//      Collections.singletonList, Collections.emptyList) se recorren con
//      un simple for-each secuencial; no se requiere ningun metodo nuevo
//      de "SequencedCollection" (getFirst/getLast/reversed) para esta
//      logica.

import java.util.*;

public class P05TransactionPipeline_v21 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
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

    private static Map<String, Object> tx(int id, double amount, String status, String cat) {
        var m = new HashMap<String, Object>();
        m.put("id", id); m.put("amount", amount);
        m.put("status", status); m.put("category", cat);
        return m;
    }

    public static void main(String[] args) {
        // TC01: filtrar negativo y rechazado, aplicar fee
        List<Map<String, Object>> txs1 = Arrays.asList(
            tx(1, 100.0, "approved", "income"),
            tx(2, -50.0, "approved", "income"),
            tx(3, 200.0, "rejected", "income"),
            tx(4, 150.0, "approved", "expense")
        );
        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01 : "TC01 fallo";

        // TC02: lista vacia
        assert processTransactions(Collections.emptyList()) == 0.0 : "TC02 fallo";

        // TC03: un approved
        List<Map<String, Object>> txs3 = Collections.singletonList(tx(1, 1000.0, "approved", "x"));
        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01 : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se renombro la clase de "P05TransactionPipeline_v8" a
//    "P05TransactionPipeline_v21" unicamente para reflejar la version
//    objetivo del archivo; no afecta el comportamiento ni el nombre del
//    metodo publico requerido.
// 2. Se reemplazo "double total = 0.0;", "Map<String, Object> tx : ...",
//    "String status = (String) ...", "double amount = ((Number) ...)
//    .doubleValue();" y "Map<String, Object> m = new HashMap<>();" por sus
//    equivalentes con "var", ya que el tipo es evidente por el lado
//    derecho de cada asignacion (incluyendo los casts explicitos, cuyo
//    tipo real no cambia en tiempo de compilacion).
// 3. La logica interna (extraccion de campos via casts desde el Map,
//    condicion "approved".equals(status) && amount > 0, acumulacion
//    "total = total + amount * 0.9", y el redondeo final con
//    "Math.round(total * 100.0) / 100.0") se mantuvo exactamente igual,
//    cumpliendo el requisito de comportamiento identico; no existia
//    ninguna caracteristica de Java 21 que mejorara esta logica sin
//    alterar el modelo de datos "Map<String, Object>" original o sin
//    exceder el alcance de una migracion de sintaxis.
// 4. El nombre del metodo "processTransactions", el metodo helper "tx", y
//    los tres casos de prueba con "assert" se conservaron sin
//    modificaciones logicas, ya que ya eran compatibles con Java 21
//    (assert es parte del lenguaje base desde Java 1.4).