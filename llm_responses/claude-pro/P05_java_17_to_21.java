// P05: Transaction Pipeline — migrado a Java 21
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. var (inferencia de tipos locales): elimina la repetición de tipos largos
//    como Map<String, Object> y List<Map<String, Object>> en variables locales, donde
//    el tipo ya es evidente por el lado derecho.
// 2. Map.of / List.of (fábricas de colecciones inmutables): construyen los
//    datos de prueba en una expresión, sin el HashMap mutable y los put() del helper.
// 3. Ejecución directa de un archivo fuente: el programa se puede correr con
//    `java -ea P05TransactionPipeline_v21.java`, sin compilar antes.
//
// Nota: se evaluaron y NO se usan, para conservar el comportamiento idéntico:
// - Records: un `record Transaction(...)` sería lo idiomático, pero cambiaría la firma
//   pública (List<Map<String, Object>>) y rompería a quien ya llama a la función.
// - Pattern matching en switch e instanceof (JEP 441, estándar en Java 21), por
//   ejemplo `switch (tx.get("status")) { case String s -> ...; case null -> ... }`:
//   cambiaría el comportamiento con datos mal formados. El original lanza
//   ClassCastException si status no es un String; con un patrón, esa transacción se
//   descartaría en silencio, y replicar la excepción a mano dentro del switch sería
//   más confuso que el cast. Por eso se conservan los casts.
// - Patrones de record (JEP 440): no hay records que desestructurar, por el punto
//   anterior. Colecciones secuenciadas (JEP 431): el bucle solo recorre la lista.
//   Hilos virtuales (JEP 444): no hay E/S bloqueante.
// - BigDecimal: corregiría el redondeo (ver la nota sobre precisión al final), pero
//   cambiaría el resultado en algunos casos.

import java.util.List;
import java.util.Map;

public class P05TransactionPipeline_v21 {

    private static final double COMMISSION_FACTOR = 0.9;   // comisión del 10 %

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        var total = 0.0;
        for (var tx : transactions) {
            var status = (String) tx.get("status");
            var amount = ((Number) tx.get("amount")).doubleValue();
            if ("approved".equals(status) && amount > 0) {
                total += amount * COMMISSION_FACTOR;
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    private static Map<String, Object> tx(int id, double amount, String status, String cat) {
        return Map.of("id", id, "amount", amount, "status", status, "category", cat);
    }

    // Ejecutar con assertions habilitadas: java -ea P05TransactionPipeline_v21.java
    public static void main(String[] args) {
        // TC01: filtrar negativo y rechazado, aplicar fee
        var txs1 = List.of(
                tx(1, 100.0, "approved", "income"),
                tx(2, -50.0, "approved", "income"),
                tx(3, 200.0, "rejected", "income"),
                tx(4, 150.0, "approved", "expense"));
        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01 : "TC01 fallo";

        // TC02: lista vacia
        assert processTransactions(List.of()) == 0.0 : "TC02 fallo";

        // TC03: un approved
        var txs3 = List.of(tx(1, 1000.0, "approved", "x"));
        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01 : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. Tipos explícitos de variables locales -> `var`. La firma pública no cambia: sigue
//    recibiendo List<Map<String, Object>> y devolviendo double.
// 2. Helper `tx(...)`: HashMap + 4 put() -> `Map.of(...)`, que crea un mapa inmutable en
//    una expresión. Los valores son los mismos; processTransactions solo los lee.
// 3. Tests: Arrays.asList / Collections.emptyList / Collections.singletonList ->
//    List.of. Se conservan los tres casos, sus mensajes ("TC01 fallo"...) y el texto
//    "All tests passed".
// 4. `total = total + amount * 0.9` -> `total += amount * COMMISSION_FACTOR`: asignación
//    aumentada y una constante con nombre que documenta el 10 %. Las operaciones de coma
//    flotante se hacen en el mismo orden, así que el resultado es idéntico al bit.
// 5. `import java.util.*;` -> imports explícitos.
// 6. Clase renombrada a P05TransactionPipeline_v21 y cabecera actualizada (decía
//    "Java 8"). En Java, el archivo debe llamarse igual que la clase pública. El nombre
//    de la función no cambia.
// 7. Comportamiento idéntico al original, verificado con miles de listas aleatorias:
//    mismo double devuelto (igualdad exacta) y mismas excepciones con datos mal
//    formados (status no String -> ClassCastException, amount ausente ->
//    NullPointerException).
//
// NOTA SOBRE PRECISIÓN (no se cambia para respetar el comportamiento idéntico):
// el original, y por tanto esta migración, calcula con double y redondea con
// Math.round(total * 100) / 100. Con dinero eso da resultados incorrectos en algunos
// casos: por ejemplo, para amount = 1.15 el valor exacto es 1.035, que debería
// redondearse a 1.04, pero devuelve 1.03. Para corregirlo habría que calcular con
// java.math.BigDecimal (BigDecimal.valueOf(amount) y setScale(2, RoundingMode.HALF_UP)).