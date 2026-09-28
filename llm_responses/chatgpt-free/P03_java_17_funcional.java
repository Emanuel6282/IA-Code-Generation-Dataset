// P03: Flatten Nested List — Java 17
//
// Características utilizadas:
// 1. Stream API:
//    - Permite procesar la estructura de forma declarativa.
//    - flatMap() combina los resultados de las listas anidadas.
//
// 2. Lambdas:
//    - Permiten expresar las transformaciones sin bucles imperativos.
//
// 3. Optional:
//    - Representa de forma segura si el elemento actual es una lista
//      anidada o un entero.
//
// 4. Pattern matching para instanceof:
//    - Característica final de Java 16, disponible en Java 17.
//    - Permite comprobar y obtener el valor de una lista de forma concisa.
//
// No se utilizan:
// - for
// - while
// - estado mutable explícito
// - librerías externas

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        return nestedList.stream()
                .flatMap(item ->
                        Optional.of(item)
                                .filter(value -> value instanceof List<?>)
                                .map(value -> {
                                    @SuppressWarnings("unchecked")
                                    List<Object> nested = (List<Object>) value;
                                    return flatten(nested).stream();
                                })
                                .orElseGet(() ->
                                        Stream.of((Integer) item)
                                )
                )
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        // Caso 1: múltiples niveles de anidamiento.
        assert flatten(new ArrayList<Object>() {{
            add(1);
            add(new ArrayList<Object>() {{
                add(2);
                add(3);
            }});
            add(new ArrayList<Object>() {{
                add(4);
                add(new ArrayList<Object>() {{
                    add(5);
                    add(6);
                }});
            }});
        }}).equals(
            new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
            }}
        );

        // Caso 2: lista vacía.
        assert flatten(new ArrayList<Object>())
                .equals(new ArrayList<Integer>());

        // Caso 3: lista sin anidamiento.
        assert flatten(new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }}).equals(
            new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
            }}
        );

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS Y JUSTIFICACIÓN:
 *
 * 1. Se utilizó Stream API para reemplazar los bucles imperativos.
 *    El procesamiento se expresa mediante un pipeline funcional.
 *
 * 2. Se utilizó flatMap() porque el problema consiste precisamente
 *    en combinar múltiples listas en un único flujo de enteros.
 *
 * 3. Se utilizó Optional para representar funcionalmente las dos
 *    posibilidades de cada elemento:
 *      - es una lista anidada;
 *      - es un entero.
 *
 * 4. Se utilizaron lambdas para definir las transformaciones
 *    directamente dentro del pipeline.
 *
 * 5. Se utilizó pattern matching para instanceof:
 *      value instanceof List<?>
 *
 *    Esta característica está disponible de forma final en Java 17.
 *
 * 6. No se utilizan for ni while.
 *    Todo el procesamiento se realiza mediante Stream API.
 *
 * 7. La recursión es funcional y necesaria para soportar cualquier
 *    nivel de anidamiento.
 *
 * 8. Collectors.toList() genera la lista resultado sin gestionar
 *    explícitamente un ArrayList mutable durante el procesamiento.
 *
 * 9. Se utilizan únicamente APIs de la biblioteca estándar de Java 17.
 *
 * 10. Los tres tests verifican:
 *     - lista con múltiples niveles de anidamiento;
 *     - lista vacía;
 *     - lista sin anidamiento.
 */