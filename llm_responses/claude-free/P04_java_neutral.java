import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class BalancedParentheses {

    /**
     * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
     * "{", "}", retorna true si todos los parentesis/corchetes estan
     * correctamente balanceados y cerrados en orden. Retorna false en caso
     * contrario.
     *
     * Implementacion mediante una pila: cada caracter de apertura se
     * apila; cada caracter de cierre debe coincidir con el ultimo caracter
     * de apertura apilado, o la cadena no esta balanceada.
     *
     * @param s Cadena que contiene unicamente los caracteres de parentesis
     *          y corchetes indicados.
     * @return true si la cadena esta correctamente balanceada, false en
     *         caso contrario. Una cadena vacia se considera balanceada
     *         (true).
     */
    public static boolean isBalanced(String s) {
        Map<Character, Character> paresCierre = new HashMap<>();
        paresCierre.put(')', '(');
        paresCierre.put(']', '[');
        paresCierre.put('}', '{');

        Deque<Character> pila = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char caracter = s.charAt(i);

            if (caracter == '(' || caracter == '[' || caracter == '{') {
                pila.push(caracter);
            } else if (paresCierre.containsKey(caracter)) {
                if (pila.isEmpty() || !pila.peek().equals(paresCierre.get(caracter))) {
                    return false;
                }
                pila.pop();
            }
        }

        return pila.isEmpty();
    }

    public static void main(String[] args) {
        // Caso 1: balanceado simple
        assert isBalanced("()") == true : "Caso 1 fallido";

        // Caso 2: orden incorrecto de cierre -> no balanceado
        assert isBalanced("([)]") == false : "Caso 2 fallido";

        // Caso 3: anidamiento balanceado de varios tipos
        assert isBalanced("{[]}") == true : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}