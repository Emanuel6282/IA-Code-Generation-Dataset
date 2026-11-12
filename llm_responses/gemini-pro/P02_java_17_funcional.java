import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearchFunctional17 {

    /**
     * Java 14+ (estandarizado en 17): 'record' define clases portadoras de datos 
     * puramente inmutables de forma concisa. Actuará como nuestra máquina de estados.
     */
    private record SearchState(int left, int right, int result) {}

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Implementación estrictamente funcional, inmutable y evaluada perezosamente (lazy).
     */
    public static int binarySearch(int[] arr, int target) {
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> Stream.iterate(
                        // Estado inicial: result -2 indica "buscando"
                        new SearchState(0, a.length - 1, -2),
                        s -> {
                            // Condición de parada estructural (límites cruzados)
                            if (s.left() > s.right()) {
                                return new SearchState(s.left(), s.right(), -1);
                            }

                            // Uso de 'var' (Java 10+) dentro de lambdas para inferencia de tipos
                            var mid = s.left() + (s.right() - s.left()) / 2;
                            var cmp = Integer.compare(a[mid], target);

                            // Java 14+: Expresiones Switch (Switch Expressions)
                            // Retornan un valor directamente de forma declarativa, sin sentencias 'break'
                            return switch (Integer.signum(cmp)) {
                                case 0  -> new SearchState(s.left(), s.right(), mid); // Encontrado
                                case -1 -> new SearchState(mid + 1, s.right(), -2);   // Buscar en derecha
                                case 1  -> new SearchState(s.left(), mid - 1, -2);    // Buscar en izquierda
                                default -> throw new IllegalStateException("Unreachable code");
                            };
                        }
                )
                // Java 9+: 'dropWhile' descarta perezosamente los estados donde seguimos iterando (result == -2)
                .dropWhile(s -> s.result() == -2)
                // Detiene el stream infinito exactamente cuando se encuentra la respuesta
                .findFirst()
                .map(SearchState::result))
                .orElse(-1);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Recuerda ejecutar con la bandera '-ea' (enableassertions) de la JVM

        // 1. Caso base: el elemento existe (índice 2)
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso: el elemento no existe (retorna -1)
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite: el arreglo vacío o nulo
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";
        assert binarySearch(null, 5) == -1 : "Error: Arreglo nulo debería retornar -1";

        // Java 15+: Text Blocks para cadenas multilínea sin caracteres de escape '\n'
        System.out.println("""
            ===========================================================
            Todos los tests funcionales en Java 17 pasaron correctamente.
            ===========================================================
            """);
    }
}