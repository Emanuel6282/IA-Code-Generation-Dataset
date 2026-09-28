import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequency = new HashMap<String, Integer>();

        for (String word : words) {
            if (frequency.containsKey(word)) {
                frequency.put(word, frequency.get(word) + 1);
            } else {
                frequency.put(word, 1);
            }
        }

        return frequency;
    }

    public static void main(String[] args) {
        // Caso de prueba 1
        List<String> test1 = new ArrayList<String>();
        test1.add("the");
        test1.add("fox");
        test1.add("the");

        assert wordFrequency(test1).equals(createMap("the", 2, "fox", 1));

        // Caso de prueba 2: lista vacía
        List<String> test2 = new ArrayList<String>();

        assert wordFrequency(test2).equals(new HashMap<String, Integer>());

        // Caso de prueba 3
        List<String> test3 = new ArrayList<String>();
        test3.add("hello");

        assert wordFrequency(test3).equals(createMap("hello", 1));

        System.out.println("Todos los tests pasaron correctamente.");
    }

    private static Map<String, Integer> createMap(
            String word1, int count1,
            String word2, int count2) {

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(word1, count1);
        map.put(word2, count2);

        return map;
    }

    private static Map<String, Integer> createMap(
            String word, int count) {

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(word, count);

        return map;
    }
}