# -*- coding: utf-8 -*-
#
# run.py
#
# Prints the top n sentences with the highest similarity to a given Hangul sentence.
#
# Input: a Hangul sentence

import sys
import time

from spm.train import *
import spm.similarity as similarity


def main(argv):

  '''
  main runs training model, sorting and printing results and timing.

  @param argv command-line parameter
  
  '''
  
  try:
    if len(argv) != 2:
      raise ValueError

    option = int(argv[1])
  
  except ValueError as VE:
    print(VE.__str__())
    sys.exit(2)

  vocab_size = int(input('vocabulary size >> '))

  trainStartTime = time.time()

  train(vocab_size)

  trainEndTime = time.time()

  trainElapsedTime = trainEndTime - trainStartTime

  inputSentence = input('Enter a Hangul sentence >> ')

  compareStartTime = time.time()

  similarities = similarity.get(inputSentence)

  compareEndTime = time.time()

  topSimilars = sorted(similarities.keys(), key=lambda x: similarities[x], reverse=True)[:option]

  compareElapsedTime = compareEndTime - compareStartTime

  elapsedTime = trainElapsedTime + compareElapsedTime

  for index, sentence in enumerate(topSimilars):
      print("%d. %s:\t%f%%" % (index+1, sentence, similarities[sentence]))

  print("Elapsed time: %fs" % elapsedTime)


if __name__ == '__main__':
  main(sys.argv)
