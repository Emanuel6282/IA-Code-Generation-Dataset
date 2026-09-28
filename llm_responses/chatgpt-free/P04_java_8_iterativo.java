// P04: Balanced Parentheses — Java 8
//
// Características utilizadas:
// 1. ArrayList:
//    Se utiliza como pila mutable para almacenar los símbolos de apertura.
//
// 2. HashMap:
//    Asocia cada símbolo de cierre con su símbolo de apertura correspondiente.
//
// 3. for:
//    Recorre la cadena de forma iterativa.
//
// 4. Inferencia con tipos genéricos (diamond operator <>):
//    Disponible en Java 7+ y utilizado de forma idiomática en Java 8
//    para evitar repetir los tipos en la instanciación.
//
// No se utilizan Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalancedParentheses {

    public static boolean isBalanced(String s) {
        ArrayList<Character> stack = new ArrayList<Character>();

        HashMap<Character, Character> pairs =
                new HashMap<Character, Character>();

        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.add(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                char opening = stack.remove(stack.size() - 1);

                if (opening != pairs.get(ch)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}