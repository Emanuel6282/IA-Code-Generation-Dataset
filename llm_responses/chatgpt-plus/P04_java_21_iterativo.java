// P04: Balanced Parentheses — Java 21
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. SequencedCollection (Java 21):
//    ArrayList dispone de removeLast(), que permite
//    extraer directamente el último elemento.
//
// 2. Inferencia de tipos con var (Java 10):
//    Reduce la repetición de tipos en variables locales.
//
// 3. Switch expressions (Java 14):
//    Permiten identificar símbolos de apertura.
//
// 4. ArrayList<Character>:
//    Implementa una pila mutable.
//
// 5. HashMap<Character, Character>:
//    Relaciona símbolos de cierre con sus aperturas.
//
// 6. Bucle for:
//    Recorre la cadena de forma estrictamente iterativa.
//
// Sin Streams, lambdas ni recursión.
// Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.HashMap;

public class P04BalancedParentheses_v21 {

    public static boolean isBalanced(String s) {

        // Pila mutable de símbolos de apertura.
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

                // Guardar símbolo de apertura.
                stack.add(current);

            } else {

                // Verificar que exista una apertura previa.
                if (stack.isEmpty()) {
                    return false;
                }

                // Java 21: extraer el último elemento.
                char lastOpen = stack.removeLast();

                // Comprobar la correspondencia.
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