package vector

import (
	"os"

	"github.com/ynqa/wego/pkg/builder"
	"github.com/ynqa/wego/pkg/model/word2vec"
)

// Train trains word vectors using wego APIs.
func Train(data, output string) error {
	b := builder.NewWord2vecBuilder()

	b.Dimension(10).Window(5).Model(word2vec.CBOW).Optimizer(word2vec.NEGATIVE_SAMPLING).NegativeSampleSize(5).Verbose()

	model, err := b.Build()
	if err != nil {
		return err
	}

	input, err := os.Open(data)
	if err != nil {
		return err
	}

	if err = model.Train(input); err != nil {
		return err
	}

	if err = model.Save(output); err != nil {
		return err
	}

	return nil
}
