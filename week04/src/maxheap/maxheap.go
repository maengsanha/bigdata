// Package maxheap implements max heap.
package maxheap

import "github.com/joshua-dev/bigdata/week04/src/vector"

// MaxHeap is a max heap type.
type MaxHeap []vector.Expr

// New returns a new max heap.
func New() *MaxHeap {
	return &MaxHeap{}
}

// Len implements the heap interface.
func (m MaxHeap) Len() int {
	return len(m)
}

// Less implements the heap interface.
func (m MaxHeap) Less(i, j int) bool {
	return m[i].Similarity > m[j].Similarity
}

// Swap implements the heap interface.
func (m MaxHeap) Swap(i, j int) {
	m[i], m[j] = m[j], m[i]
}

// Push implements the heap interface.
func (m *MaxHeap) Push(element interface{}) {
	*m = append(*m, element.(vector.Expr))
}

// Pop implements the heap interface.
func (m *MaxHeap) Pop() interface{} {
	old := *m
	l := old.Len()
	element := old[l-1]
	*m = old[0 : l-1]
	return element
}
