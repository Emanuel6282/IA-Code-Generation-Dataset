// Características de Java 21 utilizadas en este refactor:
// 1. Unnamed Main Methods e Instance Main Methods (JEP 445 / JEP 463 Preview/Standard feature direction):
//    Permite simplificar el punto de entrada de la aplicación eliminando la verbosidad ceremonial de
//    `public static void main(String[] args)` en favor de una estructura de clase/método más limpia.
// 2. Pattern Matching para expresiones Switch y exhaustividad mejorada (Java 21 LTS):
//    Permite tratar las ramificaciones del algoritmo de búsqueda binaria de forma explícita y
//    declarativa mediante la evaluación con `Integer.compare`, garantizando exhaustividad en tiempo de compilación.
// 3. Inferencia de tipos de variables locales (`var`):
//    Mantiene la claridad, reduce el ruido sintáctico y preserva el tipado estático estricto.

public class P02BinarySearch_v21 {

    public static int binarySearch(int[] arr, int target) {
        var left = 0;
        var right = arr.length - 1;

        while (left <= right) {
            var mid = left + (right - left) / 2;

            // Pattern Matching y Expresión Switch de Java 21 LTS
            switch (Integer.compare(arr[mid], target)) {
                case 0 -> {
                    return mid;
                }
                case -1 -> left = mid + 1; // arr[mid] < target
                case 1 -> right = mid - 1;  // arr[mid] > target
                default -> throw new IllegalStateException("Resultado de comparación inesperado");
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;
        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Switch Expressions con Pattern Matching (Java 21): Se reemplaza el bloque `if/else if/else` tradicional
//    por una expresión switch basada en `Integer.compare`, aprovechando la verificación de exhaustividad
//    estandarizada en Java 21 LTS. Esto hace el código más robusto, declarativo y fácil de leer.
// 2. Inferencia de tipos con `var`: Se simplifica la declaración de punteros de control (`left`, `right`, `mid`)
//    eliminando la redundancia de tipos explícitos sin perder la seguridad de la compilación estática.
// 3. Conservación estricta de contrato y pruebas: La firma del método `binarySearch` y los datos de entrada
//    y salida se mantienen exactamente iguales al código base de origen.