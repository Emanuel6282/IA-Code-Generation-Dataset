// P04: Balanced Parentheses — Java 17
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Stream API y lambdas:
//    Procesan los caracteres de forma declarativa.
//
// 2. Optional<Character>:
//    Representa de forma segura la última apertura.
//
// 3. reduce():
//    Acumula un estado inmutable.
//
// 4. record (Java 16, disponible en Java 17):
//    Representa el estado sin escribir constructores
//    ni métodos de acceso manualmente.
//
// 5. Switch expressions (Java 14):
//    Simplifican la comprobación de símbolos.
//
// 6. Pattern matching con instanceof (Java 16):
//    Disponible en Java 17, aunque no se necesita
//    para este algoritmo.
//
// Biblioteca estándar únicamente.
// Sin for, while, variables mutables ni recursión.

import java.util.Optional;

public class P04BalancedParentheses_v17 {

    // Estado inmutable: aperturas pendientes y validez.
    private record State(String stack, boolean valid) {

        // Devuelve un nuevo estado sin modificar el actual.
        State process(char current) {

            if (!valid) {
                return this;
            }

            // Apertura: crear una nueva pila inmutable.
            if (current == '(' ||
                current == '[' ||
                current == '{') {

                return new State(stack + current, true);
            }

            // Obtener la última apertura de forma segura.
            Optional<Character> lastOpen = stack.isEmpty()
                ? Optional.empty()
                : Optional.of(stack.charAt(stack.length() - 1));

            // Comprobar si el cierre coincide.
            boolean matches = lastOpen
                .map(open -> switch (current) {
                    case ')' -> open == '(';
                    case ']' -> open == '[';
                    case '}' -> open == '{';
                    default -> false;
                })
                .orElse(false);

            if (!matches) {
                return new State(stack, false);
            }

            // Cierre correcto: nueva pila sin el último símbolo.
            return new State(
                stack.substring(0, stack.length() - 1),
                true
            );
        }
    }

    public static boolean isBalanced(String s) {

        // Estado inicial inmutable.
        State initial = new State("", true);

        // Procesamiento secuencial mediante Stream API.
        State result = s.chars()
            .mapToObj(c -> (char) c)
            .reduce(
                initial,
                (state, current) -> state.process(current),
                (left, right) -> {
                    throw new UnsupportedOperationException(
                        "La reducción requiere orden secuencial"
                    );
                }
            );

        // Verificar validez y ausencia de aperturas pendientes.
        return result.valid() && result.stack().isEmpty();
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