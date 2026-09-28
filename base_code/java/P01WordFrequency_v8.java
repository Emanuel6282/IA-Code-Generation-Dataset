// P01: Word Frequency — Java 8 (codigo base para refactoring)
import java.util.*;

public class P01WordFrequency_v8 {

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
        assert wordFrequency(Arrays.asList("the","fox","the")).equals(Map.of("the",2,"fox",1));
        assert wordFrequency(Collections.emptyList()).equals(Map.of());
        assert wordFrequency(Arrays.asList("hello")).equals(Map.of("hello",1));
        System.out.println("All tests passed");
    }
}
