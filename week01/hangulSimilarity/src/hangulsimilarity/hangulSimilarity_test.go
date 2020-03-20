package hangulsimilarity

import "fmt"

func ExampleCompareBySyllables() {
	sentencesList := [][]string{
		[]string{"낮 말은 새가 듣고,", "밤 말은 쥐가 듣는다."},
		[]string{"자라 보고 놀란 가슴", "솥뚜껑 보고 놀란다."},
		[]string{"화성에서 온 남자,", "금성에서 온 여자"},
		[]string{"Go는 재미있다.", "Haskell은 재미있다."},
	}

	for _, sentences := range sentencesList {
		fmt.Printf("공통 음절 갯수에 의한 유사도: %f%%\n", CompareBySyllables(sentences[0], sentences[1]))
	}
	// Output:
	// 공통 음절 갯수에 의한 유사도: 57.142857%
	// 공통 음절 갯수에 의한 유사도: 50.000000%
	// 공통 음절 갯수에 의한 유사도: 71.428571%
	// 공통 음절 갯수에 의한 유사도: 57.142857%
}

func ExampleCompareBySegments() {
	sentencesList := [][]string{
		[]string{"낮 말은 새가 듣고,", "밤 말은 쥐가 듣는다."},
		[]string{"자라 보고 놀란 가슴", "솥뚜껑 보고 놀란다."},
		[]string{"화성에서 온 남자,", "금성에서 온 여자"},
		[]string{"Go는 재미있다.", "Haskell은 재미있다."},
	}

	for _, sentences := range sentencesList {
		fmt.Printf("공통 어절 갯수에 의한 유사도: %f%%\n", CompareBySegments(sentences[0], sentences[1]))
	}
	// Output:
	// 공통 어절 갯수에 의한 유사도: 25.000000%
	// 공통 어절 갯수에 의한 유사도: 33.333333%
	// 공통 어절 갯수에 의한 유사도: 33.333333%
	// 공통 어절 갯수에 의한 유사도: 50.000000%
}
