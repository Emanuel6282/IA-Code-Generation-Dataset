// P03: Flatten Nested List — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Pattern matching para instanceof (JEP 394, estándar desde Java 16): comprueba el
//    tipo y declara la variable ya convertida en una sola expresión. Elimina los casts
//    explícitos `(List<?>) item` e `(Integer) item`, que repetían el tipo ya comprobado.
// 2. var (inferencia de tipos locales, Java 10): evita repetir tipos genéricos cuando
//    ya son evidentes por el lado derecho de la asignación.
// 3. List.of (fábricas de colecciones inmutables, Java 9): sustituye a Arrays.asList y
//    Collections.emptyList en los tests, con listas más concisas e inmutables.
// 4. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se puede
//    correr con `java -ea P03FlattenList_v17.java`, sin compilar antes.
//
// Nota: las demás novedades de Java 17 y sus versiones previas (records, clases
// sealed, switch como expresión, text blocks) no mejoran esta función. El pattern
// matching en switch, que sí encajaría aquí, solo es estándar desde Java 21.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P03FlattenList_v17 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Object>();
        stack.push(nested);

        while (!stack.isEmpty()) {
            var item = stack.pop();
            if (item instanceof List<?> list) {
                // Agregar en orden inverso para preservar el orden original.
                // ListIterator desde el final: O(1) por elemento también en LinkedList.
                for (var it = list.listIterator(list.size()); it.hasPrevious(); ) {
                    stack.push(it.previous());
                }
            } else if (item instanceof Integer number) {
                result.add(number);
            }
        }
        return result;
    }

    // Ejecutar con assertions habilitadas: java -ea P03FlattenList_v17.java
    public static void main(String[] args) {
        // TC01: anidamiento basico
        var input1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(input1).equals(List.of(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(new ArrayList<>()).equals(List.of()) : "TC02 fallo";

        // TC03: ya plana
        var input3 = List.<Object>of(1, 2, 3);
        assert flatten(input3).equals(List.of(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
// 1. `if (item instanceof List) { List<?> list = (List<?>) item; ...}` ->
//    `if (item instanceof List<?> list)`, y lo mismo con Integer: el pattern matching
//    sustituye al patrón "comprobar, declarar y convertir", que es justo para lo que
//    se añadió al lenguaje.
// 2. Bucle `for (int i = list.size() - 1; i >= 0; i--) stack.push(list.get(i))` ->
//    recorrido con `list.listIterator(list.size())` hacia atrás: mismos elementos en el
//    mismo orden, pero sin depender de get(i), que en una LinkedList cuesta O(n) por
//    llamada (O(n²) en total). Con ArrayList el coste es el mismo que antes.
// 3. `List<Integer> result = new ArrayList<>()` y `Deque<Object> stack = new ArrayDeque<>()`
//    -> `var`: estilo habitual para variables locales con tipo obvio. La firma pública
//    no cambia: sigue recibiendo Object y devolviendo List<Integer>.
// 4. Comportamiento idéntico al original, incluidos sus casos límite (verificado con
//    miles de entradas aleatorias): un Integer suelto devuelve [n], los elementos que no
//    son Integer ni List se ignoran, y un elemento null sigue lanzando
//    NullPointerException al apilarlo en el ArrayDeque.
// 5. `import java.util.*;` -> imports explícitos: dejan claro qué clases se usan.
// 6. Tests: Arrays.asList / Collections.emptyList -> List.of. Se conservan los tres
//    casos, sus mensajes ("TC01 fallo"...) y el texto "All tests passed".
// 7. Clase renombrada a P03FlattenList_v17 para reflejar la versión (en Java, el archivo
//    debe llamarse igual que la clase pública). El nombre de la función no cambia.