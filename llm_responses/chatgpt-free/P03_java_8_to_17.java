// P03: Flatten Nested List — Java 17
//
// Características utilizadas:
// 1. Pattern Matching for instanceof:
//    Permite declarar y comprobar el tipo de la variable en una sola expresión,
//    reduciendo casts explícitos y mejorando la legibilidad.
//
// 2. var para variables locales:
//    Reduce código repetitivo manteniendo el tipo determinado en tiempo de compilación.
//    Es apropiado cuando el tipo resulta evidente por el lado derecho.
//
// 3. Deque + ArrayDeque:
//    Se mantiene la pila explícita para conservar el enfoque iterativo y evitar
//    recursión, preservando el comportamiento original.
//
// 4. Collections y List de la biblioteca estándar:
//    Se mantienen porque son suficientes para resolver el problema sin introducir
//    dependencias externas.

import java.util.*;

public class P03FlattenList_v17 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parámetro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Object>();

        stack.push(nested);

        while (!stack.isEmpty()) {
            var item = stack.pop();

            if (item instanceof List<?> list) {
                // Agregar en orden inverso para preservar el orden original
                for (var i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (item instanceof Integer integer) {
                result.add(integer);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // TC01: anidamiento basico
        List<Object> input1 = Arrays.asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        );

        assert flatten(input1).equals(
                Arrays.asList(1, 2, 3, 4, 5, 6)
        ) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(new ArrayList<>()).equals(
                Collections.emptyList()
        ) : "TC02 fallo";

        // TC03: ya plana
        List<Object> input3 = Arrays.<Object>asList(1, 2, 3);

        assert flatten(input3).equals(
                Arrays.asList(1, 2, 3)
        ) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS Y POR QUE SON IDIOMATICOS EN JAVA 17:
 *
 * 1. Pattern Matching for instanceof
 *    Antes:
 *        if (item instanceof List) {
 *            List<?> list = (List<?>) item;
 *
 *    Ahora:
 *        if (item instanceof List<?> list) {
 *
 *    Java 17 permite comprobar el tipo y declarar la variable al mismo tiempo.
 *    Esto elimina el cast explícito y hace el código más claro.
 *
 * 2. Pattern Matching para Integer
 *    Antes:
 *        else if (item instanceof Integer) {
 *            result.add((Integer) item);
 *
 *    Ahora:
 *        else if (item instanceof Integer integer) {
 *            result.add(integer);
 *
 *    Se elimina nuevamente el cast explícito.
 *
 * 3. Uso de var
 *    Se utiliza var para variables locales cuyo tipo es evidente:
 *
 *        var result = new ArrayList<Integer>();
 *        var stack = new ArrayDeque<Object>();
 *        var item = stack.pop();
 *        var i = ...
 *
 *    Esto reduce código repetitivo sin perder seguridad de tipos.
 *
 * 4. Se mantiene ArrayDeque
 *    ArrayDeque sigue siendo una implementación adecuada de Deque para utilizar
 *    una pila. Se conserva porque la lógica iterativa original funciona
 *    correctamente y no necesita ser reemplazada.
 *
 * 5. Se mantiene el algoritmo iterativo
 *    No se introduce recursión ni Streams. La pila explícita permite soportar
 *    listas con cualquier nivel de anidamiento sin depender de la profundidad
 *    de la pila de llamadas.
 *
 * 6. Se conserva exactamente la función flatten
 *    La firma continúa siendo:
 *
 *        public static List<Integer> flatten(Object nested)
 *
 *    Por lo tanto, el nombre y el comportamiento de la función permanecen
 *    idénticos al código Java 8.
 *
 * 7. Se conservan los tres casos de prueba
 *    TC01, TC02 y TC03 mantienen los mismos escenarios y resultados esperados.
 *
 * 8. No se fuerza una característica exclusiva de Java 17 que no aporte valor.
 *    Pattern Matching for instanceof sí es una característica final de Java 17
 *    y mejora directamente este código. En cambio, var no es exclusivo de Java 17,
 *    pero sigue siendo una forma idiomática de escribir código Java moderno.
 */