// P03: Flatten Nested List — Java 17 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Pattern matching for instanceof (JEP 394, Java 16, vigente en 17): en
//    "item instanceof List<?> list" el cast y el binding de la variable
//    "list" ocurren en la misma condicion, eliminando el cast explicito
//    separado "(List<?>) item" que usaba el codigo base. De forma similar,
//    "item instanceof Integer numero" vincula directamente el valor ya
//    casteado a Integer, sin necesitar "(Integer) item" por separado.
// 2. Inferencia de tipos locales con "var" (desde Java 10, JEP 286,
//    idiomatica en 17): usada en variables locales donde el tipo es
//    evidente por el contexto (el indice "i", el "item" desapilado, y las
//    listas de prueba en main).
// 3. Nota de honestidad: revise tambien records, sealed classes y text
//    blocks; ninguna aplica de forma natural aqui, ya que no hay una
//    entidad de datos que modelar como record, ni jerarquias de clases que
//    sellar, ni strings multilinea en este algoritmo de aplanado con pila
//    explicita.

import java.util.*;

public class P03FlattenList_v17 {

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
            if (item instanceof List<?> list) {
                // Agregar en orden inverso para preservar el orden original
                for (var i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (item instanceof Integer numero) {
                result.add(numero);
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
// 1. Se renombro la clase de "P03FlattenList_v8" a "P03FlattenList_v17"
//    unicamente para reflejar la version objetivo del archivo; no afecta
//    el comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo "if (item instanceof List) { List<?> list = (List<?>)
//    item; ... }" por "if (item instanceof List<?> list) { ... }",
//    aprovechando pattern matching for instanceof para eliminar el cast
//    explicito por separado. De forma analoga, "else if (item instanceof
//    Integer) { result.add((Integer) item); }" se convirtio en "else if
//    (item instanceof Integer numero) { result.add(numero); }".
// 3. Se reemplazo "List<Integer> result = new ArrayList<>();", "Object
//    item = stack.pop();", "for (int i = ...)" y las listas de prueba en
//    main por sus equivalentes con "var", ya que el tipo es evidente por
//    el lado derecho de cada asignacion. El tipo real de cada variable no
//    cambia en tiempo de compilacion.
// 4. La logica interna (pila explicita con push/pop, insercion en orden
//    inverso para preservar el orden de aparicion) se mantuvo exactamente
//    igual, cumpliendo el requisito de comportamiento identico; no existia
//    ninguna caracteristica de Java 17 que mejorara esta logica de
//    aplanado iterativo sin alterarla artificialmente.
// 5. El nombre del metodo "flatten" y los tres casos de prueba con
//    "assert" se conservaron sin modificaciones logicas, ya que ya eran
//    compatibles con Java 17 (assert es parte del lenguaje base desde Java
//    1.4).