// P05: Transaction Pipeline — Java 17
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. Pattern Matching para instanceof (Java 16):
//    Permite comprobar y convertir tipos de forma concisa.
//    Es una característica estable y disponible en Java 17.
//
// 2. Inferencia de tipos con var (Java 10):
//    Reduce la repetición de tipos en variables locales.
//    Es compatible con Java 17.
//
// 3. Operador diamante <> (Java 7):
//    Simplifica la creación de ArrayList y HashMap.
//
// 4. Bucle for-each:
//    Recorre las transacciones de manera iterativa.
//
// 5. Math.round():
//    Redondea el total final a dos decimales.
//
// NOTA:
// Java 17 no introduce una estructura de iteración nueva
// necesaria para este problema. Se utilizan características
// modernas ya incorporadas al lenguaje y disponibles en 17.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class P05TransactionPipeline_v17 {

    public static double processTransactions(
            ArrayList<HashMap<String, Object>> transactions) {

        double total = 0.0;

        // Recorrido estrictamente iterativo.
        for (var transaction : transactions) {

            String status = (String) transaction.get("status");
            Object amountValue = transaction.get("amount");

            // Pattern matching para comprobar y obtener el importe.
            if (amountValue instanceof Number amount) {

                double amountValueDouble = amount.doubleValue();

                // Filtrar transacciones válidas.
                if ("approved".equals(status) && amountValueDouble > 0) {

                    // Aplicar comisión del 10 %.
                    double netAmount = amountValueDouble * 0.9;

                    // Acumular el importe neto.
                    total += netAmount;
                }
            }
        }

        // Redondear a dos decimales.
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {

        // TEST 1: Una transacción aprobada y otra rechazada.
        var transactions1 = new ArrayList<HashMap<String, Object>>();

        var transaction1 = new HashMap<String, Object>();
        transaction1.put("id", 1);
        transaction1.put("amount", 100.0);
        transaction1.put("status", "approved");
        transaction1.put("category", "sales");
        transactions1.add(transaction1);

        var transaction2 = new HashMap<String, Object>();
        transaction2.put("id", 2);
        transaction2.put("amount", 200.0);
        transaction2.put("status", "rejected");
        transaction2.put("category", "sales");
        transactions1.add(transaction2);

        assert processTransactions(transactions1) == 90.0;


        // TEST 2: Dos aprobadas y una con importe negativo.
        var transactions2 = new ArrayList<HashMap<String, Object>>();

        var transaction3 = new HashMap<String, Object>();
        transaction3.put("id", 3);
        transaction3.put("amount", 100.0);
        transaction3.put("status", "approved");
        transaction3.put("category", "sales");
        transactions2.add(transaction3);

        var transaction4 = new HashMap<String, Object>();
        transaction4.put("id", 4);
        transaction4.put("amount", 50.0);
        transaction4.put("status", "approved");
        transaction4.put("category", "services");
        transactions2.add(transaction4);

        var transaction5 = new HashMap<String, Object>();
        transaction5.put("id", 5);
        transaction5.put("amount", -20.0);
        transaction5.put("status", "approved");
        transaction5.put("category", "refund");
        transactions2.add(transaction5);

        assert processTransactions(transactions2) == 135.0;


        // TEST 3: Lista vacía.
        var transactions3 = new ArrayList<HashMap<String, Object>>();

        assert processTransactions(transactions3) == 0.0;

        System.out.println("All tests passed");
    }
}