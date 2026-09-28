import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;

public class BalancedParentheses {

    /**
     * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
     * "{", "}", retorna true si todos los parentesis/corchetes estan
     * correctamente balanceados y cerrados en orden. Retorna false en caso
     * contrario.
     *
     * Implementacion en paradigma funcional: usa la Stream API
     * (s.chars().mapToObj(...)) junto con reduce() para recorrer la cadena
     * acumulando el estado como un Optional<Deque<Character>>, donde
     * Optional.empty() representa "ya se detecto un desbalance"
     * (cortocircuito funcional). En cada paso se construye una NUEVA pila
     * (copia via el constructor de ArrayDeque) en lugar de mutar la
     * anterior, preservando el espiritu de inmutabilidad del paradigma
     * funcional. No se usan bucles for/while imperativos. Compatible con
     * Java 17.
     *
     * @param s Cadena que contiene unicamente los caracteres de parentesis
     *          y corchetes indicados.
     * @return true si la cadena esta correctamente balanceada, false en
     *         caso contrario. Una cadena vacia se considera balanceada
     *         (true).
     */
    public static boolean isBalanced(String s) {
        // Map.of (Java 9+, vigente en 17): mapa inmutable conciso para las
        // tres asociaciones de cierre/apertura, sin necesitar HashMap +
        // put() repetidos.
        var paresCierre = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );

        var estadoFinal = s.chars()
                .mapToObj(codigo -> (char) codigo)
                .reduce(
                        Optional.of(new ArrayDeque<Character>()),
                        (estado, caracter) -> procesarCaracter(estado, caracter, paresCierre),
                        (estado1, estado2) -> estado1 // combiner: no usado en reduce secuencial
                );

        return estadoFinal.map(Deque::isEmpty).orElse(false);
    }

    /**
     * Funcion pura auxiliar: dado un estado (Optional<Deque<Character>>) y
     * un caracter, retorna un NUEVO estado. Si el estado de entrada ya es
     * Optional.empty() (desbalance ya detectado), se propaga sin cambios
     * via flatMap (cortocircuito funcional). Nunca muta la pila del estado
     * anterior: siempre construye una copia nueva antes de modificarla.
     */
    private static Optional<Deque<Character>> procesarCaracter(
            Optional<Deque<Character>> estado,
            char caracter,
            Map<Character, Character> paresCierre) {

        return estado.flatMap(pilaAnterior -> {
            var nuevaPila = new ArrayDeque<>(pilaAnterior);

            if (caracter == '(' || caracter == '[' || caracter == '{') {
                nuevaPila.push(caracter);
                return Optional.of(nuevaPila);
            } else if (paresCierre.containsKey(caracter)) {
                if (nuevaPila.isEmpty() || !nuevaPila.peek().equals(paresCierre.get(caracter))) {
                    return Optional.<Deque<Character>>empty();
                }
                nuevaPila.pop();
                return Optional.of(nuevaPila);
            }

            return Optional.of(nuevaPila);
        });
    }

    public static void main(String[] args) {
        // Caso 1: balanceado simple
        assert isBalanced("()") == true : "Caso 1 fallido";

        // Caso 2: orden incorrecto de cierre -> no balanceado
        assert isBalanced("([)]") == false : "Caso 2 fallido";

        // Caso 3: anidamiento balanceado de varios tipos
        assert isBalanced("{[]}") == true : "Caso 3 fallido";

        // Uso adicional genuino de Optional: verificar de forma segura el
        // resultado intermedio de una cadena balanceada
        var paresCierre = Map.of(')', '(', ']', '[', '}', '{');
        var resultadoOpt = "{[]}".chars()
                .mapToObj(codigo -> (char) codigo)
                .reduce(
                        Optional.of(new ArrayDeque<Character>()),
                        (estado, caracter) -> procesarCaracter(estado, caracter, paresCierre),
                        (estado1, estado2) -> estado1
                );
        assert resultadoOpt.isPresent() && resultadoOpt.get().isEmpty()
                : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}