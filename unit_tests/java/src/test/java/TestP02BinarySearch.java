import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests - P02: Binary Search
 * La IA debe implementar: public static int binarySearch(int[] arr, int target)
 */
class TestP02BinarySearch {

    @Test @DisplayName("TC01 - Target en el medio")
    void tc01() { assertEquals(2, Solution.binarySearch(new int[]{1,3,5,7,9}, 5)); }

    @Test @DisplayName("TC02 - Target al inicio")
    void tc02() { assertEquals(0, Solution.binarySearch(new int[]{1,3,5,7,9}, 1)); }

    @Test @DisplayName("TC03 - Target al final")
    void tc03() { assertEquals(4, Solution.binarySearch(new int[]{1,3,5,7,9}, 9)); }

    @Test @DisplayName("TC04 - Target no encontrado")
    void tc04() { assertEquals(-1, Solution.binarySearch(new int[]{1,3,5,7,9}, 4)); }

    @Test @DisplayName("TC05 - Array vacio")
    void tc05() { assertEquals(-1, Solution.binarySearch(new int[]{}, 1)); }

    @Test @DisplayName("TC06 - Un solo elemento encontrado")
    void tc06() { assertEquals(0, Solution.binarySearch(new int[]{1}, 1)); }

    @Test @DisplayName("TC07 - Array de longitud par")
    void tc07() { assertEquals(4, Solution.binarySearch(new int[]{2,4,6,8,10,12}, 10)); }
}
