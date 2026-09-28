import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Solution {

    /**
     * Java 21 - Paradigma Funcional Estricto
     * Utiliza Stream API, Optional, lambdas, Pattern Matching para switch como expresión (Java 21)
     * y Stream.toList() para aplanar de forma declarativa cualquier lista anidada sin bucles imperativos.
     */
    public static List<Integer> flatten(List<?> nestedList) {
        return Optional.ofNullable(nestedList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .flatMap(element -> Optional.ofNullable(element)
                        .map(item -> switch (item) {
                            case List<?> list -> flatten(list).stream();
                            case Integer val -> Stream.of(val);
                            default -> Stream.<Integer>empty();
                        })
                        .orElseGet(Stream::empty))
                .toList(); // Característica idiomática de colección inmutable
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando assert (Java 21 - Funcional) ---

        // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
        List<?> test1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(test1).equals(List.of(1, 2, 3, 4, 5, 6)) : "Falló el Caso 1";

        // Caso 2: Lista vacía y estructuras con sublistas vacías
        List<?> test2a = List.of();
        List<?> test2b = List.of(List.of(), List.of(List.of()), List.of(1, List.of()), List.of(List.of(2)), List.of());
        assert flatten(test2a).equals(List.of()) : "Falló el Caso 2a (Lista vacía)";
        assert flatten(test2b).equals(List.of(1, 2)) : "Falló el Caso 2b (Sublistas vacías)";

        // Caso 3: Lista totalmente plana y lista profundamente anidada
        List<?> test3a = List.of(10, 20, 30);
        List<?> test3b = List.of(List.of(List.of(List.of(List.of(99)))));
        assert flatten(test3a).equals(List.of(10, 20, 30)) : "Falló el Caso 3a";
        assert flatten(test3b).equals(List.of(99)) : "Falló el Caso 3b";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}