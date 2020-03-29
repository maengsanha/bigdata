# Go parameter

GOCMD = go
GOBUILD = $(GOCMD) build
GOCLEAN = $(GOCMD) clean
GOTEST = $(GOCMD) test
GOGET = $(GOCMD) get
BINARY_NAME = ngram
BINARY_UNIX = $(BINARY_NAME)_unix
RM = rm -f

# Python parameter

PYTHON = python3


all: build spm

build:
	$(GOBUILD) -o $(BINARY_NAME) -v ./src/main.go

test:
	$(GOTEST) -v ./src/ngram/...

clean:
	$(GOCLEAN)
	$(RM) $(BINARY_NAME)
	$(RM) $(BINARY_UNIX)

run:
	@$(GOBUILD) -o $(BINARY_NAME) -v ./src/main.go
	@./$(BINARY_NAME)
	@$(GOCLEAN)
	@$(RM) $(BINARY_NAME)
	@$(RM) $(BINARY_UNIX)

spm:
	$(PYTHON) ./src/spm/tokenizer.py
