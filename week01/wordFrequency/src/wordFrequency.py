# -*- coding: utf-8 -*-

import time
import os
import re


def count(textFile: str) -> None:
    '''
    count prints size of textFile, frequencies of words in textFile, and elapsed time to calculate it.

    >>> @param
      textFile: a text file.
    
    >>> @return
      N/A
    '''

    hangulRegex = "[.,!?~ㆍ:/\"\']"

    size = os.path.getsize(textFile)

    frequencies = dict()

    start = time.time()

    with open(textFile) as file:
        lines = file.readlines()

        for line in lines:
            words = line.split()

            for word in words:
                word = re.sub(hangulRegex, '', word.strip())

                if word not in frequencies:
                    frequencies[word] = 1
                else:
                    frequencies[word] += 1

    file.close()

    end = time.time()

    elapsedTime = end - start

    print("-" * 40, "어절빈도 조사 결과", "-" * 40)
    print()

    for word in sorted(frequencies.keys(), key=lambda x: frequencies[x]):
        print(f'{word}: {frequencies[word]}회')

    print()
    print("-" * 80)

    print(f'파일 크기(byte): {size}')

    print(f'소요 시간(sec): {elapsedTime}\n')

    return


if __name__ == "__main__":
    count("src/KCC940_Korean_sentences_UTF8.txt")
