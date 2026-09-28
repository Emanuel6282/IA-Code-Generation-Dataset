import java.util.Optional;
import java.util.stream.IntStream;

public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {
        Optional<Integer> result = IntStream.range(0, arr.length)
                .filter(index -> arr[index] == target)
                .boxed()
                .findFirst();

        return result.orElse(-1);
    }

    public static void main(String[] args) {
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;

        System.out.println("Todos los tests pasaron correctamente.");
    }
}