package ua.com.foxminded.division.math;

import java.util.ArrayList;
import java.util.Collections;

import ua.com.foxminded.division.math.Result.Step;

public class Divider {
    public Result result = new Result();
    private int indexDigitsOfDividend = 0;
    private int indexOfZeroInQuotient = 0;
    private boolean shiftDigit = false;
    private int dividend;
    private int divisor;
    
    public Result divide(int dividend, int divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
        result.setDividend(dividend);
        result.setDivisor(divisor);
        int i = 0;
        splitDividendIntoDigits(dividend);
        findFirstPartialDividend(i);
        indexDigitsOfDividend = result.countDigitsInArray(result.partialDividend);
        result.step.digitsOfQuotient.add(div(result.partialDividend.get(i), divisor));
        result.step.setProduct(multiply(result.step.digitsOfQuotient.get(i), divisor));
        result.step.setRemainder(result.partialDividend.get(i) - result.step.getProduct());
        if(indexDigitsOfDividend < result.digitsOfDividend.size()) {
            result.step.setIntegralPartialDividend(findFirstIntegralPartialDividend(i));
        } else if(indexDigitsOfDividend == result.digitsOfDividend.size() && shiftDigit == false) { 
            result.addStep();
            return result; 
        } else if(indexDigitsOfDividend == result.digitsOfDividend.size() && shiftDigit == true && result.step.getIntegralPartialDividend() >= divisor) {
            result.step.digitsOfQuotient.add(div(result.step.getIntegralPartialDividend(), divisor));
            result.step.setProduct(multiply(result.step.digitsOfQuotient.get(i), divisor));
            result.step.setRemainder(result.partialDividend.get(i) - result.step.getProduct());
            result.addStep();
            return result;
        } else if(indexDigitsOfDividend == result.digitsOfDividend.size() && shiftDigit == true && result.step.getIntegralPartialDividend() < divisor) {
            result.addStep();
            return result;
        }
        result.addStep();
       
        while(indexDigitsOfDividend <= result.digitsOfDividend.size()) {
            Result.Step step = new Result().new Step();
            if (result.arrayOfSteps.get(i).getIntegralPartialDividend() < divisor) { break; }
            step.digitsOfQuotient.add(div(result.arrayOfSteps.get(i).getIntegralPartialDividend(), divisor));
            if (result.countDigitsInIntegralPartialDividend(0) - result.countDigitsInRemainder(i) > 1 && result.arrayOfSteps.get(i).getRemainder() != 0) { indexOfZeroInQuotient = i+1; }
                //i++;
            if (step.digitsOfQuotient.get(0) == 0) { ///STEP
                result.step.setProduct(multiply(step.digitsOfQuotient.get(0), divisor));
            } else { 
                step.setProduct(multiply(step.digitsOfQuotient.get(0), divisor));
            }
            step.setRemainder(result.arrayOfSteps.get(i).getIntegralPartialDividend() - step.getProduct());
            i++;
            if(indexDigitsOfDividend < result.digitsOfDividend.size()) {
                step.setIntegralPartialDividend(findIntegralPartialDividend(step));
            } else { indexDigitsOfDividend++; }
            
            result.arrayOfSteps.add(step);
        }
        
        if(indexOfZeroInQuotient != 0) { 
//            result.digitsOfQuotient.add(indexOfZeroInQuotient, 0);
            result.arrayOfSteps.get(indexOfZeroInQuotient-1).digitsOfQuotient.add(0, 0);
        }
        return result;
    }
    
