import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Unit Tests - P03: Flatten Nested List
 * La IA debe implementar: public static List<Integer> flatten(Object nested)
 * donde nested puede ser Integer o List<?> anidada.
 */
class TestP03FlattenList {

    @Test @DisplayName("TC01 - Anidamiento basico")
    void tc01() {
        // [1,[2,3],[4,[5,6]]]
        List<Object> input = List.of(1, List.of(2,3), List.of(4, List.of(5,6)));
        assertEquals(List.of(1,2,3,4,5,6), Solution.flatten(input));
    }

    @Test @DisplayName("TC02 - Lista vacia")
    void tc02() {
        assertEquals(List.of(), Solution.flatten(List.of()));
    }

    @Test @DisplayName("TC03 - Ya plana")
    void tc03() {
        assertEquals(List.of(1,2,3), Solution.flatten(List.of(1,2,3)));
    }

    @Test @DisplayName("TC04 - Profundamente anidada")
    void tc04() {
        List<Object> input = List.of(List.of(1, List.of(2, List.of(3, List.of(4)))));
        assertEquals(List.of(1,2,3,4), Solution.flatten(input));
    }

    @Test @DisplayName("TC05 - Lista anidada vacia")
    void tc05() {
        assertEquals(List.of(), Solution.flatten(List.of(List.of())));
    }
}
