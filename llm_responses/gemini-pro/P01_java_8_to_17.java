// CARACTERISTICAS DE JAVA 17 (Y VERSIONES PREVIAS HASTA LA 17) UTILIZADAS Y POR QUE:
// 1. Inferencia de tipos locales con 'var' (Java 10+): Se utiliza para reducir la verbosidad en las declaraciones de variables locales, mejorando la legibilidad al evitar la repetición de tipos genéricos, sin perder la seguridad del tipado estático.
// 2. Factory Methods para Colecciones Inmutables (Java 9+): Se emplean List.of() en lugar de Arrays.asList() y Collections.emptyList() en las pruebas. Proporcionan listas verdaderamente inmutables y una sintaxis mucho más limpia.
// 3. Map.merge() (Optimización idiomática): Aunque se introdujo en Java 8, su uso junto con referencias a métodos (Integer::sum) es el estándar idiomático en Java moderno para evitar el bloque if/else verboso y mejorar el rendimiento.

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P01WordFrequency_v17 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Uso de var para inferencia de tipos
        var result = new HashMap<String, Integer>();
        
        // Uso de var en el bucle iterativo
        for (var word : words) {
            // Un solo acceso al mapa por iteración: mejora legibilidad y rendimiento
            result.merge(word, 1, Integer::sum);
        }
        
        return result;
    }

    public static void main(String[] args) {
        // Adaptación de los tests usando la sintaxis inmutable idiomática (List.of)
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(List.of()).equals(Map.of());
        assert wordFrequency(List.of("hello")).equals(Map.of("hello", 1));
        
        System.out.println("All tests passed");
    }
}

// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 17:
// 
// 1. Reemplazo del bloque if/else iterativo: En el código original, el HashMap 
//    era consultado varias veces (containsKey, get, y put) por cada palabra. 
//    Al refactorizarlo usando `result.merge(word, 1, Integer::sum)`, la estructura 
//    interna del HashMap realiza una única operación atómica por iteración. Si la llave 
//    no existe, inserta '1'; si existe, aplica la suma (Integer::sum). Esto mejora el rendimiento.
//
// 2. Adopción de la palabra clave 'var': En lugar de escribir explícitamente 
//    `Map<String, Integer> result = new HashMap<>();`, usamos `var result = new HashMap<String, Integer>();`. 
//    Esto permite centrar la lectura en el valor asignado y el nombre de la variable, en 
//    lugar del tipo, reduciendo el "ruido visual" (boilerplate).
//
// 3. Modernización de las aserciones en el main: Se migró `Arrays.asList(...)` y 
//    `Collections.emptyList()` a `List.of(...)` y `List.of()`. Esto no solo es mucho más 
//    corto y claro, sino que devuelve colecciones estructuralmente inmutables de alto rendimiento.