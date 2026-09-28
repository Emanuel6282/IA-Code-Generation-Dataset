// Java 17 - Paradigma Iterativo Estricto
// Utiliza Switch Expressions (Java 14/17 LTS), Map.of() inmutable (Java 9/17)
// y ArrayDeque / ArrayList de forma totalmente imperativa sin Streams ni lambdas.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Solution {

    /**
     * Mapa inmutable de pares de delimitadores (cierre -> apertura)
     * utilizando la sintaxis de fábrica `Map.of()` introducida en Java 9+.
     */
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    /**
     * Verifica si una cadena de caracteres está balanceada iterativamente.
     * Utiliza expresiones `switch` de Java 17 con flechas `->` para ramificación clara.
     */
    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        // Deque (ArrayDeque) utilizado como pila LIFO explícita
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            // Switch Expressions (Java 14/17) para control imperativo de caracteres
            switch (ch) {
                case '(', '[', '{' -> stack.push(ch);
                case ')', ']', '}' -> {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    char top = stack.pop();
                    if (top != PAIRS.get(ch)) {
                        return false;
                    }
                }
                default -> {
                    // Si se encuentra cualquier carácter no permitido
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con la opción `-ea` (ej. java -ea Solution)

        // Caso 1: Ejemplos base provistos en el enunciado
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y estructuras compuestas válidas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas o cierres huérfanos y nulos
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";
        assert isBalanced(null) == false : "Falló null";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}