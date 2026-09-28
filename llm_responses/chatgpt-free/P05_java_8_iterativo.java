// P05: Transaction Pipeline — Java 8
//
// Características utilizadas:
// 1. ArrayList y HashMap: colecciones estándar disponibles en Java 8
//    para representar la lista de transacciones y sus campos.
// 2. Bucle for mejorado: permite recorrer las transacciones de forma
//    iterativa y legible.
// 3. Inferencia con el tipo de colección y operaciones estándar de Java 8.
//    No se utilizan Streams ni lambdas porque el paradigma requerido es
//    estrictamente iterativo.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionPipeline {

public static double processTransactions(
        List<HashMap<String, Object>> transactions) {

    double total = 0.0;

    for (HashMap<String, Object> transaction : transactions) {
        String status = (String) transaction.get("status");
        double amount = ((Number) transaction.get("amount")).doubleValue();

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
    HashMap<String, Object> transaction1 = new HashMap<String, Object>();
    transaction1.put("id", 1);
    transaction1.put("amount", 100.0);
    transaction1.put("status", "approved");
    transaction1.put("category", "food");

    HashMap<String, Object> transaction2 = new HashMap<String, Object>();
    transaction2.put("id", 2);
    transaction2.put("amount", 200.0);
    transaction2.put("status", "rejected");
    transaction2.put("category", "shopping");

    ArrayList<HashMap<String, Object>> transactions2 =
            new ArrayList<HashMap<String, Object>>();
    transactions2.add(transaction1);
    transactions2.add(transaction2);

    assert processTransactions(transactions2) == 90.0;

    // Caso 3: no hay transacciones válidas
    HashMap<String, Object> transaction3 = new HashMap<String, Object>();
    transaction3.put("id", 3);
    transaction3.put("amount", -50.0);
    transaction3.put("status", "approved");
    transaction3.put("category", "food");

    ArrayList<HashMap<String, Object>> transactions3 =
            new ArrayList<HashMap<String, Object>>();
    transactions3.add(transaction3);

    assert processTransactions(transactions3) == 0.0;

    System.out.println("All tests passed");
}

}

/*

* Cambios y decisiones:
*
* 1. Se utiliza ArrayList y HashMap, tal como requiere el paradigma
* iterativo de este ejercicio.
*
* 2. Se utiliza un for-each para recorrer las transacciones. No se usan
* Streams, lambdas ni operaciones funcionales.
*
* 3. Se obtiene amount mediante Number.doubleValue(), permitiendo que
* el valor numérico almacenado sea Float, Double u otro Number.
*
* 4. Se utiliza "approved".equals(status) para comparar cadenas de forma
* segura incluso si status fuera null.
*
* 5. La comisión se aplica multiplicando amount por 0.9.
*
* 6. Math.round(total * 100.0) / 100.0 redondea el resultado a dos
* posiciones decimales.
*
* 7. Se conservaron exactamente 3 casos de prueba mediante assert:
* lista vacía, transacciones válidas/inválidas y ausencia de
* transacciones válidas.
*
* 8. No se utilizan características posteriores a Java 8, Streams,
* lambdas ni recursión.
  */
