// Características de Java 17 utilizadas en este refactor:
// 1. Inferencia de tipos para variables locales (`var` - Java 10+):
//    Reduce la redundancia sintáctica sin perder el tipado estático fuerte en variables locales.
// 2. Expresiones Switch (Switch Expressions con flechas `->` - Java 14+):
//    Reemplaza la cadena tradicional de `if/else if/else` por una estructura declarativa y exhaustiva
//    basada en el resultado de `Integer.compare`, eliminando la necesidad de `break` explícitos.

public class P02BinarySearch_v17 {

    public static int binarySearch(int[] arr, int target) {
        var left = 0;
        var right = arr.length - 1;

        while (left <= right) {
            var mid = left + (right - left) / 2;

            // Evaluación del flujo de control usando Expresiones Switch (Java 17)
            switch (Integer.compare(arr[mid], target)) {
                case 0 -> {
                    return mid;
                }
                case -1 -> left = mid + 1; // arr[mid] < target
                case 1 -> right = mid - 1;  // arr[mid] > target
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
// 1. Uso de `var` para variables locales: Se aplica inferencia de tipos en `left`, `right` y `mid`.
//    Esto mejora la legibilidad al evitar la repetición del tipo `int`, manteniendo la seguridad del sistema de tipos en compilación.
// 2. Refactorización de `if/else if/else` a Expresiones Switch: Se aprovecha `Integer.compare(arr[mid], target)`
//    junto con la sintaxis de flecha (`->`) introducida en versiones LTS recientes (Java 17). Esto elimina la verbosidad
//    y el riesgo de fall-through de los switches clásicos, declarando la intención del algoritmo de forma más directa.
// 3. Mantenimiento estricto del comportamiento y la firma: La función conserva el nombre exacto `binarySearch`,
//    opera de manera idéntica en tiempo de ejecución y ejecuta exactamente las mismas aserciones en el método `main`.