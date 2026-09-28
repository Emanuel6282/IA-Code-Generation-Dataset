import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionPipeline {

    /**
     * Representa una transaccion financiera con sus cuatro campos.
     *
     * Se modela como un "record" (JEP 395, Java 16, vigente en 17): genera
     * automaticamente el constructor, los accesores (id(), amount(),
     * status(), category()), equals(), hashCode() y toString(), la forma
     * idiomatica y concisa en Java moderno para un portador de datos
     * inmutable como este.
     */
    public record Transaction(int id, double amount, String status, String category) {
    }

    /**
     * Procesa una lista de transacciones financieras: filtra las aprobadas
     * con monto positivo, aplica una comision del 10%, y retorna la suma
     * total resultante redondeada a 2 decimales.
     *
     * Implementacion en paradigma funcional: usa la Stream API con
     * filter() para el filtrado, map() con una lambda para aplicar la
     * comision, y reduce() para la suma final, expuesta como
     * Optional<Double> para manejar explicitamente el caso de ausencia de
     * transacciones validas. No se usan bucles for/while imperativos.
     * Compatible con Java 17.
     *
     * @param transactions Lista de transacciones a procesar.
     * @return La suma total de los montos aprobados y positivos, despues
     *         de aplicar la comision del 10%, redondeada a 2 decimales.
     *         Si la lista esta vacia o no hay transacciones validas,
     *         retorna 0.0.
     */
    public static double processTransactions(List<Transaction> transactions) {
        Optional<Double> totalOpt = transactions.stream()
                .filter(t -> "approved".equals(t.status()) && t.amount() > 0)
                .map(t -> t.amount() * 0.9)
                .reduce(Double::sum);

        var total = totalOpt.orElse(0.0);

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

        // Uso adicional genuino de Optional: verificar de forma segura la
        // ausencia de transacciones validas en una lista vacia
        var resultadoVacioOpt = transacciones2.stream()
                .filter(t -> "approved".equals(t.status()) && t.amount() > 0)
                .map(t -> t.amount() * 0.9)
                .reduce(Double::sum);
        assert !resultadoVacioOpt.isPresent() : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}