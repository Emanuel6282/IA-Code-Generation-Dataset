import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearchFunctional21 {

    // 1. Tipos de Datos Algebraicos (ADTs): Usamos clases selladas (Sealed Interfaces)
    // y Records para definir los estados inmutables posibles de nuestra búsqueda.
    sealed interface SearchStep permits Searching, Found, NotFound {}
    record Searching(int left, int right) implements SearchStep {}
    record Found(int index) implements SearchStep {}
    record NotFound() implements SearchStep {}

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Implementación estrictamente funcional usando Java 21 Pattern Matching.
     */
    public static int binarySearch(int[] arr, int target) {
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> Stream.iterate(
                        // Estado inicial
                        (SearchStep) new Searching(0, a.length - 1),
                        step -> switch (step) {
                            // 2. Pattern Matching for Switch con Cláusulas de Guarda (when)
                            // Característica estándar de Java 21 (JEP 441)
                            case Searching(int l, int r) when l > r -> new NotFound();
                            
                            // 3. Record Patterns (JEP 440) para deconstruir variables directamente
                            case Searching(int l, int r) -> {
                                int mid = l + (r - l) / 2;
                                int cmp = Integer.compare(a[mid], target);
                                
                                // Retorno declarativo inmutable (yield implícito)
                                yield switch (Integer.signum(cmp)) {
                                    case 0  -> new Found(mid);
                                    case -1 -> new Searching(mid + 1, r);
                                    case 1  -> new Searching(l, mid - 1);
                                    default -> new NotFound(); // Caso inalcanzable de seguridad
                                };
                            }
                            // 4. Exhaustividad garantizada: al ser una interfaz sellada, 
                            // el compilador sabe que no hay más casos posibles (no requiere default).
                            case Found f -> f;
                            case NotFound n -> n;
                        }
                )
                // 5. Evaluación perezosa: dejamos caer los estados intermedios y detenemos
                // el flujo infinito exactamente cuando la estructura cambia a Found o NotFound.
                .dropWhile(step -> step instanceof Searching)
                .findFirst()
                .flatMap(step -> switch (step) {
                    // Pattern Matching para extraer el índice final sin castings (Record Patterns)
                    case Found(int idx) -> Optional.of(idx);
                    default -> Optional.empty();
                }))
                .orElse(-1);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Recuerda que la JVM debe ejecutarse con la bandera '-ea' o '-enableassertions'

        // 1. Caso base del ejemplo
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo (no encontrado)
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite (arreglo vacío y nulo)
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";
        assert binarySearch(null, 5) == -1 : "Error: Arreglo nulo debería retornar -1";

        System.out.println("""
            ===================================================================
            Todos los tests funcionales puros en Java 21 pasaron correctamente.
            ===================================================================
            """);
    }
}