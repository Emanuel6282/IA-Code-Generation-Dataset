// P03: Flatten Nested List — Java 8 (codigo base para refactoring)
// Estilo: imperativo con Stack explicita, compatible con Java 8
import java.util.*;

public class P03FlattenList_v8 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();
        stack.push(nested);

        while (!stack.isEmpty()) {
            Object item = stack.pop();
            if (item instanceof List) {
                List<?> list = (List<?>) item;
                // Agregar en orden inverso para preservar el orden original
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (item instanceof Integer) {
                result.add((Integer) item);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // TC01: anidamiento basico
        List<Object> input1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        assert flatten(input1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(new ArrayList<>()).equals(Collections.emptyList()) : "TC02 fallo";

        // TC03: ya plana
        List<Object> input3 = Arrays.<Object>asList(1, 2, 3);
        assert flatten(input3).equals(Arrays.asList(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}
