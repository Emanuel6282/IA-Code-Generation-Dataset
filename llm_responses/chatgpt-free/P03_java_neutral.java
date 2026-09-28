import java.util.ArrayList;
import java.util.List;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        List<Integer> result = new ArrayList<>();

        for (Object item : nestedList) {
            if (item instanceof List<?>) {
                result.addAll(flatten((List<Object>) item));
            } else {
                result.add((Integer) item);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        assert flatten(List.of(
                1,
                List.of(2, 3),
                List.of(4, List.of(5, 6))
        )).equals(List.of(1, 2, 3, 4, 5, 6));

        assert flatten(List.of()).equals(List.of());

        assert flatten(List.of(1, 2, 3)).equals(List.of(1, 2, 3));

        System.out.println("All tests passed");
    }
}