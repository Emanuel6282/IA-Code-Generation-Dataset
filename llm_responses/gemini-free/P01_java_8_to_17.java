// Características de Java 17 utilizadas en este refactor:
// 1. Inferencia de tipos de variables locales (`var` - JEP 286):
//    Mejora la legibilidad reduciendo el ruido visual en la declaración de variables locales.
// 2. Método `Map.merge()` / `Map.putIfAbsent()` / `Map.getOrDefault()`:
//    Simplifica las operaciones de actualización en mapas sin requerir bloques `if/else`.
// 3. Fábricas de colecciones inmutables (`List.of()` - introducido en Java 9, estándar en LTS modernas):
//    Sustituye `Arrays.asList()` por una sintaxis más limpia e inmutable en los casos de prueba.

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

    public static void main(String[] args) {
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Uso de `var`: Reduce la verbosidad en la inicialización de mapas y en la iteración del bucle.
// 2. Método `Map.merge()`: Sustituye la comprobación manual `if (result.containsKey(word))` por una sola
//    instrucción atómica y funcional (`Integer::sum`), eliminando búsquedas dobles en la tabla hash.
// 3. Reemplazo de `Arrays.asList()` por `List.of()`: En los tests, `List.of()` genera una lista inmutable
//    más liviana e idiomática en las versiones modernas de Java.
// 4. Eliminación de importaciones con comodín (`import java.util.*`): Se reemplazó por importaciones explícitas
//    de las clases utilizadas para seguir las buenas prácticas de mantenimiento.