// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Inferencia de tipos con 'var' (JEP 286): Aunque se introdujo en Java 10, es el estándar de facto en Java 17 para variables locales. Reduce la redundancia visual (boilerplate) y permite que la atención del lector se centre en la lógica y los nombres de las variables, no en sus tipos estáticos.
// 2. Text Blocks (JEP 378): Utilizados para la salida en consola. Permiten definir cadenas literales multilínea sin caracteres de escape, lo que mejora drásticamente la limpieza del código al imprimir mensajes estructurados.

public class P02BinarySearch_v17 {

    public static int binarySearch(int[] arr, int target) {
        // Uso de 'var' para variables locales
        var left = 0;
        var right = arr.length - 1;
        
        while (left <= right) {
            var mid = left + (right - left) / 2;
            
            // Extraemos el valor a una variable local para evitar accesos repetidos 
            // al arreglo, aprovechando la sintaxis limpia de 'var'
            var midVal = arr[mid];
            
            if (midVal == target) {
                return mid;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        // Mismos tests adaptados
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;
        
        // Uso de Text Blocks para salida formateada
        System.out.println("""
            ================
            All tests passed
            ================
            """);
    }
}

// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN JAVA 17:
//
// 1. Transición a `var`: Declaraciones como `int left = 0` pasaron a ser `var left = 0`. En el Java moderno (post Java 11/17), si el tipo de dato es obvio en el lado derecho de la asignación, se prefiere `var`. Esto hace que el código se sienta más ágil y similar a lenguajes modernos.
//
// 2. Almacenamiento en caché de la variable `midVal`: En el código Java 8, se leía `arr[mid]` hasta dos veces por iteración del bucle. Al almacenarlo en una variable local `midVal`, se optimiza el acceso a memoria, lo cual es considerado una mejor práctica, y encaja muy bien con el uso de `var` para mantener la brevedad.
//
// 3. Imprimir con Text Blocks (`"""`): Se reemplazó el tradicional `System.out.println("...")` por un String multilinea. Hoy en día es la forma idiomática de escupir plantillas o recuadros de texto a la consola o para la generación de strings complejos, previniendo la antigua necesidad de concatenar líneas con el operador `+`.