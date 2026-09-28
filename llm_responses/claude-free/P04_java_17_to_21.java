// P04: Balanced Parentheses — Java 21 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Map.of (Java 9+, JEP 269, vigente en 21): reemplaza "new HashMap<>()"
//    seguido de tres llamadas a "pairs.put(...)" por una unica expresion
//    inmutable y concisa. Es seguro aqui porque "pairs" nunca se modifica
//    despues de su creacion en el codigo original.
// 2. Inferencia de tipos locales con "var" (desde Java 10, idiomatica en
//    21): usada en "pairs" y en el caracter del for-each.
// 3. Nota de honestidad: se mantuvo intencionalmente "java.util.Stack" (en
//    lugar de ArrayDeque/Deque) y la comparacion "stack.peek() !=
//    pairs.get(c)" tal cual (en lugar de ".equals()"), por la misma razon
//    que en la migracion a Java 17: el requisito pide comportamiento
//    identico y una migracion de sintaxis, no un cambio estructural de
//    tipo de dato ni una correccion de un detalle que, para los
//    caracteres ASCII involucrados aqui, ya produce el resultado correcto
//    gracias al cache interno de Character en Java. Se revisaron ademas
//    las novedades especificas de Java 21 — virtual threads (JEP 444),
//    pattern matching for switch (JEP 441), record patterns (JEP 440) y
//    sequenced collections (JEP 431). Ninguna aplica de forma natural sin
//    exceder el alcance de esta migracion puntual:
//    - Virtual threads: el problema es computo puro y secuencial, sin I/O
//      bloqueante.
//    - Pattern matching for switch / record patterns: la logica actual
//      son comparaciones simples de "char" y consultas a un Map, sin
//      jerarquia de tipos ni estructuras de datos que desestructurar;
//      forzar un "switch" con patrones aqui reemplazaria un "if/else if"
//      ya claro por una construccion mas compleja sin beneficio real, y
//      ademas alteraria la estructura del codigo mas alla de una simple
//      migracion de version.
//    - Sequenced collections: "Stack" (subclase legacy de "Vector") no
//      participa de esta nueva jerarquia de forma relevante para el
//      patron push/peek/pop usado aqui, y cambiarla excederia el alcance
//      de la migracion.

import java.util.Stack;
import java.util.Map;

public class P04BalancedParentheses_v21 {

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
//    "P04BalancedParentheses_v21" unicamente para reflejar la version
//    objetivo del archivo; no afecta el comportamiento ni el nombre del
//    metodo publico requerido.
// 2. Se reemplazo "Map<Character, Character> pairs = new HashMap<>();"
//    seguido de tres "pairs.put(...)" por "var pairs = Map.of(...)". Como
//    "pairs" nunca se reasigna ni se modifica tras su creacion en el
//    codigo original, este cambio es completamente seguro; se elimino
//    tambien el import de "java.util.HashMap", que ya no es necesario.
// 3. Se reemplazo "for (char c : s.toCharArray())" por "for (var c :
//    s.toCharArray())", usando inferencia de tipos locales donde el tipo
//    (char) es evidente por el contexto.
// 4. La logica interna (Stack<Character> explicita, push/peek/pop, la
//    comparacion "stack.peek() != pairs.get(c)" tal cual, y el retorno
//    final "stack.isEmpty()") se mantuvo exactamente igual, cumpliendo el
//    requisito de comportamiento identico; no existia ninguna
//    caracteristica de Java 21 que mejorara esta logica de balanceo con
//    pila sin alterarla artificialmente o sin exceder el alcance de una
//    migracion de sintaxis.
// 5. El nombre del metodo "isBalanced" y los tres casos de prueba con
//    "assert" se conservaron sin modificaciones logicas, ya que ya eran
//    compatibles con Java 21 (assert es parte del lenguaje base desde Java
//    1.4).