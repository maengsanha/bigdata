package preprocessor;

import org.apache.hadoop.util.ToolRunner;
import preprocess.Preprocessor;

public class PreprocessorTest {

  public static void main(String[] not_used) throws Exception {
    String[] args = {"/home/joshua/Downloads/soc-LiveJournal1.txt", "/home/joshua/Downloads/task1.out"};

    ToolRunner.run(new Preprocessor(), args);
  } // end method main.

} // end class PreprocessorTest.
