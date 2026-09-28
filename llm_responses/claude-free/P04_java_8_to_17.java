// P04: Balanced Parentheses — Java 17 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Map.of (Java 9+, JEP 269, vigente en 17): reemplaza "new HashMap<>()"
//    seguido de tres llamadas a "pairs.put(...)" por una unica expresion
//    inmutable y concisa. Es seguro aqui porque "pairs" nunca se modifica
//    despues de su creacion en el codigo original, por lo que el cambio de
//    mutable a inmutable no altera el comportamiento.
// 2. Inferencia de tipos locales con "var" (desde Java 10, idiomatica en
//    17): usada en "pairs" y en el caracter del for-each, donde el tipo es
//    evidente por el contexto.
// 3. Nota de honestidad: se mantuvo intencionalmente "java.util.Stack"
//    (en lugar de migrar a ArrayDeque/Deque, la alternativa moderna mas
//    eficiente) porque el requisito pide "comportamiento identico" y una
//    migracion de sintaxis, no un cambio estructural de tipo de dato;
//    cambiar Stack por Deque excederia el alcance de esta migracion
//    puntual. De igual forma, se conservo la comparacion "stack.peek() !=
//    pairs.get(c)" tal cual, sin cambiarla a ".equals()": aunque "!="
//    compara identidad de referencia en lugar de valor para objetos
//    Character, todos los caracteres involucrados en este problema son
//    ASCII de bajo valor, que Java cachea automaticamente (Character
//    cache, valores entre -128 y 127), por lo que "!=" produce
//    exactamente el mismo resultado que ".equals()" en este contexto
//    especifico; cambiarlo seria alterar una decision del codigo original
//    mas alla de la migracion de version solicitada. Se reviso tambien
//    pattern matching for instanceof, records, sealed classes y text
//    blocks; ninguna aplica de forma natural aqui, ya que no hay chequeos
//    de tipo con instanceof, ni entidades de datos que modelar como
//    record, ni jerarquias que sellar, ni strings multilinea.

import java.util.Stack;
import java.util.Map;

public class P04BalancedParentheses_v17 {

    public static boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        var pairs = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );
        for (var c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(c)) return false;
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
}

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se renombro la clase de "P04BalancedParentheses_v8" a
//    "P04BalancedParentheses_v17" unicamente para reflejar la version
//    objetivo del archivo; no afecta el comportamiento ni el nombre del
//    metodo publico requerido.
// 2. Se reemplazo "Map<Character, Character> pairs = new HashMap<>();"
//    seguido de tres "pairs.put(...)" por "var pairs = Map.of(...)",
//    usando el mapa inmutable de Java 9+ (idiomatico y vigente en 17).
//    Como "pairs" nunca se reasigna ni se modifica tras su creacion en el
//    codigo original, este cambio es completamente seguro y no altera el
//    comportamiento; ademas se elimino el import de "java.util.HashMap",
//    que ya no es necesario.
// 3. Se reemplazo "for (char c : s.toCharArray())" por "for (var c :
//    s.toCharArray())", usando inferencia de tipos locales donde el tipo
//    (char) es evidente por el contexto.
// 4. La logica interna (Stack<Character> explicita, push/peek/pop, la
//    comparacion "stack.peek() != pairs.get(c)" tal cual, y el retorno
//    final "stack.isEmpty()") se mantuvo exactamente igual, cumpliendo el
//    requisito de comportamiento identico; no existia ninguna
//    caracteristica de Java 17 que mejorara esta logica de balanceo con
//    pila sin alterarla artificialmente o sin exceder el alcance de una
//    migracion de sintaxis.
// 5. El nombre del metodo "isBalanced" y los tres casos de prueba con
//    "assert" se conservaron sin modificaciones logicas, ya que ya eran
//    compatibles con Java 17 (assert es parte del lenguaje base desde Java
//    1.4).