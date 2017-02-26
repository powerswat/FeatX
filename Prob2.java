import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Prob2 {
    private static final int NUM_PIXELS = 256;
    private static final int NUM_DATA = 50000;

    private static int[] prediction;
    private static int[][] evalTable = new int[10][10];

    private double[] locateProb(ArrayList<FilterList> filters, HashMap<String, Integer> filterIdxMap,
                                  BigInteger x, int pixelNum){

        BigInteger mask = BigInteger.valueOf(1).shiftLeft(pixelNum);
        int value = (x.and(mask).shiftRight(pixelNum)).intValue();
        String signature = Integer.toString(value) + "_" + Integer.toString(pixelNum);
        int filterIdx = filterIdxMap.get(signature);
        FilterList selectedFilter = filters.get(filterIdx);

        return selectedFilter.probabilities;
    }

    private double[] accumulateProb(double[] curDigitProbs, double[] curPixProbs){
        boolean isFirst = true;
        for (int i = 0; i < 10; i++) {
            if (curDigitProbs[i] > 0) {
                isFirst = false;
                break;
            }
        }

        for (int i = 0; i < 10; i++) {
            if (curDigitProbs[i] == 0 && isFirst == false)
                continue;
            curDigitProbs[i] += Math.log(curPixProbs[i]) / Math.log(2);
        }
        return curDigitProbs;
    }

    private void predictDigit(int[] prediction, double[] curDigitProbs, int dataIdx){
        double maxVal = Double.NEGATIVE_INFINITY;
        int maxIdx = -1;

        for (int i = 0; i < curDigitProbs.length; i++) {
            if (maxVal < curDigitProbs[i]) {
                maxVal = curDigitProbs[i];
                maxIdx = i;
            }
        }
        prediction[dataIdx] = maxIdx;
    }

    private void evaluateDigit(int[] y){
        for (int i = 0; i < y.length; i++)
            evalTable[y[i]][prediction[i]]++;
    }

    public void solve(ArrayList<FilterList> filters, HashMap<String, Integer> filterIdxMap,
                      BigInteger[] X, int[] y){
        prediction = new int[NUM_DATA];
        for (int i = 0; i < NUM_DATA; i++) {
            double[] curDigitProbs = new double[10];
            for (int j = 0; j < NUM_PIXELS; j++) {
                double[] curPixProbs
                        = locateProb(filters, filterIdxMap, X[i], NUM_PIXELS-(j+1));
                curDigitProbs = accumulateProb(curDigitProbs, curPixProbs);
            }
            predictDigit(prediction, curDigitProbs, i);
        }

        evaluateDigit(y);

        for (int i = 0; i < evalTable.length; i++){
            int numData = 0;
            for (int j = 0; j < evalTable.length; j++) {
                numData += evalTable[i][j];
                System.out.print(evalTable[i][j] + "\t");
            }
            System.out.println(numData);
        }
    }
}