    private void findFirstPartialDividend(int i) {
        String tempDividendString = "";
        int tempDividendInt; 
        if(i == 0) {    // Looking for the first value for ArrayList<Integer> partialDividend
            if (div(result.digitsOfDividend.get(i), divisor) > 0 || result.digitsOfDividend.get(i) == 0) {
                result.partialDividend.add(result.digitsOfDividend.get(i));
            } else {
                tempDividendString = Integer.toString(result.digitsOfDividend.get(i));
                tempDividendInt = Integer.parseInt(tempDividendString);
                i++;
                if(i < result.digitsOfDividend.size()) {
                    while(div(tempDividendInt, divisor) == 0) {
                        if (i == result.digitsOfDividend.size()) { break; }
                        tempDividendString = tempDividendString + Integer.toString(result.digitsOfDividend.get(i));
                        tempDividendInt = Integer.parseInt(tempDividendString); 
                       i++; 
                    }
                }
                result.setFirstPartialDividend(tempDividendInt); /////!!!!!
                result.partialDividend.add(tempDividendInt);
            } 
        }
    }
    
    private void splitDividendIntoDigits(int dividend) {
        if(dividend == 0) {
            result.digitsOfDividend.add(0);
        }else {
            for(int j=10; dividend%j != 0 || dividend != 0;) {
                result.digitsOfDividend.add(dividend%j);
                    dividend = dividend-(dividend%j);
                    dividend/=j; 
            }
            Collections.reverse(result.digitsOfDividend);
        }  
    }
    
    private int findFirstIntegralPartialDividend(int i) {
        String tempString = "0";
      if (result.step.getRemainder() >= divisor) { ///STEP
          return result.step.getRemainder(); ///STEP
      } else { // Concatenate remainder and partialDividend 
          if (result.step.getRemainder() != 0) { ///STEP 
              tempString = Integer.toString(result.step.getRemainder()); ///STEP 
          } else if (indexDigitsOfDividend < result.digitsOfDividend.size()) {
              tempString = Integer.toString(result.digitsOfDividend.get(indexDigitsOfDividend));
              indexDigitsOfDividend++;
          }
          while (Integer.parseInt(tempString) <= divisor && indexDigitsOfDividend < result.digitsOfDividend.size()) {
              tempString += Integer.toString(result.digitsOfDividend.get(indexDigitsOfDividend));
              indexDigitsOfDividend++;
          }
          if (result.step.getRemainder() == 0 && indexDigitsOfDividend == result.digitsOfDividend.size()) { ///STEP
              result.step.digitsOfQuotient.add(0); ///STEP
              shiftDigit = true;
          }
          return Integer.parseInt(tempString); 
        } 
    }
    
    private int findIntegralPartialDividend(Step step) {
        String tempString = "0";
            if (step.getRemainder() >= divisor) { ///STEP
                return step.getRemainder(); ///STEP
            } else { // Concatenate remainder and partialDividend
                if (step.getRemainder() != 0) { ///STEP 
                    tempString = Integer.toString(step.getRemainder()); ///STEP 
                } else if (indexDigitsOfDividend < result.digitsOfDividend.size()) {
                    tempString = Integer.toString(result.digitsOfDividend.get(indexDigitsOfDividend));
                    indexDigitsOfDividend++;
                }
        
                while (Integer.parseInt(tempString) <= divisor && indexDigitsOfDividend < result.digitsOfDividend.size()) {
                    tempString += Integer.toString(result.digitsOfDividend.get(indexDigitsOfDividend));
                    indexDigitsOfDividend++;
                }
                if (step.getRemainder() == 0 && indexDigitsOfDividend == result.digitsOfDividend.size()) { ///STEP
                    step.digitsOfQuotient.add(0); ///STEP
                    shiftDigit = true;
                }
                return Integer.parseInt(tempString);
            } 
    }
    
    public int div(int dividend, int divisor) {
        int counter = 0;
        if(divisor == 0) { throw new ArithmeticException("Dividing a number by 0"); }
        if(dividend == 0 || dividend < divisor) {
            return 0;
        } else {
            while(dividend >= divisor) {
                dividend -= divisor;
                counter++;
            }
            return counter;
        }
    }
    
    public int multiply(int a, int b) {
        int sum = a;
        if(a == 0 || b == 0) {
            return 0;
        } else {
            for(int i=1; i < b; i++) {
                sum +=a;
            }
            return sum;
        }
    }
}
