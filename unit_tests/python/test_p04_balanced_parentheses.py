"""
Unit Tests — P04: Balanced Parentheses
========================================
"""
import unittest, importlib.util, os, sys

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestBalancedParentheses(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "is_balanced"): raise AttributeError("Falta funcion 'is_balanced(s)'")
        cls.fn = staticmethod(cls.sol.is_balanced)

    def test_tc01_simple_balanced(self):        self.assertTrue(self.fn("()"))
    def test_tc02_multiple_types(self):         self.assertTrue(self.fn("()[]{}"))
    def test_tc03_mismatched(self):             self.assertFalse(self.fn("(]"))
    def test_tc04_interleaved(self):            self.assertFalse(self.fn("([)]"))
    def test_tc05_nested_balanced(self):        self.assertTrue(self.fn("{[]}"))
    def test_tc06_empty_string(self):           self.assertTrue(self.fn(""))
    def test_tc07_unclosed(self):               self.assertFalse(self.fn("("))
    def test_tc08_deeply_nested_same_type(self):self.assertTrue(self.fn("((()))"))

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestBalancedParentheses)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
