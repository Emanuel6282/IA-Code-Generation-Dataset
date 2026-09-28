import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Flatten {

    /**
     * Dada una lista arbitrariamente anidada de enteros, retorna una lista
     * plana con todos los enteros en el mismo orden de aparicion.
     *
     * Implementacion en paradigma funcional: usa la Stream API con
     * flatMap para "aplanar" cada elemento en un Stream<Integer>, y
     * Optional para decidir de forma funcional (sin if/else imperativo
     * de control de flujo) si un elemento debe tratarse como sublista
     * (que se aplana recursivamente) o como entero "hoja". No se usan
     * bucles for/while imperativos. La recursion es necesaria porque el
     * nivel de anidamiento es arbitrario y desconocido de antemano; un
     * Stream por si solo no puede expresar un descenso de profundidad
     * variable sin ella.
     *
     * @param nested Lista que puede contener Integer y/o otras List<Object>
     *               anidadas a cualquier profundidad.
     * @return Una List<Integer> plana con todos los enteros en el mismo
     *         orden de aparicion. Si la lista de entrada esta vacia,
     *         retorna una lista vacia.
     */
    public static List<Integer> flatten(List<Object> nested) {
        return nested.stream()
                .flatMap(Flatten::elementToStream)
                .collect(Collectors.toList());
    }

    /**
     * Funcion pura auxiliar: convierte un elemento individual (que puede
     * ser un Integer "hoja" o una List anidada) en un Stream<Integer> ya
     * aplanado. Usa Optional para expresar de forma funcional la
     * bifurcacion "es lista vs. es entero", en lugar de un if/else
     * imperativo de control.
     */
    @SuppressWarnings("unchecked")
    private static Stream<Integer> elementToStream(Object element) {
        return Optional.of(element)
                .filter(e -> e instanceof List)
                .map(e -> (List<Object>) e)
                .map(subLista -> flatten(subLista).stream())
                .orElseGet(() -> Stream.of((Integer) element));
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
        List<Object> caso2 = Collections.emptyList();
        assert flatten(caso2).isEmpty() : "Caso 2 fallido";

        // Caso 3: lista plana sin anidamiento
        List<Object> caso3 = Arrays.asList(1, 2, 3);
        assert flatten(caso3).equals(Arrays.asList(1, 2, 3)) : "Caso 3 fallido";

        // Uso adicional genuino de Optional: verificar de forma segura si
        // un elemento individual es una sublista antes de procesarlo
        Optional<List<Object>> subListaOpt = Optional.of((Object) Arrays.asList(5, 6))
                .filter(e -> e instanceof List)
                .map(e -> (List<Object>) e);
        assert subListaOpt.isPresent() && flatten(subListaOpt.get()).equals(Arrays.asList(5, 6))
                : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}