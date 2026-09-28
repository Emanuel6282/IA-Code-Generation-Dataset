// P03: Flatten Nested List — Java 21 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Pattern matching for switch (JEP 441, estandar desde Java 21): la
//    cadena "if (item instanceof List) {...} else if (item instanceof
//    Integer) {...}" del codigo base se reemplazo por una sentencia
//    "switch (item)" con patrones ("case List<?> list ->", "case Integer
//    numero ->", "default ->"), que es la evolucion natural del pattern
//    matching for instanceof (JEP 394) hacia una forma mas declarativa y
//    exhaustiva de expresar bifurcaciones por tipo, evitando el cast
//    explicito separado del codigo original.
// 2. Inferencia de tipos locales con "var" (desde Java 10, vigente en 21):
//    usada en variables locales donde el tipo es evidente por el
//    contexto (el indice "i", el "item" desapilado, "result", y las
//    listas de prueba en main).
// 3. Nota de honestidad: revise tambien virtual threads (JEP 444), record
//    patterns (JEP 440) y sequenced collections (JEP 431). Ninguna aplica
//    de forma natural aqui:
//    - Virtual threads: el problema es computo puro y secuencial sobre
//      una estructura en memoria, sin I/O bloqueante.
//    - Record patterns: no hay ningun record en este problema (no hay una
//      entidad de datos compuesta que modelar; solo Integer y List<?>),
//      por lo que no aplica desestructuracion de records.
//    - Sequenced collections: el patron push/pop de ArrayDeque usado aqui
//      no se beneficia de la nueva API de SequencedCollection, y el
//      problema no requiere acceder especificamente al primer/ultimo
//      elemento de una List con esa API.

import java.util.*;

public class P03FlattenList_v21 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        Deque<Object> stack = new ArrayDeque<>();
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
                case Integer numero -> result.add(numero);
                default -> {
                    // Cualquier otro tipo se ignora silenciosamente, ya
                    // que el contrato de la funcion asume una estructura
                    // de enteros anidados.
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // TC01: anidamiento basico
        var input1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        assert flatten(input1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(new ArrayList<>()).equals(Collections.emptyList()) : "TC02 fallo";

        // TC03: ya plana
        var input3 = Arrays.<Object>asList(1, 2, 3);
        assert flatten(input3).equals(Arrays.asList(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se renombro la clase de "P03FlattenList_v8" a "P03FlattenList_v21"
//    unicamente para reflejar la version objetivo del archivo; no afecta
//    el comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo la cadena "if (item instanceof List) { List<?> list =
//    (List<?>) item; ... } else if (item instanceof Integer) {
//    result.add((Integer) item); }" por una sentencia "switch (item) {
//    case List<?> list -> {...} case Integer numero -> ...; default ->
//    {} }", usando pattern matching for switch (JEP 441). Esto elimina el
//    cast explicito por separado y hace exhaustiva y declarativa la
//    bifurcacion por tipo, con un "default" explicito para cualquier otro
//    caso no contemplado (mismo comportamiento que el "if/else if" sin
//    "else" final del codigo base, que simplemente no hacia nada en ese
//    caso).
// 3. Se reemplazo "List<Integer> result = new ArrayList<>();", "Object
//    item = stack.pop();", "for (int i = ...)" y las listas de prueba en
//    main por sus equivalentes con "var", ya que el tipo es evidente por
//    el lado derecho de cada asignacion.
// 4. La logica interna (pila explicita con push/pop, insercion en orden
//    inverso para preservar el orden de aparicion) se mantuvo exactamente
//    igual, cumpliendo el requisito de comportamiento identico; no existia
//    ninguna caracteristica de Java 21 que mejorara esta logica de
//    aplanado iterativo sin alterarla artificialmente.
// 5. El nombre del metodo "flatten" y los tres casos de prueba con
//    "assert" se conservaron sin modificaciones logicas, ya que ya eran
//    compatibles con Java 21 (assert es parte del lenguaje base desde Java
//    1.4).