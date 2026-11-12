// CARACTERISTICAS DE JAVA 8 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Imperativo/Iterativo Clásico: En cumplimiento estricto con los requisitos, se ha evitado por completo la API de Streams y las expresiones Lambda (que son las principales novedades funcionales de Java 8). Toda la lógica recae en construcciones imperativas tradicionales.
// 2. Acumulación de Estado Mutable: Se define una variable local primitiva (`total`) que muta en cada iteración válida, reemplazando cualquier tipo de reducción funcional (`reduce` o `collect`).
// 3. Flujo de Control Explícito: Se emplea un bucle 'for' mejorado (enhanced for-loop) para recorrer la estructura de datos, evaluando las reglas de negocio con un bloque `if` tradicional y comprobación segura de cadenas (evitando NullPointerException al llamar a `equals` sobre la constante "approved").

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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

public class P05TransactionPipeline_v8_Iterative {

    /**
     * Procesa una lista de transacciones filtrando las aprobadas y con monto positivo,
     * aplica una comisión del 10% y retorna la suma total redondeada a 2 decimales.
     */
    public static double processTransactions(List<Transaction> transactions) {
        // Estado mutable acumulador
        double total = 0.0;
        
        // Iteración explícita y manual
        for (Transaction tx : transactions) {
            // 1. Filtrar: status == "approved" Y amount > 0
            if ("approved".equals(tx.status) && tx.amount > 0) {
                // 2. Aplicar: comisión del 10% y acumulación iterativa
                total += tx.amount * 0.9;
            }
        }
        
        // 3. Retornar: suma total redondeada a 2 decimales usando aritmética estándar
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
        assert processTransactions(Arrays.asList(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        )) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista vacía y montos negativos/nulos
        assert processTransactions(new ArrayList<>()) == 0.0 : "Error: Lista vacía esperado 0.0";
        assert processTransactions(Arrays.asList(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        )) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión y acumulación múltiple
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma iterativa acumulada = 27.594 -> Redondeo = 27.59
        assert processTransactions(Arrays.asList(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        )) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}