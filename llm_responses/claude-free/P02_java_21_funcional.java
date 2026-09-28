import java.util.Optional;

public class BinarySearch {

    /**
     * Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
     * retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
     * retorna -1.
     *
     * Implementacion en paradigma funcional: sin bucles for/while
     * imperativos. El nucleo del algoritmo se resuelve con recursion pura
     * (funciones sin efectos secundarios, sin variables mutables externas),
     * y el resultado final se envuelve en un Optional<Integer> para exponer
     * una API funcional idiomatica antes de convertirlo al int requerido por
     * la firma. No usa Arrays.binarySearch ni ninguna otra funcion de
     * busqueda de la biblioteca estandar. Compatible con Java 21.
     *
     * @param arr Arreglo de enteros ordenado ascendentemente.
     * @param target Valor entero a buscar.
     * @return El indice donde se encuentra target dentro de arr, o -1 si
     *         target no esta presente.
     */
    public static int binarySearch(int[] arr, int target) {
        var resultado = buscarRecursivo(arr, target, 0, arr.length - 1);
        return resultado.orElse(-1);
    }

    /**
     * Funcion pura auxiliar: dado un rango [left, right] inmutable en cada
     * llamada, retorna un Optional con el indice encontrado, o
     * Optional.empty() si el rango se agota sin exito. No muta ningun
     * estado externo; cada llamada recursiva recibe nuevos valores de
     * left/right calculados, nunca variables reasignadas.
     */
    private static Optional<Integer> buscarRecursivo(int[] arr, int target, int left, int right) {
        if (left > right) {
            return Optional.empty();
        }

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return Optional.of(mid);
        } else if (arr[mid] < target) {
            return buscarRecursivo(arr, target, mid + 1, right);
        } else {
            return buscarRecursivo(arr, target, left, mid - 1);
        }
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, objetivo presente en el medio
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Caso 1 fallido";

        // Caso 2: ejemplo del enunciado, objetivo ausente
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Caso 2 fallido";

        // Caso 3: arreglo vacio -> siempre -1
        assert binarySearch(new int[]{}, 1) == -1 : "Caso 3 fallido";

        // Uso adicional genuino de Optional: obtener el resultado de forma
        // segura sin exponer el -1 "magico" en codigo intermedio
        var indiceOpt = buscarRecursivo(new int[]{1, 3, 5, 7, 9}, 5, 0, 4);
        assert indiceOpt.isPresent() && indiceOpt.get() == 2 : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}