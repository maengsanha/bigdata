package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"

	"github.com/joshua-dev/bigdata/week02/ngram/src/ngram"
)

func main() {
	args := os.Args

	const invalidCommandLineArgMessage string = `
Invalid command-line arguments:
Usage: ./ngram [arguments]
The arguments are:
2: compare two Hangul strings using bigram.
3: compare two Hangul strings using trigram.
5: compare two Hangul strings using both bigram and trigram.
`

	if len(args) == 1 {
		log.Fatal(invalidCommandLineArgMessage)
	}

	options := args[1:]
	if len(options) != 1 {
		log.Fatal(invalidCommandLineArgMessage)
	}

	option, err := strconv.Atoi(options[0])
	if err != nil {
		log.Fatal(invalidCommandLineArgMessage)
	}

	if option != 2 && option != 3 && option != 5 {
		log.Fatal(invalidCommandLineArgMessage)
	}

	var first, second string
	reader := bufio.NewReader(os.Stdin)

	switch option {
	case 2:
		fmt.Fprintln(os.Stdout, "Start Hangul similarity test using Bigram.\n")
	case 3:
		fmt.Fprintln(os.Stdout, "Start Hangul similarity test using Trigram.\n")
	case 5:
		fmt.Fprintln(os.Stdout, "Start Hangul similarity test using Bigram and Trigram.\n")
	default:
		log.Fatal(invalidCommandLineArgMessage)
	}

	fmt.Fprint(os.Stdout, "첫번째 문장 >> ")
	first, err = reader.ReadString('\n')
	fmt.Fprint(os.Stdout, "두번째 문장 >> ")
	second, err = reader.ReadString('\n')

	switch option {
	case 2:
		fmt.Fprintf(os.Stdout, "음절 Bigram에 의한 유사도: %f%%\n", ngram.Compare(first, second, 2))
	case 3:
		fmt.Fprintf(os.Stdout, "음절 Trigram에 의한 유사도: %f%%\n", ngram.Compare(first, second, 3))
	case 5:
		fmt.Fprintf(os.Stdout, "음절 Bigram에 의한 유사도: %f%%\n", ngram.Compare(first, second, 2))
		fmt.Fprintf(os.Stdout, "음절 Trigram에 의한 유사도: %f%%\n", ngram.Compare(first, second, 3))
	default:
		log.Fatal(invalidCommandLineArgMessage)
	}
}
