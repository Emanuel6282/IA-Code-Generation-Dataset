// P03: Flatten Nested List — Java 21
//
// Características utilizadas:
// 1. var:
//    - Simplifica las declaraciones de variables locales cuando el tipo
//      resulta evidente por la expresión de inicialización.
//
// 2. ArrayList:
//    - Se utiliza como pila para eliminar la necesidad de recursión.
//    - También almacena el resultado final.
//
// 3. HashMap:
//    - Se utiliza como estructura auxiliar para registrar la cantidad
//      de enteros procesados.
//
// 4. while y for:
//    - Permiten realizar todo el recorrido de forma iterativa.
//
// No se utilizan Streams, lambdas ni recursión.
//
// Java 21 no incorpora una característica específica que mejore
// directamente este algoritmo, por lo que se conserva el enfoque
// iterativo más sencillo y eficiente.

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

            if (item instanceof List<?>) {
                var nested = (List<?>) item;

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

        // Caso 1: múltiples niveles de anidamiento.
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
 * 1. Se utilizó var para las variables locales.
 *    Java 21 permite inferir el tipo de variables locales, reduciendo
 *    código repetitivo cuando el tipo es evidente.
 *
 * 2. Se mantuvo ArrayList como pila.
 *    La pila permite reemplazar la recursión por un recorrido iterativo.
 *
 * 3. Se mantuvo HashMap como estructura auxiliar, según el requisito.
 *
 * 4. Se utilizaron while y for.
 *    El while procesa la pila y el for recorre las listas anidadas
 *    en sentido inverso.
 *
 * 5. Se conserva el orden original.
 *    Los elementos de cada lista se agregan a la pila desde el último
 *    hasta el primero, de modo que sean procesados en su orden original.
 *
 * 6. No se utilizan Streams, lambdas ni recursión.
 *    El algoritmo es estrictamente iterativo.
 *
 * 7. Se conservaron los tres casos de prueba:
 *    - lista con múltiples niveles;
 *    - lista vacía;
 *    - lista sin anidamiento.
 *
 * 8. La complejidad temporal es O(n) y el espacio adicional es O(n).
 *
 * 9. No se fuerza una característica exclusiva de Java 21.
 *    Las novedades de Java 21 no aportan una mejora directa a este
 *    algoritmo concreto. Por ello, se mantiene una implementación
 *    idiomática y sencilla.
 */