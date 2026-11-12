// P01: Word Frequency — migrado a Java 21
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Map.getOrDefault: reemplaza containsKey + get + if/else por una sola línea
//    y reduce las búsquedas en el mapa de tres a dos por palabra repetida.
//    Se prefiere a Map.merge porque merge inserta las claves nuevas al principio
//    de su bucket y puede cambiar el orden de iteración del HashMap respecto al
//    original; getOrDefault + put conserva ese orden exactamente.
// 2. var (inferencia de tipos locales): evita repetir el tipo genérico cuando ya
//    es evidente por el lado derecho de la asignación.
// 3. List.of / Map.of (fábricas de colecciones inmutables): reemplazan a
//    Arrays.asList y Collections.emptyList en los tests.
//
// Nota sobre las novedades propias de Java 21: se evaluaron y NO se usan, porque
// ninguna mejora esta función y forzarlas empeoraría la legibilidad.
// - Patrones de record y pattern matching en switch (JEP 440/441): no hay records
//   ni jerarquías de tipos que desestructurar o distinguir.
// - Colecciones secuenciadas (JEP 431): el resultado no tiene orden definido;
//   cambiar HashMap por un mapa ordenado alteraría el orden de iteración del
//   resultado, lo que rompería el requisito de comportamiento idéntico.
// - Hilos virtuales (JEP 444): el conteo es secuencial y en memoria; no hay E/S
//   bloqueante que se beneficie de ellos.
// - String templates: eran solo vista previa en Java 21 y se retiraron después.
// La mejora de rendimiento propia de 21 llega por la JVM sin tocar el código
// (por ejemplo, ZGC generacional, JEP 439, activable con
// -XX:+UseZGC -XX:+ZGenerational para cargas con mucha basura de corta vida).

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v21 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();
        for (var word : words) {
            result.put(word, result.getOrDefault(word, 0) + 1);
        }
        return result;
    }

    // Ejecutar con assertions habilitadas: java -ea P01WordFrequency_v21.java
    public static void main(String[] args) {
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. if (containsKey) { put(get + 1) } else { put(1) } -> put(word, getOrDefault(word, 0) + 1):
//    expresa "insertar 1 o sumar 1" en una sola línea, sin rama if/else. El resultado
//    es idéntico, incluido el orden de iteración del HashMap (verificado).
// 2. Map<String, Integer> result = new HashMap<>() -> var result = new HashMap<String, Integer>():
//    var es el estilo habitual para variables locales con tipo obvio. La firma pública
//    sigue declarando Map<String, Integer>, así que el contrato no cambia.
// 3. for (String word : words) -> for (var word : words): mismo criterio.
// 4. Arrays.asList(...) / Collections.emptyList() -> List.of(...) en los tests:
//    más conciso y con listas inmutables.
// 5. import java.util.*; -> imports explícitos: dejan claro qué clases se usan y
//    evitan conflictos de nombres.
// 6. Se mantiene HashMap como implementación: el orden de iteración del resultado
//    no cambia respecto al original.
// 7. Clase renombrada a P01WordFrequency_v21 para reflejar la versión (en Java, el
//    archivo debe llamarse igual que la clase pública). El nombre de la función, la
//    lógica y los tres casos de prueba no cambian.
// 8. El comentario de cabecera del código base decía "Java 8" y no compilaba en esa
//    versión (Map.of es de Java 9); en Java 21 es válido y se actualiza la cabecera.