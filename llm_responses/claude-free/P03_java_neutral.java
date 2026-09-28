import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flatten {

    /**
     * Dada una lista arbitrariamente anidada de enteros, retorna una lista
     * plana con todos los enteros en el mismo orden de aparicion.
     *
     * En Java no existe un tipo nativo que represente directamente "una
     * lista que puede contener enteros o mas listas anidadas" de forma tan
     * flexible como en Python; se modela con List<Object>, donde cada
     * elemento es, en tiempo de ejecucion, o bien un Integer (un "hoja") o
     * bien otra List<?> (un nivel de anidamiento). Se usa instanceof para
     * distinguir ambos casos en cada elemento.
     *
     * @param nested Lista que puede contener Integer y/o otras List<Object>
     *               anidadas a cualquier profundidad.
     * @return Una List<Integer> plana con todos los enteros en el mismo
     *         orden de aparicion. Si la lista de entrada esta vacia,
     *         retorna una lista vacia.
     */
    public static List<Integer> flatten(List<Object> nested) {
        List<Integer> result = new ArrayList<>();

        for (Object element : nested) {
            if (element instanceof List) {
                // Llamada recursiva: cada nivel de anidamiento se resuelve
                // aplanando esa sublista y agregando (addAll) sus resultados.
                @SuppressWarnings("unchecked")
                List<Object> subLista = (List<Object>) element;
                result.addAll(flatten(subLista));
            } else if (element instanceof Integer) {
                result.add((Integer) element);
            }
            // Cualquier otro tipo se ignora silenciosamente, ya que el
            // contrato de la funcion asume una lista de enteros anidados.
        }

        return result;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
        List<Object> caso1 = Arrays.asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        );
        assert flatten(caso1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Caso 1 fallido";

        // Caso 2: lista vacia -> lista vacia
        List<Object> caso2 = new ArrayList<>();
        assert flatten(caso2).isEmpty() : "Caso 2 fallido";

        // Caso 3: lista plana sin anidamiento (caso base, sin sublistas)
        List<Object> caso3 = Arrays.asList(1, 2, 3);
        assert flatten(caso3).equals(Arrays.asList(1, 2, 3)) : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}