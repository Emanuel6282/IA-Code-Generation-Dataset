import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearch {

    // Estado inmutable de la búsqueda binaria.
    record SearchState(int left, int right, Optional<Integer> result) {
    }

    public static int binarySearch(int[] arr, int target) {

        SearchState initialState =
                new SearchState(0, arr.length - 1, Optional.empty());

        Optional<Integer> result = Stream.iterate(
                        initialState,
                        state -> state.result().isEmpty()
                                && state.left() <= state.right(),
                        state -> nextState(state, arr, target)
                )
                .filter(state -> state.result().isPresent())
                .map(state -> state.result().get())
                .findFirst();

        return result.orElse(-1);
    }

    private static SearchState nextState(
            SearchState state,
            int[] arr,
            int target) {

        int mid = state.left()
                + (state.right() - state.left()) / 2;

        if (arr[mid] == target) {
            return new SearchState(
                    state.left(),
                    state.right(),
                    Optional.of(mid)
            );
        }

        if (arr[mid] < target) {
            return new SearchState(
                    mid + 1,
                    state.right(),
                    Optional.empty()
            );
        }

        return new SearchState(
                state.left(),
                mid - 1,
                Optional.empty()
        );
    }

    public static void main(String[] args) {
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;

        System.out.println("Todos los tests pasaron correctamente.");
    }
}