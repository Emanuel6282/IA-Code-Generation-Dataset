// P03: Flatten Nested List — Java 17
// Migración desde Java 8
// Estilo: Imperativo, con pila explícita.
//
// CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
//
// 1. Pattern matching para instanceof (Java 16):
//    Permite comprobar el tipo y declarar una variable
//    tipada sin realizar conversiones explícitas.
//
// 2. List.of() (Java 9):
//    Simplifica la creación de listas de prueba.
//    Produce listas inmutables.
//
// 3. Operador diamante <>:
//    Permite inferir parámetros genéricos.
//    Ya existía en Java 8, pero sigue siendo idiomático.
//
// 4. ArrayDeque como pila:
//    Mantiene el algoritmo iterativo original.
//    No se introduce recursión ni Stream API.
//
// 5. Inferencia de tipos mediante pattern matching:
//    Mejora la legibilidad sin cambiar la complejidad.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03FlattenList_v17 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     *
     * El parámetro es Object porque puede representar
     * un Integer o una List<?> anidada.
     *
     * Conserva el comportamiento original:
     * los elementos que no sean listas ni enteros
     * se ignoran.
     */
    public static List<Integer> flatten(Object nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        stack.push(nested);

        while (!stack.isEmpty()) {
            Object item = stack.pop();

            if (item instanceof List<?> list) {

                // Agregar en orden inverso para preservar
                // el orden original de aparición.
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }

            } else if (item instanceof Integer number) {
                result.add(number);
            }
        }

        return result;
    }

    public static void main(String[] args) {

        // TC01: anidamiento básico
        List<Object> input1 = List.of(
                1,
                List.of(2, 3),
                List.of(4, List.of(5, 6))
        );

        assert flatten(input1).equals(
                List.of(1, 2, 3, 4, 5, 6)
        ) : "TC01 fallo";

        // TC02: lista vacía
        assert flatten(new ArrayList<>()).equals(
                List.of()
        ) : "TC02 fallo";

        // TC03: ya plana
        List<Object> input3 = List.of(1, 2, 3);

        assert flatten(input3).equals(
                List.of(1, 2, 3)
        ) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// RESUMEN DE CAMBIOS — JAVA 8 A JAVA 17
//
// 1. Se reemplazó:
//
//       if (item instanceof List) {
//           List<?> list = (List<?>) item;
//
//    por:
//
//       if (item instanceof List<?> list) {
//
//    Motivo: pattern matching para instanceof,
//    incorporado definitivamente en Java 16.
//
// 2. Se reemplazó:
//
//       result.add((Integer) item);
//
//    por:
//
//       result.add(number);
//
//    Motivo: evitar conversiones explícitas después
//    de comprobar el tipo Integer.
//
// 3. Se reemplazó Arrays.asList() por List.of()
//    en los casos de prueba.
//
//    Motivo: crear listas inmutables de forma concisa.
//    Los tests originales no modifican esas listas.
//
// 4. Se reemplazó Collections.emptyList() por List.of().
//
//    Motivo: expresar una lista vacía de forma moderna.
//
// 5. Se conservaron ArrayList y ArrayDeque.
//
//    Motivo: mantener las mismas estructuras de datos
//    y el mismo comportamiento iterativo.
//
// 6. Se conservaron los bucles while y for.
//
//    Motivo: preservar la lógica y la complejidad.
//
// 7. Se mantuvo la firma:
//
//       public static List<Integer> flatten(Object nested)
//
//    Motivo: conservar la compatibilidad con el código
//    original y admitir Integer o List<?>.
//
// 8. Se conservaron los tres casos de prueba:
//    TC01, TC02 y TC03, con sus mensajes originales.
//
// 9. No se utilizaron Streams, lambdas ni recursión.
//
//    Motivo: una migración de versión no requiere
//    modificar el paradigma del algoritmo.