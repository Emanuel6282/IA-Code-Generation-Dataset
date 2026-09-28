// P01: Word Frequency — Java 21
//
// Características utilizadas:
// 1. var:
//    Se utiliza para la variable local "result", ya que el tipo se
//    determina claramente mediante la expresión new HashMap<>().
//
// 2. Map.merge():
//    Permite actualizar el contador de cada palabra de forma concisa,
//    reemplazando containsKey() + get() + put().
//
// 3. Referencia a método Integer::sum:
//    Expresa directamente la operación utilizada para combinar el
//    contador existente con el nuevo valor.
//
// Nota:
// Java 21 introduce características como pattern matching for switch,
// record patterns y virtual threads, pero ninguna mejora de forma
// natural este algoritmo sencillo de conteo. Por ello no se introducen
// artificialmente características que no aportan valor al problema.

import java.util.*;

public class P01WordFrequency_v21 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();

        for (String word : words) {
            result.merge(word, 1, Integer::sum);
        }

        return result;
    }

    public static void main(String[] args) {

        // Test 1: palabras repetidas
        assert wordFrequency(Arrays.asList("the", "fox", "the"))
                .equals(Map.of("the", 2, "fox", 1));

        // Test 2: lista vacía
        assert wordFrequency(Collections.emptyList())
                .equals(Map.of());

        // Test 3: una sola palabra
        assert wordFrequency(Arrays.asList("hello"))
                .equals(Map.of("hello", 1));

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. Versión actualizada a Java 21.
 *    La clase ahora se denomina P01WordFrequency_v21.
 *
 * 2. Se reemplazó:
 *
 *      Map<String, Integer> result = new HashMap<>();
 *
 *    por:
 *
 *      var result = new HashMap<String, Integer>();
 *
 *    "var" permite inferencia del tipo de variables locales y está
 *    disponible en Java desde la versión 10. En este caso mejora la
 *    legibilidad porque el tipo concreto queda determinado claramente
 *    por la expresión new HashMap<String, Integer>().
 *
 * 3. Se reemplazó la lógica:
 *
 *      if (result.containsKey(word)) {
 *          result.put(word, result.get(word) + 1);
 *      } else {
 *          result.put(word, 1);
 *      }
 *
 *    por:
 *
 *      result.merge(word, 1, Integer::sum);
 *
 *    Esto expresa directamente la operación de acumulación y evita
 *    realizar varias búsquedas sobre el mapa.
 *
 * 4. Se utiliza Integer::sum como referencia a método.
 *    Si la palabra no existe, se introduce el valor 1. Si ya existe,
 *    se suma 1 al valor almacenado.
 *
 * 5. Los tres tests originales se conservan:
 *      - ["the", "fox", "the"] -> {"the": 2, "fox": 1}
 *      - lista vacía -> {}
 *      - ["hello"] -> {"hello": 1}
 *
 * 6. El comportamiento es idéntico al código original.
 *    La función sigue siendo:
 *
 *      wordFrequency(List<String> words)
 *
 *    y devuelve un Map<String, Integer>.
 *
 * 7. No se utilizan Streams ni se cambia el paradigma.
 *    El algoritmo continúa utilizando un recorrido iterativo de la
 *    lista para preservar la lógica original.
 *
 * 8. No se introducen pattern matching, record patterns o switch
 *    expressions de Java 21 porque no aportan una mejora real a este
 *    problema. Utilizar una característica únicamente por ser nueva
 *    produciría código menos natural.
 */