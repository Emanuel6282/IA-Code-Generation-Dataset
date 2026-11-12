import java.util.ArrayDeque;
import java.util.Deque;

public class BalancedParentheses {

    public static boolean isBalanced(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {

            // Guardar símbolos de apertura.
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            }

            // Verificar símbolos de cierre.
            else {
                if (stack.isEmpty()) {
                    return false;
                }

                char lastOpen = stack.pop();

                if ((c == ')' && lastOpen != '(') ||
                    (c == ']' && lastOpen != '[') ||
                    (c == '}' && lastOpen != '{')) {
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