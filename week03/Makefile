# Python parameter

PYTHON = python3
SOURCE = ./src/run.py
argv = 
CACHE = ./src/spm/__pycache__/
RM = rm -rf


all: run

run:
	@$(PYTHON) $(SOURCE) $(argv)
	@$(RM) $(CACHE)
	@rm -f BPE.model
	@rm -f BPE.vocab

clean:
	@$(RM) $(CACHE)
	@rm -f BPE.model
	@rm -f BPE.vocab
