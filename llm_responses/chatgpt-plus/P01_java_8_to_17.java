/*
 * P01: Word Frequency — Java 17
 *
 * Características modernas utilizadas:
 *
 * 1. List.of() (Java 9):
 *    Permite crear listas inmutables de forma concisa.
 *
 * 2. Map.of() (Java 9):
 *    Simplifica la construcción de mapas esperados en los tests.
 *
 * 3. Operador diamante <>:
 *    Reduce la repetición de tipos genéricos.
 *
 * 4. HashMap y getOrDefault() (Java 8):
 *    Simplifican el conteo de frecuencias.
 *
 * 5. Compatibilidad con Java 17:
 *    Aprovecha las optimizaciones internas del intérprete
 *    sin modificar el comportamiento del algoritmo.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v17 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> result = new HashMap<>();

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
 *    Se utiliza una sintaxis más concisa para crear las listas
 *    de prueba. List.of() genera listas inmutables.
 *
 * 2. Collections.emptyList() -> List.of():
 *    Simplifica la creación de listas vacías.
 *
 * 3. Se conserva Map.of():
 *    Permite construir mapas inmutables para las pruebas.
 *
 * 4. containsKey() + get() -> getOrDefault():
 *    Reduce el código necesario para actualizar frecuencias
 *    y elimina la estructura condicional explícita.
 *
 * 5. Se conserva HashMap:
 *    Mantiene el almacenamiento mutable y el comportamiento
 *    original del algoritmo.
 *
 * 6. Se conserva el bucle for-each:
 *    Mantiene el paradigma iterativo del código base.
 *
 * 7. Se conservan los tres tests originales:
 *    Verifican palabras repetidas, lista vacía y palabra única.
 *
 * 8. Se mantiene el nombre wordFrequency:
 *    Garantiza compatibilidad con el código que utiliza
 *    la función original.
 *
 * Nota: Las características utilizadas no son exclusivas
 * de Java 17, pero representan prácticas idiomáticas
 * compatibles con esta versión.
 */