// P04: Balanced Parentheses — Java 8
//
// Características utilizadas:
// 1. Stream API:
//    Permite procesar los caracteres de la cadena de forma declarativa.
//
// 2. Lambdas:
//    Se utilizan para definir las transformaciones y el procesamiento
//    del estado sin utilizar for/while.
//
// 3. Optional:
//    Representa de forma segura el acceso al elemento superior de la pila.
//
// 4. reduce():
//    Procesa todos los caracteres manteniendo un estado inmutable.
//
// 5. Java 8:
//    Stream API, lambdas y Optional son características introducidas
//    en Java 8.
//
// No se utilizan bucles for/while ni estado mutable.

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BalancedParentheses {

    private static class State {
        private final List<Character> stack;
        private final boolean valid;

        State(List<Character> stack, boolean valid) {
            this.stack = stack;
            this.valid = valid;
        }
    }

    public static boolean isBalanced(String s) {
        Map<Character, Character> pairs = StreamSupport
                .pairs()
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> pair[1]
                ));

        State finalState = s.chars()
                .mapToObj(value -> (char) value)
                .reduce(
                        new State(Collections.emptyList(), true),
                        (state, ch) -> {
                            if (!state.valid) {
                                return state;
                            }

                            if (ch == '(' || ch == '[' || ch == '{') {
                                List<Character> newStack =
                                        Stream.concat(
                                                state.stack.stream(),
                                                Stream.of(ch)
                                        ).collect(Collectors.toList());

                                return new State(newStack, true);
                            }

                            Optional<Character> top =
                                    state.stack.stream().reduce(
                                            (first, second) -> second
                                    );

                            if (!top.isPresent()
                                    || !top.get().equals(pairs.get(ch))) {
                                return new State(state.stack, false);
                            }

                            List<Character> newStack =
                                    state.stack.stream()
                                            .limit(state.stack.size() - 1)
                                            .collect(Collectors.toList());

                            return new State(newStack, true);
                        },
                        (left, right) -> right
                );

        return finalState.valid && finalState.stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}