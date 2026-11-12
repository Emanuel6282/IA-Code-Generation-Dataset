// P04: Balanced Parentheses — Java 8
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. ArrayList<Character>:
//    Implementa una pila mutable para almacenar aperturas.
//
// 2. HashMap<Character, Character>:
//    Relaciona cada símbolo de cierre con su apertura.
//
// 3. Bucle for:
//    Recorre la cadena de forma iterativa.
//
// 4. Inferencia de tipos con <>:
//    Reduce la repetición en las declaraciones genéricas.
//
// 5. Autoboxing y unboxing:
//    Permiten utilizar char con colecciones de Character.
//
// COMPATIBILIDAD:
// Todas las características utilizadas están disponibles
// en Java 8. Algunas fueron introducidas anteriormente.
//
// Sin Streams, lambdas ni recursión.
// Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.HashMap;

public class P04BalancedParentheses_v8 {

    public static boolean isBalanced(String s) {

        // Pila mutable de símbolos de apertura.
        ArrayList<Character> stack = new ArrayList<>();

        // Correspondencia entre cierres y aperturas.
        HashMap<Character, Character> pairs = new HashMap<>();

        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        // Recorrido estrictamente iterativo.
        for (int i = 0; i < s.length(); i++) {

            char current = s.charAt(i);

            // Guardar símbolo de apertura.
            if (current == '(' ||
                current == '[' ||
                current == '{') {

                stack.add(current);

            } else {

                // Verificar que exista una apertura previa.
                if (stack.isEmpty()) {
                    return false;
                }

                // Extraer el último símbolo de apertura.
                char lastOpen = stack.remove(stack.size() - 1);

                // Verificar que coincida con el cierre.
                if (lastOpen != pairs.get(current)) {
                    return false;
                }
            }
        }

        // La pila debe quedar vacía.
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