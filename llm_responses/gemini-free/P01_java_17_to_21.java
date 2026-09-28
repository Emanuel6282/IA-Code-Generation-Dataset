// Características de Java 21 utilizadas en este refactor:
// 1. Invocación simplificada / Instance Main Methods (JEP 445 / JEP 463 preview features / Java 21 standard enhancements):
//    Se eliminan los parámetros `String[] args` innecesarios en el método `main`.
// 2. Inferencia de tipos con `var` (JEP 286):
//    Mejora la legibilidad eliminando la redundancia visual en declaraciones locales.
// 3. Método `Map.merge()` con referencia a método (`Integer::sum`):
//    Sustituye la estructura condicional por una operación atómica y más limpia en la tabla Hash.
// 4. Métodos de fábrica para colecciones inmutables (`List.of()`):
//    Sustituye `Arrays.asList()` por una opción más moderna, concisa y ligera en memoria.

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v21 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();
        for (var word : words) {
            result.merge(word, 1, Integer::sum);
        }
        return result;
    }

    public static void main() {
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Método `main()` simplificado: En Java 21+, el método de entrada `main` ya no requiere declarar
//    `String[] args` si no se consumen parámetros de la línea de comandos, haciendo el punto de entrada más limpio.
// 2. Reemplazo de `if/else` por `Map.merge()`: Evita búsquedas dobles en el mapa (containsKey + get + put),
//    realizando la actualización en un único paso y mejorando el rendimiento y legibilidad.
// 3. Uso de `var`: Reduce el código repetitivo en la definición de variables locales y en el bucle for.
// 4. Migración a `List.of()`: En lugar de usar `Arrays.asList()` o `Collections.emptyList()`, se adopta
//    `List.of()` como la forma canónica e inmutable de instanciar listas en Java moderno.