# -*- coding: utf-8 -*-
#
# run.py
#
# prints the top n sentences with the highest similarity to a given Hangul sentence.
#
# Input: a Hangul sentence


import sys
import time

from spm.train import *
from spm.compare import *


def main(argv):
  
  try:
    if len(argv) != 2:
      raise ValueError

    option = int(argv[1])
  
  except ValueError as VE:
    print(VE.__str__())
    sys.exit(2)

  vocab_size = int(input('vocabulary size >> '))

  train(vocab_size)

  inputSentence = input('')

  similarities = compute(inputSentence)

  topSimilars = sorted(similarities.keys(), key=lambda x: similarities[x], reverse=True)[:option]

  for index, sentence in enumerate(topSimilars):
    print("%d: %s" % (index, sentence))

if __name__ == '__main__':
  main(sys.argv)
