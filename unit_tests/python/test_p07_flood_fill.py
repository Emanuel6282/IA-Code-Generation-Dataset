"""
Unit Tests — P07: 2D Flood Fill
=================================
"""
import unittest, importlib.util, os, sys, copy

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestFloodFill(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "flood_fill"): raise AttributeError("Falta 'flood_fill(grid, row, col, new_color)'")
        cls.fn = staticmethod(cls.sol.flood_fill)

    def test_tc01_classic_fill(self):
        grid = [[1,1,1],[1,1,0],[1,0,1]]
        result = self.fn(copy.deepcopy(grid), 1, 1, 2)
        self.assertEqual(result, [[2,2,2],[2,2,0],[2,0,1]])

    def test_tc02_fill_entire_grid(self):
        grid = [[0,0,0],[0,0,0]]
        result = self.fn(copy.deepcopy(grid), 0, 0, 1)
        self.assertEqual(result, [[1,1,1],[1,1,1]])

    def test_tc03_same_color_no_change(self):
        grid = [[1,1,1],[1,1,0],[1,0,1]]
        result = self.fn(copy.deepcopy(grid), 1, 1, 1)
        self.assertEqual(result, [[1,1,1],[1,1,0],[1,0,1]])

    def test_tc04_single_cell(self):
        grid = [[0]]
        result = self.fn(copy.deepcopy(grid), 0, 0, 5)
        self.assertEqual(result, [[5]])

    def test_tc05_isolated_cell(self):
        grid = [[0,1,0],[1,0,1],[0,1,0]]
        result = self.fn(copy.deepcopy(grid), 0, 0, 3)
        self.assertEqual(result, [[3,1,0],[1,0,1],[0,1,0]])

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestFloodFill)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
