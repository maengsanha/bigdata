package hangulsimilarity

import (
	"regexp"
	"strings"
)

// CompareBySyllables returns similarity of given two strings
// based on the common syllables.
func CompareBySyllables(first, second string) float64 {
	var similarity float64
	var common int

	first = strings.TrimSpace(first)
	second = strings.TrimSpace(second)

	syllableCount1, len1 := countBySyllables(first)
	syllableCount2, len2 := countBySyllables(second)

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

// countBySyllables returns a map containing syllable counts of a given sentence and number of syllables.
func countBySyllables(sentence string) (map[string]int, int) {
	syllableMap := make(map[string]int)
	var cnt int

	cleansed := cleanse(sentence)
	segments := strings.Split(cleansed, " ")

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

// min returns minimum value between given two integers.
func min(first, second int) int {
	if first < second {
		return first
	}
	return second
}

// cleanse returns a string with punctuation removed.
func cleanse(s string) string {
	const hangulRegex string = `[.,!?~ㆍ:/\"\']`
	re := regexp.MustCompile(hangulRegex)
	cleansed := re.ReplaceAllString(s, "")
	return cleansed
}

// CompareBySegments returns similarity of given two strings
// based on the common segments.
func CompareBySegments(first, second string) float64 {
	var similarity float64
	var common int

	first = strings.TrimSpace(first)
	second = strings.TrimSpace(second)

	segmentCount1, len1 := countBySegments(first)
	segmentCount2, len2 := countBySegments(second)

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

// countBySegments returns a map containing segment counts of a given sentence and number of segments.
func countBySegments(sentence string) (map[string]int, int) {
	segmentMap := make(map[string]int)
	var cnt int

	cleansed := cleanse(sentence)
	segments := strings.Split(cleansed, " ")

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
