package triangleListing;

import org.apache.hadoop.util.ToolRunner;

public class TriangleListingDriver {
  public static void main(String[] args) throws Exception {
    ToolRunner.run(new WedgeMaker(), new String[]{args[0], args[1]+".tmp"});
    ToolRunner.run(new TriangleFinder(), new String[]{args[0], args[1]+".tmp", args[1]});
  }
}
