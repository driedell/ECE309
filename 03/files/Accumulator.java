
public interface Accumulator
{
public String accumulate(String oldTotal, String amount)
       throws IllegalArgumentException;
public void   clear();
}
