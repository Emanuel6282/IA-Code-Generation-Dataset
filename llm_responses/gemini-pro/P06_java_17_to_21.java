// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Inferencia de Tipos Locales (var): Se utiliza de manera generalizada para reducir el ruido visual en la inicialización de variables e índices. En Java 21, su uso es el estándar idiomático ineludible para variables de alcance local, delegando la validación estricta al compilador.
// 2. Ejecución Directa de Código Fuente (Single-File Source-Code Programs): Java 21 consolida la capacidad de ejecutar archivos `.java` directamente sin requerir el paso explícito de compilación (`javac`), acercando la experiencia iterativa a la de lenguajes de scripting.
// 3. Manejo Defensivo Moderno: Se reemplaza el acceso directo inseguro (`arr[0]`) por una cláusula de guarda (guard clause) que arroja un `IllegalArgumentException`, previniendo errores silenciosos en la evaluación algorítmica.

public class P06MaxSubarray_v21 {

    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe contener al menos un elemento.");
        }

        var currentMax = arr[0];
        var globalMax = arr[0];
        
        for (var i = 1; i < arr.length; i++) {
            // Se delega la bifurcación lógica a las funciones matemáticas optimizadas de la JVM
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            globalMax = Math.max(globalMax, currentMax);
        }
        
        return globalMax;
    }

    public static void main(String[] args) {
        // En Java 21, este archivo se ejecuta directamente en consola con:
        // java -ea P06MaxSubarray_v21.java
        
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6 : "Fallo caso 1";
        assert maxSubarraySum(new int[]{1}) == 1 : "Fallo caso 2";
        assert maxSubarraySum(new int[]{-2, -1}) == -1 : "Fallo caso 3";
        
        System.out.println("Todos los tests en Java 21 pasaron correctamente.");
    }
}

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 21:

1. **Ergonomía con `var`**: 
   El código base de Java 8 era extremadamente repetitivo en sus declaraciones de tipo (`int currentMax`, `int globalMax`, `int i`). La migración a `var` alinea el código con las prácticas modernas de Java 21, donde el enfoque visual se traslada a la lógica de negocio y no a la ceremonia estática.

2. **Validación Explícita Temprana**: 
   Introducir el chequeo `if (arr == null || arr.length == 0)` refleja el cambio de paradigma hacia el diseño de software "Fail-Fast". Evita que el programa falle más adelante con un críptico `ArrayIndexOutOfBoundsException` provocado por la máquina virtual.

3. **Inmutabilidad del Paradigma Iterativo en el Tiempo**: 
   Al añadir este último snippet a tu página web de recolección de prompts, logras consolidar un punto de inflexión crítico para la redacción del libro de tu trabajo final de grado en la FP-UNA. A través de este dataset, demuestras que mientras la IA genera variaciones sintácticas drásticas al aplicar el paradigma funcional a través de las versiones (desde clases envoltorio en Java 8 hasta *Records* en Java 21), la base del paradigma iterativo procedimental permanece sintácticamente estática. Java 21 opta por no alterar los constructos de bucles imperativos, dejando que el salto cualitativo ocurra bajo el capó mediante las hiperoptimizaciones en tiempo de ejecución del motor JIT.
*/