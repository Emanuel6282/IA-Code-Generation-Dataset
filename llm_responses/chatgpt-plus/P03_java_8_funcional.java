// P03: Flatten Nested List — Java 8
// Paradigma: Funcional
//
// Características:
// - Stream API de Java 8.
// - Expresiones lambda.
// - flatMap() para aplanar listas anidadas.
// - Optional para representar valores presentes.
// - Recursión sin bucles for/while.
// - Sin modificar las listas de entrada.
// - Biblioteca estándar únicamente.

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P03Flatten_v8 {

    public static List<Integer> flatten(List<?> nested) {
        return nested.stream()
                .flatMap(item ->
                        Optional.ofNullable(item)
                                .map(value -> {
                                    if (value instanceof List<?>) {
                                        return flatten((List<?>) value).stream();
                                    }

                                    return Stream.of((Integer) value);
                                })
                                .orElseGet(Stream::empty)
                )
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        // Test 1: Lista con varios niveles de anidamiento
        assert flatten(Arrays.asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        )).equals(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Test 2: Lista vacía
        assert flatten(Collections.emptyList())
                .equals(Collections.emptyList());

        // Test 3: Lista plana
        assert flatten(Arrays.asList(1, 2, 3))
                .equals(Arrays.asList(1, 2, 3));

        System.out.println("All tests passed");
    }
}