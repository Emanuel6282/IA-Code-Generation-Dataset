// P03: Flatten Nested List — migrado a Java 21
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Pattern matching en switch (JEP 441, estándar en Java 21): clasifica cada elemento
//    por tipo en un único switch y declara la variable ya convertida en cada rama.
//    Sustituye a la cadena if/else con instanceof y casts, y la rama `default` deja
//    explícito algo que en el original quedaba implícito: los elementos que no son List
//    ni Integer se ignoran.
// 2. List.reversed() (sequenced collections, JEP 431, nuevo en Java 21): devuelve una
//    vista de la lista en orden inverso, sin copiarla. Sustituye al bucle con índices
//    `list.get(i)`, que en una LinkedList cuesta O(n) por llamada (O(n²) en total);
//    la vista se recorre en O(1) por elemento con cualquier tipo de lista.
// 3. var (inferencia de tipos locales): evita repetir tipos genéricos evidentes.
// 4. List.of (fábricas de colecciones inmutables): sustituye a Arrays.asList y
//    Collections.emptyList en los tests.
//
// Nota: no se usan los patrones de record (no hay records que desestructurar), los
// hilos virtuales (no hay E/S bloqueante) ni las string templates (solo vista previa
// en Java 21, retiradas después).

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class P03FlattenList_v21 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Object>();
        stack.push(nested);

        while (!stack.isEmpty()) {
            switch (stack.pop()) {
                // Agregar en orden inverso para preservar el orden original
                case List<?> list -> list.reversed().forEach(stack::push);
                case Integer number -> result.add(number);
                default -> { }   // cualquier otro tipo se ignora, igual que en el original
            }
        }
        return result;
    }

    // Ejecutar con assertions habilitadas: java -ea P03FlattenList_v21.java
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

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. if (item instanceof List) {...cast...} else if (item instanceof Integer) {...cast...}
//    -> switch (stack.pop()) con `case List<?> list`, `case Integer number` y `default`:
//    el pattern matching en switch es la forma idiomática en Java 21 de despachar por
//    tipo. Se eliminan la variable temporal `item` y los dos casts.
// 2. `for (int i = list.size() - 1; i >= 0; i--) stack.push(list.get(i))` ->
//    `list.reversed().forEach(stack::push)`: apila los mismos elementos en el mismo
//    orden, sin aritmética de índices y sin el coste cuadrático de get(i) en LinkedList.
// 3. Comportamiento idéntico al original, incluidos sus casos límite (verificado con
//    miles de entradas aleatorias): un Integer suelto devuelve [n], los elementos que no
//    son Integer ni List se ignoran, y un elemento null sigue lanzando
//    NullPointerException al apilarlo en el ArrayDeque (por eso el switch nunca recibe
//    null y no necesita `case null`).
// 4. `List<Integer> result = new ArrayList<>()` y `Deque<Object> stack = new ArrayDeque<>()`
//    -> `var`. La firma pública no cambia: sigue recibiendo Object y devolviendo
//    List<Integer>.
// 5. `import java.util.*;` -> imports explícitos.
// 6. Tests: Arrays.asList / Collections.emptyList -> List.of. Se conservan los tres
//    casos, sus mensajes ("TC01 fallo"...) y el texto "All tests passed".
// 7. Clase renombrada a P03FlattenList_v21 y cabecera actualizada (decía "Java 8"). El
//    nombre de la función no cambia.