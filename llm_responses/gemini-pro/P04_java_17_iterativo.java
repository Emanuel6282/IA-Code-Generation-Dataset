// CARACTERISTICAS DE JAVA 17 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Inferencia de tipos con 'var' (JEP 286): Permite declarar las variables locales (el mapa y la lista) sin redundancia de tipos genéricos, limpiando visualmente el código y enfocando la lectura en la inicialización.
// 2. Collection Factory Methods (Map.of): Estándar en Java moderno, permite inicializar los pares del HashMap en una sola expresión concisa, eliminando por completo los antiguos bloques estáticos llenos de `put()`.
// 3. Paradigma Iterativo Estricto: Implementación basada en el control explícito del flujo con un bucle `for`, mutando un `ArrayList` (para simular operaciones `push` y `pop`) y consultando un `HashMap`. No hay lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalancedParentheses {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación iterativa en Java 17 usando ArrayList y HashMap.
     */
    public static boolean isBalanced(String s) {
        // Inicialización del HashMap apoyada en literales de colección modernos
        var pairs = new HashMap<Character, Character>(Map.of(
            ')', '(', 
            ']', '[', 
            '}', '{'
        ));

        // Pila mutable utilizando ArrayList
        var stack = new ArrayList<Character>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // Si es un caracter de cierre (llave en el mapa)
            if (pairs.containsKey(c)) {
                // Desbalance detectado: cierre sin apertura previa
                if (stack.isEmpty()) {
                    return false;
                }
                
                // Extracción del tope de la pila simulada (comportamiento LIFO)
                char topElement = stack.remove(stack.size() - 1);
                
                // Verificamos si corresponde a su apertura correcta
                if (topElement != pairs.get(c)) {
                    return false;
                }
            } else {
                // Si es un caracter de apertura, se añade al final de la lista
                stack.add(c);
            }
        }

        // Si la pila queda vacía, la cadena estaba perfectamente balanceada.
        // Una cadena vacía ("") salta el bucle y evalúa aquí a true correctamente.
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

        System.out.println("Todos los tests iterativos en Java 17 pasaron correctamente.");
    }
}