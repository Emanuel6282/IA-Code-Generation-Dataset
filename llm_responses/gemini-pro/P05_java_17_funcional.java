// CARACTERISTICAS DE JAVA 17 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Records (JEP 395): Se utiliza `record` para definir la estructura de la transacción. Es la culminación del diseño orientado a datos inmutables en Java. Al carecer de 'setters', fuerza naturalmente el diseño libre de efectos secundarios requerido por el paradigma funcional.
// 2. Local Variable Type Inference (var): Utilizado en los casos de prueba para inferir `List<Transaction>`, manteniendo el código limpio y enfocado en los datos.
// 3. Optional.orElseGet con List.of() (Java 9+): Se combina la mónada `Optional` con la fábrica de colecciones inmutables `List.of()` para manejar las listas nulas de forma funcional sin condicionales imperativos (if/else).
// 4. Stream API Puro: Transformación mediante un pipeline perezoso (`filter`, `map`, `reduce`). Se evita `mapToDouble().sum()` para poder utilizar explícitamente `reduce(Double::sum)`, el cual retorna un `Optional<Double>`, permitiendo encadenar el redondeo como una operación monádica segura (`map`) y extraer el valor por defecto puro con `orElse`.

import java.util.List;
import java.util.Optional;

// Java 17: Estructura de datos inmutable nativa (Record)
record Transaction(int id, double amount, String status, String category) {}

public class P05TransactionPipeline_v17_Functional {

    /**
     * Procesa una lista de transacciones usando un pipeline funcional estricto en Java 17.
     */
    public static double processTransactions(List<Transaction> transactions) {
        // Envolvemos la entrada en un Optional para programación defensiva pura
        return Optional.ofNullable(transactions)
            // Si es null, proveemos una lista inmutable vacía (List.of() es de Java 9+)
            .orElseGet(List::of)
            .stream()
            
            // 1. Filter: Predicado puro
            // En un Record, el acceso a los datos se hace con métodos homónimos al campo
            .filter(tx -> "approved".equals(tx.status()) && tx.amount() > 0)
            
            // 2. Map: Transformación sin estado, extrae la comisión
            .map(tx -> tx.amount() * 0.9)
            
            // 3. Reduce: Combinador funcional (fold) 
            // Retorna Optional<Double> protegiendo contra flujos vacíos
            .reduce(Double::sum)
            
            // Operaciones monádicas: Aplicamos la función matemática de redondeo
            // solo si el reduce produjo un valor
            .map(sum -> Math.round(sum * 100.0) / 100.0)
            
            // Desempaquetado seguro: si la lista original era nula, estaba vacía
            // o ningún elemento sobrevivió al filtro, retorna la identidad matemática
            .orElse(0.0);
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Recordar ejecutar la JVM con el flag -ea (enable assertions)
        // ==========================================

        // 1. Caso base del ejemplo: filtrado estricto (descarta rejected) y aplica comisión
        // Uso de 'var' y 'List.of' (inmutabilidad por defecto)
        var test1 = List.of(
            new Transaction(1, 100.0, "approved", "General"),
            new Transaction(2, 200.0, "rejected", "General")
        );
        assert processTransactions(test1) == 90.0 : "Error: Caso base esperado 90.0";

        // 2. Casos límite: lista nula, lista vacía y montos negativos/nulos
        assert processTransactions(null) == 0.0 : "Error: Manejo funcional de nulos falló";
        assert processTransactions(List.of()) == 0.0 : "Error: Lista vacía esperado 0.0";
        
        var test2 = List.of(
            new Transaction(3, -50.0, "approved", "Refund"),
            new Transaction(4, 0.0, "approved", "Test")
        );
        assert processTransactions(test2) == 0.0 : "Error: Montos negativos o cero esperado 0.0";

        // 3. Caso de precisión matemática y acumulación múltiple funcional
        // 10.55 * 0.9 = 9.495
        // 20.11 * 0.9 = 18.099
        // Suma reducida = 27.594 -> Redondeo en pipeline = 27.59
        var test3 = List.of(
            new Transaction(5, 10.55, "approved", "Services"),
            new Transaction(6, 20.11, "approved", "Products")
        );
        assert processTransactions(test3) == 27.59 : "Error: Fallo en el cálculo de precisión y redondeo";

        System.out.println("Todos los tests funcionales en Java 17 pasaron correctamente.");
    }
}