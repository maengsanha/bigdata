// Package vector implements word vector struct of word2vec.
package vector

import (
	"fmt"
	"math"
	"strconv"
)

// Vector is a type which represents a word vector.
type Vector []float64

// New returns a new vector with a given data.
func New(data []string) Vector {
	v := make([]float64, len(data))
	for index, f := range data {
		ele, _ := strconv.ParseFloat(f, 64)
		v[index] = ele
	}
	// var syncer sync.WaitGroup
	// syncer.Add(len(data))
	// for index, f := range data {
	// 	go func(i int, f string) {
	// 		defer syncer.Done()
	// 		v[i], _ = strconv.ParseFloat(f, 64)
	// 	}(index, f)
	// }
	// syncer.Wait()
	return Vector(v)
}

// String implements the fmt.Stringer interface.
func (v Vector) String() (str string) {
	str = "Vector("
	for index := 0; index < len(v)-1; index++ {
		str += fmt.Sprintf("%f ", v[index])
	}
	str += fmt.Sprintf("%f)\nL2 Norm: %f", v[len(v)-1], v.norm())
	return
}

// Cos computes the cosine similarity of the given vectors.
func Cos(v, u Vector) float64 {
	return v.dot(u) / (v.norm() * u.norm())
}

// norm computes the L2 Norm of v.
func (v Vector) norm() float64 {
	var scala float64
	for _, f := range v {
		scala += math.Pow(f, 2)
	}
	return math.Sqrt(scala)
}

// dot computes the dot product with u.
func (v Vector) dot(u Vector) (scala float64) {
	if len(v) != len(u) || len(v) < 1 || len(u) < 1 {
		return 0
	}
	for index := 0; index < len(v); index++ {
		scala += v[index] * u[index]
	}
	return
}

// Word2Sent returns the average vector of the given vectors.
func Word2Sent(vectors []Vector) Vector {
	if len(vectors) < 1 {
		return nil
	}
	v := make([]float64, len(vectors[0]))
	for i := 0; i < len(vectors[0]); i++ {
		var f float64
		for j := 0; j < len(vectors); j++ {
			if len(vectors[j]) < 1 {
				return nil
			}
			f += vectors[j][i]
		}
		v = append(v, f)
	}

	for index, f := range v {
		v[index] = f / float64(len(vectors))
	}
	return Vector(v)
}

// Expr is an expression type.
type Expr struct {
	Sentence   string
	Similarity float64
}

// String implements fmt.Stringer interface.
func (e Expr) String() string {
	return fmt.Sprintf("%s\t%f%%", e.Sentence, 100*e.Similarity)
}

// NewExpr returns a new Expr.
func NewExpr(v, u Vector, sentence string) Expr {
	similarity := Cos(v, u)
	return Expr{
		Sentence:   sentence,
		Similarity: similarity,
	}
}
