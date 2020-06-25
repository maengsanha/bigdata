package bigdata;

import org.apache.hadoop.util.ToolRunner;

public class Task1Test {
  public static void main(String[] not_used) throws Exception {
    String[] args = {"/home/joshua/workspace/soc-LiveJournal1.txt", "/home/joshua/workspace/task1.out"};

    ToolRunner.run(new Task1(), args);
  } //  end main.

} //  end Task1Test.
