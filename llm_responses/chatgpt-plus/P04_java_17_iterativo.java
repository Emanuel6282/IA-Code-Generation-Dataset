// P04: Balanced Parentheses — Java 17
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. ArrayList<Character>:
//    Implementa una pila mutable de símbolos de apertura.
//
// 2. HashMap<Character, Character>:
//    Relaciona cada cierre con su apertura.
//
// 3. Bucle for:
//    Recorre los caracteres de forma iterativa.
//
// 4. Inferencia de tipos con var:
//    Disponible desde Java 10 y compatible con Java 17.
//
// 5. Expresiones switch modernas:
//    Disponibles desde Java 14 y compatibles con Java 17.
//    Permiten identificar las aperturas de forma legible.
//
// Sin Streams, lambdas ni recursión.
// Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.HashMap;

public class P04BalancedParentheses_v17 {

    public static boolean isBalanced(String s) {

        // Pila mutable para almacenar aperturas.
        var stack = new ArrayList<Character>();

        // Correspondencia entre cierres y aperturas.
        var pairs = new HashMap<Character, Character>();

        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        // Recorrido estrictamente iterativo.
        for (int i = 0; i < s.length(); i++) {

            char current = s.charAt(i);

            // Switch moderno para identificar aperturas.
            boolean isOpening = switch (current) {
                case '(', '[', '{' -> true;
                default -> false;
            };

            if (isOpening) {
                stack.add(current);

            } else {

                // Comprobar que exista una apertura previa.
                if (stack.isEmpty()) {
                    return false;
                }

                // Extraer la última apertura.
                char lastOpen = stack.remove(stack.size() - 1);

                // Verificar la correspondencia.
                if (lastOpen != pairs.get(current)) {
                    return false;
                }
            }
        }

        // Todos los símbolos deben estar cerrados.
        return stack.isEmpty();
    }

    public static void main(String[] args) {

        // Test 1: Paréntesis correctamente balanceados.
        assert isBalanced("()") == true;

        // Test 2: Símbolos cerrados en orden incorrecto.
        assert isBalanced("([)]") == false;

        // Test 3: Corchetes anidados correctamente.
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}