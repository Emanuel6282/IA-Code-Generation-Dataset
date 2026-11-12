// P03: Flatten Nested List — Java 21
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
// 1. Stream API y lambdas: procesamiento declarativo.
// 2. flatMap(): aplanamiento de elementos anidados.
// 3. Optional: tratamiento funcional de valores opcionales.
// 4. Pattern matching para switch (Java 21):
//    Distinción de tipos sin conversiones explícitas.
// 5. Stream.toList(): construcción del resultado.
// 6. List.of(): creación de listas de prueba.
//
// Sin bucles for/while ni estado mutable explícito.

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class P03Flatten_v21 {

    public static List<Integer> flatten(List<?> nested) {
        return nested.stream()
                .flatMap(item ->
                        Optional.ofNullable(item)
                                .map(value -> switch (value) {
                                    case List<?> sublist ->
                                            flatten(sublist).stream();

                                    case Integer number ->
                                            Stream.of(number);

                                    default ->
                                            throw new IllegalArgumentException(
                                                    "Solo se permiten enteros y listas"
                                            );
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

        // Test 3: Lista con anidamiento profundo
        assert flatten(List.of(
                1,
                List.of(List.of(2)),
                List.of(),
                List.of(3, List.of(4, List.of(5)))
        )).equals(List.of(1, 2, 3, 4, 5));

        System.out.println("All tests passed");
    }
}