import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class FlattenNestedList {

    public static List<Integer> flatten(List<?> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        // Insertar los elementos en orden inverso.
        for (int i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        while (!stack.isEmpty()) {
            Object item = stack.pop();

            if (item instanceof List<?>) {
                List<?> sublist = (List<?>) item;

                for (int i = sublist.size() - 1; i >= 0; i--) {
                    stack.push(sublist.get(i));
                }
            } else {
                result.add((Integer) item);
            }
        }

        return result;
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

        // Test 3: Lista con anidamiento profundo
        assert flatten(Arrays.asList(
                1,
                Arrays.asList(Arrays.asList(2)),
                Collections.emptyList(),
                Arrays.asList(3, Arrays.asList(4, Arrays.asList(5)))
        )).equals(Arrays.asList(1, 2, 3, 4, 5));

        System.out.println("All tests passed");
    }
}