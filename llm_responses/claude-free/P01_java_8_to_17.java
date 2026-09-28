// P01: Word Frequency — Java 17 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (introducida en Java 10, JEP 286,
//    y plenamente idiomatica en 17): se usa en la declaracion de "result"
//    dentro de wordFrequency y en las variables locales de main, ya que el
//    tipo es evidente por el contexto (new HashMap<>(), el elemento de un
//    for-each sobre List<String>). Reduce verbosidad sin perder seguridad de
//    tipos (siguen siendo estaticos, solo se infieren en compilacion).
// 2. Nota de honestidad: revise otras novedades de Java 17 como records
//    (JEP 395), sealed classes (JEP 409), pattern matching for instanceof
//    (JEP 394) y text blocks (JEP 378, desde Java 15). Ninguna aplica de
//    forma natural aqui: no hay jerarquias de clases que sellar, no hay
//    "instanceof" que simplificar, y no hay strings multilinea. El uso de
//    "Map.of(...)" en los tests ya provenia del codigo original en Java 8
//    (introducido en Java 9, JEP 269) y se conserva sin cambios porque sigue
//    siendo la forma idiomatica de crear mapas inmutables de prueba.

import java.util.*;

public class P01WordFrequency_v17 {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        var result = new HashMap<String, Integer>();
        for (var word : words) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1);
            } else {
                result.put(word, 1);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        assert wordFrequency(Arrays.asList("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));
        assert wordFrequency(Collections.emptyList()).equals(Map.of());
        assert wordFrequency(Arrays.asList("hello")).equals(Map.of("hello", 1));
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se renombro la clase de "P01WordFrequency_v8" a "P01WordFrequency_v17"
//    unicamente para reflejar la version objetivo del archivo; no afecta el
//    comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo "Map<String, Integer> result = new HashMap<>();" por
//    "var result = new HashMap<String, Integer>();", usando inferencia de
//    tipos locales (var). Es idiomatico en Java 17 para declaraciones donde
//    el tipo es obvio por el lado derecho de la asignacion, y no cambia el
//    tipo real de la variable (sigue siendo HashMap<String, Integer> en
//    tiempo de compilacion).
// 3. Se reemplazo "for (String word : words)" por "for (var word : words)"
//    por la misma razon: el tipo String es evidente porque "words" es
//    List<String>.
// 4. La logica interna (containsKey/get/put para contar ocurrencias) se
//    mantuvo exactamente igual, cumpliendo el requisito de comportamiento
//    identico; no existia ninguna caracteristica de Java 17 que mejorara
//    esta logica de conteo sin alterarla artificialmente (por ejemplo, no
//    aplica ni Stream API ni pattern matching, ya que el requisito de esta
//    migracion es preservar el paradigma imperativo original del codigo
//    base, sin introducir Streams/lambdas que no estaban en la version 8).
// 5. El nombre del metodo "wordFrequency" y los tres casos de prueba con
//    "assert" (incluido el uso de "Map.of(...)" para las comparaciones) se
//    conservaron sin modificaciones logicas, ya que ya eran compatibles con
//    Java 17 (Map.of proviene de Java 9 y assert es parte del lenguaje base
//    desde Java 1.4).