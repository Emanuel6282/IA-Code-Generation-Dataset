import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Unit Tests - P01: Word Frequency
 * Instrucciones: el codigo de la IA debe implementar el metodo:
 *   public static Map<String, Integer> wordFrequency(List<String> words)
 */
class TestP01WordFrequency {

    @Test @DisplayName("TC01 - Palabra repetida basica")
    void tc01_basic() {
        Map<String,Integer> expected = Map.of("the",2,"fox",1);
        assertEquals(expected, Solution.wordFrequency(List.of("the","fox","the")));
    }

    @Test @DisplayName("TC02 - Lista vacia")
    void tc02_empty() {
        assertEquals(Map.of(), Solution.wordFrequency(List.of()));
    }

    @Test @DisplayName("TC03 - Una sola palabra")
    void tc03_single() {
        assertEquals(Map.of("hello",1), Solution.wordFrequency(List.of("hello")));
    }

    @Test @DisplayName("TC04 - Multiples frecuencias")
    void tc04_multi() {
        Map<String,Integer> expected = Map.of("a",3,"b",2,"c",1);
        assertEquals(expected, Solution.wordFrequency(List.of("a","b","a","c","b","a")));
    }

    @Test @DisplayName("TC05 - Tres palabras distintas")
    void tc05_three() {
        Map<String,Integer> expected = Map.of("cat",3,"dog",2,"bird",1);
        assertEquals(expected, Solution.wordFrequency(List.of("cat","dog","cat","bird","dog","cat")));
    }
}
