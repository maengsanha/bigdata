package preprocessor;

import org.apache.hadoop.util.ToolRunner;
import preprocess.Preprocessor;

public class PreprocessorTest {

  public static void main(String[] not_used) throws Exception {
    String[] args = {"src/test/resources/soc-LiveJournal1.txt", "src/test/resources/soc-LiveJournal1.txt.out"};

    ToolRunner.run(new Preprocessor(), args);
  } // end method main.

} // end class PreprocessorTest.
