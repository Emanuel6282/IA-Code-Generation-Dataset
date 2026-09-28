// Java 21 - Paradigma Funcional Estricto
// Aprovecha características de Java 21 LTS como Record Patterns (JEP 440), 
// Pattern Matching para switch (JEP 441), Map.of() e inmutabilidad con Records y Stream API.

import java.util.Map;
import java.util.Optional;

public class Solution {

    /**
     * Record inmutable que representa el estado del acumulador durante el procesamiento funcional del Stream.
     */
    private record State(boolean isValid, String stack) {
        public static State initial() {
            return new State(true, "");
        }

        public static State invalid() {
            return new State(false, "");
        }
    }

    /**
     * Mapa inmutable de delimitadores de cierre a aperturas asociadas.
     */
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    /**
     * Función pura de transformación de estado.
     * Utiliza Pattern Matching para switch y Record Patterns (Java 21) en un flujo funcional inmutable.
     */
    private static State step(State state, char ch) {
        // Desestructuración del Record usando Record Patterns de Java 21
        if (state instanceof State(boolean isValid, String stack)) {
            if (!isValid) {
                return State.invalid();
            }

            return Optional.ofNullable(PAIRS.get(ch))
                .map(expectedOpening -> {
                    // Cierre: validar que coincida con el tope de la pila inmutable
                    if (!stack.isEmpty() && stack.charAt(stack.length() - 1) == expectedOpening) {
                        return new State(true, stack.substring(0, stack.length() - 1));
                    } else {
                        return State.invalid();
                    }
                })
                .orElseGet(() -> switch (ch) {
                    // Pattern Matching para switch (Java 21) en apertura
                    case '(', '[', '{' -> new State(true, stack + ch);
                    default -> State.invalid();
                });
        }

        return State.invalid();
    }

    /**
     * Verifica si una cadena de paréntesis está balanceada utilizando
     * la API de Streams y una reducción funcional con acumulación inmutable.
     */
    public static boolean isBalanced(String s) {
        return Optional.ofNullable(s)
            .map(str -> str.chars()
                .mapToObj(c -> (char) c)
                .reduce(State.initial(), Solution::step, (s1, s2) -> s1)
            )
            .filter(State::isValid)
            .map(State::stack)
            .map(String::isEmpty)
            .orElse(false);
    }

    public static void main(String[] args) {
        // Para ejecutar las aserciones en Java, utilizar el flag -ea:
        // java -ea Solution

        // Caso 1: Ejemplos base del problema
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y estructuras compuestas válidas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas/cierres huérfanos o puntero nulo
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";
        assert isBalanced(null) == false : "Falló null";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}