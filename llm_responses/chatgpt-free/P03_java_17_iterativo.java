// P03: Flatten Nested List — Java 17
//
// Características utilizadas:
// 1. Pattern matching para instanceof:
//    - Permite comprobar y convertir el elemento a List<?> de forma
//      más concisa y legible.
//    - Disponible como característica final en Java 16 y, por tanto,
//      plenamente disponible en Java 17.
//
// 2. var para variables locales:
//    - Reduce código repetitivo sin perder claridad en variables
//      cuyo tipo es evidente.
//
// 3. ArrayList:
//    - Se utiliza como pila y como lista de resultados.
//
// 4. HashMap:
//    - Se utiliza para almacenar información auxiliar del procesamiento.
//
// No se utilizan Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlattenNestedList {

    public static List<Integer> flatten(List<Object> nestedList) {
        var result = new ArrayList<Integer>();
        var stack = new ArrayList<Object>(nestedList);

        var info = new HashMap<String, Integer>();
        info.put("processed", 0);

        while (!stack.isEmpty()) {
            var item = stack.remove(stack.size() - 1);

            if (item instanceof List<?> nested) {
                for (var i = nested.size() - 1; i >= 0; i--) {
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
        assert flatten(new ArrayList<Object>() {{
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
        }}).equals(
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
        assert flatten(new ArrayList<Object>() {{
            add(1);
            add(2);
            add(3);
        }}).equals(
            new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
            }}
        );

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS Y JUSTIFICACIÓN:
 *
 * 1. Se utilizó pattern matching para instanceof:
 *      item instanceof List<?> nested
 *
 *    Esto evita escribir por separado la comprobación y el cast:
 *      if (item instanceof List<?>) {
 *          List<?> nested = (List<?>) item;
 *      }
 *
 *    Es una característica final de Java 16 y está disponible en Java 17.
 *
 * 2. Se utilizó var para variables locales:
 *      var result = new ArrayList<Integer>();
 *      var stack = new ArrayList<Object>(nestedList);
 *
 *    Java 17 permite utilizar var en variables locales cuando el tipo
 *    puede deducirse claramente de la expresión de inicialización.
 *
 * 3. Se mantuvo ArrayList como pila.
 *    Esto permite eliminar la recursión y procesar cualquier nivel
 *    de anidamiento de forma iterativa.
 *
 * 4. Se mantuvo HashMap como estructura auxiliar, según el requisito.
 *
 * 5. Se conservaron for y while.
 *    El while procesa la pila y el for recorre cada lista anidada
 *    en orden inverso para conservar el orden original.
 *
 * 6. Se eliminaron Streams, lambdas y recursión.
 *    El algoritmo es completamente iterativo.
 *
 * 7. Los tres casos de prueba se mantienen funcionalmente iguales:
 *    - lista con múltiples niveles de anidamiento;
 *    - lista vacía;
 *    - lista sin anidamiento.
 *
 * 8. La complejidad temporal es O(n), donde n es la cantidad total
 *    de elementos procesados, y el espacio adicional es O(n).
 *
 * 9. No se utilizaron características de Java posteriores a 17.
 *    El código es compatible exclusivamente con las características
 *    disponibles en Java 17.
 */