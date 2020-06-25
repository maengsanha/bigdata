package bigdata;

import org.apache.hadoop.util.ToolRunner;

public class Task2Test {
  public static void main(String[] not_used) throws Exception {
    String[] args = {"/home/joshua/workspace/task1.out/part-r-00000", "/home/joshua/workspace/task2.out"};

    ToolRunner.run(new Task2(), args);
  } //  end main.

} //  end Task2Test.
