import os, glob
from pathlib import Path

test_dir = Path(r'c:\Users\Ema\Documents\Dataset\unit_tests\python')
for file in test_dir.glob('test_*.py'):
    content = file.read_text('utf-8')
    content = content.replace('cls.fn = staticmethod(cls.sol.word_frequency', 'cls.fn = staticmethod(cls.sol.word_frequency)')
    content = content.replace('cls.fn = staticmethod(cls.sol.binary_search\n', 'cls.fn = staticmethod(cls.sol.binary_search)\n')
    content = content.replace('cls.fn = staticmethod(cls.sol.flatten_list\n', 'cls.fn = staticmethod(cls.sol.flatten_list)\n')
    content = content.replace('cls.fn = staticmethod(cls.sol.is_balanced\n', 'cls.fn = staticmethod(cls.sol.is_balanced)\n')
    content = content.replace('cls.fn = staticmethod(cls.sol.process_transactions\n', 'cls.fn = staticmethod(cls.sol.process_transactions)\n')
    content = content.replace('cls.fn = staticmethod(cls.sol.max_subarray_sum\n', 'cls.fn = staticmethod(cls.sol.max_subarray_sum)\n')
    content = content.replace('cls.fn = staticmethod(cls.sol.flood_fill\n', 'cls.fn = staticmethod(cls.sol.flood_fill)\n')
    file.write_text(content, 'utf-8')
print("Python syntax fixed.")
