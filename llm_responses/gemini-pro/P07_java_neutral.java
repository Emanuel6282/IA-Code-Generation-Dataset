import java.util.Arrays;

public class P07FloodFill_Base {

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        // Validación de matriz vacía o nula
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        int originalColor = grid[row][col];

        // Si el color original ya es el nuevo color, se retorna la matriz sin cambios
        if (originalColor == newColor) {
            return grid;
        }

        // Se inicia la búsqueda en profundidad (DFS)
        dfs(grid, row, col, originalColor, newColor);
        
        return grid;
    }

    private static void dfs(int[][] grid, int r, int c, int originalColor, int newColor) {
        // Condiciones de parada: 
        // 1. Fuera de los límites de la matriz
        // 2. El píxel actual no tiene el color original
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != originalColor) {
            return;
        }

        // Se actualiza el color del píxel actual
        grid[r][c] = newColor;

        // Llamadas recursivas para las 4 direcciones adyacentes
        dfs(grid, r - 1, c, originalColor, newColor); // Arriba
        dfs(grid, r + 1, c, originalColor, newColor); // Abajo
        dfs(grid, r, c - 1, originalColor, newColor); // Izquierda
        dfs(grid, r, c + 1, originalColor, newColor); // Derecha
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P07FloodFill_Base.java

        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: reemplazo de unos conectados a partir de (1,1)
        int[][] grid1 = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };
        int[][] expected1 = {
            {2, 2, 2},
            {2, 2, 0},
            {2, 0, 1}
        };
        int[][] res1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(res1, expected1) : "Error en el caso base 1";

        // 2. Caso sin cambios: el color inicial ya es igual al nuevo color
        int[][] grid2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] expected2 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] res2 = floodFill(grid2, 0, 0, 0);
        assert Arrays.deepEquals(res2, expected2) : "Error en el caso de no reemplazo";

        // 3. Caso de aislamiento: solo cambia el centro, los otros 1s no están conectados en 4 direcciones
        int[][] grid3 = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };
        int[][] expected3 = {
            {1, 0, 1},
            {0, 3, 0},
            {1, 0, 1}
        };
        int[][] res3 = floodFill(grid3, 1, 1, 3);
        assert Arrays.deepEquals(res3, expected3) : "Error en el caso de aislamiento";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}