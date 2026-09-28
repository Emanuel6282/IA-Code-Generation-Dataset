// P04: Balanced Parentheses — Java 17
//
// Características utilizadas:
// 1. var:
//    Mejora la legibilidad de las variables locales evitando repetir tipos
//    evidentes.
//
// 2. ArrayList:
//    Se utiliza como pila mutable para almacenar los símbolos de apertura.
//
// 3. HashMap:
//    Asocia cada símbolo de cierre con su correspondiente símbolo de apertura.
//
// 4. for:
//    Recorre la cadena de forma estrictamente iterativa.
//
// 5. Pattern matching for instanceof:
//    Es una característica final de Java 16 y está disponible en Java 17.
//    No resulta necesaria en este problema porque el algoritmo trabaja con
//    caracteres, por lo que no se fuerza artificialmente.
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
 * 1. Se actualizó el código a Java 17.
 *
 * 2. Se utiliza `var` para variables locales:
 *
 *       var stack = new ArrayList<Character>();
 *       var pairs = new HashMap<Character, Character>();
 *
 *    Esto reduce código repetitivo y es idiomático para variables cuyo
 *    tipo queda claro a partir de la expresión de inicialización.
 *
 * 3. Se mantiene ArrayList como pila:
 *
 *       stack.add(ch);
 *       stack.remove(stack.size() - 1);
 *
 *    Esto conserva el enfoque iterativo y mutable solicitado.
 *
 * 4. Se mantiene HashMap para las correspondencias:
 *
 *       ')' -> '('
 *       ']' -> '['
 *       '}' -> '{'
 *
 * 5. Se mantiene el bucle for para recorrer la cadena.
 *
 * 6. No se utilizan Streams ni lambdas porque el requisito exige una
 *    implementación estrictamente iterativa.
 *
 * 7. No se utiliza recursión.
 *
 * 8. La cadena vacía continúa siendo balanceada porque la pila permanece
 *    vacía y la función retorna `true`.
 *
 * 9. Se conserva exactamente el nombre de la función:
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
 * 12. Aunque Java 17 incorpora pattern matching for instanceof, no se
 *     utiliza artificialmente porque no existe ningún `instanceof` en
 *     este algoritmo donde dicha característica aporte una mejora real.
 */