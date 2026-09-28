// P05: Transaction Pipeline — Java 17
//
// Características utilizadas:
// 1. Stream API: permite expresar el procesamiento como un pipeline
//    funcional mediante filter(), map() y reduce().
// 2. Lambdas: permiten definir las operaciones de filtrado y transformación
//    de forma declarativa.
// 3. Optional<Double>: representa la ausencia de un resultado cuando no
//    existen transacciones válidas.
// 4. Method reference Double::sum: simplifica la operación de acumulación.
//
// Estas características son compatibles con Java 17 y permiten mantener
// el procesamiento sin bucles imperativos ni estado mutable.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransactionPipeline {

public static double processTransactions(
        List<HashMap<String, Object>> transactions) {

    Optional<Double> total = transactions.stream()
            .filter(transaction -> {
                String status = (String) transaction.get("status");
                double amount =
                        ((Number) transaction.get("amount")).doubleValue();

                return "approved".equals(status) && amount > 0;
            })
            .map(transaction ->
                    ((Number) transaction.get("amount")).doubleValue() * 0.9
            )
            .reduce(Double::sum);

    return Math.round(total.orElse(0.0) * 100.0) / 100.0;
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
* 1. Se utiliza Stream API para reemplazar el procesamiento iterativo
* y expresar las operaciones como un pipeline funcional.
*
* 2. filter() mantiene únicamente las transacciones con status "approved"
* y amount mayor que cero.
*
* 3. map() aplica la comisión del 10% mediante amount * 0.9.
*
* 4. reduce(Double::sum) acumula los importes procesados.
*
* 5. Optional<Double> permite representar un resultado ausente cuando
* la lista está vacía o ninguna transacción cumple los filtros.
*
* 6. orElse(0.0) garantiza que una lista sin transacciones válidas
* produzca exactamente 0.0.
*
* 7. Math.round(total * 100.0) / 100.0 mantiene el redondeo a
* dos decimales.
*
* 8. Se utiliza var en variables locales de los tests, una característica
* disponible en Java 17 que reduce código repetitivo.
*
* 9. Se conservaron exactamente tres casos de prueba mediante assert.
*
* 10. No se utilizan bucles for/while ni recursión dentro del procesamiento,
* ```
  y se utilizan únicamente APIs de la biblioteca estándar.
  ```

*/
