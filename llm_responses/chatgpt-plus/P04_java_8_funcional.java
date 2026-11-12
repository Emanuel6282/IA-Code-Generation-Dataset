// P04: Balanced Parentheses — Java 8
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS DE JAVA 8 UTILIZADAS:
//
// 1. Stream API:
//    Procesa los caracteres mediante operaciones funcionales.
//
// 2. Expresiones lambda:
//    Definen las transformaciones del flujo.
//
// 3. Optional:
//    Representa de forma segura la existencia de una apertura.
//
// 4. reduce():
//    Acumula un estado inmutable durante el procesamiento.
//
// 5. Referencias a métodos:
//    Mejoran la legibilidad de las transformaciones.
//
// No utiliza for, while, variables mutables ni recursión.
// Biblioteca estándar únicamente.

import java.util.Optional;
import java.util.stream.IntStream;

public class P04BalancedParentheses_v8 {

    // Estado inmutable del procesamiento.
    private static final class State {

        private final String stack;
        private final boolean valid;

        private State(String stack, boolean valid) {
            this.stack = stack;
            this.valid = valid;
        }

        // Procesar un carácter y devolver un nuevo estado.
        private State process(char current) {

            // Conservar un estado inválido.
            if (!valid) {
                return this;
            }

            // Símbolo de apertura.
            if (current == '(' ||
                current == '[' ||
                current == '{') {

                return new State(stack + current, true);
            }

            // Obtener la última apertura mediante Optional.
            Optional<Character> lastOpen =
                stack.isEmpty()
                    ? Optional.empty()
                    : Optional.of(stack.charAt(stack.length() - 1));

            // Comprobar la correspondencia entre símbolos.
            boolean matches = lastOpen
                .map(open ->
                    (current == ')' && open == '(') ||
                    (current == ']' && open == '[') ||
                    (current == '}' && open == '{')
                )
                .orElse(false);

            // Si el cierre es incorrecto, invalidar el estado.
            if (!matches) {
                return new State(stack, false);
            }

            // Cierre correcto: crear una nueva pila inmutable.
            return new State(
                stack.substring(0, stack.length() - 1),
                true
            );
        }
    }

    public static boolean isBalanced(String s) {

        // Estado inicial: pila vacía y validez verdadera.
        State initial = new State("", true);

        // Stream de caracteres y reducción funcional.
        State result = s.chars()
            .mapToObj(c -> (char) c)
            .reduce(
                initial,
                (state, current) -> state.process(current),
                (left, right) -> {
                    throw new UnsupportedOperationException(
                        "Este reduce requiere procesamiento secuencial"
                    );
                }
            );

        // La cadena es válida si no quedan aperturas.
        return result.valid && result.stack.isEmpty();
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