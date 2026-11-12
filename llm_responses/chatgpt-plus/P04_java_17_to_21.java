// P04: Balanced Parentheses — Java 21
// Migración desde Java 17
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. SequencedCollection (Java 21, JEP 431):
//    Introduce operaciones uniformes para acceder
//    al primer y último elemento de una colección.
//
// 2. getLast() y removeLast() (Java 21):
//    Permiten consultar y eliminar la última apertura
//    almacenada en ArrayList.
//
// 3. var (Java 10, disponible en Java 21):
//    Permite inferir tipos de variables locales.
//
// 4. Switch expressions (Java 14, disponible en Java 21):
//    Simplifican la identificación de aperturas.
//
// 5. Map.of() (Java 9, disponible en Java 21):
//    Permite definir las correspondencias mediante
//    un mapa inmutable y conciso.
//
// Se conserva el paradigma iterativo.
// No se utilizan Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.Map;

public class P04BalancedParentheses_v21 {

    public static boolean isBalanced(String s) {

        // Pila mutable.
        var stack = new ArrayList<Character>();

        // Mapa inmutable de correspondencias.
        Map<Character, Character> pairs = Map.of(
            ')', '(',
            ']', '[',
            '}', '{'
        );

        // Recorrido iterativo original.
        for (char c : s.toCharArray()) {

            // Identificación de símbolos de apertura.
            boolean isOpening = switch (c) {
                case '(', '[', '{' -> true;
                default -> false;
            };

            if (isOpening) {

                stack.add(c);

            } else if (pairs.containsKey(c)) {

                // Verificar que exista una apertura coincidente.
                if (stack.isEmpty() ||
                    stack.getLast() != pairs.get(c)) {
                    return false;
                }

                // Java 21: eliminar la última apertura.
                stack.removeLast();
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {

        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 17 -> JAVA 21
 *
 * 1. Nombre de clase:
 *    Antes: P04BalancedParentheses_v8
 *    Ahora: P04BalancedParentheses_v21
 *
 *    Motivo:
 *    Identificar la versión de destino.
 *
 * 2. Stack<Character> -> ArrayList<Character>:
 *
 *    Antes:
 *        Stack<Character> stack = new Stack<>();
 *
 *    Ahora:
 *        var stack = new ArrayList<Character>();
 *
 *    Motivo:
 *    ArrayList implementa SequencedCollection en Java 21,
 *    ofreciendo métodos para acceder a ambos extremos.
 *    Además, evita la sincronización propia de Stack.
 *
 * 3. peek() -> getLast():
 *
 *    Antes:
 *        stack.peek()
 *
 *    Ahora:
 *        stack.getLast()
 *
 *    Motivo:
 *    getLast() es una operación disponible en ArrayList
 *    desde Java 21 y expresa claramente la intención.
 *
 * 4. pop() -> removeLast():
 *
 *    Antes:
 *        stack.pop()
 *
 *    Ahora:
 *        stack.removeLast()
 *
 *    Motivo:
 *    Utilizar la nueva API de colecciones secuenciadas
 *    introducida en Java 21.
 *
 * 5. HashMap mutable -> Map.of():
 *
 *    Antes:
 *        Map<Character, Character> pairs = new HashMap<>();
 *        pairs.put(')', '(');
 *        pairs.put(']', '[');
 *        pairs.put('}', '{');
 *
 *    Ahora:
 *        Map<Character, Character> pairs = Map.of(
 *            ')', '(',
 *            ']', '[',
 *            '}', '{'
 *        );
 *
 *    Motivo:
 *    Las correspondencias nunca cambian.
 *    Un mapa inmutable expresa mejor esa intención.
 *    Map.of() está disponible desde Java 9.
 *
 * 6. Condiciones de apertura -> switch expression:
 *
 *    Antes:
 *        c == '(' || c == '[' || c == '{'
 *
 *    Ahora:
 *        switch (c) {
 *            case '(', '[', '{' -> true;
 *            default -> false;
 *        }
 *
 *    Motivo:
 *    Utilizar una sintaxis moderna y legible.
 *    Esta característica está disponible desde Java 14.
 *
 * 7. Comportamiento conservado:
 *
 *    - Las aperturas se almacenan en orden.
 *    - Los cierres deben coincidir con la última apertura.
 *    - Los caracteres no reconocidos se ignoran,
 *      igual que en el código original.
 *    - Una cadena vacía devuelve true.
 *    - No se modifica la firma de isBalanced().
 *
 * 8. Pruebas conservadas:
 *
 *    assert isBalanced("()") == true;
 *    assert isBalanced("([)]") == false;
 *    assert isBalanced("") == true;
 *
 * 9. Salida conservada:
 *
 *    System.out.println("All tests passed");
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(n).
 *
 * RESULTADO:
 *    Misma lógica y resultados, con una implementación
 *    iterativa que aprovecha las APIs modernas de Java 21.
 */