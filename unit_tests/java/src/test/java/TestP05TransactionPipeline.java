import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Unit Tests - P05: Transaction Pipeline
 * La IA debe implementar:
 *   public static double processTransactions(List<Map<String,Object>> transactions)
 *
 * Reglas:
 *   - Filtrar: status == "approved" AND amount > 0
 *   - Aplicar: fee 10% (amount * 0.9)
 *   - Retornar: suma total redondeada a 2 decimales
 */
class TestP05TransactionPipeline {

    private Map<String,Object> tx(int id, double amount, String status, String cat) {
        Map<String,Object> m = new HashMap<>();
        m.put("id",id); m.put("amount",amount); m.put("status",status); m.put("category",cat);
        return m;
    }

    @Test @DisplayName("TC01 - Filtrar y aplicar fee")
    void tc01() {
        List<Map<String,Object>> txs = List.of(
            tx(1,100.0,"approved","income"),
            tx(2,-50.0,"approved","income"),
            tx(3,200.0,"rejected","income"),
            tx(4,150.0,"approved","expense")
        );
        assertEquals(225.0, Solution.processTransactions(txs), 0.01);
    }

    @Test @DisplayName("TC02 - Lista vacia")
    void tc02() { assertEquals(0.0, Solution.processTransactions(List.of()), 0.01); }

    @Test @DisplayName("TC03 - Todos rechazados")
    void tc03() {
        assertEquals(0.0, Solution.processTransactions(List.of(tx(1,200.0,"rejected","income"))), 0.01);
    }

    @Test @DisplayName("TC04 - Un approved: 1000 * 0.9 = 900")
    void tc04() {
        assertEquals(900.0, Solution.processTransactions(List.of(tx(1,1000.0,"approved","income"))), 0.01);
    }

    @Test @DisplayName("TC05 - Dos approved: (500+300)*0.9 = 720")
    void tc05() {
        List<Map<String,Object>> txs = List.of(
            tx(1,500.0,"approved","income"),
            tx(2,300.0,"approved","expense")
        );
        assertEquals(720.0, Solution.processTransactions(txs), 0.01);
    }
}
