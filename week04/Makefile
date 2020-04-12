# Go parameter

GOCMD = go
GOBUILD = $(GOCMD) build
GOCLEAN = $(GOCMD) clean
GOTEST = $(GOCMD) test
GOGET = $(GOCMD) get
BINARY_NAME = word2vec
BINARY_UNIX = $(BINARY_NAME)_unix
RM = rm -f


all: build

build:
	$(GOBUILD) -o $(BINARY_NAME) -v ./src/main.go

test:
	$(GOTEST) -v ./src/vector/...

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

install:
	$(GOGET) -u github.com/ynqa/wego
	$(GOGET) -u github.com/joshua-dev/bigdata/
