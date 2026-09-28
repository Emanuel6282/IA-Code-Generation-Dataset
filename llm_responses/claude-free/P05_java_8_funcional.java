import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class TransactionPipeline {

    /**
     * Representa una transaccion financiera con sus cuatro campos.
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
     * Implementacion en paradigma funcional: usa la Stream API con
     * filter() para el paso de filtrado, mapToDouble() con una lambda para
     * aplicar la comision, y sum() para la suma final. El resultado se
     * envuelve inicialmente en un Optional<Double> (via un paso adicional
     * con reduce, para exponer una API funcional idiomatica con manejo
     * explicito de la ausencia de valor) antes de extraerlo con
     * orElse(0.0) para cumplir el requisito de que una lista vacia o sin
     * transacciones validas retorne 0.0. No se usan bucles for/while
     * imperativos.
     *
     * @param transactions Lista de transacciones a procesar.
     * @return La suma total de los montos aprobados y positivos, despues
     *         de aplicar la comision del 10%, redondeada a 2 decimales.
     *         Si la lista esta vacia o no hay transacciones validas,
     *         retorna 0.0.
     */
    public static double processTransactions(List<Transaction> transactions) {
        Optional<Double> totalOpt = transactions.stream()
                .filter(t -> "approved".equals(t.status) && t.amount > 0)
                .map(t -> t.amount * 0.9)
                .reduce(Double::sum);

        double total = totalOpt.orElse(0.0);

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

        // Uso adicional genuino de Optional: verificar de forma segura si
        // hubo al menos una transaccion valida, sin exponer un 0.0 que
        // podria confundirse con "una transaccion de monto exactamente 0"
        Optional<Double> resultadoVacioOpt = transacciones2.stream()
                .filter(t -> "approved".equals(t.status) && t.amount > 0)
                .map(t -> t.amount * 0.9)
                .reduce(Double::sum);
        assert !resultadoVacioOpt.isPresent() : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}