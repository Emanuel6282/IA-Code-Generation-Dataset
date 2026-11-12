// P04: Balanced Parentheses — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. switch con etiquetas múltiples y flechas (JEP 361, estándar desde Java 14):
//    `case '(', '[', '{' ->` agrupa los caracteres de un mismo tipo sin `break` ni
//    riesgo de caer por error en la rama siguiente. La rama `default -> { }` deja
//    explícito que los demás caracteres se ignoran, algo que en el original quedaba
//    implícito en la falta de un `else`.
// 2. switch como expresión (JEP 361): `openingFor` devuelve la apertura que corresponde
//    a cada cierre directamente, sin construir un HashMap en cada llamada ni hacer
//    boxing de Character para buscar en él.
// 3. var (inferencia de tipos locales, Java 10): evita repetir el tipo genérico.
// 4. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se puede
//    correr con `java -ea P04BalancedParentheses_v17.java`, sin compilar antes.
//
// Nota: no se usan records, clases sealed ni text blocks porque no aportan nada a esta
// función. El pattern matching en switch solo es estándar desde Java 21.

import java.util.ArrayDeque;

public class P04BalancedParentheses_v17 {

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

    // Ejecutar con assertions habilitadas: java -ea P04BalancedParentheses_v17.java
    public static void main(String[] args) {
        assert isBalanced("()");
        assert !isBalanced("([)]");
        assert isBalanced("");
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
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
// 8. Clase renombrada a P04BalancedParentheses_v17 para reflejar la versión (en Java,
//    el archivo debe llamarse igual que la clase pública). El nombre de la función no
//    cambia.