import java.util.ArrayDeque;
import java.util.Deque;

public class BalancedParentheses {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     */
    public static boolean isBalanced(String s) {
        // Usamos ArrayDeque como pila (Stack) por su alto rendimiento
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // Si es un caracter de apertura, lo apilamos
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } 
            // Si es un caracter de cierre, verificamos el balance
            else if (c == ')' || c == ']' || c == '}') {
                // Si la pila está vacía, hay un cierre sin apertura previa
                if (stack.isEmpty()) {
                    return false;
                }
                
                char top = stack.pop();
                
                // Verificamos que el cierre corresponda al último caracter de apertura
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return false;
                }
            }
        }
        
        // Si la pila queda vacía, todo se cerró correctamente
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea BalancedParentheses)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: un solo par balanceado
        assert isBalanced("()") == true : "Error: '()' debería retornar true";

        // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
        assert isBalanced("([)]") == false : "Error: '([)]' debería retornar false";

        // 3. Caso del ejemplo: anidamiento profundo balanceado
        assert isBalanced("{[]}") == true : "Error: '{[]}' debería retornar true";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}