// Características de Java 21 utilizadas en este refactor:
// 1. Pattern Matching para expresiones `switch` (JEP 441 - Java 21 LTS):
//    Sustituye la cadena de `if-else` con `instanceof` por una estructura `switch` declarativa y exhaustiva,
//    asignando los tipos desestructurados directamente a variables locales (`case List<?> list`, `case Integer val`).
// 2. Inferencia de tipos con `var` (JEP 286 - Java 10):
//    Evita la redundancia en declaraciones locales sin perder la seguridad de tipos estática.
// 3. Métodos de fábrica para colecciones inmutables (`List.of`) (JEP 269 - Java 9):
//    Remplaza `Arrays.asList` y `Collections.emptyList()` por la sintaxis estandarizada y eficiente `List.of()`.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03FlattenList_v21 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros usando Java 21.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        Deque<Object> stack = new ArrayDeque<>();
        stack.push(nested);

        while (!stack.isEmpty()) {
            Object item = stack.pop();

            // Pattern Matching para switch (característica estándar en Java 21 LTS)
            switch (item) {
                case List<?> list -> {
                    // Agregar en orden inverso para preservar el orden original
                    for (int i = list.size() - 1; i >= 0; i--) {
                        stack.push(list.get(i));
                    }
                }
                case Integer val -> result.add(val);
                case null -> { /* Omitir elementos nulos */ }
                default -> throw new IllegalArgumentException("Elemento no soportado: " + item);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // TC01: anidamiento basico
        List<Object> input1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(input1).equals(List.of(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(List.of()).equals(List.of()) : "TC02 fallo";

        // TC03: ya plana
        List<Object> input3 = List.of(1, 2, 3);
        assert flatten(input3).equals(List.of(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Reemplazo del bloque `if/else` por `switch` con Pattern Matching (Java 21 LTS):
//    Permite despachar acciones según el tipo del objeto directamente (`case List<?> list` y `case Integer val`),
//    lo que resulta en un código más conciso, expresivo e idiómático.
// 2. Manejo explícito de nulos y casos por defecto:
//    Pattern Matching en `switch` incluye soporte nativo para `case null`, lo que previene un `NullPointerException` imprevisto
//    y permite manejar estados no deseados con `default`.
// 3. Modernización de las aserciones de prueba con `List.of()`:
//    Sustituye la sintaxis antigua de `Arrays.asList()` por las fábricas de colecciones inmutables introducidas en Java moderno,
//    haciendo las pruebas más seguras y fáciles de leer.