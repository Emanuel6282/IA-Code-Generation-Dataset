// P05: Transaction Pipeline — Java 17
//
// Características utilizadas:
// 1. Inferencia de tipos con var: reduce código repetitivo en variables
//    locales sin cambiar el comportamiento del algoritmo.
// 2. ArrayList y HashMap: colecciones estándar utilizadas para representar
//    la lista de transacciones y sus campos.
// 3. For-each: permite recorrer las transacciones de forma iterativa y
//    legible.
//
// Nota: Java 17 no introduce una característica exclusiva que mejore
// materialmente este algoritmo. No se utilizan Streams, lambdas ni
// recursión porque el paradigma requerido es estrictamente iterativo.

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
    // Caso 1: lista vacía
    assert processTransactions(
            new ArrayList<HashMap<String, Object>>()
    ) == 0.0;

    // Caso 2: solo la transacción aprobada es procesada
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

    var transactions2 = new ArrayList<HashMap<String, Object>>();
    transactions2.add(transaction1);
    transactions2.add(transaction2);

    assert processTransactions(transactions2) == 90.0;

    // Caso 3: no hay transacciones válidas
    var transaction3 = new HashMap<String, Object>();
    transaction3.put("id", 3);
    transaction3.put("amount", -50.0);
    transaction3.put("status", "approved");
    transaction3.put("category", "food");

    var transactions3 = new ArrayList<HashMap<String, Object>>();
    transactions3.add(transaction3);

    assert processTransactions(transactions3) == 0.0;

    System.out.println("All tests passed");
}

}

/*

* Cambios realizados:
*
* 1. Se utiliza var para variables locales:
* mejora la legibilidad al evitar tipos repetitivos. var está disponible
* desde Java 10 y, por tanto, forma parte de Java 17.
*
* 2. Se mantienen ArrayList y HashMap:
* cumplen explícitamente con los requisitos del paradigma iterativo.
*
* 3. Se utiliza un for-each:
* permite procesar cada transacción sin Streams ni lambdas.
*
* 4. Se conserva un acumulador double:
* mantiene el estado mutable necesario para el enfoque iterativo.
*
* 5. Se mantiene Math.round(total * 100.0) / 100.0:
* conserva exactamente el comportamiento de redondeo requerido.
*
* 6. Se conservaron los tres casos de prueba:
* lista vacía, transacción aprobada y ausencia de transacciones válidas.
*
* 7. No se utilizan características posteriores a Java 17 ni APIs externas.
* Tampoco se utilizan Streams, lambdas o recursión.
  */
