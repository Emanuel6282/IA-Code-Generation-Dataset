// P03: Flatten Nested List — Java 8
//
// Características utilizadas:
// - ArrayList: utilizada como pila y para almacenar el resultado.
// - HashMap: utilizada para mantener información auxiliar durante el recorrido.
// - for y while: recorrido completamente iterativo.
// - No se utilizan Streams, lambdas ni recursión.
//
// Java 8 permite utilizar estas colecciones y estructuras de forma
// eficiente manteniendo el algoritmo completamente iterativo.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        List<Integer> result = new ArrayList<Integer>();

        // Pila utilizada para evitar la recursión.
        List<Object> stack = new ArrayList<Object>(nestedList);

        // Información auxiliar del procesamiento.
        Map<String, Integer> info = new HashMap<String, Integer>();
        info.put("processed", 0);

        while (!stack.isEmpty()) {
            Object item = stack.remove(stack.size() - 1);

            if (item instanceof List<?>) {
                List<?> nested = (List<?>) item;

                // Se agregan en orden inverso para conservar
                // el orden original al extraer de la pila.
                for (int i = nested.size() - 1; i >= 0; i--) {
                    stack.add(nested.get(i));
                }
            } else {
                result.add((Integer) item);
                info.put("processed", info.get("processed") + 1);
            }
        }

        return result;
    }

    public static void main(String[] args) {

        // Caso 1: lista con varios niveles de anidamiento.
        assert flatten(
                new ArrayList<Object>() {{
                    add(1);
                    add(new ArrayList<Object>() {{
                        add(2);
                        add(3);
                    }});
                    add(new ArrayList<Object>() {{
                        add(4);
                        add(new ArrayList<Object>() {{
                            add(5);
                            add(6);
                        }});
                    }});
                }}
        ).equals(
                new ArrayList<Integer>() {{
                    add(1);
                    add(2);
                    add(3);
                    add(4);
                    add(5);
                    add(6);
                }}
        );

        // Caso 2: lista vacía.
        assert flatten(new ArrayList<Object>())
                .equals(new ArrayList<Integer>());

        // Caso 3: lista sin anidamiento.
        assert flatten(
                new ArrayList<Object>() {{
                    add(1);
                    add(2);
                    add(3);
                }}
        ).equals(
                new ArrayList<Integer>() {{
                    add(1);
                    add(2);
                    add(3);
                }}
        );

        System.out.println("All tests passed");
    }
}