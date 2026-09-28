// Características de Java 17 utilizadas en este refactoring:
// 1. Switch Expressions con flechas `->` (Java 14/17 LTS): Reemplaza las comprobaciones
//    múltiples con `if-else` y elimina la necesidad de buscar en un mapa dinámico en runtime.
// 2. Map.of() / Map Inmutable (Java 9/17): Permite declarar la tabla de parejas
//    de forma estática, concisa e inmutable en lugar de mutar un HashMap en cada llamada.
// 3. Deque / ArrayDeque en lugar de java.util.Stack (Java 6+ / Recomendación Java 17):
//    `java.util.Stack` hereda de `Vector` y sincroniza todos sus métodos de forma innecesaria.
//    `ArrayDeque` no está sincronizada y ofrece mejor rendimiento de memoria y velocidad.
// 4. Inferencia de tipos con `var` (Java 10/17): Mejora la legibilidad en variables locales
//    sin perder la verificación de tipos en tiempo de compilación.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class P04BalancedParentheses_v17 {

    // Tabla inmutable de pares (Cierre -> Apertura) estática
    private static final Map<Character, Character> PAIRS = Map.of(
        ')', '(',
        ']', '[',
        '}', '{'
    );

    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        // Reemplazo de java.util.Stack por Deque/ArrayDeque
        Deque<Character> stack = new ArrayDeque<>();

        for (var c : s.toCharArray()) {
            // Evaluado con Switch Expressions de Java 17
            switch (c) {
                case '(', '[', '{' -> stack.push(c);
                case ')', ']', '}' -> {
                    if (stack.isEmpty() || stack.pop() != PAIRS.get(c)) {
                        return false;
                    }
                }
                default -> {
                    // Manejo explícito de caracteres no permitidos
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Para ejecutar aserciones estándar en Java: java -ea P04BalancedParentheses_v17
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;
        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Sustitución de `java.util.Stack` por `ArrayDeque`:
//    La clase `Stack` es una clase obsoleta que añade sobrecoste debido a la sincronización
//    heredada de `Vector`. La documentación oficial de Java recomienda usar `Deque` / `ArrayDeque`.
// 2. Implementación de Switch Expressions con sintaxis `->`:
//    Simplifica las múltiples ramas condicionales `if-else if` del código original.
//    Permite agrupar múltiples casos de apertura (`'('`, `'['`, `'{'`) en una sola línea clara.
// 3. Factoría estática `Map.of()` para inmutabilidad:
//    Se extrajo la inicialización del `HashMap` local hacia una constante inmutable `PAIRS`,
//    evitando instanciar un nuevo mapa mutable en cada invocación de la función `isBalanced`.
// 4. Inferencia de tipo con `var`:
//    Se aplicó `var` en la iteración del arreglo de caracteres (`for (var c : s.toCharArray())`)
//    para un estilo de código más idiomatico y limpio.