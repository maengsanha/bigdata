# -*- coding: utf-8 -*-
#
# tokenizer.py
#
# tokenize two sentences using trained model obtained by train.py and
# compare two sentences 


import sentencepiece as spm

from collections import Counter


def tokenize(first: str, second: str):
  
  '''
  tokenize tokenizes two given strings using trained model obtained by train.py
  
  >>> @param
    
    first: a Hangul sentence.
    
    second: a Hangul sentence.
  
  >>> @return
    
    two token list of first and second.

  '''

  processor = spm.SentencePieceProcessor()
  processor.load('./src/spm/model.model')

  tokens_first = processor.EncodeAsPieces(first)
  tokens_second = processor.EncodeAsPieces(second)

  return tokens_first, tokens_second
  

def compare(first: str, second: str) -> float:

  '''
  compare compares two string using spm model.

  >>> @param

    first: a Hangul sentence.
    second: a Hangul sentence.
  
  >>> @return
    
    similarity between two sentences using spm model.
  
  '''

  tokens_first, tokens_second = tokenize(first, second)

  if len(tokens_first) > len(tokens_second):
    return compare(second, first)

  token_count_first = Counter(tokens_first)
  token_count_second = Counter(tokens_second)

  common = 0

  for token in token_count_first:
    if token in token_count_second:
      common += min(token_count_first[token], token_count_second[token])

  return 100 * common / len(token_count_first)


def main():

  first = input('첫번째 문장 >> ')
  second = input('두번째 문장 >> ')

  similarity = compare(first, second)

  print(f'SPM 모델로 측정한 유사도: {similarity}%')


if __name__ == '__main__':
  main()
