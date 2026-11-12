// CARACTERISTICAS DE JAVA 21 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Record Patterns (JEP 440): Características consolidadas en Java 21 que permiten desestructurar el record directamente en sus componentes (id, amount, status, category) dentro de la declaración, evitando invocar manualmente los métodos de acceso durante la iteración.
// 2. Pattern Matching para switch con Guardas 'when' (JEP 441): En Java 21, el clásico 'switch' iterativo evoluciona drásticamente. Se utiliza la cláusula 'when' para inyectar la validación lógica (status aprobado y monto positivo) directamente en el enrutamiento. Esto fusiona la extracción de variables y las reglas de negocio en una sola estructura nativa altamente optimizada.
// 3. Paradigma Iterativo Estricto: Ausencia total de la API de Streams. El procesamiento se orquesta a través de un bucle 'for' mejorado y una variable mutable ('total') que acumula el estado, donde el switch avanzado de Java 21 actúa como el motor de evaluación imperativa.

import java.util.List;
import java.util.ArrayList;

// Estructura inmutable nativa sin boilerplate
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v21_Iterative {

    /**
     * Procesa una lista de transacciones usando características avanzadas de Java 21
     * dentro de un enfoque estrictamente iterativo.
     */
    public static double processTransactions(List<Transaction> transactions) {
        if (transactions == null) {
            return 0.0;
        }

        // Estado mutable acumulador
        double total = 0.0;
        
        // Iteración imperativa explícita
        for (Transaction tx : transactions) {
            
            // Java 21: Pattern Matching for switch con Record Patterns y guardas (when)
            switch (tx) {
                // Desestructuración del Record y evaluación condicional en un solo paso
                case Transaction(int id, double amount, String status, String category)
                     when "approved".equals(status) && amount > 0 -> {
                    
                    // Mutación iterativa del estado
                    total += amount * 0.9;
                }
                // Manejo seguro: captura nulos explícitamente y transacciones que no pasan la guarda
                case null, default -> {
                    // La iteración continúa silenciosamente
                }
            }
        }
        
        // Retorno aplicando el redondeo matemático a 2 decimales
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: filtrado correcto (descarta rejected) y aplicación de comisión
        var test1 = List.of(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        );
        assert processTransactions(test1) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista nula, lista vacía y montos negativos/nulos
        assert processTransactions(null) == 0.0 : "Error: Fallo en manejo de lista nula";
        assert processTransactions(List.of()) == 0.0 : "Error: Lista vacía esperado 0.0";
        
        var test2 = List.of(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        );
        assert processTransactions(test2) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión matemática y acumulación múltiple iterativa
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma acumulada iterativamente = 27.594 -> Redondeo = 27.59
        var test3 = List.of(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        );
        assert processTransactions(test3) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}