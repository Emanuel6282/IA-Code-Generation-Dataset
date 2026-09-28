import os
from pathlib import Path

def remove_bom(file_path):
    with open(file_path, 'rb') as f:
        content = f.read()
    if content.startswith(b'\xef\xbb\xbf'):
        content = content[3:]
        with open(file_path, 'wb') as f:
            f.write(content)
        print(f"BOM removed: {file_path.name}")

test_dir_java = Path(r'c:\Users\Ema\Documents\Dataset\unit_tests\java\src\test\java')
for f in test_dir_java.glob('*.java'): remove_bom(f)

test_dir_scala = Path(r'c:\Users\Ema\Documents\Dataset\unit_tests\scala\src\test\scala')
for f in test_dir_scala.glob('*.scala'): remove_bom(f)
