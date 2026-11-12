import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlattenNestedList {

    /**
     * Toma una lista arbitrariamente anidada (representada como List<Object> en Java)
     * y retorna una lista plana unidimensional de enteros.
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        List<Integer> result = new ArrayList<>();
        
        for (Object item : nestedList) {
            if (item instanceof Integer) {
                // Si es un entero escalar, lo agregamos al resultado
                result.add((Integer) item);
            } else if (item instanceof List) {
                // Si es una sublista, llamamos recursivamente y agregamos todos sus elementos
                result.addAll(flatten((List<Object>) item));
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea FlattenNestedList)
        
        // 1. Caso base del ejemplo: [1, [2, 3], [4, [5, 6]]]
        List<Object> nested1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        List<Integer> expected1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        assert flatten(nested1).equals(expected1) : "Error: Falló el caso base del ejemplo";

        // 2. Caso límite: Lista completamente vacía
        List<Object> nested2 = new ArrayList<>();
        List<Integer> expected2 = new ArrayList<>();
        assert flatten(nested2).equals(expected2) : "Error: Una lista vacía debería retornar una lista vacía";

        // 3. Caso extremo: Anidamiento profundo asimétrico con listas vacías en el medio [1, [], [[[3]]]]
        List<Object> nested3 = Arrays.asList(1, new ArrayList<>(), Arrays.asList(Arrays.asList(Arrays.asList(3))));
        List<Integer> expected3 = Arrays.asList(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló al procesar listas vacías internas";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}