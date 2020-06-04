package degree;

import org.apache.hadoop.util.ToolRunner;

public class ObtainerTest {
  public static void main(String[] not_used) throws Exception{
    String[] args = {"/home/joshua/Downloads/task1.out/part-r-00000", "/home/joshua/Downloads/task2.out"};

    ToolRunner.run(new Obtainer(), args);
  }
}
