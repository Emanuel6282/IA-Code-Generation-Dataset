// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Inferencia de tipos con 'var' (JEP 286): Ampliamente consolidado en Java 21, elimina la redundancia visual al declarar variables locales cuyo tipo es obvio, haciendo el código más conciso.
// 2. Expresiones Switch (Switch Expressions - JEP 361): Permiten usar 'switch' no solo como una estructura de control con 'break', sino como una expresión segura y directa mediante la sintaxis de flecha (->). Previene los clásicos errores de "fall-through" y hace el enrutamiento lógico más declarativo.
// 3. Text Blocks (JEP 378): Utilizado para simplificar la impresión por consola, permitiendo formatear strings multilinea sin usar secuencias de escape (\n) o concatenaciones (+).

public class P02BinarySearch_v21 {

    public static int binarySearch(int[] arr, int target) {
        // Uso de 'var' en lugar de tipos explícitos ('int')
        var left = 0;
        var right = arr.length - 1;
        
        while (left <= right) {
            var mid = left + (right - left) / 2;
            
            // Comparamos el valor del medio con el objetivo para usarlo en el switch
            var cmp = Integer.compare(arr[mid], target);
            
            // Switch moderno: sintaxis de flecha (->) sin necesidad de 'break'.
            // Integer.signum normaliza el resultado de la comparación a -1, 0, o 1.
            switch (Integer.signum(cmp)) {
                case 0  -> { return mid; }       // Encontrado
                case -1 -> left = mid + 1;       // El elemento del medio es menor (buscar en la derecha)
                case 1  -> right = mid - 1;      // El elemento del medio es mayor (buscar en la izquierda)
            }
        }
        
        return -1;
    }

    // Nota: Aunque Java 21 introduce 'Unnamed Classes and Instance Main Methods' en Preview (JEP 445),
    // mantenemos la firma estandar de 'main' para asegurar la ejecucion sin necesidad de flags de preview.
    public static void main(String[] args) {
        // Tests originales adaptados al entorno moderno
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;
        
        // Uso de Text Blocks para imprimir resultados visualmente ordenados
        System.out.println("""
            ================
            All tests passed
            ================
            """);
    }
}

// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 21:
//
// 1. Transformación del flujo condicional (if-else a switch): En el código de Java 8, se usaban múltiples `if-else` y se accedía dos veces al arreglo en `arr[mid]`. En Java 21, es idiomático extraer la comparación y usar las nuevas "Switch Expressions" con la sintaxis de flecha `->`. Esto mapea perfectamente los tres estados posibles (-1, 0, 1) de una búsqueda binaria, logrando un código más seguro, limpio y que no requiere la molesta sentencia `break`.
//
// 2. Adopción de `var`: El tipado estático tradicional (`int left = 0`) se ha reemplazado por `var`. En el ecosistema de Java moderno (LTS 17 y 21), se recomienda usar `var` para variables de ámbito local y bucles, ya que el compilador infiere el tipo a la perfección y reduce el ruido visual.
//
// 3. String multilinea (Text Blocks): El `System.out.println("All tests passed");` simple fue reemplazado por un bloque `""" ... """`. Aunque para un mensaje de una línea no es estrictamente necesario, es el estándar actual en Java 21 cuando se desea maquetar la salida de terminal, integrar JSON, HTML o SQL, evitando los viejos delimitadores y concatenaciones de Java 8.