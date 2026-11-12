import java.util.ArrayList;
import java.util.Map;

public class BalancedParentheses {

    /** Mapeo inmutable cierre -> apertura. */
    private static final Map<Character, Character> PAIRS = Map.of(')', '(', ']', '[', '}', '{');

    /**
     * Retorna true si los paréntesis, corchetes y llaves de {@code s} están balanceados
     * y se cierran en el orden correcto; false en caso contrario.
     *
     * @throws IllegalArgumentException si {@code s} contiene un carácter que no es ( ) [ ] { }
     */
    public static boolean isBalanced(String s) {
        // Sequenced collections (JEP 431, nuevo en Java 21): ArrayList gana addLast y
        // removeLast, así que funciona como pila sin la aritmética `size() - 1`.
        var stack = new ArrayList<Character>();
        for (var c : s.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> stack.addLast(c);                 // apertura: se apila
                case ')', ']', '}' -> {
                    // Un cierre debe corresponder a la última apertura pendiente.
                    if (stack.isEmpty() || !PAIRS.get(c).equals(stack.removeLast())) {
                        return false;
                    }
                }
                default -> throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
            }
        }
        return stack.isEmpty();   // quedan aperturas sin cerrar -> no balanceada
    }

    /** Caso de prueba como record: inmutable y sin boilerplate. */
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
            // Record pattern en switch (JEP 440 + JEP 441, estándar en Java 21):
            // desestructura el record en sus componentes sin llamar a los getters.
            switch (testCase) {
                case TestCase(var name, var input, var expected) -> {
                    var result = isBalanced(input);
                    if (result != expected) {
                        failures.add("Caso '%s' (\"%s\"): esperado %b, obtenido %b"
                                .formatted(name, input, expected, result));
                    }
                }
            }
        }

        // getFirst() (sequenced collections) en lugar de failures.get(0); solo se
        // evalúa si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}