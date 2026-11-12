// P04: Balanced Parentheses — Java 17
// Migración desde Java 8
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. var (Java 10):
//    Permite inferir el tipo de las variables locales.
//    Reduce la repetición sin perder seguridad de tipos.
//
// 2. Switch expressions (Java 14):
//    Permiten identificar los símbolos de apertura
//    mediante una sintaxis más clara y concisa.
//
// 3. Case labels múltiples (Java 14):
//    Agrupan varios caracteres en una misma alternativa.
//
// 4. Compatibilidad con Java 17:
//    Se utilizan únicamente características estables,
//    sin necesidad de habilitar funciones preview.
//
// Se conserva Stack, Map y HashMap para mantener
// la estructura y el comportamiento del código original.

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class P04BalancedParentheses_v17 {

    public static boolean isBalanced(String s) {

        // Inferencia de tipos locales con var.
        var stack = new Stack<Character>();

        Map<Character, Character> pairs = new HashMap<>();

        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        // Se conserva el recorrido iterativo original.
        for (char c : s.toCharArray()) {

            // Switch expression compatible con Java 17.
            boolean isOpening = switch (c) {
                case '(', '[', '{' -> true;
                default -> false;
            };

            if (isOpening) {
                stack.push(c);

            } else if (pairs.containsKey(c)) {

                // Misma validación que en Java 8.
                if (stack.isEmpty() || stack.peek() != pairs.get(c)) {
                    return false;
                }

                stack.pop();
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
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 8 -> JAVA 17
 *
 * 1. Nombre de clase:
 *    P04BalancedParentheses_v8 -> P04BalancedParentheses_v17.
 *    Motivo: identificar la versión de Java utilizada.
 *
 * 2. Inferencia de tipos con var:
 *    Antes:
 *        Stack<Character> stack = new Stack<>();
 *
 *    Ahora:
 *        var stack = new Stack<Character>();
 *
 *    Motivo:
 *    Java 10 introdujo la inferencia de tipos locales.
 *    Es compatible con Java 17 y evita repetir el tipo.
 *
 * 3. Identificación de aperturas con switch expression:
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
 *    Las expresiones switch, estables desde Java 14,
 *    permiten agrupar alternativas de forma legible.
 *
 * 4. Se conserva Stack<Character>:
 *    Motivo:
 *    Mantener la estructura de datos y las operaciones
 *    push(), peek() y pop() del algoritmo original.
 *
 *    Aunque ArrayDeque suele preferirse para código
 *    nuevo, reemplazar Stack no es necesario para
 *    esta migración de comportamiento equivalente.
 *
 * 5. Se conserva Map<Character, Character>:
 *    Motivo:
 *    Mantener las mismas correspondencias entre
 *    símbolos de apertura y cierre.
 *
 * 6. Se conserva la condición pairs.containsKey(c):
 *    Motivo:
 *    Preservar exactamente el comportamiento original,
 *    que ignora los caracteres no reconocidos.
 *
 * 7. Se mantienen los tres assert originales:
 *    assert isBalanced("()") == true;
 *    assert isBalanced("([)]") == false;
 *    assert isBalanced("") == true;
 *
 * 8. Se conserva la salida:
 *    System.out.println("All tests passed");
 *
 * RESULTADO:
 *    Misma lógica y comportamiento que Java 8,
 *    utilizando sintaxis moderna compatible con Java 17.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(n).
 */