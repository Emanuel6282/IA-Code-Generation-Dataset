import java.util.ArrayDeque;
import java.util.Deque;

public class Solution {

    /**
     * Verifica si una cadena de símbolos '()[]{}' está correctamente balanceada.
     * Utiliza Deque (ArrayDeque) como pila por rendimiento y buenas prácticas en Java.
     */
    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : s.toCharArray()) {
            switch (ch) {
                case '(', '[', '{' -> stack.push(ch);
                case ')' -> {
                    if (stack.isEmpty() || stack.pop() != '(') return false;
                }
                case ']' -> {
                    if (stack.isEmpty() || stack.pop() != '[') return false;
                }
                case '}' -> {
                    if (stack.isEmpty() || stack.pop() != '{') return false;
                }
                default -> {
                    // Carácter no permitido o no esperado
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con la opción `-ea` (e.g., java -ea Solution)

        // Caso 1: Ejemplos base del enunciado
        assert isBalanced("()") : "Falló ()";
        assert !isBalanced("([)]") : "Falló ([)]";
        assert isBalanced("{[]}") : "Falló {[]}";

        // Caso 2: Cadena vacía y secuencias válidas intercaladas
        assert isBalanced("") : "Falló cadena vacía";
        assert isBalanced("()[]{}") : "Falló ()[]{}";

        // Caso 3: Desbalance por cierres o aperturas sobrantes
        assert !isBalanced("((()") : "Falló ((()";
        assert !isBalanced("]") : "Falló ]";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}