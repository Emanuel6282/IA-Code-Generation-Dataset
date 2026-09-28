// Características de Java 21 LTS utilizadas en este refactoring:
// 1. Sequenced Collections (JEP 431 - Java 21): Permite usar `addFirst()` y `removeFirst()`
//    sobre interfaces `Deque`/`SequencedCollection`, expresando semánticamente
//    el comportamiento de pila (LIFO) de manera uniforme y más clara.
// 2. Pattern Matching para switch Expressions (JEP 441 - Java 21 LTS): Mejora la ramificación
//    y evaluación de casos sobre tipos primitivos/caracteres, permitiendo agrupar y
//    desestructurar condiciones de control de forma más expresiva.
// 3. Map.of() inmutable estático (Java 9/21): Elimina la re-creación de HashMap en cada llamada
//    a favor de una constante global unmodificable.
// 4. Inferencia de tipos con `var` (Java 10/21): Simplifica las declaraciones locales de variables.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class P04BalancedParentheses_v21 {

    // Mapa estático e inmutable de parejas (Cierre -> Apertura)
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        // Deque es una SequencedCollection en Java 21
        Deque<Character> stack = new ArrayDeque<>();

        for (var c : s.toCharArray()) {
            // Pattern Matching en Switch Expressions (Java 21)
            switch (c) {
                case '(', '[', '{' -> stack.addFirst(c); // API de Sequenced Collection (JEP 431)
                case ')', ']', '}' -> {
                    if (stack.isEmpty() || stack.removeFirst() != PAIRS.get(c)) {
                        return false;
                    }
                }
                default -> {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Ejecutar aserciones estándar habilitando el flag: java -ea P04BalancedParentheses_v21
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;
        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Adopción de Sequenced Collections (JEP 431):
//    En Java 21, `Deque` implementa `SequencedCollection<E>`. Se reemplazó `.push()` y `.pop()`
//    por `.addFirst()` y `.removeFirst()`, que declaran explícitamente el acceso directo
//    al primer elemento de la secuencia de forma idiomatica y uniforme.
// 2. Pattern Matching para switch (JEP 441):
//    Sustituye la estructura obsoleta de `if-else` encadenada por expresiones `switch` de Java 21,
//    lo que optimiza el flujo condicional en el bytecode y elimina comprobaciones redundantes.
// 3. Remoción de `java.util.Stack` y `HashMap` local:
//    Se eliminó la clase legada `Stack` (obsoleta y con overhead de sincronización)
//    y se migró a `ArrayDeque`. Además, el mapa de parejas `PAIRS` se extrajo a una constante
//    estática inmutable creada con `Map.of()`, reduciendo asignaciones de memoria innecesarias.
// 4. Inferencia de tipos con `var`:
//    Mantiene el código conciso al iterar los caracteres en el bucle principal.