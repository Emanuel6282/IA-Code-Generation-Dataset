// P01: Word Frequency — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Map.merge con referencia a método (Integer::sum): API de Java 8, pero es la
//    forma idiomática de contar en cualquier versión moderna. Hace una sola
//    búsqueda en el mapa por palabra, frente a las dos o tres de
//    containsKey + get + put, así que mejora legibilidad y rendimiento.
// 2. var (inferencia de tipos locales, Java 10): evita repetir el tipo genérico
//    cuando ya es evidente por el lado derecho de la asignación.
// 3. List.of / Map.of (fábricas de colecciones inmutables, Java 9): reemplazan a
//    Arrays.asList y Collections.emptyList en los tests y dejan las entradas
//    como listas realmente inmutables.
//
// Nota: las novedades del lenguaje de Java 17 y sus versiones previas (records,
// clases sealed, switch como expresión, pattern matching en instanceof, text
// blocks) no aportan nada a una función de conteo, así que no se fuerzan.

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v17 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();
        for (var word : words) {
            result.merge(word, 1, Integer::sum);
        }
        return result;
    }

    // Ejecutar con assertions habilitadas: java -ea P01WordFrequency_v17.java
    public static void main(String[] args) {
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
// 1. if (containsKey) { put(get + 1) } else { put(1) } -> result.merge(word, 1, Integer::sum):
//    expresa "insertar 1 o sumar 1" en una sola llamada y una sola búsqueda en el mapa.
//    El resultado es idéntico: las palabras nuevas empiezan en 1 y las repetidas suman 1.
// 2. Map<String, Integer> result = new HashMap<>() -> var result = new HashMap<String, Integer>():
//    var es el estilo habitual para variables locales con tipo obvio. La firma pública
//    sigue declarando Map<String, Integer>, así que el contrato de la función no cambia.
// 3. for (String word : words) -> for (var word : words): mismo criterio.
// 4. Arrays.asList(...) / Collections.emptyList() -> List.of(...) en los tests:
//    las fábricas de Java 9+ son más concisas y producen listas inmutables.
// 5. import java.util.*; -> imports explícitos: dejan claro qué clases se usan y
//    evitan conflictos de nombres; es la convención de las guías de estilo actuales.
// 6. Clase renombrada a P01WordFrequency_v17 para reflejar la versión, siguiendo la
//    convención del original (en Java, el archivo debe llamarse igual que la clase
//    pública). El nombre de la función, la lógica y los tres casos de prueba no cambian.
// 7. Corrección respecto al original: el código base no compilaba en Java 8, porque
//    usaba Map.of, que se añadió en Java 9. En Java 17 ese uso ya es válido.