// P05: Transaction Pipeline — Java 8
//
// Características utilizadas:
// 1. Stream API: permite expresar el pipeline de procesamiento de forma
//    declarativa mediante filter(), map() y reduce().
// 2. Lambdas: permiten definir las operaciones de filtrado y transformación
//    directamente sobre el pipeline.
// 3. Optional<Double>: representa de forma segura la ausencia de un resultado
//    cuando no existen transacciones válidas.
//
// Estas características forman parte de Java 8 y son apropiadas para
// implementar el paradigma funcional solicitado.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionPipeline {

public static double processTransactions(
        List<HashMap<String, Object>> transactions) {

    Optional<Double> total = transactions.stream()
            .filter(transaction -> {
                String status = (String) transaction.get("status");
                double amount = ((Number) transaction.get("amount")).doubleValue();

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
* 1. Se reemplazó el bucle iterativo por Stream API para cumplir el
* paradigma funcional de Java 8.
*
* 2. filter() realiza el filtrado de las transacciones:
* status == "approved" y amount > 0.
*
* 3. map() aplica la comisión del 10% mediante amount * 0.9.
*
* 4. reduce(Double::sum) combina todos los importes procesados en
* un único resultado.
*
* 5. Optional<Double> permite representar la ausencia de un resultado
* cuando la lista está vacía o ninguna transacción es válida.
*
* 6. orElse(0.0) garantiza que una lista sin transacciones válidas
* produzca exactamente 0.0.
*
* 7. Math.round(total * 100.0) / 100.0 mantiene el redondeo a
* dos decimales requerido.
*
* 8. Se conservaron exactamente 3 casos de prueba mediante assert.
*
* 9. Se utilizan únicamente características y clases disponibles
* en Java 8; no se utilizan APIs posteriores.
  */
