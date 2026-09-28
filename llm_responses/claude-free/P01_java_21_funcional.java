import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class WordFrequency {

    /**
     * Dada una lista de palabras (strings), retorna un mapa que asocia
     * cada palabra unica con el numero de veces que aparece en la lista.
     *
     * Implementacion estrictamente funcional: usa la Stream API con lambdas
     * (Collectors.toMap con funcion de fusion) en lugar de bucles for/while
     * imperativos. Solo biblioteca estandar de Java 21.
     *
     * @param words Lista de strings.
     * @return Un Map<String, Integer> con la frecuencia de cada palabra.
     *         Si la lista de entrada esta vacia, retorna un mapa vacio.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        return words.stream()
                .collect(Collectors.toMap(
                        word -> word,                 // key mapper (lambda)
                        word -> 1,                     // value mapper (lambda): cada ocurrencia vale 1
                        Integer::sum,                   // merge function: suma en colisiones (method reference)
                        HashMap::new                    // supplier del mapa resultante
                ));
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado
        var caso1 = wordFrequency(Arrays.asList("the", "fox", "the"));
        var esperado1 = new HashMap<String, Integer>();
        esperado1.put("the", 2);
        esperado1.put("fox", 1);
        assert caso1.equals(esperado1) : "Caso 1 fallido: " + caso1;
        // Uso de Optional para validar de forma segura una clave especifica
        int frecuenciaThe = Optional.ofNullable(caso1.get("the")).orElse(0);
        assert frecuenciaThe == 2 : "Caso 1 (Optional) fallido: " + frecuenciaThe;

        // Caso 2: lista vacia -> mapa vacio
        var caso2 = wordFrequency(Collections.<String>emptyList());
        assert caso2.isEmpty() : "Caso 2 fallido: " + caso2;

        // Caso 3: todas las palabras distintas
        var caso3 = wordFrequency(Arrays.asList("a", "b", "c"));
        var esperado3 = new HashMap<String, Integer>();
        esperado3.put("a", 1);
        esperado3.put("b", 1);
        esperado3.put("c", 1);
        assert caso3.equals(esperado3) : "Caso 3 fallido: " + caso3;

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}