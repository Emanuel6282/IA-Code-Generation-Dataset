// P03: Flatten Nested List — Java 21
//
// Características utilizadas:
// 1. Stream API:
//    - Permite procesar la estructura de forma declarativa.
//    - flatMap() combina los elementos de las listas anidadas.
//
// 2. Lambdas:
//    - Permiten expresar las transformaciones sin bucles imperativos.
//
// 3. Optional:
//    - Permite representar funcionalmente si el elemento actual
//      corresponde a una lista anidada.
//
// 4. Pattern matching para instanceof:
//    - Característica disponible en Java 21.
//    - Simplifica la comprobación y conversión de tipos.
//
// 5. toList():
//    - Disponible desde Java 16 y apropiado en Java 21.
//    - Permite obtener directamente una lista inmodificable.
//
// No se utilizan:
// - for
// - while
// - estado mutable explícito
//
// Java 21 no introduce una nueva operación de Stream específica para
// flatten, por lo que se utilizan las características funcionales
// más apropiadas disponibles en esta versión.

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        return nestedList.stream()
                .flatMap(item ->
                        Optional.of(item)
                                .filter(value -> value instanceof List<?>)
                                .map(value -> {
                                    List<?> nested = (List<?>) value;
                                    return flatten(castToObjectList(nested)).stream();
                                })
                                .orElseGet(() ->
                                        Stream.of((Integer) item)
                                )
                )
                .toList();
    }

    private static List<Object> castToObjectList(List<?> list) {
        return list.stream()
                .map(item -> (Object) item)
                .toList();
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
            List.of(1, 2, 3, 4, 5, 6)
        );

        // Caso 2: lista vacía.
        assert flatten(new ArrayList<Object>())
                .equals(List.of());

        // Caso 3: lista sin anidamiento.
        assert flatten(new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }}).equals(
            List.of(1, 2, 3)
        );

        System.out.println("All tests passed");
    }
}