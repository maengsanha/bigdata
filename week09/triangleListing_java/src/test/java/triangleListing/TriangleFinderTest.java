package triangleListing;

import org.apache.hadoop.util.ToolRunner;

public class TriangleFinderTest {
  public static void main(String[] not_used) throws Exception {
    String args[] = {"src/test/resources/facebook_combined.txt", "src/test/resources/wedge_maker_output", "src/test/resources/facebook_combined.out"};
    ToolRunner.run(new TriangleFinder(), args);
  }
}
