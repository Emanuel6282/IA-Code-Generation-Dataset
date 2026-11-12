// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. HashMap.newHashMap(int) (Introducido en Java 19, estándar en 21): Este factory method preasigna la capacidad exacta del mapa considerando el "load factor" (factor de carga), previniendo costosas redimensiones en memoria.
// 2. Inferencia de tipos con 'var' (Java 10+, consolidado en 21): Reduce la verbosidad visual (boilerplate) manteniendo la seguridad del tipado estático estricto de Java.
// 3. Factory Methods inmutables (List.of, Map.of): Reemplazan a los antiguos Arrays.asList() y Collections.emptyList() para crear instancias verdaderamente inmutables y de sintaxis limpia en las pruebas.
// 4. Map.merge(): Reemplaza el verboso bloque if/else clásico, actualizando los contadores de forma concisa y atómica (altamente optimizado a nivel de JVM).

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v21 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Usamos el factory method moderno para evitar el overhead de redimensionamiento,
        // asumiendo en el peor caso (todas palabras únicas) que el mapa tendrá words.size() elementos.
        var result = HashMap.<String, Integer>newHashMap(words.size());
        
        for (var word : words) {
            // Un solo acceso al mapa por iteración: si la clave no existe, asigna 1; 
            // si existe, aplica Integer::sum (el valor actual + 1).
            result.merge(word, 1, Integer::sum);
        }
        
        return result;
    }

    public static void main(String[] args) {
        // Se utilizan las colecciones inmutables modernas (List.of) para adaptar las aserciones
        // Nota: Recuerda ejecutar la JVM con la bandera '-ea' para habilitar los asserts.
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        
        System.out.println("All tests passed");
    }
}

// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 21:
//
// 1. Asignación de Capacidad Segura (newHashMap): En versiones anteriores, hacer `new HashMap<>(words.size())` 
//    era un anti-patrón porque la JVM redimensionaba el mapa al llegar al 75% de esa capacidad (load factor). 
//    En Java 21, `HashMap.newHashMap(numMappings)` hace el cálculo matemático correcto internamente para 
//    asegurar que el mapa pueda contener esa cantidad de elementos sin redimensionarse jamás. Esto mejora el 
//    rendimiento iterativo sustancialmente.
//
// 2. Refactorización del Bloque Imperativo: El código de Java 8 hacía 3 operaciones en el mapa por cada 
//    palabra repetida (`containsKey`, `get` y `put`). En Java 21, aprovechar `result.merge(word, 1, Integer::sum)` 
//    junto con el bucle for-each es el enfoque iterativo idiomático moderno: ejecuta una sola operación 
//    optimizada por ciclo.
//
// 3. Adopción de `var`: El uso repetitivo de tipos como `Map<String, Integer> result = new HashMap...` y 
//    `for (String word : words)` ha sido reemplazado por `var`. En el ecosistema de Java 21, `var` es el 
//    estándar para variables locales porque permite al desarrollador enfocarse en los nombres de las variables 
//    y en el comportamiento en lugar de en la redundancia del sistema de tipos.
//
// 4. Colecciones Inmutables en Tests: Pasar de `Arrays.asList()` a `List.of()` no solo hace el código más 
//    corto de leer, sino que evita usar estructuras mutables para casos de prueba que por definición 
//    son de solo lectura, mejorando la seguridad y el uso de memoria de la JVM.