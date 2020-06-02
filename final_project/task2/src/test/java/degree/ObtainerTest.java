package degree;

import org.apache.hadoop.util.ToolRunner;

public class ObtainerTest {
  public static void main(String[] not_used) throws Exception{
    // TODO: Set file path of Live Journal text and temporary output file.
    String[] args = {"", ""};

    ToolRunner.run(new Obtainer(), args);
  }
}
