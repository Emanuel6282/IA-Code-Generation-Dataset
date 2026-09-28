import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    /**
     * Dada una lista arbitrariamente anidada de enteros, retorne una lista plana
     * con todos los enteros en el mismo orden de aparición.
     */
    public static List<Integer> flatten(List<?> nestedList) {
        List<Integer> result = new ArrayList<>();
        if (nestedList == null) {
            return result;
        }

        for (Object element : nestedList) {
            if (element instanceof List<?>) {
                result.addAll(flatten((List<?>) element));
            } else if (element instanceof Integer) {
                result.add((Integer) element);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando assert ---

        // Caso 1: Ejemplo del problema con múltiples niveles de anidamiento
        List<?> test1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        assert flatten(test1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Falló el Caso 1";

        // Caso 2: Lista vacía o con sublistas vacías intercaladas
        List<?> test2 = Arrays.asList(Arrays.asList(), Arrays.asList(Arrays.asList()), Arrays.asList(1, Arrays.asList()), Arrays.asList(Arrays.asList(2)), Arrays.asList());
        assert flatten(test2).equals(Arrays.asList(1, 2)) : "Falló el Caso 2";

        // Caso 3: Lista totalmente plana y lista profundamente anidada
        List<?> test3a = Arrays.asList(10, 20, 30);
        List<?> test3b = Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(99)))));
        assert flatten(test3a).equals(Arrays.asList(10, 20, 30)) : "Falló el Caso 3a";
        assert flatten(test3b).equals(Arrays.asList(99)) : "Falló el Caso 3b";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}