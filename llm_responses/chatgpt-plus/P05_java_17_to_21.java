// P05: Transaction Pipeline — Java 21
// Migración del código proporcionado a Java 21
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Pattern Matching para switch (Java 21, JEP 441):
//    Permite identificar tipos mediante patrones.
//    Se utiliza para extraer el importe numérico.
//
// 2. Inferencia de tipos con var (Java 10):
//    Reduce la repetición de tipos en variables locales.
//    Es una característica estable disponible en Java 21.
//
// 3. Operador diamante <> (Java 7):
//    Simplifica la creación de colecciones genéricas.
//
// 4. Bucle for-each:
//    Conserva el procesamiento iterativo original.
//
// 5. Math.round():
//    Mantiene el redondeo original a dos decimales.
//
// NOTA:
// Pattern Matching para switch es una característica
// estabilizada en Java 21. Las demás construcciones
// mencionadas existían en versiones anteriores.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.*;

public class P05TransactionPipeline_v21 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(
            List<Map<String, Object>> transactions) {

        double total = 0.0;

        // Recorrido iterativo.
        for (var tx : transactions) {

            String status = (String) tx.get("status");

            // Pattern matching para switch de Java 21.
            // Se conserva el comportamiento del cast original:
            // un valor no numérico provoca ClassCastException.
            // Un valor null provoca NullPointerException.
            Number amountNumber = switch (tx.get("amount")) {
                case Number number -> number;
                case null -> (Number) null;
                default -> throw new ClassCastException(
                    "El importe debe ser numérico"
                );
            };

            double amount = amountNumber.doubleValue();

            // Mantener las condiciones originales.
            if ("approved".equals(status) && amount > 0) {

                // Aplicar la comisión del 10 %.
                total += amount * 0.9;
            }
        }

        // Mantener el redondeo original.
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
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A JAVA 21
 *
 * 1. Pattern Matching para switch (JEP 441):
 *
 *    Antes:
 *      double amount =
 *          ((Number) tx.get("amount")).doubleValue();
 *
 *    Ahora:
 *      Number amountNumber = switch (tx.get("amount")) {
 *          case Number number -> number;
 *          case null -> (Number) null;
 *          default -> throw new ClassCastException(
 *              "El importe debe ser numérico"
 *          );
 *      };
 *
 *      double amount = amountNumber.doubleValue();
 *
 *    Motivo:
 *    Utiliza una característica estable de Java 21
 *    para identificar y extraer valores numéricos.
 *
 *    Se conservan los tipos de excepción originales:
 *    ClassCastException para valores no numéricos
 *    y NullPointerException para valores null.
 *
 * 2. Inferencia de tipos locales con var:
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
 *    Para double, mantiene la operación original.
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
 *    Se mantienen el for-each, las condiciones:
 *      "approved".equals(status) && amount > 0
 *
 *    También se conservan:
 *      amount * 0.9
 *      Math.round(total * 100.0) / 100.0
 *
 *    Motivo:
 *    Preservar las reglas financieras originales.
 *
 * 6. Conservación de los tres tests:
 *
 *    TC01, TC02 y TC03 mantienen exactamente
 *    las mismas entradas, aserciones y resultados.
 *
 * 7. Compatibilidad con Java 21:
 *
 *    Se utilizan únicamente características estables,
 *    sin habilitar funcionalidades preview.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(1).
 */