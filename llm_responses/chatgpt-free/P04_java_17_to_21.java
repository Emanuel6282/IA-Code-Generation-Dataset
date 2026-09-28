// P04: Balanced Parentheses — Java 21
//
// Características modernas utilizadas:
//
// 1. Switch expression:
//    Permite expresar de forma directa la relación entre cada carácter
//    de cierre y su carácter de apertura correspondiente.
//
// 2. ArrayDeque:
//    Reemplaza java.util.Stack, una clase legacy de Java. ArrayDeque es
//    más apropiada y eficiente para implementar una pila.
//
// 3. Map.of():
//    Permite crear un mapa inmutable de forma compacta.
//
// 4. var:
//    Se utiliza para variables locales cuando el tipo resulta evidente,
//    reduciendo ruido visual sin perder el tipado estático.
//
// Nota:
// Java 21 no introduce una nueva estructura de datos específica que
// mejore sustancialmente este algoritmo. Por ello se utilizan las
// características modernas que aportan claridad sin forzar cambios
// innecesarios.

import java.util.ArrayDeque;
import java.util.Map;

public class P04BalancedParentheses_v21 {

    public static boolean isBalanced(String s) {
        var stack = new ArrayDeque<Character>();

        var pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        for (var c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                var expectedOpening = switch (c) {
                    case ')' -> '(';
                    case ']' -> '[';
                    case '}' -> '{';
                    default -> null;
                };

                if (stack.isEmpty() || stack.peek() != expectedOpening) {
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
     * Cambios realizados y por qué son idiomáticos en Java 21:
     *
     * 1. Stack<Character> -> ArrayDeque<Character>
     *    Stack es una clase legacy. ArrayDeque proporciona una implementación
     *    más apropiada para una pila mediante push(), pop() y peek().
     *
     * 2. HashMap mutable -> Map.of(...)
     *    El conjunto de pares nunca cambia durante la ejecución, por lo que
     *    una colección inmutable expresa mejor la intención del código.
     *
     * 3. Introducción de un switch expression
     *    El switch expression permite obtener directamente el carácter de
     *    apertura esperado para cada carácter de cierre.
     *
     *    Esta sintaxis moderna evita una cadena adicional de if/else y hace
     *    explícita la correspondencia entre cierres y aperturas.
     *
     * 4. Uso de var
     *    Se utiliza en variables locales cuyo tipo puede deducirse fácilmente,
     *    como la pila, el mapa y el carácter recorrido.
     *
     * 5. Se mantiene el algoritmo original
     *    Los caracteres de apertura se agregan a la pila y cada carácter de
     *    cierre se compara con el elemento situado en la cima.
     *
     * 6. Se mantiene exactamente la función isBalanced.
     *
     * 7. Se conservan los mismos tres casos de prueba:
     *    "()", "([)]" y "".
     *
     * 8. El comportamiento y los resultados son idénticos al código original.
     */
}