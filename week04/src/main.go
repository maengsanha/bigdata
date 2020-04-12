package main

import (
	"bufio"
	"container/heap"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/joshua-dev/bigdata/week04/src/maxheap"
	"github.com/joshua-dev/bigdata/week04/src/vector"
)

// sentenceNvector stores a sentence and its vector.
type sentenceNvector struct {
	sentence string
	vector   vector.Vector
}

// wrtie appends sentence to the end of the file.
func write(sentence, file string) error {
	origin := fmt.Sprintf("%s\n", sentence)
	f, err := os.OpenFile(file, os.O_APPEND|os.O_WRONLY, 0644)
	if err != nil {
		return err
	}
	defer f.Close()
	if _, err = f.WriteString(origin); err != nil {
		return err
	}
	return nil
}

func main() {
	const (
		inputData  string = "./src/KCCq28_Korean_sentences_UTF8.txt"
		outputData string = "./vectors.txt"
	)

	runtime.GOMAXPROCS(runtime.NumCPU())

	args := os.Args
	if len(args) == 1 {
		panic("command line argument needed.")
	} else if len(args) != 2 {
		panic("command line argument must be 1.")
	}
	argv, err := strconv.Atoi(args[1])
	if err != nil {
		panic(err)
	}

	var input string
	reader := bufio.NewReader(os.Stdin)

	fmt.Fprint(os.Stdout, "Input a Hangul sentence >> ")
	input, err = reader.ReadString('\n')
	if err != nil {
		panic(err)
	}

	origin := strings.TrimSpace(input)
	if err = write(origin, inputData); err != nil {
		log.Fatal(err)
	}

	vector.Train(inputData, outputData)

	startTime := time.Now()

	vectors := make(map[string]vector.Vector)

	outputFile, err := os.Open(outputData)
	if err != nil {
		panic(err)
	}
	defer outputFile.Close()

	scanner := bufio.NewScanner(outputFile)
	for scanner.Scan() {
		txt := strings.Split(strings.TrimSpace(scanner.Text()), " ")
		word := txt[0]
		data := txt[1:]
		vector := vector.New(data)
		vectors[word] = vector
	}

	var sentenceNvectors []sentenceNvector

	inputFile, err := os.Open(inputData)
	if err != nil {
		panic(err)
	}
	defer inputFile.Close()

	var originVector vector.Vector

	scanner = bufio.NewScanner(inputFile)
	for scanner.Scan() {
		sentence := strings.TrimSpace(scanner.Text())
		words := strings.Split(sentence, " ")
		wordVectors := make([]vector.Vector, len(words))
		for index, word := range words {
			wordVectors[index] = vectors[word]
		}
		sentenceVector := vector.Word2Sent(wordVectors)
		if sentenceVector != nil {
			if sentence == origin {
				originVector = sentenceVector
			} else {
				temp := sentenceNvector{
					sentence: sentence,
					vector:   sentenceVector,
				}
				sentenceNvectors = append(sentenceNvectors, temp)
			}
		}
	}

	expressions := make(chan vector.Expr, len(sentenceNvectors))
	var syncer sync.WaitGroup
	syncer.Add(len(sentenceNvectors))

	for index := 0; index < len(sentenceNvectors); index++ {
		go func(i int, ch chan vector.Expr) {
			defer syncer.Done()
			expression := vector.NewExpr(originVector, sentenceNvectors[i].vector, sentenceNvectors[i].sentence)
			ch <- expression
		}(index, expressions)
	}
	defer close(expressions)
	syncer.Wait()

	maxHeap := maxheap.New()
	heap.Init(maxHeap)

	for i := 0; i < len(expressions); i++ {
		heap.Push(maxHeap, <-expressions)
	}

	for i := 0; i < argv; i++ {
		expr := heap.Pop(maxHeap).(vector.Expr)
		fmt.Fprintf(os.Stdout, "%d. %s\n", i+1, expr)
	}

	elapsedTime := time.Since(startTime)
	fmt.Fprintf(os.Stdout, "\nElapsed Time:\t%s\n\n", elapsedTime)
}
