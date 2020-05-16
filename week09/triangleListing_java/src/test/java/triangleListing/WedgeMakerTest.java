package triangleListing;

import org.apache.hadoop.util.ToolRunner;

public class WedgeMakerTest {

  public static void main(String[] not_used) throws Exception {
    String args[] = {"src/test/resources/facebook_combined.txt", "src/test/resources/wedge_maker_output"};
    ToolRunner.run(new WedgeMaker(), args);
  } // end method main.

} // end class WedgeMakerTest.
