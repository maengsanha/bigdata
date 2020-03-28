// Package ngram implements N-gram model.
package ngram

import (
	"log"
	"math"
	"regexp"
	"strings"
)

type ngram []string

// New returns a new n-gram with the given n and string.
func New(n int, s string) ngram {
	splited := strings.Split(strings.TrimSpace(s), " ")
	var cleansed string
	for _, str := range splited {
		cleansed += cleanse(str)
	}

	var result ngram
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

// count counts the frequency of n-grams.
func count(ngrams ngram) (map[string]int, int) {
	frequency := make(map[string]int)

	for _, gram := range ngrams {
		if _, exists := frequency[gram]; exists {
			frequency[gram]++
		} else {
			frequency[gram] = 1
		}
	}
	return frequency, len(ngrams)
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

	for gram, cnt1 := range firstCount {
		if cnt2, exists := secondCount[gram]; exists {
			common += math.Min(float64(cnt1), float64(cnt2))
		}
	}

	return 100 * common / float64(firstLen)
}
