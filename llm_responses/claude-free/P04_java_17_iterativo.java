import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class BalancedParentheses {

    /**
     * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
     * "{", "}", retorna true si todos los parentesis/corchetes estan
     * correctamente balanceados y cerrados en orden. Retorna false en caso
     * contrario.
     *
     * Implementacion estrictamente iterativa: usa un bucle for explicito y
     * una pila mutable (ArrayDeque) para recorrer la cadena. No se usan
     * Streams ni lambdas. Compatible con Java 17.
     *
     * @param s Cadena que contiene unicamente los caracteres de parentesis
     *          y corchetes indicados.
     * @return true si la cadena esta correctamente balanceada, false en
     *         caso contrario. Una cadena vacia se considera balanceada
     *         (true).
     */
    public static boolean isBalanced(String s) {
        // Map.of (Java 9+, vigente en 17): forma concisa e idiomatica de
        // crear un mapa inmutable pequeño y fijo, en lugar de HashMap +
        // tres llamadas a put() como en las versiones de Java 8.
        var paresCierre = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        Deque<Character> pila = new ArrayDeque<>();
        var balanceado = true;

        for (var i = 0; i < s.length() && balanceado; i++) {
            var caracter = s.charAt(i);

            if (caracter == '(' || caracter == '[' || caracter == '{') {
                pila.push(caracter);
            } else if (paresCierre.containsKey(caracter)) {
                if (pila.isEmpty()) {
                    balanceado = false;
                } else if (!pila.peek().equals(paresCierre.get(caracter))) {
                    balanceado = false;
                } else {
                    pila.pop();
                }
            }
        }

        if (!pila.isEmpty()) {
            balanceado = false;
        }

        return balanceado;
    }

    public static void main(String[] args) {
        // Caso 1: balanceado simple
        assert isBalanced("()") == true : "Caso 1 fallido";

        // Caso 2: orden incorrecto de cierre -> no balanceado
        assert isBalanced("([)]") == false : "Caso 2 fallido";

        // Caso 3: anidamiento balanceado de varios tipos
        assert isBalanced("{[]}") == true : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}