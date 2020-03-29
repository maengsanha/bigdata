// Package ngram implements N-gram model.
package ngram

import (
	"log"
	"math"
	"regexp"
	"strings"
)

// Ngram is n-gram type.
type Ngram []string

// New returns a new n-gram with the given n and string.
func New(n int, s string) Ngram {
	splited := strings.Split(strings.TrimSpace(s), " ")
	var cleansed string
	for _, str := range splited {
		cleansed += cleanse(str)
	}

	var result Ngram
	for i := 0; i <= len(cleansed)-n; i++ {
		result = append(result, cleansed[i:i+n])
	}
	return result
}

// cleanse returns a string with punctuation removed.
func cleanse(s string) string {
	const hangulRegex string = `[.,!?~ㆍ:/\"\']`
	re := regexp.MustCompile(hangulRegex)
	return re.ReplaceAllString(s, "")
}

// count counts the frequency of tokens.
func count(tokens Ngram) (map[string]int, int) {
	frequency := make(map[string]int)

	for _, token := range tokens {
		if _, exists := frequency[token]; exists {
			frequency[token]++
		} else {
			frequency[token] = 1
		}
	}
	return frequency, len(tokens)
}

// Compare compares two hangul string with ngram.
func Compare(first, second string, n int) float64 {
	firstCount, firstLen := count(New(n, first))
	secondCount, secondLen := count(New(n, second))

	if firstLen > secondLen {
		return Compare(second, first, n)
	}

	if firstLen < 1 {
		log.Fatalf("Improper sentence of length 0: %s\n", first)
	}

	var common float64

	for token, cnt1 := range firstCount {
		if cnt2, exists := secondCount[token]; exists {
			common += math.Min(float64(cnt1), float64(cnt2))
		}
	}

	return 100 * common / float64(firstLen)
}
