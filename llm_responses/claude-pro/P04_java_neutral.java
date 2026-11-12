public class BalancedParentheses {

    /**
     * Retorna true si los paréntesis, corchetes y llaves de {@code s} están balanceados
     * y se cierran en el orden correcto; false en caso contrario.
     *
     * @throws IllegalArgumentException si {@code s} contiene un carácter que no es ( ) [ ] { }
     */
    public static boolean isBalanced(String s) {
        // Pila de aperturas sobre un char[]: evita el boxing de Character que tendría
        // un ArrayDeque<Character>. Como mucho habrá s.length() aperturas pendientes.
        char[] stack = new char[s.length()];
        int top = 0;   // número de elementos en la pila
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                case '[':
                case '{':
                    stack[top++] = c;
                    break;
                case ')':
                case ']':
                case '}':
                    // Un cierre debe corresponder a la última apertura pendiente.
                    if (top == 0 || stack[--top] != opening(c)) {
                        return false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Carácter no válido: '" + c + "'");
            }
        }
        return top == 0;   // quedan aperturas sin cerrar -> no balanceada
    }

    /** Devuelve la apertura que corresponde a un carácter de cierre. */
    private static char opening(char closing) {
        return closing == ')' ? '(' : closing == ']' ? '[' : '{';
    }

    // Ejecutar con assertions habilitadas: java -ea BalancedParentheses.java
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplos balanceados del enunciado y cadena vacía
        assert isBalanced("()");
        assert isBalanced("{[]}");
        assert isBalanced("");

        // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
        assert !isBalanced("([)]");

        // Caso 3: aperturas sin cerrar y cierres sin abrir
        assert !isBalanced("((");
        assert !isBalanced("())");

        System.out.println("Todas las pruebas pasaron.");
    }
}