import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BalancedParentheses {

    /** Mapeo inmutable cierre -> apertura. */
    private static final Map<Character, Character> PAIRS = Map.of(')', '(', ']', '[', '}', '{');

    /**
     * Pila inmutable como jerarquía sellada de records: vacía ({@code Empty}) o un nodo
     * con cima y resto ({@code Node}). Apilar crea un nodo nuevo en O(1), sin copiar. Al
     * ser {@code sealed}, el compilador conoce todos los casos y verifica que cada switch
     * los cubra.
     */
    sealed interface Stack permits Empty, Node {}

    record Empty() implements Stack {}

    record Node(char top, Stack rest) implements Stack {}

    /**
     * Desapila si la cima es {@code expected}; si no (o si la pila está vacía),
     * Optional.empty(). Pattern matching en switch con patrones de record y guarda
     * `when` (JEP 440 + JEP 441, estándar en Java 21): desestructura el nodo y
     * comprueba la cima en el propio patrón.
     */
    private static Optional<Stack> popIfTop(Stack stack, char expected) {
        return switch (stack) {
            case Node(var top, var rest) when top == expected -> Optional.of(rest);  // coincide
            case Node node -> Optional.empty();                                     // no coincide
            case Empty empty -> Optional.empty();                                   // cierre sin apertura
        };
    }

    /**
     * Procesa un carácter: recibe el estado y devuelve uno nuevo, sin modificar el anterior.
     * El estado es un Optional: Optional.of(pila) mientras todo va bien, y Optional.empty()
     * en cuanto un cierre no corresponde; map y flatMap propagan ese error solos.
     */
    private static Optional<Stack> step(Optional<Stack> state, char c) {
        return switch (c) {
            case '(', '[', '{' -> state.map(stack -> new Node(c, stack));                 // apertura
            case ')', ']', '}' -> state.flatMap(stack -> popIfTop(stack, PAIRS.get(c)));  // cierre
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
                .reduce(Optional.<Stack>of(new Empty()), BalancedParentheses::step,
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); })
                .map(stack -> stack instanceof Empty)
                .orElse(false);
    }

    /** Caso de prueba como record. */
    record TestCase(String name, String input, boolean expected) {}

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    static Optional<String> failure(TestCase testCase) {
        return switch (testCase) {
            case TestCase(var name, var input, var expected) -> Optional.of(isBalanced(input))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s' (\"%s\"): esperado %b, obtenido %b"
                            .formatted(name, input, expected, actual));
        };
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
                .map(BalancedParentheses::failure)
                .flatMap(Optional::stream)   // descarta los casos que pasan
                .toList();                   // lista inmutable

        // getFirst() (sequenced collections, Java 21) sobre los fallos; solo se evalúa
        // si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}