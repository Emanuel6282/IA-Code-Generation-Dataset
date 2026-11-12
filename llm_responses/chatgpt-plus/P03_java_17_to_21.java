// P03: Flatten Nested List — Java 21
// Migración desde Java 17
// Paradigma: Iterativo con pila explícita.
//
// CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
//
// 1. Pattern matching para switch (Java 21):
//    Permite distinguir tipos mediante patrones.
//    Elimina casts explícitos y mejora la legibilidad.
//
// 2. Sintaxis case ... -> (switch moderno):
//    Evita fall-through y simplifica las condiciones.
//
// 3. List.of() (Java 9+, compatible con Java 21):
//    Permite crear listas de prueba de forma concisa.
//
// 4. Operador diamante <>:
//    Mantiene la inferencia de tipos en constructores.
//
// 5. ArrayDeque y ArrayList:
//    Conservan el algoritmo iterativo y su rendimiento.
//
// NOTA:
// No se utiliza recursión, Stream API ni lambdas.
// La lógica original se mantiene intacta.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03FlattenList_v21 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     *
     * @param nested Integer o List<?> anidada.
     * @return Lista plana con los enteros en orden original.
     *
     * Los elementos de otros tipos se ignoran,
     * igual que en la implementación original.
     */
    public static List<Integer> flatten(Object nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        stack.push(nested);

        while (!stack.isEmpty()) {
            Object item = stack.pop();

            switch (item) {

                case List<?> list -> {
                    // Insertar en orden inverso para
                    // preservar el orden original.
                    for (int i = list.size() - 1; i >= 0; i--) {
                        stack.push(list.get(i));
                    }
                }

                case Integer number -> result.add(number);

                default -> {
                    // Ignorar elementos de otros tipos,
                    // conservando el comportamiento original.
                }
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

// RESUMEN DE CAMBIOS — JAVA 17 A JAVA 21
//
// 1. Se reemplazaron las condiciones instanceof
//    por pattern matching para switch.
//
//    Motivo: Java 21 permite distinguir tipos
//    directamente en las ramas case.
//
// 2. Se eliminaron las conversiones explícitas:
//
//       (List<?>) item
//       (Integer) item
//
//    Motivo: las variables de patrón ya tienen
//    el tipo correspondiente.
//
// 3. Se incorporó la sintaxis case ... ->.
//
//    Motivo: mejora la claridad y evita fall-through.
//
// 4. Se reemplazó Arrays.asList() por List.of()
//    en los casos de prueba.
//
//    Motivo: sintaxis concisa para listas inmutables.
//
// 5. Se reemplazó Collections.emptyList()
//    por List.of().
//
//    Motivo: expresar una lista vacía de forma sencilla.
//
// 6. Se conservaron ArrayList y ArrayDeque.
//
//    Motivo: mantener las estructuras de datos
//    del algoritmo original.
//
// 7. Se conservaron los bucles while y for.
//
//    Motivo: mantener el paradigma iterativo.
//
// 8. Se mantuvo la firma:
//
//       public static List<Integer> flatten(Object nested)
//
//    Motivo: conservar la compatibilidad.
//
// 9. Se conservaron exactamente los tres tests:
//    TC01, TC02 y TC03.
//
// 10. No se utilizaron características que
//     alterasen el comportamiento o la complejidad.
//
// NOTA:
// List.of() y el operador diamante no son novedades
// específicas de Java 21. La mejora específica
// de esta versión es el pattern matching para switch.