import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests - P07: 2D Flood Fill
 * La IA debe implementar: public static int[][] floodFill(int[][] grid, int row, int col, int newColor)
 */
class TestP07FloodFill {

    @Test @DisplayName("TC01 - Fill clasico")
    void tc01() {
        int[][] grid = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] expected = {{2,2,2},{2,2,0},{2,0,1}};
        assertArrayEquals(expected, Solution.floodFill(grid, 1, 1, 2));
    }

    @Test @DisplayName("TC02 - Llenar toda la grilla")
    void tc02() {
        int[][] grid = {{0,0,0},{0,0,0}};
        int[][] expected = {{1,1,1},{1,1,1}};
        assertArrayEquals(expected, Solution.floodFill(grid, 0, 0, 1));
    }

    @Test @DisplayName("TC03 - Mismo color, sin cambios")
    void tc03() {
        int[][] grid = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] expected = {{1,1,1},{1,1,0},{1,0,1}};
        assertArrayEquals(expected, Solution.floodFill(grid, 1, 1, 1));
    }

    @Test @DisplayName("TC04 - Grilla 1x1")
    void tc04() {
        int[][] grid = {{0}};
        assertArrayEquals(new int[][]{{5}}, Solution.floodFill(grid, 0, 0, 5));
    }

    @Test @DisplayName("TC05 - Celda aislada")
    void tc05() {
        int[][] grid = {{0,1,0},{1,0,1},{0,1,0}};
        int[][] expected = {{3,1,0},{1,0,1},{0,1,0}};
        assertArrayEquals(expected, Solution.floodFill(grid, 0, 0, 3));
    }
}
