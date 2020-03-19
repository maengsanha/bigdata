package main

import (
	"fmt"
	"os"
)

func main() {
	var first, second string
	fmt.Fscanf(os.Stdin, "%s %s", &first, &second)
	fmt.Fprintf(os.Stdout, "%s %s", first, second)
}
