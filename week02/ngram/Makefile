GOCMD = go
GOBUILD = $(GOCMD) build
GOCLEAN = $(GOCMD) clean
GOTEST = $(GOCMD) test
GOGET = $(GOCMD) get
BINARY_NAME = ngram
BINARY_UNIX = $(BINARY_NAME)_unix
RM = rm -f

all: build

build:
	$(GOBUILD) -o $(BINARY_NAME) -v

test:
	$(GOTEST) -v ./src/ngram/...

clean:
	$(GOCLEAN)
	$(RM) $(BINARY_NAME)
	$(RM) $(BINARY_UNIX)

run:
	@$(GOBUILD) -o $(BINARY_NAME) -v ./main.go
	@./$(BINARY_NAME)
	@$(GOCLEAN)
	@$(RM) $(BINARY_NAME)
	@$(RM) $(BINARY_UNIX)
