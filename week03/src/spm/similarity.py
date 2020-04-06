# -*- coding: utf-8 -*-
#
# similarity.py
#
# Computes similarities between KCC sentences and a given Hangul sentence.

import re
from collections import Counter

import sentencepiece as spm


def get(inputSentence: str) -> dict:

  '''
  get returns similarites of sentences in KCC sentences to a given Hangul sentence.

  @param inputSentence a Hangul sentence
  @return a map containing similarities between sentence of KCC sentences and input sentence

  '''

  hangulRegex = "[,!?~ㆍ:/\"\']"

  similarities = dict()

  processor = spm.SentencePieceProcessor()
  processor.load('./BPE.model')

  tokens_input = processor.EncodeAsPieces(inputSentence)

  with open('./src/spm/KCC940_Korean_sentences_UTF8.txt') as f:

    for line in f.readlines():
      sentences = line.split('.')
      for sentence in sentences:
        sentence = re.sub(hangulRegex, '', sentence.strip())
        if sentence == '':
          continue
        tokens = processor.EncodeAsPieces(sentence)
        similarity = compare(tokens_input, tokens)
        similarities[sentence] = similarity
    
  return similarities


def compare(tokens_first: list, tokens_second: list) -> float:

  '''
  compare compares two token lists using BPE model.

  @param tokens_first a Hangul sentence tokens.
  @param tokens_second a Hangul sentence tokens.
  @return similarity between two sentence tokens using BPE model.
  
  '''

  if len(tokens_first) > len(tokens_second):
    return compare(tokens_second, tokens_first)

  token_count_first = Counter(tokens_first)
  token_count_second = Counter(tokens_second)

  common = 0

  for token in token_count_first:
    if token in token_count_second:
      common += min(token_count_first[token], token_count_second[token])

  return 100 * common / len(token_count_first)
