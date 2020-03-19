package hangulsimilarity

import "strings"

// CompareBySyllables returns similarity of given two strings
// based on the common syllables.
func CompareBySyllables(first, second string) float64 {
	var similarity float64
	var common int

	syllableCount1, len1 := countInSyllables(first)
	syllableCount2, len2 := countInSyllables(second)

	if len1 < len2 {
		return CompareBySyllables(second, first)
	}

	for syllable, cnt2 := range syllableCount2 {
		if cnt1, exists := syllableCount1[syllable]; exists {
			common += min(cnt1, cnt2)
		} else {
			continue
		}
	}

	similarity = 100 * float64(common) / float64(len2)

	return similarity
}

func countInSyllables(sentence string) (map[string]int, int) {
	var syllableMap = map[string]int{}
	var cnt int

	segments := strings.Split(sentence, " ")

	for _, segment := range segments {
		syllables := strings.Split(segment, "")
		for _, syllable := range syllables {
			cnt++
			if _, exists := syllableMap[syllable]; !exists {
				syllableMap[syllable] = 1
			} else {
				syllableMap[syllable]++
			}
		}
	}

	return syllableMap, cnt
}

func min(first, second int) int {
	if first < second {
		return first
	}
	return second
}

// CompareBySegments returns similarity of given two strings
// based on the common segments.
func CompareBySegments(first, second string) float64 {
	var similarity float64
	var common int

	segmentCount1, len1 := countInSegments(first)
	segmentCount2, len2 := countInSegments(second)

	if len1 < len2 {
		return CompareBySegments(second, first)
	}

	for segment, cnt2 := range segmentCount2 {
		if cnt1, exists := segmentCount1[segment]; exists {
			common += min(cnt1, cnt2)
		} else {
			continue
		}
	}

	similarity = 100 * float64(common) / float64(len2)

	return similarity
}

func countInSegments(sentence string) (map[string]int, int) {
	var segmentMap = map[string]int{}
	var cnt int

	segments := strings.Split(sentence, " ")

	for _, segment := range segments {
		cnt++
		if _, exists := segmentMap[segment]; !exists {
			segmentMap[segment] = 1
		} else {
			segmentMap[segment]++
		}
	}

	return segmentMap, cnt
}
