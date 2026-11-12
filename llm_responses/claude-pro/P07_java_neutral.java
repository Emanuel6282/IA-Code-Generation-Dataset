import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class FloodFill {

    /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
    private static final int[][] OFFSETS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Rellena con {@code newColor} la región conectada (4 direcciones) que contiene
     * (row, col): todas las celdas alcanzables desde ahí moviéndose arriba, abajo,
     * izquierda o derecha por celdas del mismo color original.
     *
     * <p>La matriz se modifica en el lugar y se retorna la misma referencia. Si la celda
     * ya tiene {@code newColor}, se retorna sin cambios.
     *
     * <p>Se recorre con una pila explícita en lugar de recursión: una región grande (por
     * ejemplo, 2000 x 2000 de un solo color) desbordaría la pila de llamadas de la JVM
     * con un DFS recursivo. Cada celda se pinta al apilarla, así que no se visita dos
     * veces. O(filas * columnas) en tiempo. Admite matrices irregulares (filas de
     * distinto largo): los límites se comprueban por fila.
     *
     * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
     * @throws NullPointerException si {@code grid} o alguna de sus filas es null
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length) {
            throw new IndexOutOfBoundsException(
                    "posición (" + row + ", " + col + ") fuera de la matriz");
        }

        int original = grid[row][col];
        // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación
        // el bucle no terminaría, porque las celdas pintadas seguirían teniendo el color
        // original.
        if (original == newColor) {
            return grid;
        }

        Deque<int[]> stack = new ArrayDeque<>();
        grid[row][col] = newColor;
        stack.push(new int[] {row, col});

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            for (int[] offset : OFFSETS) {
                int r = cell[0] + offset[0];
                int c = cell[1] + offset[1];
                if (r >= 0 && r < grid.length && c >= 0 && c < grid[r].length
                        && grid[r][c] == original) {
                    grid[r][c] = newColor;
                    stack.push(new int[] {r, c});
                }
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
        int[][] grid1 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        int[][] result1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(result1, new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}})
                : "ejemplo: " + Arrays.deepToString(result1);
        assert result1 == grid1 : "debe modificar y retornar la misma matriz";

        // Caso 2: el color ya es el nuevo -> la matriz no cambia
        int[][] grid2 = {{0, 0, 0}, {0, 1, 1}};
        assert Arrays.deepEquals(floodFill(grid2, 1, 1, 1), new int[][] {{0, 0, 0}, {0, 1, 1}})
                : "color ya es el nuevo: " + Arrays.deepToString(grid2);

        // Caso 3: no se rellena en diagonal
        int[][] grid3 = {{1, 0}, {0, 1}};
        assert Arrays.deepEquals(floodFill(grid3, 0, 0, 3), new int[][] {{3, 0}, {0, 1}})
                : "sin diagonales: " + Arrays.deepToString(grid3);

        // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
        int[][] bad = {{2, 0}, {-1, 0}, {0, 3}};
        for (int[] p : bad) {
            boolean threw = false;
            try {
                floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, p[0], p[1], 5);
            } catch (IndexOutOfBoundsException e) {
                threw = true;
            }
            assert threw : "(" + p[0] + ", " + p[1] + ") debería lanzar IndexOutOfBoundsException";
        }

        System.out.println("Todas las pruebas pasaron.");
    }
}