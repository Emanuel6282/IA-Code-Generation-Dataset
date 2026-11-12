import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearchFunctional8 {

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Implementación estrictamente funcional usando Stream API y Optional.
     */
    public static int binarySearch(int[] arr, int target) {
        // 1. Optional.ofNullable previene condicionales imperativos (if arr == null)
        // 2. filter asegura que no procesemos arreglos vacíos de forma declarativa
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> 
                    // 3. Stream.iterate genera un flujo infinito de estados inmutables.
                    // Cada estado es un arreglo: {left, right, result_index}
                    // -2 indica "buscando", -1 indica "no encontrado", >= 0 indica "encontrado"
                    Stream.iterate(new int[]{0, a.length - 1, -2}, state -> {
                        int left = state[0];
                        int right = state[1];

                        // Condición de parada funcional (límites cruzados)
                        if (left > right) {
                            return new int[]{left, right, -1};
                        }

                        int mid = left + (right - left) / 2;

                        // Retornamos un nuevo estado inmutable dependiendo de la evaluación
                        if (a[mid] == target) {
                            return new int[]{left, right, mid}; // Encontrado
                        } else if (a[mid] < target) {
                            return new int[]{mid + 1, right, -2}; // Mitad derecha
                        } else {
                            return new int[]{left, mid - 1, -2}; // Mitad izquierda
                        }
                    })
                    // 4. filter: Deja pasar solo el estado final (cuando result_index ya no es -2)
                    .filter(state -> state[2] != -2)
                    // 5. findFirst: Fuerza la evaluación perezosa cortando el Stream infinito apenas se encuentre
                    .findFirst()
                    // 6. Extraemos el resultado final del estado
                    .map(state -> state[2])
                )
                // 7. Si el Optional está vacío (nulo o no encontrado), retorna -1
                .orElse(-1);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Para que las aserciones validen correctamente, la JVM debe 
        // ejecutarse con la bandera '-ea' (enableassertions).

        // 1. Caso base del ejemplo: el elemento existe y retorna su índice
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite: el arreglo está vacío o es nulo
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";
        assert binarySearch(null, 5) == -1 : "Error: Arreglo nulo debería retornar -1";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}