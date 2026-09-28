// Java 21 - Paradigma Iterativo Estricto
// Utiliza Sequenced Collections (JEP 431 - Java 21) mediante Deque/ArrayDeque (`addFirst`/`removeFirst`),
// Switch Expressions con Pattern Matching (JEP 441 - Java 21) y Map.of() inmutable.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Solution {

    /**
     * Mapa inmutable de delimitadores de cierre a sus correspondientes de apertura.
     */
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    /**
     * Verifica si una cadena de símbolos está balanceada de forma totalmente iterativa.
     * Aprovecha Sequenced Collections (`addFirst`/`removeFirst`) y Switch Pattern Matching de Java 21.
     */
    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        // Deque implementa SequencedCollection<Character> en Java 21
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            // Pattern Matching en Switch Expressions (Java 21 LTS)
            switch (ch) {
                case '(', '[', '{' -> stack.addFirst(ch); // SequencedCollection: apilar en la primera posición
                case ')', ']', '}' -> {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    // SequencedCollection: desapilar del inicio de forma explícita
                    char top = stack.removeFirst();
                    if (top != PAIRS.get(ch)) {
                        return false;
                    }
                }
                default -> {
                    // Carácter no válido / no esperado
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Ejecutar las aserciones con la opción `-ea` (ejemplo: java -ea Solution)

        // Caso 1: Ejemplos base del enunciado
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y secuencias válidas intercaladas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas/cierres sobrantes o referencia nula
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";
        assert isBalanced(null) == false : "Falló null";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}