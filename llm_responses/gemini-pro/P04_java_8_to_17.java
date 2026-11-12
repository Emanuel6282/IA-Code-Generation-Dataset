// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Inferencia de Tipos con 'var' (JEP 286): Standard en versiones modernas, elimina la redundancia visual al declarar el mapa y la pila, dejando que el compilador infiera el tipo exacto.
// 2. Collection Factory Methods (Map.of): Introducido en Java 9 y consolidado en Java 17. Permite crear un mapa inmutable directamente en su inicialización de forma limpia y segura, eliminando las múltiples y tediosas llamadas a `put()`.
// 3. ArrayDeque en lugar de Stack: Aunque no es exclusivo de Java 17, en el desarrollo Java moderno se prohíbe tácitamente el uso de `java.util.Stack` (por ser una clase heredada de `Vector`, sincronizada y lenta). `ArrayDeque` es la implementación estándar y de alto rendimiento para pilas iterativas.

import java.util.ArrayDeque;
import java.util.Map;

public class P04BalancedParentheses_v17 {

    public static boolean isBalanced(String s) {
        // 'var' infiere ArrayDeque<Character>
        var stack = new ArrayDeque<Character>();
        
        // 'var' infiere Map<Character, Character>. Map.of crea un diccionario inmutable.
        var pairs = Map.of(
            ')', '(', 
            ']', '[', 
            '}', '{'
        );

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                // Validación idéntica usando los métodos modernos de Deque
                if (stack.isEmpty() || stack.peek() != pairs.get(c)) {
                    return false;
                }
                stack.pop();
            }
        }
        
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // Recordar ejecutar con el flag -ea (enable assertions)
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;
        
        System.out.println("All tests passed");
    }
}

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 17:

1. Reemplazo de inicialización de colecciones (Map.of):
   El código base Java 8 instanciaba un `HashMap` vacío y luego mutaba su estado agregando tres elementos línea por línea. En Java 17, es idiomático utilizar `Map.of()` para declarar conjuntos estáticos de datos. Esto no solo reduce código, sino que el mapa resultante es estructuralmente inmutable, lo cual es más seguro y eficiente en memoria.

2. Adopción de `var`:
   En lugar de escribir `Stack<Character> stack = new Stack<>()` y `Map<Character, Character> pairs = new HashMap<>()`, usamos `var`. En Java 17, esto es considerado una buena práctica siempre que el lado derecho de la asignación deje perfectamente claro el tipo de dato que se está instanciando.

3. Sustitución de `java.util.Stack` por `java.util.ArrayDeque`:
   La clase `Stack` original pertenece a la primera versión de Java (JDK 1.0). Al heredar de `Vector`, todos sus métodos adquieren bloqueos de sincronización (thread-safe), lo que penaliza el rendimiento en contextos de un solo hilo. `ArrayDeque` es la estructura de datos idiomática, moderna y rápida para representar pilas (LIFO) en las versiones recientes de Java.
*/