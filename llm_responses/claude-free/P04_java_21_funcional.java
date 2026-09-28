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
     * en lugar de mutar la anterior. La bifurcacion "apertura / cierre /
     * otro" se expresa con pattern matching for switch (JEP 441, estandar
     * desde Java 21) sobre un Character clasificado previamente. No se
     * usan bucles for/while imperativos. Compatible con Java 21.
     *
     * @param s Cadena que contiene unicamente los caracteres de parentesis
     *          y corchetes indicados.
     * @return true si la cadena esta correctamente balanceada, false en
     *         caso contrario. Una cadena vacia se considera balanceada
     *         (true).
     */
    public static boolean isBalanced(String s) {
        var paresCierre = Map.of(
                ')', '(',
                ']', '[',
                '}', '{'
        );
        var aperturas = Map.of(
                '(', ')',
                '[', ']',
                '{', '}'
        );

        var estadoFinal = s.chars()
                .mapToObj(codigo -> (char) codigo)
                .reduce(
                        Optional.of(new ArrayDeque<Character>()),
                        (estado, caracter) -> procesarCaracter(estado, caracter, paresCierre, aperturas),
                        (estado1, estado2) -> estado1 // combiner: no usado en reduce secuencial
                );

        return estadoFinal.map(Deque::isEmpty).orElse(false);
    }

    /**
     * Funcion pura auxiliar: dado un estado (Optional<Deque<Character>>) y
     * un caracter, retorna un NUEVO estado. Usa pattern matching for
     * switch para clasificar el caracter como apertura, cierre o "otro",
     * de forma mas declarativa que una cadena if/else if.
     */
    private static Optional<Deque<Character>> procesarCaracter(
            Optional<Deque<Character>> estado,
            char caracter,
            Map<Character, Character> paresCierre,
            Map<Character, Character> aperturas) {

        return estado.flatMap(pilaAnterior -> {
            var nuevaPila = new ArrayDeque<>(pilaAnterior);

            // Pattern matching for switch (JEP 441) sobre una clasificacion
            // booleana previa, usando guards ("when") para expresar los
            // tres casos posibles de forma exhaustiva y declarativa.
            Boolean esApertura = aperturas.containsKey(caracter);
            Boolean esCierre = paresCierre.containsKey(caracter);

            return switch (Boolean.TRUE) {
                case Boolean b when esApertura -> {
                    nuevaPila.push(caracter);
                    yield Optional.of(nuevaPila);
                }
                case Boolean b when esCierre -> {
                    if (nuevaPila.isEmpty() || !nuevaPila.peek().equals(paresCierre.get(caracter))) {
                        yield Optional.<Deque<Character>>empty();
                    }
                    nuevaPila.pop();
                    yield Optional.of(nuevaPila);
                }
                default -> Optional.of(nuevaPila);
            };
        });
    }

    public static void main(String[] args) {
        // Caso 1: balanceado simple
        assert isBalanced("()") == true : "Caso 1 fallido";

        // Caso 2: orden incorrecto de cierre -> no balanceado
        assert isBalanced("([)]") == false : "Caso 2 fallido";

        // Caso 3: anidamiento balanceado de varios tipos
        assert isBalanced("{[]}") == true : "Caso 3 fallido";

        // Uso adicional genuino de Optional
        var paresCierre = Map.of(')', '(', ']', '[', '}', '{');
        var aperturas = Map.of('(', ')', '[', ']', '{', '}');
        var resultadoOpt = "{[]}".chars()
                .mapToObj(codigo -> (char) codigo)
                .reduce(
                        Optional.of(new ArrayDeque<Character>()),
                        (estado, caracter) -> procesarCaracter(estado, caracter, paresCierre, aperturas),
                        (estado1, estado2) -> estado1
                );
        assert resultadoOpt.isPresent() && resultadoOpt.get().isEmpty()
                : "Caso Optional fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}