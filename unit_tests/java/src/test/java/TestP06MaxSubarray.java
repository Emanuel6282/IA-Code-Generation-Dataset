import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests - P06: Maximum Subarray Sum (Kadane)
 * La IA debe implementar: public static int maxSubarraySum(int[] arr)
 */
class TestP06MaxSubarray {

    @Test @DisplayName("TC01 - Kadane clasico")
    void tc01() { assertEquals(6, Solution.maxSubarraySum(new int[]{-2,1,-3,4,-1,2,1,-5,4})); }

    @Test @DisplayName("TC02 - Un elemento positivo")
    void tc02() { assertEquals(1, Solution.maxSubarraySum(new int[]{1})); }

    @Test @DisplayName("TC03 - Un elemento negativo")
    void tc03() { assertEquals(-1, Solution.maxSubarraySum(new int[]{-1})); }

    @Test @DisplayName("TC04 - Mayormente positivos")
    void tc04() { assertEquals(23, Solution.maxSubarraySum(new int[]{5,4,-1,7,8})); }

    @Test @DisplayName("TC05 - Todos negativos")
    void tc05() { assertEquals(-1, Solution.maxSubarraySum(new int[]{-2,-1})); }

    @Test @DisplayName("TC06 - Todos positivos")
    void tc06() { assertEquals(15, Solution.maxSubarraySum(new int[]{1,2,3,4,5})); }

    @Test @DisplayName("TC07 - Subarray en el medio")
    void tc07() { assertEquals(5, Solution.maxSubarraySum(new int[]{-3,2,-1,4,-2})); }
}
