// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Pattern Matching para switch (JEP 441): Permite evaluar tipos directamente dentro de un bloque `switch` mediante casos como `case List<?> list`. Reemplaza las engorrosas cadenas de `if-else if` y casteos manuales, brindando un enrutamiento de tipos más limpio y declarativo.
// 2. Sequenced Collections (JEP 431): Java 21 dota a `List` del método `.reversed()`, el cual devuelve una vista invertida de la colección en O(1). Esto elimina el anticuado y propenso a errores bucle `for` inverso basado en índices.
// 3. Inferencia de tipos locales con 'var' (JEP 286): Para instanciar listas y extraer elementos de la pila sin redundancia visual.
// 4. Factory Methods inmutables (List.of): Para refactorizar los verbosos `Arrays.asList()` de los casos de prueba por constructores modernos e inmutables.

import java.util.*;

public class P03FlattenList_v21 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Object>();
        
        stack.push(nested);

        while (!stack.isEmpty()) {
            var item = stack.pop();
            
            // Expresión switch de Java 21 con Pattern Matching
            switch (item) {
                case List<?> list -> {
                    // Sequenced Collections: iteramos directamente sobre la vista invertida
                    for (var elem : list.reversed()) {
                        stack.push(elem);
                    }
                }
                case Integer number -> 
                    // Si es un Integer, el casteo ya está hecho en la variable 'number'
                    result.add(number);
                    
                case null, default -> {
                    // Ignora elementos nulos o tipos no soportados de forma segura
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenList_v21)

        // TC01: anidamiento basico usando List.of()
        var input1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(input1).equals(List.of(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(List.of()).equals(List.of()) : "TC02 fallo";

        // TC03: ya plana
        var input3 = List.of(1, 2, 3);
        assert flatten(input3).equals(List.of(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed in Java 21");
    }
}

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 21:

1. Pattern Matching en `switch`:
   En el código base, la lógica dependía de `if (item instanceof List) {...} else if (item instanceof Integer) {...}`. Con Java 21, el `switch` evolucionó para soportar tipos complejos. Ahora escribimos `case List<?> list ->`, lo cual no solo verifica el tipo sino que inyecta la variable ya casteada en ese bloque. Además, incorporar `case null` previene de forma nativa los clásicos NullPointerExceptions.

2. `list.reversed()` (Sequenced Collections):
   En Java 8, para insertar una lista en una pila manteniendo el orden final correcto, teníamos que iterar del final hacia el principio con un bucle imperativo: `for (int i = list.size() - 1; i >= 0; i--)`. Java 21 introdujo las Sequenced Collections, otorgando el método `.reversed()` a todas las listas. Esto permite usar un `for-each` estándar y declarativo (`for (var elem : list.reversed())`), mejorando notablemente la legibilidad sin sacrificar rendimiento.

3. Prácticas Modernas (`var` y `List.of`):
   Sustituimos explícitamente tipos repetitivos como `List<Integer> result = new ArrayList<>();` por `var`. Para los tests, reemplazamos `Arrays.asList` y `Collections.emptyList()` con `List.of()`, que es el enfoque canónico moderno para construir estructuras de datos inmutables de prueba en memoria.
*/