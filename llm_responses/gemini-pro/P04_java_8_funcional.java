// CARACTERISTICAS DE JAVA 8 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Stream API con `reduce`: Sustituimos el bucle `for` por una operación de reducción funcional (fold) sobre el flujo de caracteres. La reducción toma un estado inicial y aplica una función pura de acumulación.
// 2. Inmutabilidad: En lugar de una Pila mutable, utilizamos inmutabilidad pura modelando la pila como un `String`. En cada paso construimos un nuevo String agregando (push) o recortando (pop) el último carácter.
// 3. Optional como Mónada de Fallo (Short-Circuit): Representamos el estado válido con `Optional.of(estado)` y la detección de un desbalance (fallo) con `Optional.empty()`. Usar `flatMap` garantiza que si la cadena se desbalancea en cualquier punto, el `Optional.empty()` se propagará limpiamente hasta el final sin procesar lógicas innecesarias.

import java.util.Optional;

public class BalancedParenthesesFunctional {

    /**
     * Verifica si una cadena de paréntesis, corchetes y llaves 
     * está correctamente balanceada y cerrada en orden.
     * 
     * Implementación estrictamente funcional usando Streams, lambdas y Optional.
     */
    public static boolean isBalanced(String s) {
        // Obtenemos un IntStream de caracteres, mapeamos a objetos Character y reducimos
        Optional<String> finalState = s.chars()
            .mapToObj(c -> (char) c)
            .reduce(
                // Estado inicial: Optional con un String vacío (pila inmutable vacía)
                Optional.of(""),
                
                // Función acumuladora: (estadoPrevio, caracterActual) -> nuevoEstado
                (optStack, ch) -> optStack.flatMap(stack -> {
                    // Si es una apertura, agregamos el caracter al final (push funcional)
                    if (ch == '(' || ch == '[' || ch == '{') {
                        return Optional.of(stack + ch);
                    }
                    
                    // Si es un cierre pero la pila está vacía, falla (desbalance)
                    if (stack.isEmpty()) {
                        return Optional.empty();
                    }
                    
                    // Extraemos el último elemento (tope de la pila inmutable)
                    char top = stack.charAt(stack.length() - 1);
                    
                    // Verificamos si el par es correcto
                    if ((ch == ')' && top == '(') || 
                        (ch == ']' && top == '[') || 
                        (ch == '}' && top == '{')) {
                        // Pop funcional: retornamos un nuevo String sin el último caracter
                        return Optional.of(stack.substring(0, stack.length() - 1));
                    }
                    
                    // Si no hace match, propagamos el fallo
                    return Optional.empty();
                }),
                
                // Función combinadora (requerida por el API de reduce, pero no 
                // se ejecuta en streams secuenciales)
                (opt1, opt2) -> opt1 
            );

        // Si el balance fue perfecto, el Optional debe contener un valor y la pila debe estar vacía
        return finalState.map(String::isEmpty).orElse(false);
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea BalancedParenthesesFunctional)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: un solo par balanceado
        assert isBalanced("()") == true : "Error: '()' debería retornar true";

        // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
        assert isBalanced("([)]") == false : "Error: '([)]' debería retornar false";

        // 3. Caso del ejemplo: anidamiento profundo balanceado
        assert isBalanced("{[]}") == true : "Error: '{[]}' debería retornar true";
        
        // 4. Caso de los requisitos: cadena vacía
        assert isBalanced("") == true : "Error: Una cadena vacía debería retornar true";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}