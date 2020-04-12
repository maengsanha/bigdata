// Package vector implements word vector struct of word2vec.
package vector

import (
	"fmt"
	"strings"
)

func ExampleNew() {
	data := strings.Split("1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 ", " ")
	v := New(data)
	fmt.Println(v)
	// Output:
	// Vector(1.000000 1.000000 1.000000 1.000000 1.000000 1.000000 1.000000 1.000000)
	// L2 Norm: 2.828427
}

func Example_norm() {
	v := New(strings.Split("1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 ", " "))
	fmt.Println(v.norm())
	// Output:
	// 2.8284271247461903
}

func Example_dot() {
	v := New(strings.Split("1.0 0.0 0.0 1.0 0.0 1.0 ", " "))
	u := New(strings.Split(" 0.0 1.0 0.0 0.0 1.0 1.0", " "))
	fmt.Println(v.dot(u))
	// Output:
	// 1
}

func ExampleCos() {
	v := New(strings.Split("1.0 0.0 0.0 1.0 0.0 1.0 ", " "))
	u := New(strings.Split(" 0.0 1.0 0.0 0.0 1.0 1.0", " "))
	fmt.Println(Cos(v, u))
	// Output:
	// 0.33333333333333337
}

func ExampleExpr() {
	v := New(strings.Split("1.0 0.0 0.0 1.0 0.0 1.0 ", " "))
	u := New(strings.Split(" 0.0 1.0 0.0 0.0 1.0 1.0", " "))
	s := "king"
	expr := NewExpr(v, u, s)
	fmt.Println(expr)
	// Output:
	// king	0.333333%
}
