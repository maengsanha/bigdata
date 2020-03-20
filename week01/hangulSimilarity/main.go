package main

import (
	"bufio"
	"fmt"
	"os"

	"github.com/joshua-dev/hangulsimilarity"
)

func main() {
	var first, second string
	reader := bufio.NewReader(os.Stdin)

	fmt.Fprint(os.Stdout, "첫번째 문장 >> ")
	first, err := reader.ReadString('\n')
	fmt.Fprint(os.Stdout, "두번째 문장 >> ")
	second, err = reader.ReadString('\n')
	if err != nil {
		panic(err)
	}
	fmt.Fprintf(os.Stdout, "공통 음절 갯수에 의한 유사도: %f%%\n", hangulsimilarity.CompareBySyllables(first, second))
	fmt.Fprintf(os.Stdout, "공통 어절 갯수에 의한 유사도: %f%%\n", hangulsimilarity.CompareBySegments(first, second))
}
