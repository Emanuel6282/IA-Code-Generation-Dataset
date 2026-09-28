public class BinarySearch {

    /**
     * Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
     * retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
     * retorna -1.
     *
     * Implementacion manual del algoritmo de busqueda binaria (sin usar
     * java.util.Arrays.binarySearch ni ninguna otra funcion de busqueda de
     * la biblioteca estandar).
     *
     * @param arr Arreglo de enteros ordenado ascendentemente.
     * @param target Valor entero a buscar.
     * @return El indice donde se encuentra target dentro de arr, o -1 si
     *         target no esta presente.
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, objetivo presente en el medio
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Caso 1 fallido";

        // Caso 2: ejemplo del enunciado, objetivo ausente
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Caso 2 fallido";

        // Caso 3: arreglo vacio -> siempre -1
        assert binarySearch(new int[]{}, 1) == -1 : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}