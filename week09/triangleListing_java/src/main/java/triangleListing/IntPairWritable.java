package triangleListing;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntPairWritable implements WritableComparable<IntPairWritable> {
  int u, v;

  public void set(int u, int v) {
    this.u = u;
    this.v = v;
  } // end method set.

  @Override
  public int compareTo(IntPairWritable o) {
    if (this.u != o.u) return Integer.compare(this.u, o.u);
    return Integer.compare(this.v, o.v);
  } // end method compareTo.

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeInt(this.u);
    dataOutput.writeInt(this.v);
  } // end method write.

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.u = dataInput.readInt();
    this.v = dataInput.readInt();
  } // end method readFields.

  @Override
  public String toString() {
    return this.u + "\t" + this.v;
  } // end method toString.

} // end class IntPairWritable.
