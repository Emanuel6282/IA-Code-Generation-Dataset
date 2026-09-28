// Java 8 - Paradigma Funcional Estricto
// Utiliza Stream API (chars()), reducciones (`reduce`), expresiones lambda, `Optional`
// y estructuras inmutables para el manejo del estado acumulado sin bucles imperativos (`for`/`while`).

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Solution {

    /**
     * Clase inmutable que representa el acumulador del estado en cada paso del Stream.
     * Mantiene un indicador de validez y la secuencia de caracteres acumulados en la pila.
     */
    private static class State {
        private final boolean isValid;
        private final String stack;

        public State(boolean isValid, String stack) {
            this.isValid = isValid;
            this.stack = stack;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getStack() {
            return stack;
        }

        public static State initial() {
            return new State(true, "");
        }

        public static State invalid() {
            return new State(false, "");
        }
    }

    /**
     * Mapa estático e inmutable de parejas de delimitadores de cierre a apertura.
     */
    private static final Map<Character, Character> PAIRS;
    static {
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');
        PAIRS = Collections.unmodifiableMap(map);
    }

    /**
     * Función pura que procesa cada carácter sobre el estado acumulado usando Optional.
     */
    private static State step(State state, char ch) {
        if (!state.isValid()) {
            return State.invalid();
        }

        return Optional.ofNullable(PAIRS.get(ch))
            .map(expectedOpening -> {
                // Es un carácter de cierre: verificar si coincide con el tope de la pila
                String currentStack = state.getStack();
                if (!currentStack.isEmpty() && currentStack.charAt(currentStack.length() - 1) == expectedOpening) {
                    return new State(true, currentStack.substring(0, currentStack.length() - 1));
                } else {
                    return State.invalid();
                }
            })
            .orElseGet(() -> {
                // Es un carácter de apertura: apilarlo
                return new State(true, state.getStack() + ch);
            });
    }

    /**
     * Verifica si una cadena de delimitadores está balanceada mediante un Stream
     * y una reducción funcional acumulativa.
     */
    public static boolean isBalanced(String s) {
        return Optional.ofNullable(s)
            .map(str -> str.chars()
                .mapToObj(c -> (char) c)
                .reduce(State.initial(), Solution::step, (s1, s2) -> s1)
            )
            .filter(State::isValid)
            .map(State::getStack)
            .map(String::isEmpty)
            .orElse(false);
    }

    public static void main(String[] args) {
        // NOTA: Para habilitar aserciones en Java, ejecutar con el flag -ea:
        // java -ea Solution

        // Caso 1: Ejemplos base del enunciado
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y secuencias válidas compuestas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas/cierres huérfanos o punteros nulos
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";
        assert isBalanced(null) == false : "Falló null";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}