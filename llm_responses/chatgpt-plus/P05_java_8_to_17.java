// P05: Transaction Pipeline — Java 17
// Migración desde Java 8
// Paradigma: ITERATIVO
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. Pattern Matching para instanceof (Java 16, JEP 394):
//    Permite comprobar y convertir el importe numérico
//    sin repetir un cast explícito.
//
// 2. Inferencia de tipos con var (Java 10):
//    Simplifica las declaraciones de variables locales
//    cuando el tipo resulta evidente.
//
// 3. Operador diamante <> (Java 7):
//    Evita repetir los parámetros genéricos.
//
// 4. Bucle for-each:
//    Conserva el procesamiento iterativo original.
//
// 5. Math.round():
//    Conserva el redondeo a dos decimales.
//
// NOTA:
// Pattern Matching para instanceof es estable desde
// Java 16 y está disponible en Java 17.
// var y el operador diamante ya existían anteriormente.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.*;

public class P05TransactionPipeline_v17 {

    /**
     * Procesa una lista de transacciones:
     * 1. Filtra: status == "approved" AND amount > 0
     * 2. Aplica comisión del 10 % (amount * 0.9)
     * 3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(
            List<Map<String, Object>> transactions) {

        double total = 0.0;

        for (var tx : transactions) {

            String status = (String) tx.get("status");
            Object amountValue = tx.get("amount");

            // Pattern matching para instanceof.
            if (!(amountValue instanceof Number number)) {
                throw new ClassCastException(
                    "El importe debe ser numérico"
                );
            }

            double amount = number.doubleValue();

            if ("approved".equals(status) && amount > 0) {
                total += amount * 0.9;
            }
        }

        return Math.round(total * 100.0) / 100.0;
    }

    // Construye una transacción con los mismos campos.
    private static Map<String, Object> tx(
            int id, double amount, String status, String cat) {

        var m = new HashMap<String, Object>();

        m.put("id", id);
        m.put("amount", amount);
        m.put("status", status);
        m.put("category", cat);

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

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 8 -> JAVA 17
 *
 * 1. Inferencia de tipos locales con var:
 *
 *    Antes:
 *      for (Map<String, Object> tx : transactions)
 *
 *    Ahora:
 *      for (var tx : transactions)
 *
 *    Motivo:
 *    Reduce la repetición de tipos sin perder
 *    la comprobación estática.
 *
 * 2. Pattern Matching para instanceof:
 *
 *    Antes:
 *      double amount =
 *          ((Number) tx.get("amount")).doubleValue();
 *
 *    Ahora:
 *      Object amountValue = tx.get("amount");
 *      if (!(amountValue instanceof Number number)) {
 *          throw new ClassCastException(...);
 *      }
 *      double amount = number.doubleValue();
 *
 *    Motivo:
 *    Permite obtener una variable tipada directamente
 *    después de comprobar el tipo.
 *
 *    Observación:
 *    Para los datos válidos del problema, el resultado
 *    es idéntico. El tratamiento de importes nulos
 *    o no numéricos puede diferir del código original.
 *
 * 3. Simplificación de la acumulación:
 *
 *    Antes:
 *      total = total + amount * 0.9;
 *
 *    Ahora:
 *      total += amount * 0.9;
 *
 *    Motivo:
 *    Expresa de forma más concisa la acumulación.
 *
 * 4. Inferencia en la creación del HashMap:
 *
 *    Antes:
 *      Map<String, Object> m = new HashMap<>();
 *
 *    Ahora:
 *      var m = new HashMap<String, Object>();
 *
 *    Motivo:
 *    Utiliza inferencia de tipos locales.
 *
 * 5. Conservación del algoritmo:
 *
 *    Se mantienen el for-each, las condiciones,
 *    la comisión del 10 % y el redondeo original.
 *
 * 6. Conservación de los tres tests:
 *
 *    TC01, TC02 y TC03 mantienen exactamente
 *    sus entradas, aserciones y resultados esperados.
 *
 * 7. Compatibilidad:
 *
 *    Se utilizan únicamente características estables
 *    disponibles en Java 17, sin funcionalidades preview.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(1).
 */