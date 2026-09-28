import java.util.ArrayList;
import java.util.List;

public class TransactionPipeline {

    /**
     * Representa una transaccion financiera con sus cuatro campos.
     * Se modela como una clase simple (POJO) ya que Java es un lenguaje de
     * tipado estatico y no tiene un equivalente directo al diccionario
     * dinamico usado en Python.
     */
    public static class Transaction {
        public final int id;
        public final double amount;
        public final String status;
        public final String category;

        public Transaction(int id, double amount, String status, String category) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.category = category;
        }
    }

    /**
     * Procesa una lista de transacciones financieras: filtra las aprobadas
     * con monto positivo, aplica una comision del 10%, y retorna la suma
     * total resultante redondeada a 2 decimales.
     *
     * Pasos:
     * 1. Filtrar: mantener solo transacciones con status == "approved" Y
     *    amount > 0.
     * 2. Aplicar: una comision del 10% (multiplicar cada amount por 0.9).
     * 3. Retornar: la suma total de los amounts resultantes, redondeada a
     *    2 decimales.
     *
     * @param transactions Lista de transacciones a procesar.
     * @return La suma total de los montos aprobados y positivos, despues
     *         de aplicar la comision del 10%, redondeada a 2 decimales.
     *         Si la lista esta vacia o no hay transacciones validas,
     *         retorna 0.0.
     */
    public static double processTransactions(List<Transaction> transactions) {
        double total = 0.0;

        for (Transaction t : transactions) {
            if ("approved".equals(t.status) && t.amount > 0) {
                total += t.amount * 0.9;
            }
        }

        // Redondeo a 2 decimales, evitando problemas de precision binaria
        // de punto flotante al multiplicar y luego dividir por 100.
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, solo una transaccion valida
        List<Transaction> transacciones1 = new ArrayList<>();
        transacciones1.add(new Transaction(1, 100.0, "approved", "sales"));
        transacciones1.add(new Transaction(2, 200.0, "rejected", "sales"));
        assert processTransactions(transacciones1) == 90.0 : "Caso 1 fallido";

        // Caso 2: lista vacia -> 0.0
        List<Transaction> transacciones2 = new ArrayList<>();
        assert processTransactions(transacciones2) == 0.0 : "Caso 2 fallido";

        // Caso 3: varias transacciones validas, incluyendo un monto negativo
        // (que debe excluirse) y una rechazada (que tambien debe excluirse)
        List<Transaction> transacciones3 = new ArrayList<>();
        transacciones3.add(new Transaction(1, 50.0, "approved", "food"));
        transacciones3.add(new Transaction(2, -20.0, "approved", "food"));
        transacciones3.add(new Transaction(3, 30.0, "rejected", "food"));
        transacciones3.add(new Transaction(4, 25.5, "approved", "travel"));
        // (50.0 * 0.9) + (25.5 * 0.9) = 45.0 + 22.95 = 67.95
        assert processTransactions(transacciones3) == 67.95 : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}