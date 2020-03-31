# -*- coding: utf-8 -*-
#
# train.py
#
# Trains a sentence piece trainer.
#
# Model: SPM (Google sentencepiece)
# Input: KCC Korean sentences


import sentencepiece as spm


spm.SentencePieceTrainer.Train('--input=KCCq28_Korean_sentences_UTF8.txt --model_prefix=model --vocab_size=100000')
