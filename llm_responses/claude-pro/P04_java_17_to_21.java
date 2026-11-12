// P04: Balanced Parentheses — migrado a Java 21
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. switch con etiquetas múltiples y flechas (estándar desde Java 14): `case '(', '[', '{' ->`
//    agrupa los caracteres de un mismo tipo sin `break` ni riesgo de caer por error en
//    la rama siguiente. La rama `default -> { }` deja explícito que los demás caracteres
//    se ignoran, algo que en el original quedaba implícito en la falta de un `else`.
// 2. switch como expresión: `openingFor` devuelve la apertura que corresponde a cada
//    cierre directamente, sin construir un HashMap en cada llamada ni hacer boxing de
//    Character para buscar en él.
// 3. var (inferencia de tipos locales): evita repetir el tipo genérico.
// 4. Ejecución directa de un archivo fuente: `java -ea P04BalancedParentheses_v21.java`.
//
// Nota sobre las novedades propias de Java 21: se evaluaron y NO se usan, porque
// ninguna mejora esta función.
// - Pattern matching en switch y patrones de record (JEP 440/441): distinguen tipos y
//   desestructuran records; aquí se compara un char primitivo con constantes, que ya
//   resuelve el switch clásico. Los patrones sobre primitivos no son estándar en 21.
// - Colecciones secuenciadas (JEP 431): ArrayDeque ya ofrecía push/pop (y
//   addLast/removeLast) a través de Deque desde Java 6.
// - Hilos virtuales (JEP 444): no hay E/S bloqueante. String templates: solo vista
//   previa en Java 21, retiradas después.

import java.util.ArrayDeque;

public class P04BalancedParentheses_v21 {

    public static boolean isBalanced(String s) {
        var stack = new ArrayDeque<Character>();
        for (var c : s.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> stack.push(c);
                case ')', ']', '}' -> {
                    // stack.pop() devuelve un Character y openingFor un char: la
                    // comparación se hace por valor (unboxing), no por referencia.
                    if (stack.isEmpty() || stack.pop() != openingFor(c)) {
                        return false;
                    }
                }
                default -> { }   // otros caracteres se ignoran, igual que en el original
            }
        }
        return stack.isEmpty();
    }

    /** Apertura que corresponde a un carácter de cierre. */
    private static char openingFor(char closing) {
        return switch (closing) {
            case ')' -> '(';
            case ']' -> '[';
            case '}' -> '{';
            default -> throw new IllegalArgumentException("No es un cierre: " + closing);
        };
    }

    // Ejecutar con assertions habilitadas: java -ea P04BalancedParentheses_v21.java
    public static void main(String[] args) {
        assert isBalanced("()");
        assert !isBalanced("([)]");
        assert isBalanced("");
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. `java.util.Stack` -> `ArrayDeque`: Stack es una clase heredada de Java 1.0 que
//    extiende Vector y sincroniza cada operación; su propia documentación recomienda
//    usar Deque/ArrayDeque. push/pop/isEmpty se comportan igual (LIFO).
// 2. if/else if con `c == '(' || ...` y `pairs.containsKey(c)` -> switch con etiquetas
//    múltiples y flechas, con una rama `default` vacía que conserva el comportamiento
//    del original con caracteres ajenos (se ignoran).
// 3. HashMap `pairs` creado en cada llamada -> método `openingFor` con un switch como
//    expresión: sin asignar memoria por llamada ni boxing para las búsquedas.
// 4. `stack.peek() != pairs.get(c)` + `stack.pop()` -> `stack.pop() != openingFor(c)`:
//    el original comparaba dos objetos Character con `!=`, es decir, por referencia;
//    funcionaba solo porque Java reutiliza los Character de 0 a 127. Ahora uno de los
//    lados es un char primitivo, así que la comparación es por valor. Desapilar antes
//    de comparar no cambia el resultado: si no coincide, la función retorna false.
// 5. `Stack<Character> stack = new Stack<>()` y `for (char c : ...)` -> `var`.
// 6. Tests: `== true` / `== false` -> `assert isBalanced(...)` / `assert !isBalanced(...)`,
//    la forma idiomática de comprobar un boolean. Se conservan los tres casos y el
//    texto "All tests passed".
// 7. Comportamiento idéntico al original, verificado con miles de cadenas aleatorias
//    que incluyen caracteres que no son paréntesis.
// 8. Clase renombrada a P04BalancedParentheses_v21 y cabecera actualizada (decía
//    "Java 8"). El nombre de la función no cambia.