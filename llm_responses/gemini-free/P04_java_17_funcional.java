// Java 17 - Paradigma Funcional Estricto
// Aprovecha las características de Java 14/17 LTS como Records (JEP 395), Map.of(),
// Switch Expressions y el procesamiento de streams con `Stream.reduce` y `Optional`.

import java.util.Map;
import java.util.Optional;

public class Solution {

    /**
     * Record inmutable introducido en Java 14/16/17 (JEP 395) que representa el estado
     * del acumulador durante la reducción del stream. Mantiene el indicador de validez
     * y la cadena que actúa como pila inmutable de delimitadores de apertura.
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
     * Mapa inmutable creado con la sintaxis de fábrica `Map.of()` (Java 9+).
     */
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    /**
     * Función pura de transformación de estado. Evalúa cada carácter de la cadena
     * sobre el estado acumulado utilizando `Optional` y expresiones `switch`.
     */
    private static State step(State state, char ch) {
        if (!state.isValid()) {
            return State.invalid();
        }

        return Optional.ofNullable(PAIRS.get(ch))
            .map(expectedOpening -> {
                // Es un carácter de cierre: verificar si coincide con el tope de la pila
                String currentStack = state.stack();
                if (!currentStack.isEmpty() && currentStack.charAt(currentStack.length() - 1) == expectedOpening) {
                    return new State(true, currentStack.substring(0, currentStack.length() - 1));
                } else {
                    return State.invalid();
                }
            })
            .orElseGet(() -> switch (ch) {
                // Es un carácter de apertura: apilarlo de forma inmutable
                case '(', '[', '{' -> new State(true, state.stack() + ch);
                // Carácter no válido / no reconocido
                default -> State.invalid();
            });
    }

    /**
     * Verifica si una cadena de delimitadores está balanceada utilizando
     * la API de Streams de Java y una reducción funcional acumulativa.
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

        // Caso 1: Ejemplos base del enunciado
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y secuencias compuestas válidas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas/cierres huérfanos o referencia nula
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";
        assert isBalanced(null) == false : "Falló null";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}