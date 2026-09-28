// P01: Word Frequency — Java 21 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (desde Java 10, JEP 286, vigente
//    y plenamente idiomatica en 21): usada en "result" dentro de
//    wordFrequency y en el elemento del for-each ("word"), ya que el tipo es
//    evidente por el contexto (new HashMap<>(), List<String>). No afecta el
//    tipado estatico, solo se infiere en compilacion.
// 2. Nota de honestidad: revise las novedades principales de Java 21 —
//    virtual threads (JEP 444), pattern matching for switch (JEP 441),
//    record patterns (JEP 440) y sequenced collections (JEP 431). Ninguna
//    aplica de forma natural y no artificial a este problema:
//    - Virtual threads: pensados para concurrencia de I/O; este codigo es
//      computo puro y secuencial sobre una lista en memoria, sin operaciones
//      bloqueantes que paralelizar.
//    - Pattern matching for switch / record patterns: utiles para
//      desestructurar jerarquias de tipos o sealed classes; aqui solo hay un
//      String y un contador Integer, sin jerarquia que inspeccionar.
//    - Sequenced collections (SequencedMap, getFirst()/getLast(), etc.):
//      HashMap no es una SequencedCollection (no tiene orden definido), y el
//      problema no requiere acceder al primer/ultimo elemento insertado.
//    Forzar cualquiera de estas caracteristicas anadiria complejidad
//    artificial sin mejorar la solucion. El uso de "Map.of(...)" en los
//    tests ya provenia del codigo original en Java 8 (introducido en Java 9,
//    JEP 269) y se conserva porque sigue siendo la forma idiomatica de crear
//    mapas inmutables para comparaciones en pruebas.

import java.util.*;

public class P01WordFrequency_v21 {

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
// 1. Se renombro la clase de "P01WordFrequency_v8" a "P01WordFrequency_v21"
//    unicamente para reflejar la version objetivo del archivo; no afecta el
//    comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo "Map<String, Integer> result = new HashMap<>();" por
//    "var result = new HashMap<String, Integer>();", usando inferencia de
//    tipos locales. Es idiomatico en Java 21 cuando el tipo es obvio por el
//    lado derecho de la asignacion; el tipo real sigue siendo
//    HashMap<String, Integer> en tiempo de compilacion, por lo que no hay
//    perdida de seguridad de tipos.
// 3. Se reemplazo "for (String word : words)" por "for (var word : words)"
//    por la misma razon: el tipo String es evidente porque "words" es
//    List<String>.
// 4. La logica interna (containsKey/get/put para contar ocurrencias) se
//    mantuvo exactamente igual, cumpliendo el requisito de comportamiento
//    identico. No existia ninguna caracteristica de Java 21 (virtual
//    threads, pattern matching, record patterns, sequenced collections) que
//    mejorara esta logica de conteo simple sin alterarla artificialmente o
//    sin contradecir el paradigma imperativo del codigo base original.
// 5. El nombre del metodo "wordFrequency" y los tres casos de prueba con
//    "assert" (incluido "Map.of(...)" para las comparaciones) se
//    conservaron sin modificaciones logicas, ya que ya eran compatibles con
//    Java 21 (Map.of es de Java 9; assert es parte del lenguaje base desde
//    Java 1.4).