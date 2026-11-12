// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Switch Expressions (JEP 361) / Agrupación de Casos: El switch moderno con sintaxis de flecha (->) nos permite eliminar por completo el uso del 'HashMap' y los bloques 'if/else', evaluando directamente los caracteres a nivel de bytecode de forma nativa y agrupando las aperturas en una sola línea.
// 2. Sequenced Collections (JEP 431): Es una de las características estrella de Java 21. Colecciones como 'ArrayDeque' o 'ArrayList' ahora implementan la interfaz 'SequencedCollection', exponiendo métodos estandarizados como 'addLast()', 'getLast()' y 'removeLast()'. Esto unifica la semántica de una Pila (Stack) de forma intuitiva y moderna.
// 3. Inferencia de tipos con 'var': Mantiene la legibilidad limpia al instanciar la colección.

import java.util.ArrayDeque;

public class P04BalancedParentheses_v21 {

    public static boolean isBalanced(String s) {
        // En Java 21, ArrayDeque implementa SequencedCollection
        var stack = new ArrayDeque<Character>();

        for (char c : s.toCharArray()) {
            // Enrutamiento de control nativo y eficiente usando switch moderno
            switch (c) {
                // Agrupación de casos: si es apertura, insertamos al final (push)
                case '(', '[', '{' -> stack.addLast(c);
                
                // Si es cierre, verificamos el balance utilizando el nuevo API SequencedCollection
                case ')' -> {
                    if (stack.isEmpty() || stack.removeLast() != '(') return false;
                }
                case ']' -> {
                    if (stack.isEmpty() || stack.removeLast() != '[') return false;
                }
                case '}' -> {
                    if (stack.isEmpty() || stack.removeLast() != '{') return false;
                }
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
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 21:

1. Eliminación del HashMap y Adopción de Switch:
   El código base en Java 8 instanciaba un `Map` únicamente para emparejar caracteres, lo que consume memoria e implica cálculos de hash en cada validación. En Java 21, usar un `switch` con sintaxis de flecha (arrow syntax) es la forma idiomática de enrutar flujo basado en valores finitos. El compilador lo optimiza drásticamente, haciendo que la ejecución sea mucho más rápida y el código más declarativo.

2. Sequenced Collections (JEP 431):
   Se reemplazó la clase obsoleta `java.util.Stack` por `ArrayDeque`. Sin embargo, en lugar de usar los antiguos métodos `push()` y `pop()`, utilizamos `addLast()` y `removeLast()`. En Java 21, estos métodos pertenecen a la nueva interfaz `SequencedCollection`, que finalmente estandariza cómo interactuamos con el principio y el final de las colecciones en Java, representando semánticamente una pila (LIFO) perfecta y nativa.

3. Reducción de Ruido Visual (`var` y eliminación de bloques vacíos):
   Al combinar `var`, el switch moderno y los métodos unificados de las Sequenced Collections, el código de validación de balance se redujo a la mínima expresión imperativa, siendo al mismo tiempo la implementación más rápida posible en la JVM actual sin recurrir a manejo manual de punteros en arreglos primitivos.
*/