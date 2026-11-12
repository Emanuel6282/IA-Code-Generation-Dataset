import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

// Definición de la estructura de la transacción
class Transaction {
    int id;
    double amount;
    String status;
    String category;

    public Transaction(int id, double amount, String status, String category) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.category = category;
    }
}

public class TransactionPipeline {

    /**
     * Procesa una lista de transacciones filtrando las aprobadas y con monto positivo,
     * aplica una comisión del 10% y retorna la suma total redondeada a 2 decimales.
     */
    public static double processTransactions(List<Transaction> transactions) {
        double total = 0.0;
        
        for (Transaction tx : transactions) {
            // 1. Filtrar: status == "approved" Y amount > 0
            if ("approved".equals(tx.status) && tx.amount > 0) {
                // 2. Aplicar: comisión del 10% (multiplicar por 0.9)
                total += tx.amount * 0.9;
            }
        }
        
        // 3. Retornar: suma total redondeada a 2 decimales
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
        assert processTransactions(Arrays.asList(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        )) == 90.0 : "Error en el caso base";

        // 2. Casos límite: lista vacía y montos negativos/nulos
        assert processTransactions(new ArrayList<>()) == 0.0 : "Error en lista vacía";
        assert processTransactions(Arrays.asList(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        )) == 0.0 : "Error en montos negativos o cero";

        // 3. Caso de precisión matemática y acumulación múltiple
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma acumulada = 27.594 -> Redondeo a 2 decimales = 27.59
        assert processTransactions(Arrays.asList(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        )) == 27.59 : "Error en el cálculo de precisión y redondeo";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}