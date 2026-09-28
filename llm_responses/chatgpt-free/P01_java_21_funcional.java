import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WordFrequency {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        return Optional.ofNullable(words)
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors.groupingBy(
                        word -> word,
                        HashMap::new,
                        Collectors.summingInt(word -> 1)
                ));
    }

    public static void main(String[] args) {

        // Caso de prueba 1
        List<String> test1 = new ArrayList<>();
        test1.add("the");
        test1.add("fox");
        test1.add("the");

        Map<String, Integer> expected1 = new HashMap<>();
        expected1.put("the", 2);
        expected1.put("fox", 1);

        assert wordFrequency(test1).equals(expected1);

        // Caso de prueba 2: lista vacía
        List<String> test2 = new ArrayList<>();

        assert wordFrequency(test2).equals(new HashMap<>());

        // Caso de prueba 3: una sola palabra
        List<String> test3 = new ArrayList<>();
        test3.add("hello");

        Map<String, Integer> expected3 = new HashMap<>();
        expected3.put("hello", 1);

        assert wordFrequency(test3).equals(expected3);

        System.out.println("Todos los tests pasaron correctamente.");
    }
}