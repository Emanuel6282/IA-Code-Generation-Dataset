// P04: Balanced Parentheses — Java 17
//
// Características utilizadas:
// 1. Stream API:
//    Permite procesar la cadena de manera declarativa sin bucles imperativos.
//
// 2. Lambdas:
//    Definen las transformaciones del estado durante el procesamiento.
//
// 3. Optional:
//    Se utiliza para representar de forma segura el elemento superior
//    de la pila.
//
// 4. reduce():
//    Procesa todos los caracteres manteniendo un estado funcional.
//
// 5. record:
//    Disponible desde Java 16 y plenamente disponible en Java 17.
//    Permite representar de forma concisa e inmutable el estado del algoritmo.
//
// 6. List.copyOf():
//    Disponible desde Java 10. Permite crear listas inmutables.
//
// No se utilizan for, while ni recursión.

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BalancedParentheses {

    private record State(List<Character> stack, boolean valid) {
    }

    public static boolean isBalanced(String s) {
        Map<Character, Character> pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        State finalState = s.chars()
                .mapToObj(value -> (char) value)
                .reduce(
                        new State(List.of(), true),
                        (state, ch) -> {
                            if (!state.valid()) {
                                return state;
                            }

                            if (ch == '(' || ch == '[' || ch == '{') {
                                List<Character> newStack =
                                        StreamHelper.append(
                                                state.stack(),
                                                ch
                                        );

                                return new State(newStack, true);
                            }

                            Optional<Character> top =
                                    state.stack()
                                            .stream()
                                            .reduce((first, second) -> second);

                            boolean matches = top
                                    .map(opening -> opening == pairs.get(ch))
                                    .orElse(false);

                            if (!matches) {
                                return new State(state.stack(), false);
                            }

                            List<Character> newStack =
                                    state.stack()
                                            .stream()
                                            .limit(state.stack().size() - 1)
                                            .collect(Collectors.toList());

                            return new State(
                                    List.copyOf(newStack),
                                    true
                            );
                        },
                        (left, right) -> right
                );

        return finalState.valid() && finalState.stack().isEmpty();
    }

    private static class StreamHelper {

        private static List<Character> append(
                List<Character> list,
                Character element
        ) {
            return java.util.stream.Stream.concat(
                            list.stream(),
                            java.util.stream.Stream.of(element)
                    )
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. ArrayList mutable -> List inmutable
 *
 *    La pila se representa mediante List<Character> y cada modificación
 *    genera una nueva lista. Esto evita estado mutable.
 *
 * 2. Clase State -> record State
 *
 *    Java 17 permite utilizar records como una forma concisa de representar
 *    datos inmutables. El estado contiene:
 *
 *        - stack: pila actual
 *        - valid: indica si la cadena sigue siendo válida
 *
 * 3. for -> Stream
 *
 *    La cadena se transforma mediante:
 *
 *        s.chars().mapToObj(...)
 *
 *    y se procesa mediante reduce().
 *
 * 4. reduce()
 *
 *    Sustituye el procesamiento secuencial que normalmente se realizaría
 *    mediante un bucle for, manteniendo el estado de forma funcional.
 *
 * 5. Optional
 *
 *    El elemento superior de la pila se obtiene mediante:
 *
 *        state.stack().stream()
 *            .reduce((first, second) -> second)
 *
 *    El resultado es un Optional<Character>, evitando acceder directamente
 *    a una posición inexistente cuando la pila está vacía.
 *
 * 6. Map.of()
 *
 *    Se utiliza para crear de forma concisa e inmutable la correspondencia:
 *
 *        ')' -> '('
 *        ']' -> '['
 *        '}' -> '{'
 *
 *    Map.of() está disponible desde Java 9.
 *
 * 7. List.of()
 *
 *    La pila comienza como una lista vacía inmutable.
 *
 * 8. List.copyOf() / Collectors.toUnmodifiableList()
 *
 *    Se utilizan para garantizar que las nuevas representaciones de la
 *    pila sean inmutables.
 *
 * 9. Sin for ni while
 *
 *    El procesamiento completo se realiza mediante Stream API y reduce().
 *
 * 10. Sin recursión
 *
 *    No se utiliza ninguna llamada recursiva.
 *
 * 11. Comportamiento idéntico
 *
 *    ""       -> true
 *    "()"     -> true
 *    "([)]"   -> false
 *    "{[]}"   -> true
 *
 * 12. Función
 *
 *    El nombre permanece exactamente:
 *
 *        isBalanced
 *
 * 13. Biblioteca estándar
 *
 *    Solo se utilizan clases pertenecientes a la biblioteca estándar
 *    de Java 17.
 */