import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;

public class WordFrequency {

    /**
     * Dada una lista de palabras (strings), retorna un mapa que asocia
     * cada palabra unica con el numero de veces que aparece en la lista.
     *
     * @param words Lista de strings.
     * @return Un Map<String, Integer> con la frecuencia de cada palabra.
     *         Si la lista de entrada esta vacia, retorna un mapa vacio.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> result = new HashMap<>();

        for (String word : words) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1);
            } else {
                result.put(word, 1);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado
        Map<String, Integer> caso1 = wordFrequency(Arrays.asList("the", "fox", "the"));
        Map<String, Integer> esperado1 = new HashMap<>();
        esperado1.put("the", 2);
        esperado1.put("fox", 1);
        assert caso1.equals(esperado1) : "Caso 1 fallido: " + caso1;

        // Caso 2: lista vacia -> mapa vacio
        Map<String, Integer> caso2 = wordFrequency(Collections.emptyList());
        assert caso2.isEmpty() : "Caso 2 fallido: " + caso2;

        // Caso 3: todas las palabras distintas
        Map<String, Integer> caso3 = wordFrequency(Arrays.asList("a", "b", "c"));
        Map<String, Integer> esperado3 = new HashMap<>();
        esperado3.put("a", 1);
        esperado3.put("b", 1);
        esperado3.put("c", 1);
        assert caso3.equals(esperado3) : "Caso 3 fallido: " + caso3;

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}