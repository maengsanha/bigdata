# -*- coding: utf-8 -*-
#
# compare.py
#
# computes similarities between KCC sentences and a given Hangul sentence.


import sentencepiece as spm

from collections import Counter


def compute(inputSentence: str) -> dict:

  '''
  compute computes similarites of sentences in KCC sentences to a given Hangul sentence.

  >>> @param
    inputSentence: a Hangul sentence

  >>> @return
    similarites: a map containing similarities between sentence of KCC sentences and input sentence

  '''

  similarities = dict()

  processor = spm.SentencePieceProcessor()
  processor.load('./src/spm/BPE.model')

  tokens_input = processor.EncodeAsPieces(inputSentence)

  with open('./src/spm/KCC940_Korean_sentences_UTF8.txt') as sentences:

    for sentence in sentences.readlines():
      sentence = sentence.strip()
      tokens = processor.EncodeAsPieces(sentence)
      similarity = compare(tokens_input, tokens)
      similarities[sentence] = similarity
    
  return similarities


def compare(tokens_first: list, tokens_second: list) -> float:

  '''
  compare compares two token lists using BPE model.

  >>> @param

    tokens_first: a Hangul sentence tokens.
    tokens_second: a Hangul sentence tokens.
  
  >>> @return
    
    similarity between two sentence tokens using BPE model.
  
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
