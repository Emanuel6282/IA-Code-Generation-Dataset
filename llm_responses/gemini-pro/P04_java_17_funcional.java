// CARACTERISTICAS DE JAVA 17 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Switch Expressions (JEP 361): Uso del `switch` moderno como expresión funcional (con sintaxis de flecha `->`). Elimina la verbosidad de los `case` con `break`, retornando valores directamente y asegurando evaluación exhaustiva.
// 2. Inferencia de tipos con 'var' (JEP 286): Para declarar el estado final evaluado por el Stream, reduciendo el ruido visual de los tipos genéricos.
// 3. String Inmutable como Pila + endsWith(): En lugar de gestionar índices o colecciones, aprovechamos la inmutabilidad de `String`. Además, `stack.endsWith()` actúa mágicamente como un "peek" seguro que no falla ni arroja excepciones si la pila (string) está vacía.
// 4. Mónada Optional y flatMap para Short-Circuiting: Si la cadena se desbalancea, el switch retorna `Optional.empty()`. Al usar `flatMap`, cualquier iteración subsiguiente ignora la computación, arrastrando el estado "vacío/inválido" hasta el final de la reducción sin necesidad de condicionales de control imperativos.

import java.util.Optional;

public class BalancedParentheses {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación estrictamente funcional en Java 17.
     */
    public static boolean isBalanced(String s) {
        // Reducción (fold) sobre el flujo de caracteres utilizando la API de Streams
        var finalState = s.chars()
            .mapToObj(c -> (char) c)
            .reduce(
                // Estado inicial: Optional con pila (String) inmutable vacía
                Optional.<String>of(""),
                
                // Función acumuladora: transforma el estado inmutable paso a paso
                (optStack, ch) -> optStack.flatMap(stack -> switch (ch) {
                    // Si es apertura, generamos un nuevo estado concatenando el caracter
                    case '(', '[', '{' -> Optional.of(stack + ch);
                    
                    // Si es cierre, validamos el tope de la pila simulada usando endsWith.
                    // Si coincide, retornamos un nuevo estado sin ese caracter (pop funcional).
                    // Si no coincide o la pila está vacía (endsWith da false), propagamos el fallo.
                    case ')' -> stack.endsWith("(") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    case ']' -> stack.endsWith("[") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    case '}' -> stack.endsWith("{") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    
                    // Comportamiento defensivo exhaustivo requerido por switch expressions
                    default -> Optional.empty();
                }),
                
                // Función combinadora (requerida por reduce, aunque irrelevante en streams secuenciales)
                (opt1, opt2) -> opt1
            );

        // La cadena es válida si el Optional no está vacío y la pila final no tiene elementos pendientes
        return finalState.map(String::isEmpty).orElse(false);
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
        assert isBalanced("([)]") == false : "Error: '([)]' deberia retornar false";

        // 3. Caso del ejemplo: anidamiento profundo balanceado
        assert isBalanced("{[]}") == true : "Error: '{[]}' deberia retornar true";
        
        // 4. Caso explícito de los requisitos: cadena vacía
        assert isBalanced("") == true : "Error: Una cadena vacia deberia retornar true";

        System.out.println("Todos los tests funcionales en Java 17 pasaron correctamente.");
    }
}