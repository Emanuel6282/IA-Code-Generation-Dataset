import java.util.*;

public class TransactionPipeline {

public static double processTransactions(List<Map<String, Object>> transactions) {
    double total = 0.0;

    for (Map<String, Object> transaction : transactions) {
        String status = (String) transaction.get("status");
        double amount = ((Number) transaction.get("amount")).doubleValue();

        if ("approved".equals(status) && amount > 0) {
            total += amount * 0.9;
        }
    }

    return Math.round(total * 100.0) / 100.0;
}

public static void main(String[] args) {
    assert processTransactions(Collections.emptyList()) == 0.0;

    assert processTransactions(Arrays.asList(
        new HashMap<String, Object>() {{
            put("id", 1);
            put("amount", 100.0);
            put("status", "approved");
            put("category", "food");
        }},
        new HashMap<String, Object>() {{
            put("id", 2);
            put("amount", 200.0);
            put("status", "rejected");
            put("category", "shopping");
        }}
    )) == 90.0;

    assert processTransactions(Arrays.asList(
        new HashMap<String, Object>() {{
            put("id", 1);
            put("amount", -50.0);
            put("status", "approved");
            put("category", "food");
        }},
        new HashMap<String, Object>() {{
            put("id", 2);
            put("amount", 100.0);
            put("status", "rejected");
            put("category", "transport");
        }}
    )) == 0.0;

    System.out.println("All tests passed");
}
}
