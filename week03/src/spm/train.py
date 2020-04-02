# -*- coding: utf-8 -*-
#
# train.py
#
# Trains a sentence piece trainer.
#
# Model: SPM (Google sentencepiece)
# Input: KCC Korean sentences


import sentencepiece as spm


def train(vocab_size: int):
  spm.SentencePieceTrainer.Train(f'--input=./src/spm/KCC940_Korean_sentences_UTF8.txt --model_prefix=BPE --vocab_size={vocab_size}')
