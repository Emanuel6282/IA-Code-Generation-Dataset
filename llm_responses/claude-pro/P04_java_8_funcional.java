import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BalancedParentheses {

    private static final String OPENINGS = "([{";
    private static final String CLOSINGS = ")]}";   // mismo orden que OPENINGS

    /** Pila inmutable como lista enlazada: apilar crea un nodo nuevo en O(1), sin copiar. */
    private static final class Stack {
        static final Stack EMPTY = new Stack('\0', null);

        final char top;
        final Stack rest;

        private Stack(char top, Stack rest) {
            this.top = top;
            this.rest = rest;
        }

        boolean isEmpty() {
            return this == EMPTY;
        }

        Stack push(char c) {
            return new Stack(c, this);
        }

        /** Desapila si la cima es {@code expected}; si no (o si está vacía), Optional.empty(). */
        Optional<Stack> popIfTop(char expected) {
            return Optional.of(this)
                    .filter(stack -> !stack.isEmpty() && stack.top == expected)
                    .map(stack -> stack.rest);
        }
    }

    /**
     * Procesa un carácter: recibe el estado y devuelve uno nuevo, sin modificar el anterior.
     * El estado es un Optional: Optional.of(pila) mientras todo va bien, y Optional.empty()
     * en cuanto un cierre no corresponde. flatMap hace que ese error se propague solo.
     */
    private static Optional<Stack> step(Optional<Stack> state, char c) {
        int closingIndex = CLOSINGS.indexOf(c);
        if (closingIndex >= 0) {
            return state.flatMap(stack -> stack.popIfTop(OPENINGS.charAt(closingIndex)));  // cierre
        }
        if (OPENINGS.indexOf(c) >= 0) {
            return state.map(stack -> stack.push(c));                                       // apertura
        }
        throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
    }

    /**
     * Retorna true si los paréntesis, corchetes y llaves de {@code s} están balanceados
     * y se cierran en el orden correcto; false en caso contrario.
     *
     * @throws IllegalArgumentException si {@code s} contiene un carácter que no es ( ) [ ] { }
     */
    public static boolean isBalanced(String s) {
        // reduce pliega la cadena carácter a carácter partiendo de la pila vacía. La cadena
        // está balanceada si no hubo error (Optional presente) y la pila quedó vacía.
        return s.chars()
                .mapToObj(c -> (char) c)
                .reduce(Optional.of(Stack.EMPTY), BalancedParentheses::step,
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); })
                .map(Stack::isEmpty)
                .orElse(false);
    }

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    private static Optional<String> failure(String name, String input, boolean expected) {
        boolean result = isBalanced(input);
        return Optional.of(result)
                .filter(actual -> actual != expected)
                .map(actual -> "Caso '" + name + "' (\"" + input + "\"): esperado " + expected
                        + ", obtenido " + actual);
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac BalancedParentheses.java && java -ea BalancedParentheses
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        assert isBalanced("") : "cadena vacía -> balanceada";

        List<String> failures = Stream.of(
                        // Caso 1: ejemplos balanceados del enunciado
                        failure("paréntesis simples", "()", true),
                        failure("anidamiento mixto", "{[]}", true),
                        // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
                        failure("orden incorrecto", "([)]", false),
                        // Caso 3: aperturas sin cerrar y cierres sin abrir
                        failure("apertura sin cerrar", "((", false),
                        failure("cierre sin abrir", "())", false))
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}