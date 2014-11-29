
public class TAaccumulatorTester
{
public static void main(String[] args)
  {
  Accumulator a = new AccumulatingCalculator();
  // TEST #1
  System.out.println("Test #1: Entering 10");
  try {
	  String newTotal = a.accumulate("10");
      if (newTotal.equals("10")
       || newTotal.equals("10.00"))
          System.out.println("Good! Total was " + newTotal);
       else
         {
	     System.out.println("Oops! Returned total was " + newTotal);
	     return; // stop testing!
         }
      }
  catch(Exception e)
      {	  
	  if (e instanceof IllegalArgumentException)
         {
	     System.out.println("Oops! accumulate() threw an IllegalArgumentException with message: " + e.getMessage());
	     return; // stop testing
         }
	  else
	     {
	     System.out.println("Oops! accumulate() threw an exception, but it wasn't an IllegalArgumentException!: " + e);
	     return; // stop testing
	     }
      }
  
  // TEST #2
  System.out.println("Test #2: Entering 5.00");
  try {
	  String newTotal = a.accumulate("5.00");
      if (newTotal.equals("15")
       || newTotal.equals("15.00"))
          System.out.println("Good! Total was " + newTotal);
       else
         {
	     System.out.println("Oops! Returned total was " + newTotal);
	     return; // stop testing!
         }
      }
  catch(Exception e)
      {	  
	  if (e instanceof IllegalArgumentException)
         {
	     System.out.println("Oops! accumulate() threw an IllegalArgumentException with message: " + e.getMessage());
	     return; // stop testing
         }
	  else
	     {
	     System.out.println("Oops! accumulate() threw an exception, but it wasn't an IllegalArgumentException!: " + e);
	     return; // stop testing
	     }
      }

  // TEST #3
  System.out.println("Test #3: Calling clear()");
  try {
	  a.clear();
      System.out.println("Good! No exception was thrown.");
      }
  catch(Exception e)
      {	  
	  if (e instanceof IllegalArgumentException)
         {
	     System.out.println("Oops! clear() threw an IllegalArgumentException with message: " + e.getMessage());
	     return; // stop testing
         }
	  else
	     {
	     System.out.println("Oops! clear() threw an exception, but it wasn't an IllegalArgumentException!: " + e);
	     return; // stop testing
	     }
      }

  // TEST #4
  System.out.println("Test #4: Entering 7.25");
  try {
	  String newTotal = a.accumulate("7.25");
      if (newTotal.equals("7.25"))
          System.out.println("Good! Total was " + newTotal);
       else
         {
	     System.out.println("Oops! Returned total was " + newTotal);
	     return; // stop testing!
         }
      }
  catch(Exception e)
      {	  
	  if (e instanceof IllegalArgumentException)
         {
	     System.out.println("Oops! accumulate() threw an IllegalArgumentException with message: " + e.getMessage());
	     return; // stop testing
         }
	  else
	     {
	     System.out.println("Oops! accumulate() threw an exception, but it wasn't an IllegalArgumentException!: " + e);
	     return; // stop testing
	     }
      }

  // TEST #5
  System.out.println("Test #5: Entering 5.oo (letter 'o' instead of zero!)");
  try {
	  String newTotal = a.accumulate("5.oo");
      System.out.println("Oops! accumulate should have thrown an IllegalArgumentException");
      System.out.println("Instead, it returned a new total of " + newTotal);
      return; // stop testing!
      }
  catch(Exception e)
      {	  
	  if (e instanceof IllegalArgumentException)
         {
		 String errMsg = e.getMessage();
		 if (errMsg.equals(CalculatorConstants.NOT_NUMERIC))
			System.out.println("Good! accumulate() threw the correct exception with the correct error message!");
		  else
		    {  
	        System.out.println("Oops! accumulate() threw an IllegalArgumentException, but with message: " + e.getMessage());
	        System.out.println("The error message should have been: " + CalculatorConstants.NOT_NUMERIC);
	        return;
		    }
         }
	   else
	     {
	     System.out.println("Oops! accumulate() threw an exception, but it wasn't an IllegalArgumentException!: " + e);
	     return; // stop testing
	     }
      }
  
  System.out.println("Congratulations! Your AccumulatingCalculator program passed all 5 sample tests!");
  }

}
