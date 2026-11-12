"""
Unit Tests — P01: Word Frequency
=================================
Instrucciones:
1. Guarda el codigo generado por la IA como 'solution.py' en esta misma carpeta.
2. Ejecuta: python test_p01_word_frequency.py
3. El resultado muestra cuantos tests pasan (tests_passed_ratio).
"""

import unittest
import importlib.util
import os
import sys


def load_solution():
    solution_path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(solution_path):
        raise FileNotFoundError(f"No se encontro 'solution.py' en {solution_path}")
    spec = importlib.util.spec_from_file_location("solution", solution_path)
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


class TestWordFrequency(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "word_frequency"):
            raise AttributeError("La solucion debe definir la funcion 'word_frequency(words)'")
        cls.fn = staticmethod(cls.sol.word_frequency)

    # TC01 — Palabra repetida basica
    def test_tc01_basic_repeated(self):
        result = self.fn(["the", "fox", "the"])
        self.assertEqual(result, {"the": 2, "fox": 1})

    # TC02 — Lista vacia
    def test_tc02_empty_list(self):
        result = self.fn([])
        self.assertEqual(result, {})

    # TC03 — Una sola palabra
    def test_tc03_single_word(self):
        result = self.fn(["hello"])
        self.assertEqual(result, {"hello": 1})

    # TC04 — Multiples frecuencias
    def test_tc04_multiple_frequencies(self):
        result = self.fn(["a", "b", "a", "c", "b", "a"])
        self.assertEqual(result, {"a": 3, "b": 2, "c": 1})

    # TC05 — Tres palabras distintas con distintas frecuencias
    def test_tc05_three_words(self):
        result = self.fn(["cat", "dog", "cat", "bird", "dog", "cat"])
        self.assertEqual(result, {"cat": 3, "dog": 2, "bird": 1})


if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestWordFrequency)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun
    failed = len(result.failures) + len(result.errors)
    passed = total - failed
    ratio = round(passed / total, 2) if total > 0 else 0.0
    print(f"\n--- RESULTADO FINAL ---")
    print(f"tests_passed_ratio: {ratio} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
