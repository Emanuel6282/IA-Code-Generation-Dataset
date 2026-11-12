// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Pattern Matching para instanceof (JEP 394): Permite verificar el tipo y declarar la variable casteada en una sola expresión (ej. `item instanceof List<?> list`). Esto elimina los peligrosos y ruidosos casteos manuales explícitos, haciendo el código más seguro y legible.
// 2. Inferencia de tipos con 'var' (JEP 286): Reduce la verbosidad en la declaración de variables locales (`result`, `stack`, `item`), dejando que el compilador deduzca el tipo. Esto enfoca la lectura en la lógica del negocio en lugar de en la redundancia de tipos.
// 3. Factory Methods para Colecciones Inmutables (List.of): Reemplaza la verbosidad de `Arrays.asList()` y `Collections.emptyList()`. `List.of()` es el estándar moderno para inicializar colecciones constantes y de prueba de forma concisa.

import java.util.*;

public class P03FlattenList_v17 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros.
     * El parametro es Object porque puede ser Integer o List<?> anidada.
     */
    public static List<Integer> flatten(Object nested) {
        // Uso de 'var' para la inferencia de tipos locales
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Object>();
        
        stack.push(nested);

        while (!stack.isEmpty()) {
            var item = stack.pop();
            
            // Pattern Matching para instanceof elimina los casteos explícitos
            if (item instanceof List<?> list) {
                // Agregar en orden inverso para preservar el orden original
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (item instanceof Integer number) {
                // La variable 'number' ya es de tipo Integer, lista para usarse
                result.add(number);
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenList_v17)

        // TC01: anidamiento basico (Usando List.of en lugar de Arrays.asList)
        var input1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(input1).equals(List.of(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(List.of()).equals(List.of()) : "TC02 fallo";

        // TC03: ya plana
        var input3 = List.of(1, 2, 3);
        assert flatten(input3).equals(List.of(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 17:

1. Eliminación de Casteos con Pattern Matching: 
   En la versión de Java 8, se usaba `if (item instanceof List) { List<?> list = (List<?>) item; }`. En Java 17, el lenguaje introduce Pattern Matching, lo que permite fusionar la comprobación y el casting en un solo paso: `if (item instanceof List<?> list)`. Esto es idiomático porque reduce errores en tiempo de ejecución (ClassCastException) y minimiza el código repetitivo.

2. Uso extensivo de `var`:
   Las declaraciones completas como `List<Integer> result = new ArrayList<>();` fueron cambiadas a `var result = new ArrayList<Integer>();`. En el código moderno de Java, el uso de `var` es altamente idiomático para variables locales donde el lado derecho de la asignación deja completamente claro qué tipo de objeto se está instanciando, limpiando el "ruido visual".

3. Instanciación Declarativa con `List.of()`:
   El método main original utilizaba `Arrays.asList()` anidados, lo cual era verboso y generaba listas mutables de tamaño fijo. Al migrar a `List.of()`, no solo escribimos menos código, sino que creamos listas inmutables verdaderas, lo que se considera una mejor práctica (inmutabilidad por defecto) en el ecosistema actual de Java para datos de prueba.
*/