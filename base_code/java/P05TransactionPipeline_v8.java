// P05: Transaction Pipeline — Java 8 (codigo base para refactoring)
// Estilo: bucles for-each con flags y acumuladores mutables, compatible con Java 8
import java.util.*;

public class P05TransactionPipeline_v8 {

    /**
     * Procesa una lista de transacciones:
     *   1. Filtra: status == "approved" AND amount > 0
     *   2. Aplica comision del 10% (amount * 0.9)
     *   3. Retorna la suma total redondeada a 2 decimales
     */
    public static double processTransactions(List<Map<String, Object>> transactions) {
        double total = 0.0;
        for (Map<String, Object> tx : transactions) {
            String status = (String) tx.get("status");
            double amount = ((Number) tx.get("amount")).doubleValue();
            if ("approved".equals(status) && amount > 0) {
                total = total + amount * 0.9;
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    private static Map<String, Object> tx(int id, double amount, String status, String cat) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", id); m.put("amount", amount);
        m.put("status", status); m.put("category", cat);
        return m;
    }

    public static void main(String[] args) {
        // TC01: filtrar negativo y rechazado, aplicar fee
        List<Map<String, Object>> txs1 = Arrays.asList(
            tx(1, 100.0, "approved", "income"),
            tx(2, -50.0, "approved", "income"),
            tx(3, 200.0, "rejected", "income"),
            tx(4, 150.0, "approved", "expense")
        );
        assert Math.abs(processTransactions(txs1) - 225.0) < 0.01 : "TC01 fallo";

        // TC02: lista vacia
        assert processTransactions(Collections.emptyList()) == 0.0 : "TC02 fallo";

        // TC03: un approved
        List<Map<String, Object>> txs3 = Collections.singletonList(tx(1, 1000.0, "approved", "x"));
        assert Math.abs(processTransactions(txs3) - 900.0) < 0.01 : "TC03 fallo";

        System.out.println("All tests passed");
    }
}
