import java.util.ArrayList;
import java.util.Map;

public class BalancedParentheses {

    /** Mapeo inmutable cierre -> apertura con Map.of (Java 9): sin bloque static. */
    private static final Map<Character, Character> PAIRS = Map.of(')', '(', ']', '[', '}', '{');

    /**
     * Retorna true si los paréntesis, corchetes y llaves de {@code s} están balanceados
     * y se cierran en el orden correcto; false en caso contrario.
     *
     * @throws IllegalArgumentException si {@code s} contiene un carácter que no es ( ) [ ] { }
     */
    public static boolean isBalanced(String s) {
        var stack = new ArrayList<Character>();   // la cima es el último elemento
        for (var c : s.toCharArray()) {
            // switch con etiquetas múltiples y flechas (JEP 361, estándar desde Java 14):
            // cada rama agrupa los caracteres de un mismo tipo, sin `break` ni riesgo de
            // caer por error en la rama siguiente.
            switch (c) {
                case '(', '[', '{' -> stack.add(c);                     // apertura: se apila
                case ')', ']', '}' -> {
                    // Un cierre debe corresponder a la última apertura pendiente.
                    if (stack.isEmpty() || !PAIRS.get(c).equals(stack.remove(stack.size() - 1))) {
                        return false;
                    }
                }
                default -> throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
            }
        }
        return stack.isEmpty();   // quedan aperturas sin cerrar -> no balanceada
    }

    /** Caso de prueba como record (estándar desde Java 16): inmutable y sin boilerplate. */
    record TestCase(String name, String input, boolean expected) {}

    // Ejecutar con assertions habilitadas: java -ea BalancedParentheses.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = new ArrayList<TestCase>();
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
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            var result = isBalanced(testCase.input());
            if (result != testCase.expected()) {
                // String.formatted (Java 15): formato legible sin String.format estático.
                failures.add("Caso '%s' (\"%s\"): esperado %b, obtenido %b"
                        .formatted(testCase.name(), testCase.input(), testCase.expected(), result));
            }
        }

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}