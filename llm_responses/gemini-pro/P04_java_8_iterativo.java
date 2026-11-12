// CARACTERISTICAS DE JAVA 8 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Iterativo Puro: Se utiliza un bucle 'for' imperativo tradicional para recorrer la cadena carácter por carácter. No se emplean Streams, lambdas ni recursividad.
// 2. Uso de Estructuras Mutables: Se utiliza un 'HashMap' para almacenar las parejas de caracteres y un 'ArrayList' para simular el comportamiento de una Pila (Stack) mediante los métodos 'add' y 'remove', gestionando el estado de forma manual durante cada iteración.
// 3. Bloque Static: Para inicializar el 'HashMap' de forma limpia y compatible con Java 8 (que carece de los Factory Methods inmutables como Map.of() introducidos en Java 9).

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P04BalancedParentheses_v8 {

    // Diccionario para mapear los caracteres de cierre con los de apertura
    private static final Map<Character, Character> PAIRS = new HashMap<>();
    
    static {
        PAIRS.put(')', '(');
        PAIRS.put(']', '[');
        PAIRS.put('}', '{');
    }

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación iterativa usando ArrayList y HashMap.
     */
    public static boolean isBalanced(String s) {
        // Se utiliza un ArrayList como pila (stack) mutable
        List<Character> stack = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // Si el caracter es una llave en nuestro mapa, es un caracter de cierre
            if (PAIRS.containsKey(c)) {
                // Si la pila está vacía, tenemos un cierre sin su correspondiente apertura
                if (stack.isEmpty()) {
                    return false;
                }
                
                // Removemos y obtenemos el último elemento insertado (comportamiento LIFO)
                char topElement = stack.remove(stack.size() - 1);
                
                // Comparamos si el elemento extraído coincide con el de apertura esperado
                if (topElement != PAIRS.get(c)) {
                    return false;
                }
            } else {
                // Si no es un caracter de cierre, lo asumimos como apertura y lo apilamos
                stack.add(c);
            }
        }

        // Si la lista está vacía al final del ciclo, la cadena estaba perfectamente balanceada
        // Una cadena vacía ("") no entrará al for y retornará true, cumpliendo el requisito.
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P04BalancedParentheses_v8)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: un solo par balanceado
        assert isBalanced("()") == true : "Error: '()' deberia retornar true";

        // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
        assert isBalanced("([)]") == false : "Error: '([)]' deberia retornar false";

        // 3. Caso del ejemplo: anidamiento profundo balanceado
        assert isBalanced("{[]}") == true : "Error: '{[]}' deberia retornar true";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}