// CARACTERISTICAS DE JAVA 21 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sequenced Collections (JEP 431): Esta es una de las mayores novedades de Java 21. `ArrayList` ahora implementa la interfaz `SequencedCollection`, lo que nos provee acceso directo a métodos como `addLast()` y `removeLast()`. Esto elimina la necesidad de hacer cálculos de índices propensos a errores (como `stack.remove(stack.size() - 1)`) al implementar una Pila (Stack) iterativa.
// 2. Inferencia de Tipos (`var`) y Map.of: Para inicializar la lista y el mapa de forma concisa y moderna sin redundancia genérica.
// 3. Paradigma Iterativo Estricto: Uso explícito de un bucle `for` tradicional, mutación de estado con `ArrayList` operando como pila, y consultas directas al `HashMap`. Sin Streams, ni lambdas, ni recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalancedParentheses {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación iterativa en Java 21 usando ArrayList (SequencedCollection) y HashMap.
     */
    public static boolean isBalanced(String s) {
        // Inicializamos el HashMap con los pares correspondientes
        var pairs = new HashMap<Character, Character>(Map.of(
            ')', '(', 
            ']', '[', 
            '}', '{'
        ));

        // En Java 21, ArrayList es una SequencedCollection, ideal para actuar como Pila mutable
        var stack = new ArrayList<Character>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // Verificamos si es un caracter de cierre
            if (pairs.containsKey(c)) {
                // Si la pila está vacía, tenemos un cierre sin apertura
                if (stack.isEmpty()) {
                    return false;
                }
                
                // Java 21: removeLast() extrae el último elemento insertado de forma nativa (O(1))
                char topElement = stack.removeLast();
                
                // Comparamos el tope de la pila con la apertura esperada para ese cierre
                if (topElement != pairs.get(c)) {
                    return false;
                }
            } else {
                // Java 21: addLast() inserta el caracter al final de la colección explícitamente
                stack.addLast(c);
            }
        }

        // Si iteramos toda la cadena y la pila queda vacía, está perfectamente balanceada
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea BalancedParentheses)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: un solo par balanceado
        assert isBalanced("()") == true : "Error: '()' deberia retornar true";

        // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
        assert isBalanced("([)]") == false : "Error: '([)]' deberia retornar false por cierre desordenado";

        // 3. Caso del ejemplo: anidamiento profundo balanceado
        assert isBalanced("{[]}") == true : "Error: '{[]}' deberia retornar true";
        
        // 4. Caso explícito de los requisitos: cadena vacía
        assert isBalanced("") == true : "Error: Una cadena vacia deberia retornar true";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}