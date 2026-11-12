/*
 * P01: Word Frequency — Java 21
 *
 * CARACTERÍSTICAS UTILIZADAS:
 *
 * 1. List.of() (Java 9):
 *    Permite crear listas inmutables de forma concisa.
 *
 * 2. Map.of() (Java 9):
 *    Simplifica la creación de mapas esperados en los tests.
 *
 * 3. var (Java 10):
 *    Permite inferir tipos de variables locales,
 *    reduciendo código repetitivo.
 *
 * 4. getOrDefault() (Java 8):
 *    Simplifica el conteo de frecuencias.
 *
 * 5. Java 21:
 *    Permite aprovechar las optimizaciones del JVM
 *    sin modificar la lógica del algoritmo.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v21 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();

        for (String word : words) {
            result.put(word, result.getOrDefault(word, 0) + 1);
        }

        return result;
    }

    public static void main(String[] args) {

        // Test 1: Palabras repetidas
        assert wordFrequency(List.of("the", "fox", "the"))
                .equals(Map.of("the", 2, "fox", 1));

        // Test 2: Lista vacía
        assert wordFrequency(List.of())
                .equals(Map.of());

        // Test 3: Una sola palabra
        assert wordFrequency(List.of("hello"))
                .equals(Map.of("hello", 1));

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. Arrays.asList() -> List.of():
 *    Simplifica la creación de listas de prueba.
 *    Las listas son inmutables.
 *
 * 2. Collections.emptyList() -> List.of():
 *    Utiliza una sintaxis moderna para listas vacías.
 *
 * 3. Map<String, Integer> result -> var result:
 *    Aprovecha la inferencia de tipos locales.
 *    El tipo inferido es HashMap<String, Integer>.
 *
 * 4. containsKey() + get() -> getOrDefault():
 *    Reduce la cantidad de código y mantiene
 *    el mismo comportamiento de conteo.
 *
 * 5. Se conserva HashMap:
 *    Mantiene la estructura mutable del algoritmo.
 *
 * 6. Se conserva el bucle for-each:
 *    Mantiene el paradigma iterativo original.
 *
 * 7. Se mantienen los tres tests:
 *    Verifican palabras repetidas, lista vacía
 *    y una sola palabra.
 *
 * 8. Se conserva el nombre wordFrequency:
 *    Garantiza compatibilidad con llamadas existentes.
 *
 * NOTA:
 * Las características sintácticas utilizadas fueron
 * introducidas antes de Java 21, pero son idiomáticas
 * y plenamente compatibles con esta versión.
 *
 * Java 21 incorpora características adicionales,
 * como colecciones secuenciadas y patrones de registros,
 * que no aportan ventajas a este algoritmo.
 */