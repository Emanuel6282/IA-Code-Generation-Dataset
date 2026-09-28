// Java 8 - Paradigma Iterativo Estricto
// Implementación mediante `ArrayList` actuando como pila (stack) mutable y `HashMap`
// para la correspondencia de delimitadores, evitando Streams, expresiones lambda y recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    /**
     * Verifica si una cadena de delimitadores está balanceada mediante un bucle for-each tradicional,
     * una estructura basada en `ArrayList` para emular una pila (LIFO) y un `HashMap` para mapear
     * los pares de cierre a sus correspondientes aperturas.
     */
    public static boolean isBalanced(String s) {
        if (s == null) {
            return false;
        }

        // Mapa de parejas de delimitadores de cierre -> apertura
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');

        // Estructura de pila explícita mutable usando ArrayList
        List<Character> stack = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (pairs.containsKey(ch)) {
                // Si es un carácter de cierre, verificar si coincide con el tope de la pila
                if (stack.isEmpty()) {
                    return false;
                }
                char last = stack.remove(stack.size() - 1);
                if (last != pairs.get(ch)) {
                    return false;
                }
            } else {
                // Si es un carácter de apertura, apilarlo
                stack.add(ch);
            }
        }

        // La cadena es balanceada si la pila finaliza totalmente vacía
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        // NOTA: Para habilitar aserciones en Java, ejecutar con el flag -ea:
        // java -ea Solution

        // Caso 1: Ejemplos base provistos en el problema
        assert isBalanced("()") == true : "Falló ()";
        assert isBalanced("([)]") == false : "Falló ([)]";
        assert isBalanced("{[]}") == true : "Falló {[]}";

        // Caso 2: Cadena vacía y estructuras compuestas válidas
        assert isBalanced("") == true : "Falló cadena vacía";
        assert isBalanced("()[]{}") == true : "Falló ()[]{}";

        // Caso 3: Desbalance por aperturas/cierres huérfanos
        assert isBalanced("((()") == false : "Falló ((()";
        assert isBalanced("]") == false : "Falló ]";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}