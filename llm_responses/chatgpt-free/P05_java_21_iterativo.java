// P05: Transaction Pipeline — Java 21
//
// Características utilizadas:
// 1. var: simplifica la declaración de variables locales, disponible en Java 10+
//    y, por tanto, válida en Java 21.
// 2. ArrayList y HashMap: estructuras mutables apropiadas para el paradigma
//    iterativo solicitado.
// 3. for-each: permite recorrer las transacciones de forma iterativa.
//
// No se utilizan Streams, lambdas ni recursión.
// Java 21 no aporta una característica exclusiva que mejore materialmente
// este algoritmo sin introducir un paradigma diferente.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionPipeline {

    public static double processTransactions(
            List<HashMap<String, Object>> transactions) {

        var total = 0.0;

        for (var transaction : transactions) {
            var status = (String) transaction.get("status");
            var amount = ((Number) transaction.get("amount")).doubleValue();

            if ("approved".equals(status) && amount > 0) {
                total += amount * 0.9;
            }
        }

        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {

        // Test 1: transacción aprobada + transacción rechazada
        var transaction1 = new HashMap<String, Object>();
        transaction1.put("id", 1);
        transaction1.put("amount", 100.0);
        transaction1.put("status", "approved");
        transaction1.put("category", "food");

        var transaction2 = new HashMap<String, Object>();
        transaction2.put("id", 2);
        transaction2.put("amount", 200.0);
        transaction2.put("status", "rejected");
        transaction2.put("category", "shopping");

        var transactions1 = new ArrayList<HashMap<String, Object>>();
        transactions1.add(transaction1);
        transactions1.add(transaction2);

        assert processTransactions(transactions1) == 90.0;


        // Test 2: lista vacía
        var transactions2 = new ArrayList<HashMap<String, Object>>();

        assert processTransactions(transactions2) == 0.0;


        // Test 3: transacciones inválidas
        var transaction3 = new HashMap<String, Object>();
        transaction3.put("id", 3);
        transaction3.put("amount", -50.0);
        transaction3.put("status", "approved");
        transaction3.put("category", "food");

        var transaction4 = new HashMap<String, Object>();
        transaction4.put("id", 4);
        transaction4.put("amount", 75.0);
        transaction4.put("status", "rejected");
        transaction4.put("category", "shopping");

        var transactions3 = new ArrayList<HashMap<String, Object>>();
        transactions3.add(transaction3);
        transactions3.add(transaction4);

        assert processTransactions(transactions3) == 0.0;

        System.out.println("All tests passed");
    }
}

/*
 * Cambios realizados:
 *
 * 1. Se implementó processTransactions mediante un bucle for-each,
 *    cumpliendo estrictamente el paradigma iterativo.
 *
 * 2. Se filtran las transacciones comprobando:
 *       status == "approved"
 *       amount > 0
 *
 * 3. A cada transacción válida se le aplica la comisión del 10%:
 *       amount * 0.9
 *
 * 4. Los importes procesados se acumulan en la variable total.
 *
 * 5. El resultado se redondea a dos decimales mediante:
 *       Math.round(total * 100.0) / 100.0
 *
 * 6. Si la lista está vacía o ninguna transacción es válida,
 *    total permanece en 0.0 y se retorna 0.0.
 *
 * 7. Se utiliza var para las variables locales, una característica
 *    disponible en Java 10+ y válida en Java 21.
 *
 * 8. Se utilizan ArrayList y HashMap como estructuras mutables,
 *    de acuerdo con los requisitos del paradigma iterativo.
 *
 * 9. Se incluyeron exactamente tres casos de prueba utilizando assert.
 *
 * 10. No se utilizan Streams, lambdas ni recursión.
 */
