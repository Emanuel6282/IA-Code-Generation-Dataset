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
     * flatMap para "aplanar" cada elemento en un Stream<Integer>. La
     * bifurcacion "es lista vs. es entero" se expresa con pattern
     * matching for switch (JEP 441, estandar desde Java 21) dentro de una
     * expresion switch, en lugar de un if/else imperativo de control. No
     * se usan bucles for/while imperativos. La recursion es necesaria
     * porque el nivel de anidamiento es arbitrario y desconocido de
     * antemano; un Stream por si solo no puede expresar un descenso de
     * profundidad variable sin ella. Compatible con Java 21.
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
     * aplanado. Usa pattern matching for switch (expresion switch) para
     * decidir de forma funcional y declarativa la bifurcacion, en lugar
     * de un if/else imperativo.
     */
    private static Stream<Integer> elementToStream(Object element) {
        return switch (element) {
            case List<?> subLista -> flatten(castALista(subLista)).stream();
            case Integer numero -> Stream.of(numero);
            default -> Stream.empty();
        };
    }

    /**
     * Funcion pura auxiliar que envuelve el cast inevitable (por type
     * erasure) de List<?> a List<Object>, usando Optional para expresar
     * la operacion de forma funcional en lugar de un cast directo suelto.
     */
    @SuppressWarnings("unchecked")
    private static List<Object> castALista(List<?> lista) {
        return Optional.of(lista)
                .map(l -> (List<Object>) l)
                .orElseThrow();
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
        var caso1 = Arrays.<Object>asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        );
        assert flatten(caso1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Caso 1 fallido";

        // Caso 2: lista vacia -> lista vacia
        List<Object> caso2 = Collections.emptyList();
        assert flatten(caso2).isEmpty() : "Caso 2 fallido";

        // Caso 3: lista plana sin anidamiento
        var caso3 = Arrays.<Object>asList(1, 2, 3);
        assert flatten(caso3).equals(Arrays.asList(1, 2, 3)) : "Caso 3 fallido";

        // Uso adicional genuino de Optional, combinado con pattern
        // matching for switch para una verificacion de tipo declarativa
        Object candidato = Arrays.asList(5, 6);
        var subListaOpt = switch (candidato) {
            case List<?> l -> Optional.of(l);
            default -> Optional.<List<?>>empty();
        };
        assert subListaOpt.isPresent()
                && flatten(castALista(subListaOpt.get())).equals(Arrays.asList(5, 6))
                : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}