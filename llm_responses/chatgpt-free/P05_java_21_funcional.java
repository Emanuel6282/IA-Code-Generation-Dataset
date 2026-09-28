// P05: Transaction Pipeline — Java 21
//
// Características utilizadas:
// 1. Stream API: permite construir un pipeline funcional mediante
//    filter(), map() y reduce().
// 2. Lambdas: permiten expresar las operaciones de filtrado y
//    transformación de forma declarativa.
// 3. Optional<Double>: representa la ausencia de un resultado cuando
//    no existen transacciones válidas.
// 4. Method reference Double::sum: permite expresar la acumulación
//    de los importes de forma funcional.
//
// Estas características son compatibles con Java 21.
// No se utilizan bucles for/while ni recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransactionPipeline {

    public static double processTransactions(
            List<HashMap<String, Object>> transactions) {

        Optional<Double> total = transactions.stream()
                .filter(transaction -> {
                    var status = (String) transaction.get("status");
                    var amount =
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


        // Test 3: ninguna transacción válida
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
 * 1. Se reemplazó el procesamiento iterativo por un pipeline de Stream API.
 *
 * 2. filter() conserva únicamente las transacciones que cumplen:
 *       status == "approved"
 *       amount > 0
 *
 * 3. map() aplica la comisión del 10%:
 *       amount * 0.9
 *
 * 4. reduce(Double::sum) acumula funcionalmente todos los importes
 *    procesados.
 *
 * 5. Optional<Double> representa el caso en que el pipeline no produce
 *    ningún valor.
 *
 * 6. orElse(0.0) garantiza que una lista vacía o sin transacciones
 *    válidas produzca 0.0.
 *
 * 7. Math.round(total * 100.0) / 100.0 redondea el resultado a
 *    dos decimales.
 *
 * 8. Se utiliza var en variables locales, característica disponible
 *    desde Java 10 y válida en Java 21.
 *
 * 9. Se mantuvieron exactamente tres casos de prueba mediante assert.
 *
 * 10. No se utilizan bucles for/while, estado mutable en el pipeline
 *     ni recursión.
 */