// CARACTERISTICAS DE JAVA 17 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Records (JEP 395): Se utiliza `record` para definir la estructura de datos `Transaction`. Es la forma estándar en Java 14+ para crear clases portadoras de datos inmutables y transparentes, eliminando todo el boilerplate (constructores, getters, equals, hashCode) de las clases tradicionales (POJOs).
// 2. Local Variable Type Inference (var - JEP 286): Introducido en Java 10 y consolidado en versiones posteriores, reduce la verbosidad al declarar variables locales y en bucles for-each, permitiendo que el compilador infiera el tipo automáticamente.
// 3. Paradigma Iterativo Estricto: Se evita por completo la API de Streams. Se controla el estado mediante una variable mutable (`total`) acumulando resultados a través de un bucle for-each tradicional (mejorado visualmente por `var`).

import java.util.List;
import java.util.ArrayList;

// Java 17: Uso de Record para definir la estructura de datos sin boilerplate
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v17_Iterative {

    /**
     * Procesa una lista de transacciones filtrando las aprobadas y con monto positivo,
     * aplica una comisión del 10% y retorna la suma total redondeada a 2 decimales.
     */
    public static double processTransactions(List<Transaction> transactions) {
        if (transactions == null) {
            return 0.0;
        }

        // Estado mutable acumulador
        var total = 0.0; 
        
        // Iteración explícita usando inferencia de tipos local (var)
        for (var tx : transactions) {
            // En los records, el acceso a los campos se hace mediante métodos con el mismo nombre del campo (ej. status(), amount())
            if ("approved".equals(tx.status()) && tx.amount() > 0) {
                total += tx.amount() * 0.9;
            }
        }
        
        // Retornar suma total redondeada a 2 decimales
        return Math.round(total * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
        var test1 = List.of(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        );
        assert processTransactions(test1) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista vacía y montos negativos/nulos
        assert processTransactions(new ArrayList<>()) == 0.0 : "Error: Lista vacía esperado 0.0";
        
        var test2 = List.of(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        );
        assert processTransactions(test2) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión y acumulación múltiple
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma iterativa acumulada = 27.594 -> Redondeo = 27.59
        var test3 = List.of(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        );
        assert processTransactions(test3) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests iterativos en Java 17 pasaron correctamente.");
    }
}