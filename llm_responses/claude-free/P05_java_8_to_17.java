// P05: Transaction Pipeline — Java 17 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (desde Java 10, JEP 286, vigente
//    en 17): usada en "total", el elemento del for-each ("tx"), "status",
//    "amount" y el mapa auxiliar "m" en el metodo helper, donde el tipo es
//    evidente por el contexto o por el cast explicito ya presente.
// 2. Nota de honestidad sobre el tipo de dato: el codigo base modela cada
//    transaccion como "Map<String, Object>" (con casts explicitos a
//    "String" y "Number" para extraer los campos), en lugar de una clase o
//    record tipado, como se hizo en las versiones "desde cero" de esta
//    serie de problemas. Introducir aqui un "record Transaction(...)"
//    cambiaria la firma publica del metodo (de "List<Map<String,
//    Object>>" a "List<Transaction>") y la forma de construir los casos de
//    prueba (el metodo helper "tx" tendria que devolver otro tipo), lo cual
//    excede el alcance de una migracion de version con "comportamiento
//    identico": el requisito pide adaptar la sintaxis a Java 17, no
//    rediseñar el modelo de datos de la funcion. Por eso se conserva
//    intencionalmente "Map<String, Object>" con los mismos casts
//    ("(String) tx.get(...)", "((Number) tx.get(...)).doubleValue()").
// 3. Se reviso tambien pattern matching for instanceof (JEP 394): no aplica
//    de forma directa aqui, ya que los casts actuales asumen el tipo sin
//    una comprobacion "instanceof" previa que pattern matching pudiera
//    simplificar (cambiar esto alteraria el manejo de errores implicito
//    del codigo base, que lanza ClassCastException si el tipo no coincide,
//    comportamiento que se debe preservar). Records, sealed classes y text
//    blocks tampoco aplican por las mismas razones de alcance y porque no
//    hay strings multilinea en este algoritmo.

import java.util.*;

public class P05TransactionPipeline_v17 {

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
//    "P05TransactionPipeline_v17" unicamente para reflejar la version
//    objetivo del archivo; no afecta el comportamiento ni el nombre del
//    metodo publico requerido.
// 2. Se reemplazo "double total = 0.0;", "Map<String, Object> tx : ...",
//    "String status = (String) ...", "double amount = ((Number) ...)
//    .doubleValue();" y "Map<String, Object> m = new HashMap<>();" por sus
//    equivalentes con "var", ya que el tipo es evidente por el lado
//    derecho de cada asignacion (incluyendo los casts explicitos, que
//    "var" no elimina ni oculta: el tipo real sigue siendo exactamente el
//    mismo en tiempo de compilacion).
// 3. La logica interna (extraccion de campos via casts desde el Map,
//    condicion "approved".equals(status) && amount > 0, acumulacion
//    "total = total + amount * 0.9", y el redondeo final con
//    "Math.round(total * 100.0) / 100.0") se mantuvo exactamente igual,
//    cumpliendo el requisito de comportamiento identico; no existia
//    ninguna caracteristica de Java 17 que mejorara esta logica sin
//    alterar el modelo de datos "Map<String, Object>" original o sin
//    exceder el alcance de una migracion de sintaxis.
// 4. El nombre del metodo "processTransactions", el metodo helper "tx", y
//    los tres casos de prueba con "assert" se conservaron sin
//    modificaciones logicas, ya que ya eran compatibles con Java 17
//    (assert es parte del lenguaje base desde Java 1.4).