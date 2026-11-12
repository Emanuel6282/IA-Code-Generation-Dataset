import java.util.ArrayList;
import java.util.List;

public class TransactionPipeline {

    // Representa una transacción financiera.
    static class Transaction {
        int id;
        double amount;
        String status;
        String category;

        Transaction(int id, double amount, String status, String category) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.category = category;
        }
    }

    // Procesa las transacciones y devuelve el total neto.
    public static double processTransactions(List<Transaction> transactions) {

        double total = 0.0;

        // Recorrer todas las transacciones.
        for (Transaction transaction : transactions) {

            // Filtrar las aprobadas con importe positivo.
            if ("approved".equals(transaction.status)
                    && transaction.amount > 0) {

                // Aplicar una comisión del 10 %.
                double netAmount = transaction.amount * 0.9;

                // Acumular el importe neto.
                total += netAmount;
            }
        }

        // Redondear el resultado a dos decimales.
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {

        // Test 1: Una transacción aprobada y otra rechazada.
        List<Transaction> transactions1 = new ArrayList<>();
        transactions1.add(new Transaction(1, 100.0, "approved", "sales"));
        transactions1.add(new Transaction(2, 200.0, "rejected", "sales"));

        assert processTransactions(transactions1) == 90.0;

        // Test 2: Dos aprobadas y una con importe negativo.
        List<Transaction> transactions2 = new ArrayList<>();
        transactions2.add(new Transaction(1, 100.0, "approved", "sales"));
        transactions2.add(new Transaction(2, 50.0, "approved", "services"));
        transactions2.add(new Transaction(3, -20.0, "approved", "refund"));

        assert processTransactions(transactions2) == 135.0;

        // Test 3: Lista vacía.
        List<Transaction> transactions3 = new ArrayList<>();

        assert processTransactions(transactions3) == 0.0;

        System.out.println("All tests passed");
    }
}