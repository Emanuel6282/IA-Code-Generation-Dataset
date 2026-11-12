// P03: Flatten Nested List — Java 17
// Paradigma: Funcional
//
// Características utilizadas:
// 1. Stream API y lambdas para procesar los elementos.
// 2. flatMap() para aplanar listas anidadas.
// 3. Optional para representar valores opcionales.
// 4. Pattern matching con instanceof para evitar casts explícitos.
// 5. Stream.toList() para obtener el resultado (Java 16+).
// 6. List.of() para construir los casos de prueba (Java 9+).
//
// Sin bucles for/while ni estado mutable explícito.

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class P03Flatten_v17 {

    public static List<Integer> flatten(List<?> nested) {
        return nested.stream()
                .flatMap(item ->
                        Optional.ofNullable(item)
                                .map(value -> {
                                    if (value instanceof List<?> sublist) {
                                        return flatten(sublist).stream();
                                    }

                                    return Stream.of((Integer) value);
                                })
                                .orElseGet(Stream::empty)
                )
                .toList();
    }

    public static void main(String[] args) {

        // Test 1: Lista con varios niveles de anidamiento
        assert flatten(List.of(
                1,
                List.of(2, 3),
                List.of(4, List.of(5, 6))
        )).equals(List.of(1, 2, 3, 4, 5, 6));

        // Test 2: Lista vacía
        assert flatten(List.of()).equals(List.of());

        // Test 3: Lista plana
        assert flatten(List.of(1, 2, 3))
                .equals(List.of(1, 2, 3));

        System.out.println("All tests passed");
    }
}