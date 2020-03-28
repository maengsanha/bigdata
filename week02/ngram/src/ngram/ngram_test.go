package ngram

import (
	"fmt"
	"os"
)

func ExampleBigram() {
	hangulSentenceList := [][]string{
		[]string{"슬픔은 슬픔대로 오게 하라.", "기쁨은 기쁨대로 가게 하라."},
		[]string{"우리 눈에 다 보이진 않지만,", "우리 귀에 다 들리진 않지만"},
		[]string{"아무런 기대 없이 맞이하고,", "아무런 기약 없이 헤어져도,"},
	}

	for _, hangulSentences := range hangulSentenceList {
		first, second := hangulSentences[0], hangulSentences[1]
		fmt.Fprintf(os.Stdout, "Similarity by Bigram: %f%%\n", Compare(first, second, 2))
	}
	// Output:
	// Similarity by Bigram: 46.875000%
	// Similarity by Bigram: 68.750000%
	// Similarity by Bigram: 53.125000%
}

func ExampleTrigram() {
	hangulSentenceList := [][]string{
		[]string{"슬픔은 슬픔대로 오게 하라.", "기쁨은 기쁨대로 가게 하라."},
		[]string{"우리 눈에 다 보이진 않지만, ", "우리 귀에 다 들리진 않지만"},
		[]string{"아무런 기대 없이 맞이하고,", "아무런 기약 없이 헤어져도,"},
	}

	for _, hangulSentences := range hangulSentenceList {
		first, second := hangulSentences[0], hangulSentences[1]
		fmt.Fprintf(os.Stdout, "Similarity by Trigram: %f%%\n", Compare(first, second, 3))
	}
	// Output:
	// Similarity by Trigram: 38.709677%
	// Similarity by Trigram: 61.290323%
	// Similarity by Trigram: 48.387097%
}
