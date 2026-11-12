import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P03FlattenNestedListIterative_v8 {

    /**
     * Toma una lista arbitrariamente anidada (representada como List<Object>)
     * y retorna una lista plana unidimensional de enteros.
     * 
     * Implementación estrictamente iterativa: sin recursión ni Streams,
     * utilizando ArrayList como una pila (stack) manejada por un bucle while.
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        List<Integer> result = new ArrayList<>();
        
        // Utilizamos un ArrayList como pila (stack) explícita
        List<Object> stack = new ArrayList<>();
        
        // Inicializamos la pila insertando los elementos en orden inverso.
        // De este modo, al extraer el último elemento (LIFO), conservamos el orden original.
        for (int i = nestedList.size() - 1; i >= 0; i--) {
            stack.add(nestedList.get(i));
        }
        
        while (!stack.isEmpty()) {
            // Extraemos el elemento en el tope de la pila (último índice)
            Object item = stack.remove(stack.size() - 1);
            
            if (item instanceof Integer) {
                // Si es un escalar, se añade al resultado
                result.add((Integer) item);
            } else if (item instanceof List) {
                // Si es una sublista, iteramos en orden inverso y añadimos sus 
                // elementos a la pila para procesarlos en las siguientes iteraciones
                List<Object> list = (List<Object>) item;
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.add(list.get(i));
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Recuerda ejecutar la JVM con el flag "-ea" (enableassertions) 
        // para que las sentencias assert sean evaluadas.
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: múltiples niveles de anidamiento
        List<Object> nested1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        List<Integer> expected1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        assert flatten(nested1).equals(expected1) : "Error: Falló el caso base del ejemplo";

        // 2. Caso límite: lista completamente vacía
        List<Object> nested2 = new ArrayList<>();
        List<Integer> expected2 = new ArrayList<>();
        assert flatten(nested2).equals(expected2) : "Error: Una lista vacía debería retornar una lista vacía";

        // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
        List<Object> nested3 = Arrays.asList(1, new ArrayList<>(), Arrays.asList(Arrays.asList(Arrays.asList(3))));
        List<Integer> expected3 = Arrays.asList(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló ignorando listas vacías internas";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}