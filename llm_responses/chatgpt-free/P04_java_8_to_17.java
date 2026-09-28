// P04: Balanced Parentheses — Java 17
//
// Características utilizadas:
// 1. ArrayDeque como pila:
//    Reemplaza java.util.Stack, que es una clase legacy sincronizada.
//    ArrayDeque ofrece una implementación más moderna y eficiente para
//    las operaciones de pila push/pop/peek.
//
// 2. var para variables locales:
//    Mejora la legibilidad al evitar repetir tipos genéricos largos,
//    manteniendo el tipado estático de Java.
//
// 3. Map.of para construir pares:
//    Permite crear de forma concisa un mapa inmutable con las relaciones
//    entre paréntesis de cierre y apertura.
//
// Estas características son compatibles con Java 17 y permiten modernizar
// el código sin modificar su lógica ni sus resultados.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class P04BalancedParentheses_v17 {

    public static boolean isBalanced(String s) {
        var stack = new ArrayDeque<Character>();

        var pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(c)) {
                    return false;
                }

                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;

        System.out.println("All tests passed");
    }

    /*
     * Cambios realizados y por qué son idiomáticos en Java 17:
     *
     * 1. Stack<Character> -> ArrayDeque<Character>
     *    Stack es una clase legacy de Java. ArrayDeque es preferible para
     *    implementar una pila porque evita la sincronización innecesaria
     *    y proporciona operaciones push(), pop() y peek().
     *
     * 2. Map + HashMap -> Map.of(...)
     *    El mapa de pares no necesita modificarse después de crearse.
     *    Map.of() permite expresar directamente una colección inmutable
     *    y hace el código más compacto.
     *
     * 3. Declaraciones explícitas -> var
     *    Java 17 permite utilizar var para variables locales. El tipo
     *    sigue siendo estático, pero se reduce ruido visual en declaraciones
     *    como ArrayDeque<Character> y Map<Character, Character>.
     *
     * 4. Se mantiene el algoritmo original.
     *    La función continúa recorriendo la cadena, agregando aperturas
     *    a la pila y verificando que cada cierre coincida con la apertura
     *    situada en la cima.
     *
     * 5. Se mantienen exactamente los mismos casos de prueba.
     *
     * 6. La función conserva exactamente el nombre isBalanced.
     */
}