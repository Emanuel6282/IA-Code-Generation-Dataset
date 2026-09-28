import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BalancedParentheses {

    /*
     * Java 21 — Paradigma funcional
     *
     * Características utilizadas:
     * - record: representa el estado inmutable del procesamiento.
     * - Stream API: procesa los caracteres sin bucles imperativos.
     * - lambda expressions: definen la transformación realizada por reduce.
     * - Optional: representa de forma segura el elemento superior de la pila.
     * - List.of / List.copyOf: mantienen el estado inmutable.
     *
     * No se utilizan for, while, var mutable ni colecciones mutables.
     */

    private record State(List<Character> stack, boolean valid) {
    }

    public static boolean isBalanced(String s) {

        Map<Character, Character> pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        State finalState = s.chars()
                .mapToObj(c -> (char) c)
                .reduce(
                        new State(List.of(), true),

                        (state, ch) -> {
                            if (!state.valid()) {
                                return state;
                            }

                            // Paréntesis de apertura: se agrega
                            // de forma inmutable a la pila.
                            if (ch == '(' || ch == '[' || ch == '{') {
                                List<Character> newStack =
                                        java.util.stream.Stream.concat(
                                                state.stack().stream(),
                                                java.util.stream.Stream.of(ch)
                                        ).toList();

                                return new State(newStack, true);
                            }

                            // Obtiene el elemento superior de la pila
                            // utilizando Optional.
                            Optional<Character> top =
                                    state.stack().isEmpty()
                                            ? Optional.empty()
                                            : Optional.of(
                                                    state.stack().get(
                                                            state.stack().size() - 1
                                                    )
                                            );

                            boolean matches = top
                                    .map(opening -> opening.equals(pairs.get(ch)))
                                    .orElse(false);

                            if (!matches) {
                                return new State(state.stack(), false);
                            }

                            // Elimina el último elemento creando
                            // una nueva lista inmutable.
                            List<Character> newStack =
                                    state.stack().stream()
                                            .limit(state.stack().size() - 1)
                                            .toList();

                            return new State(newStack, true);
                        },

                        // La operación es secuencial, pero se proporciona
                        // el combinador requerido por reduce.
                        (left, right) -> right
                );

        return finalState.valid() && finalState.stack().isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}