// P04: Balanced Parentheses — Java 21
//
// Características utilizadas:
// 1. var:
//    Permite declarar variables locales de forma más concisa cuando el tipo
//    queda claro por la expresión de inicialización.
//
// 2. Pattern matching para instanceof:
//    Disponible de forma definitiva desde Java 16 y, por tanto, disponible
//    en Java 21. No es necesario en este algoritmo porque trabajamos con
//    caracteres, por lo que no se fuerza artificialmente.
//
// 3. ArrayList y HashMap:
//    Se mantienen como las estructuras mutables solicitadas para implementar
//    la pila y las correspondencias entre símbolos.
//
// 4. for:
//    El recorrido de la cadena se realiza de forma estrictamente iterativa.
//
// No se utilizan Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class BalancedParentheses {

    public static boolean isBalanced(String s) {
        var stack = new ArrayList<Character>();

        var pairs = new HashMap<Character, Character>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        for (var ch : s.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.add(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                var opening = stack.remove(stack.size() - 1);

                if (opening != pairs.get(ch)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. Se actualizó el código a Java 21.
 *
 * 2. Se utiliza `var` para variables locales:
 *
 *       var stack = new ArrayList<Character>();
 *       var pairs = new HashMap<Character, Character>();
 *
 *    Esto reduce repetición y mejora la legibilidad cuando el tipo resulta
 *    evidente a partir de la expresión de inicialización.
 *
 * 3. Se mantiene ArrayList como pila:
 *
 *       stack.add(ch);
 *       stack.remove(stack.size() - 1);
 *
 *    Esto conserva el enfoque iterativo solicitado y permite acceder al
 *    último elemento como una pila LIFO.
 *
 * 4. Se mantiene HashMap para las correspondencias:
 *
 *       ')' -> '('
 *       ']' -> '['
 *       '}' -> '{'
 *
 * 5. Se mantiene el bucle `for` para procesar cada carácter.
 *
 * 6. No se utilizan Streams ni lambdas porque el requisito exige
 *    estrictamente el paradigma iterativo.
 *
 * 7. No se utiliza recursión.
 *
 * 8. La cadena vacía continúa siendo balanceada:
 *
 *       isBalanced("")
 *
 *    retorna `true` porque la pila termina vacía.
 *
 * 9. El nombre de la función permanece exactamente:
 *
 *       isBalanced
 *
 * 10. Se mantienen los tres casos de prueba:
 *
 *       "()"   -> true
 *       "([)]" -> false
 *       "{[]}" -> true
 *
 * 11. Se utiliza únicamente la biblioteca estándar de Java.
 *
 * 12. Java 21 no introduce una característica específica que mejore
 *     materialmente este algoritmo. Aunque Java 21 incorpora nuevas
 *     funcionalidades como Sequenced Collections y Pattern Matching for
 *     switch, utilizarlas aquí no aportaría una mejora real y obligaría
 *     a cambiar innecesariamente la estructura del algoritmo.
 */