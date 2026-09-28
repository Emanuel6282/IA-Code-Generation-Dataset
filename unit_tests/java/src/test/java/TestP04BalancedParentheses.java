import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests - P04: Balanced Parentheses
 * La IA debe implementar: public static boolean isBalanced(String s)
 */
class TestP04BalancedParentheses {

    @Test @DisplayName("TC01 - Parentesis simple balanceado")
    void tc01() { assertTrue(Solution.isBalanced("()")); }

    @Test @DisplayName("TC02 - Multiples tipos balanceados")
    void tc02() { assertTrue(Solution.isBalanced("()[]{}")); }

    @Test @DisplayName("TC03 - Tipos mezclados mal cerrados")
    void tc03() { assertFalse(Solution.isBalanced("(]")); }

    @Test @DisplayName("TC04 - Intercalados")
    void tc04() { assertFalse(Solution.isBalanced("([)]")); }

    @Test @DisplayName("TC05 - Anidado balanceado")
    void tc05() { assertTrue(Solution.isBalanced("{[]}")); }

    @Test @DisplayName("TC06 - Cadena vacia")
    void tc06() { assertTrue(Solution.isBalanced("")); }

    @Test @DisplayName("TC07 - Sin cerrar")
    void tc07() { assertFalse(Solution.isBalanced("(")); }

    @Test @DisplayName("TC08 - Profundamente anidado igual tipo")
    void tc08() { assertTrue(Solution.isBalanced("((()))")); }
}
