// P01: Word Frequency — Java 17
//
// Características utilizadas de Java 17:
// 1. Map.merge():
//    Simplifica la actualización del contador de cada palabra,
//    evitando la comprobación explícita con containsKey().
//
// 2. Inferencia de tipos con var:
//    Se utiliza en variables locales donde mejora la legibilidad
//    sin perder claridad sobre el tipo.
//
// 3. API de colecciones moderna:
//    Map.of() y Collections.emptyList() se mantienen porque son
//    compatibles con Java 17 y permiten expresar claramente
//    los valores esperados en los tests.

import java.util.*;

public class P01WordFrequency_v17 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> result = new HashMap<>();

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
 * CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17:
 *
 * 1. containsKey() + get() + put() -> merge()
 *    Antes:
 *
 *        if (result.containsKey(word)) {
 *            result.put(word, result.get(word) + 1);
 *        } else {
 *            result.put(word, 1);
 *        }
 *
 *    Ahora:
 *
 *        result.merge(word, 1, Integer::sum);
 *
 *    Map.merge() permite expresar directamente la operación:
 *    "si la palabra no existe, insertar 1; si existe, sumar 1".
 *    Esto reduce código y mejora la legibilidad.
 *
 * 2. Integer::sum
 *    Se utiliza una referencia a método para indicar cómo combinar
 *    el valor existente con el nuevo valor.
 *
 * 3. Los tests se mantienen equivalentes.
 *    Se conservan los mismos tres casos:
 *      - palabras repetidas,
 *      - lista vacía,
 *      - una única palabra.
 *
 * 4. El comportamiento se mantiene idéntico.
 *    La función sigue retornando un HashMap con cada palabra como
 *    clave y su cantidad de apariciones como valor.
 *
 * 5. No se utilizan Streams ni cambios de paradigma.
 *    El código continúa siendo iterativo, porque la migración
 *    solicitada conserva la lógica original.
 *
 * 6. Java 17 no introduce una nueva estructura específica para
 *    este problema que justifique modificar el algoritmo.
 *    Por ello se utilizan las mejoras de la API de colecciones
 *    disponibles en Java moderno sin introducir complejidad
 *    innecesaria.
 */