import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    /**
     * Java 8 - Paradigma Funcional Estricto
     * Utiliza la API de Stream, Optional, lambdas y flatMap para aplanar
     * de forma declarativa cualquier lista anidada sin bucles imperativos.
     */
    public static List<Integer> flatten(List<?> nestedList) {
        return Optional.ofNullable(nestedList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .flatMap(element -> Optional.ofNullable(element)
                        .map(item -> item instanceof List<?> 
                                ? flatten((List<?>) item).stream() 
                                : Stream.of((Integer) item))
                        .orElseGet(Stream::empty))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando assert (Java 8 - Funcional) ---

        // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
        List<?> test1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        assert flatten(test1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Falló el Caso 1";

        // Caso 2: Lista vacía y estructuras con sublistas vacías o elementos nulos
        List<?> test2a = Collections.emptyList();
        List<?> test2b = Arrays.asList(Arrays.asList(), Arrays.asList(Arrays.asList()), Arrays.asList(1, Arrays.asList()), Arrays.asList(Arrays.asList(2)), Arrays.asList());
        assert flatten(test2a).equals(Collections.emptyList()) : "Falló el Caso 2a (Lista vacía)";
        assert flatten(test2b).equals(Arrays.asList(1, 2)) : "Falló el Caso 2b (Sublistas vacías)";

        // Caso 3: Lista totalmente plana y lista profundamente anidada
        List<?> test3a = Arrays.asList(10, 20, 30);
        List<?> test3b = Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(99)))));
        assert flatten(test3a).equals(Arrays.asList(10, 20, 30)) : "Falló el Caso 3a";
        assert flatten(test3b).equals(Arrays.asList(99)) : "Falló el Caso 3b";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}