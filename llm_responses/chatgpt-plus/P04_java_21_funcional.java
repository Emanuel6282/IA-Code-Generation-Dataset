// P04: Balanced Parentheses — Java 21
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Stream API y lambdas (Java 8):
//    Procesan los caracteres de forma declarativa.
//
// 2. Optional (Java 8):
//    Representa la presencia o ausencia de una apertura.
//
// 3. reduce() (Java 8):
//    Acumula estados sin modificar los anteriores.
//
// 4. record (Java 16):
//    Define un estado inmutable de forma concisa.
//
// 5. Switch expressions (Java 14):
//    Permiten verificar los símbolos de cierre.
//
// 6. Java 21:
//    Todas las características anteriores son estables
//    y compatibles con Java 21, sin funciones preview.
//
// Sin for, while, colecciones mutables ni recursión.
// Biblioteca estándar únicamente.

import java.util.Optional;

public class P04BalancedParentheses_v21 {

    // Estado inmutable del procesamiento.
    private record State(String stack, boolean valid) {

        // Procesa un carácter y devuelve un nuevo estado.
        State process(char current) {

            // Si ya es inválido, conservar el estado.
            if (!valid) {
                return this;
            }

            // Símbolos de apertura.
            if (current == '(' ||
                current == '[' ||
                current == '{') {

                return new State(stack + current, true);
            }

            // Obtener la última apertura usando Optional.
            Optional<Character> lastOpen =
                stack.isEmpty()
                    ? Optional.empty()
                    : Optional.of(
                        stack.charAt(stack.length() - 1)
                    );

            // Verificar el símbolo de cierre.
            boolean matches = lastOpen
                .map(open -> switch (current) {
                    case ')' -> open == '(';
                    case ']' -> open == '[';
                    case '}' -> open == '{';
                    default -> false;
                })
                .orElse(false);

            // Cierre incorrecto.
            if (!matches) {
                return new State(stack, false);
            }

            // Cierre correcto: crear una nueva pila.
            return new State(
                stack.substring(0, stack.length() - 1),
                true
            );
        }
    }

    public static boolean isBalanced(String s) {

        // Estado inicial inmutable.
        State initial = new State("", true);

        // Reducción funcional y secuencial.
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

        // Verificar validez y pila vacía.
        return result.valid() && result.stack().isEmpty();
    }

    public static void main(String[] args) {

        // Test 1: Paréntesis balanceados.
        assert isBalanced("()") == true;

        // Test 2: Cierres en orden incorrecto.
        assert isBalanced("([)]") == false;

        // Test 3: Corchetes correctamente anidados.
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}