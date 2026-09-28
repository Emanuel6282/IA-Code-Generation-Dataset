import os, glob
from pathlib import Path

test_dir = Path(r'c:\Users\Ema\Documents\Dataset\unit_tests\python')
for file in test_dir.glob('test_*.py'):
    content = file.read_text('utf-8')
    content = content.replace('cls.fn = cls.sol.', 'cls.fn = staticmethod(cls.sol.')
    # we need to close the parenthesis
    content = content.replace('staticmethod(cls.sol.word_frequency\n', 'staticmethod(cls.sol.word_frequency)\n')
    # better regex:
    import re
    content = re.sub(r'cls\.fn = cls\.sol\.([a-zA-Z0-9_]+)', r'cls.fn = staticmethod(cls.sol.\1)', content)
    file.write_text(content, 'utf-8')
print("Python tests fixed.")
