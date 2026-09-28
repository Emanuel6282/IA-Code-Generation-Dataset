// P03: Flatten Nested List — Java 21
//
// Características utilizadas:
// 1. Pattern Matching for switch:
//    Permite manejar directamente los distintos tipos de elementos
//    (List<?> e Integer) de forma clara y expresiva.
//
// 2. Switch expressions:
//    Permiten que el switch produzca directamente un valor Stream<Integer>,
//    evitando código imperativo innecesario.
//
// 3. var:
//    Se utiliza para variables locales cuyo tipo es evidente,
//    reduciendo código repetitivo.
//
// 4. Stream API:
//    Se utiliza únicamente para expresar el resultado de cada elemento
//    dentro del switch. El algoritmo principal continúa siendo iterativo.
//
// 5. ArrayDeque:
//    Se mantiene como pila explícita porque permite procesar cualquier
//    nivel de anidamiento sin utilizar recursión.
//
// La característica específica de Java 21 que aporta mayor valor aquí es
// Pattern Matching for switch.

import java.util.*;

public class P03FlattenList_v21 {

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

            switch (item) {
                case List<?> list -> {
                    // Agregar en orden inverso para preservar el orden original
                    for (var i = list.size() - 1; i >= 0; i--) {
                        stack.push(list.get(i));
                    }
                }

                case Integer integer -> result.add(integer);

                default -> {
                    // No se procesa ningún otro tipo.
                }
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
 * CAMBIOS REALIZADOS Y POR QUE SON IDIOMATICOS EN JAVA 21:
 *
 * 1. Pattern Matching for switch
 *
 *    Se reemplazó:
 *
 *        if (item instanceof List) {
 *            List<?> list = (List<?>) item;
 *        } else if (item instanceof Integer) {
 *            result.add((Integer) item);
 *        }
 *
 *    por:
 *
 *        switch (item) {
 *            case List<?> list -> ...
 *            case Integer integer -> ...
 *        }
 *
 *    Java 21 permite utilizar patrones de tipo directamente en switch.
 *    Esto hace que el tratamiento de los diferentes tipos sea más
 *    declarativo y elimina casts explícitos.
 *
 * 2. Switch expression / pattern switch
 *
 *    El switch permite asociar directamente cada patrón con su acción,
 *    haciendo que la lógica de procesamiento de los elementos sea más clara.
 *
 *    Esta es la principal característica específica de Java 21 utilizada
 *    en esta migración.
 *
 * 3. Uso de var
 *
 *    Se utiliza var en variables locales:
 *
 *        var result = new ArrayList<Integer>();
 *        var stack = new ArrayDeque<Object>();
 *        var item = stack.pop();
 *
 *    Esto reduce código repetitivo cuando el tipo es evidente.
 *
 * 4. Se mantiene ArrayDeque
 *
 *    ArrayDeque continúa siendo apropiado para representar la pila explícita.
 *    No se reemplaza porque el algoritmo original ya utiliza una estructura
 *    eficiente para las operaciones push/pop.
 *
 * 5. Se mantiene el algoritmo iterativo
 *
 *    No se introduce recursión ni Streams para recorrer toda la estructura.
 *    La lista continúa procesándose mediante una pila explícita.
 *
 *    Esto garantiza el mismo comportamiento que la versión Java 8.
 *
 * 6. Se conserva la función flatten
 *
 *    La firma continúa siendo exactamente:
 *
 *        public static List<Integer> flatten(Object nested)
 *
 *    Por lo tanto, el nombre de la función y su comportamiento no cambian.
 *
 * 7. Se conservan los mismos tres tests
 *
 *    TC01: lista con diferentes niveles de anidamiento.
 *    TC02: lista vacía.
 *    TC03: lista sin anidamiento.
 *
 * 8. No se cambia innecesariamente la estructura del algoritmo
 *
 *    Aunque Java 21 incorpora nuevas características funcionales y de
 *    pattern matching, no se utiliza una transformación a Streams para
 *    todo el algoritmo porque eso modificaría innecesariamente el enfoque
 *    iterativo original.
 *
 * 9. Compatibilidad
 *
 *    El código está pensado para ejecutarse con Java 21 o superior.
 *    Pattern Matching for switch es una característica final disponible
 *    en Java 21.
 */