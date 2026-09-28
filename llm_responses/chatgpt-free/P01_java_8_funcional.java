import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequency {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Optional<List<String>> optionalWords = Optional.ofNullable(words);

        return optionalWords
                .orElse(new ArrayList<String>())
                .stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(word -> 1)
                ));
    }

    public static void main(String[] args) {
        // Caso de prueba 1
        List<String> test1 = Arrays.asList("the", "fox", "the");

        assert wordFrequency(test1).equals(
                createMap("the", 2, "fox", 1)
        );

        // Caso de prueba 2: lista vacía
        List<String> test2 = new ArrayList<String>();

        assert wordFrequency(test2).equals(
                new HashMap<String, Integer>()
        );

        // Caso de prueba 3
        List<String> test3 = Arrays.asList("hello");

        assert wordFrequency(test3).equals(
                createMap("hello", 1)
        );

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