// CARACTERISTICAS DE JAVA 21 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Pattern Matching for Switch (JEP 441): Completamente consolidado y finalizado en Java 21. Permite utilizar el `switch` no como una estructura de control de flujo imperativa, sino como una expresión de mapeo funcional pura (mediante la sintaxis `->`).
// 2. Inmutabilidad Absoluta (Pila como String): En el paradigma funcional no utilizamos colecciones mutables (ni siquiera las nuevas SequencedCollections de Java 21). Representamos el estado de la pila matemáticamente como un `String` inmutable.
// 3. Optional como Mónada de Fallo (Short-Circuit): El estado viaja a través del Stream envuelto en un `Optional`. Si detectamos un desbalance (retornando `Optional.empty()`), el uso de `flatMap` garantiza que las evaluaciones de los siguientes caracteres se omitan automáticamente, propagando la "falla" hasta el final sin usar condicionales imperativos como `break` o `return` tempranos.

import java.util.Optional;

public class BalancedParentheses {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación estrictamente funcional en Java 21.
     * Sin bucles iterativos, usando reducción de Streams (fold) y evaluación inmutable.
     */
    public static boolean isBalanced(String s) {
        // Transformamos la cadena en un flujo (Stream) continuo de caracteres y lo reducimos
        var finalState = s.chars()
            .mapToObj(c -> (char) c)
            .reduce(
                // Estado base: Optional con un String vacío (simula la pila inmutable vacía)
                Optional.<String>of(""),
                
                // Función acumuladora pura: (estadoActual, caracter) -> nuevoEstado
                (optStack, ch) -> optStack.flatMap(stack -> switch (ch) {
                    // Si es un caracter de apertura, retornamos un nuevo String concatenado (push inmutable)
                    case '(', '[', '{' -> Optional.of(stack + ch);
                    
                    // Si es un cierre, validamos funcionalmente con 'endsWith' que actúa como un 'peek' seguro.
                    // Si el tope coincide, creamos un nuevo String excluyendo el último caracter (pop funcional).
                    // Si no coincide o la pila (String) está vacía, emitimos un Optional.empty() (falla).
                    case ')' -> stack.endsWith("(") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    case ']' -> stack.endsWith("[") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    case '}' -> stack.endsWith("{") ? Optional.of(stack.substring(0, stack.length() - 1)) : Optional.empty();
                    
                    // Manejo seguro por defecto requerido por las switch expressions funcionales
                    default -> Optional.empty();
                }),
                
                // Función combinadora requerida por la firma de 'reduce' (irrelevante para ejecución secuencial)
                (opt1, opt2) -> opt1
            );

        // La validación final: el pipeline no debió fallar (Optional presente) y la pila final debe ser un String vacío.
        return finalState.map(String::isEmpty).orElse(false);
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests en tu entorno de pruebas, asegúrate de habilitar 
        // las aserciones en la JVM pasando el argumento "-ea" (ej. java -ea BalancedParentheses)
        
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

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}