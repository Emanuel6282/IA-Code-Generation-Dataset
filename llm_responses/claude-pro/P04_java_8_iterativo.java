import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalancedParentheses {

    /** Mapeo inmutable cierre -> apertura. */
    private static final Map<Character, Character> PAIRS;

    static {
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');
        PAIRS = Collections.unmodifiableMap(pairs);
    }

    /**
     * Retorna true si los paréntesis, corchetes y llaves de {@code s} están balanceados
     * y se cierran en el orden correcto; false en caso contrario.
     *
     * @throws IllegalArgumentException si {@code s} contiene un carácter que no es ( ) [ ] { }
     */
    public static boolean isBalanced(String s) {
        List<Character> stack = new ArrayList<>();   // la cima es el último elemento
        for (char c : s.toCharArray()) {
            Character expectedOpening = PAIRS.get(c);
            if (expectedOpening != null) {
                // Un cierre debe corresponder a la última apertura pendiente.
                if (stack.isEmpty() || !stack.remove(stack.size() - 1).equals(expectedOpening)) {
                    return false;
                }
            } else if (PAIRS.containsValue(c)) {
                stack.add(c);                          // apertura: se apila
            } else {
                throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
            }
        }
        return stack.isEmpty();   // quedan aperturas sin cerrar -> no balanceada
    }

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final String input;
        final boolean expected;

        TestCase(String name, String input, boolean expected) {
            this.name = name;
            this.input = input;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac BalancedParentheses.java && java -ea BalancedParentheses
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: ejemplos balanceados del enunciado
        testCases.add(new TestCase("paréntesis simples", "()", true));
        testCases.add(new TestCase("anidamiento mixto", "{[]}", true));
        // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
        testCases.add(new TestCase("orden incorrecto", "([)]", false));
        // Caso 3: aperturas sin cerrar y cierres sin abrir
        testCases.add(new TestCase("apertura sin cerrar", "((", false));
        testCases.add(new TestCase("cierre sin abrir", "())", false));

        assert isBalanced("") : "cadena vacía -> balanceada";

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            boolean result = isBalanced(testCase.input);
            if (result != testCase.expected) {
                failures.add("Caso '" + testCase.name + "' (\"" + testCase.input + "\"): esperado "
                        + testCase.expected + ", obtenido " + result);
            }
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}