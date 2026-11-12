import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BalancedParentheses {

    /** Mapeo inmutable cierre -> apertura con Map.of (Java 9). */
    private static final Map<Character, Character> PAIRS = Map.of(')', '(', ']', '[', '}', '{');

    /**
     * Pila inmutable como record (Java 16): lista enlazada en la que apilar crea un nodo
     * nuevo en O(1), sin copiar nada. {@code EMPTY} representa la pila vacía.
     */
    private record Stack(char top, Stack rest) {
        static final Stack EMPTY = new Stack('\0', null);

        boolean isEmpty() {
            return this == EMPTY;
        }

        Stack push(char c) {
            return new Stack(c, this);
        }

        /** Desapila si la cima es {@code expected}; si no (o si está vacía), Optional.empty(). */
        Optional<Stack> popIfTop(char expected) {
            return Optional.of(this)
                    .filter(stack -> !stack.isEmpty() && stack.top() == expected)
                    .map(Stack::rest);
        }
    }

    /**
     * Procesa un carácter: recibe el estado y devuelve uno nuevo, sin modificar el anterior.
     * El estado es un Optional: Optional.of(pila) mientras todo va bien, y Optional.empty()
     * en cuanto un cierre no corresponde; map y flatMap propagan ese error solos.
     *
     * <p>switch como expresión con etiquetas múltiples (JEP 361, Java 14): cada rama
     * produce directamente el nuevo estado, sin sentencias ni variables intermedias.
     */
    private static Optional<Stack> step(Optional<Stack> state, char c) {
        return switch (c) {
            case '(', '[', '{' -> state.map(stack -> stack.push(c));                      // apertura
            case ')', ']', '}' -> state.flatMap(stack -> stack.popIfTop(PAIRS.get(c)));   // cierre
            default -> throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
        };
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

    /** Caso de prueba como record. */
    record TestCase(String name, String input, boolean expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            return Optional.of(isBalanced(input))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s' (\"%s\"): esperado %b, obtenido %b"
                            .formatted(name, input, expected, actual));
        }
    }

    // Ejecutar con assertions habilitadas: java -ea BalancedParentheses.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        assert isBalanced("") : "cadena vacía -> balanceada";

        var failures = Stream.of(
                        // Caso 1: ejemplos balanceados del enunciado
                        new TestCase("paréntesis simples", "()", true),
                        new TestCase("anidamiento mixto", "{[]}", true),
                        // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
                        new TestCase("orden incorrecto", "([)]", false),
                        // Caso 3: aperturas sin cerrar y cierres sin abrir
                        new TestCase("apertura sin cerrar", "((", false),
                        new TestCase("cierre sin abrir", "())", false))
                .map(TestCase::failure)
                .flatMap(Optional::stream)   // Optional.stream() (Java 9): descarta los que pasan
                .toList();                   // Stream.toList() (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}