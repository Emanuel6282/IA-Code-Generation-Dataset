// P05: Transaction Pipeline — Java 8
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. ArrayList:
//    Almacena las transacciones en una lista mutable.
//
// 2. HashMap:
//    Representa cada transacción mediante pares clave-valor.
//
// 3. Bucle for-each:
//    Recorre las transacciones de forma iterativa.
//
// 4. Variables mutables:
//    Permiten acumular el importe total.
//
// 5. Condicional if:
//    Filtra transacciones aprobadas con importe positivo.
//
// 6. Math.round():
//    Redondea el resultado final a dos decimales.
//
// COMPATIBILIDAD:
// Todas las construcciones utilizadas son compatibles
// con Java 8. ArrayList, HashMap y for-each ya existían
// antes de Java 8; no es necesario utilizar Streams
// ni lambdas para resolver este problema.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class P05TransactionPipeline_v8 {

    public static double processTransactions(
            ArrayList<HashMap<String, Object>> transactions) {

        // Acumulador mutable.
        double total = 0.0;

        // Recorrer las transacciones iterativamente.
        for (HashMap<String, Object> transaction : transactions) {

            // Obtener los campos necesarios.
            String status = (String) transaction.get("status");

            double amount =
                    ((Number) transaction.get("amount")).doubleValue();

            // Filtrar transacciones aprobadas con importe positivo.
            if ("approved".equals(status) && amount > 0) {

                // Aplicar una comisión del 10 %.
                double netAmount = amount * 0.9;

                // Acumular el importe neto.
                total += netAmount;
            }
        }

        // Redondear el total final a dos decimales.
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {

        // Preparar los datos del primer test.
        ArrayList<HashMap<String, Object>> transactions1 =
                new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> transaction1 =
                new HashMap<String, Object>();

        transaction1.put("id", 1);
        transaction1.put("amount", 100.0);
        transaction1.put("status", "approved");
        transaction1.put("category", "sales");

        transactions1.add(transaction1);

        HashMap<String, Object> transaction2 =
                new HashMap<String, Object>();

        transaction2.put("id", 2);
        transaction2.put("amount", 200.0);
        transaction2.put("status", "rejected");
        transaction2.put("category", "sales");

        transactions1.add(transaction2);

        // Test 1: Una transacción aprobada y otra rechazada.
        assert processTransactions(transactions1) == 90.0;

        // Preparar los datos del segundo test.
        ArrayList<HashMap<String, Object>> transactions2 =
                new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> transaction3 =
                new HashMap<String, Object>();

        transaction3.put("id", 3);
        transaction3.put("amount", 100.0);
        transaction3.put("status", "approved");
        transaction3.put("category", "sales");

        transactions2.add(transaction3);

        HashMap<String, Object> transaction4 =
                new HashMap<String, Object>();

        transaction4.put("id", 4);
        transaction4.put("amount", 50.0);
        transaction4.put("status", "approved");
        transaction4.put("category", "services");

        transactions2.add(transaction4);

        HashMap<String, Object> transaction5 =
                new HashMap<String, Object>();

        transaction5.put("id", 5);
        transaction5.put("amount", -20.0);
        transaction5.put("status", "approved");
        transaction5.put("category", "refund");

        transactions2.add(transaction5);

        // Test 2: Dos transacciones válidas y una negativa.
        assert processTransactions(transactions2) == 135.0;

        // Test 3: Lista vacía.
        ArrayList<HashMap<String, Object>> transactions3 =
                new ArrayList<HashMap<String, Object>>();

        assert processTransactions(transactions3) == 0.0;

        System.out.println("All tests passed");
    }
}