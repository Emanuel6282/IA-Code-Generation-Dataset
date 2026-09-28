// P03: Flatten Nested List — Java 8
//
// Características utilizadas:
// - Stream API: permite procesar y combinar los elementos de forma funcional.
// - Lambdas: permiten expresar las transformaciones sin bucles imperativos.
// - Optional: permite representar de forma segura la presencia o ausencia
//   de un elemento entero.
// - flatMap(): aplana cada nivel de listas anidadas.
// - Collectors.toList(): construye la lista resultado.
//
// No se utilizan:
// - for
// - while
// - estado mutable para el procesamiento
//
// Java 8 introdujo Stream API, expresiones lambda y Optional,
// por lo que son las características funcionales apropiadas para
// este problema.

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        return nestedList.stream()
                .flatMap(item -> {
                    Optional<List<Object>> nested =
                            item instanceof List<?>
                                    ? Optional.of((List<Object>) item)
                                    : Optional.empty();

                    return nested
                            .map(list -> flatten(list).stream())
                            .orElseGet(() ->
                                    Stream.of((Integer) item));
                })
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

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

        assert flatten(new ArrayList<Object>())
                .equals(new ArrayList<Integer>());

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