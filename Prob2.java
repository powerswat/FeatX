import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Prob2 {
    private static final int NUM_PIXELS = 256;
    private static final int NUM_DATA = 50000;

    private static int[] prediction;
    private static int[][] evalTable = new int[10][10];

    FileProcessor fp = new FileProcessor();

    private double[] locateProb1p(ArrayList<FilterList> filters, HashMap<String, Integer> filterIdxMap,
                                  BigInteger x, int pixelNum){

        BigInteger mask = BigInteger.valueOf(1).shiftLeft(pixelNum);
        int value = (x.and(mask).shiftRight(pixelNum)).intValue();
        String signature = Integer.toString(value) + "_" + Integer.toString(pixelNum);

        int filterIdx = filterIdxMap.get(signature);
        FilterList selectedFilter = filters.get(filterIdx);

        return selectedFilter.probabilities;
    }

    private double[] locateProb2p(ArrayList<FilterList> filters, HashMap<String, Integer> filterIdxMap,
                                  BigInteger x, int[] pixelNums){
        BigInteger mask;
        int value = 0;
        for (int i = 0; i < pixelNums.length; i++) {
            mask = BigInteger.valueOf(1).shiftLeft(pixelNums[i]);
            if (i > 0)
                value <<= 1;
            value |= (x.and(mask).shiftRight(pixelNums[i])).intValue();
        }
        String signature = Integer.toString(value);
        for (int i = 0; i < pixelNums.length; i++)
            signature += "_" + Integer.toString(pixelNums[i]);

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
                      BigInteger[] X, int[] y, int p){
        String predFilename = "data/prediction_" + Integer.toString(p) + ".csv";
        File predFile = new File(predFilename);

        if (!predFile.exists()) {
            prediction = new int[NUM_DATA];
            for (int i = 0; i < NUM_DATA; i++) {
                double[] curDigitProbs = new double[10];
                if (p == 1)
                    for (int j = 0; j < NUM_PIXELS; j++) {
                        double[] curPixProbs
                                = locateProb1p(filters, filterIdxMap, X[i], NUM_PIXELS - (j + 1));
                        curDigitProbs = accumulateProb(curDigitProbs, curPixProbs);
                    }
                else if (p == 2)
                    for (int j = 0; j < NUM_PIXELS; j++) {
                        for (int k = j + 1; k < NUM_PIXELS; k++) {
                            int[] pixelNums = {NUM_PIXELS - (j + 1), NUM_PIXELS - (k + 1)};
                            double[] curPixProbs = locateProb2p(filters, filterIdxMap, X[i], pixelNums);
                            curDigitProbs = accumulateProb(curDigitProbs, curPixProbs);
                        }
                    }
                predictDigit(prediction, curDigitProbs, i);
            }
            ArrayList<String[]> writeData = new ArrayList<String[]>();
            for (int i = 0; i < prediction.length; i++) {
                String[] strFormat = new String[1];
                strFormat[0] = Integer.toString(prediction[i]);
                writeData.add(strFormat);
            }
            fp.writeCSV(writeData, predFilename, false);
        } else {
            fp.readCSV(predFilename, false);
            ArrayList<String> arrListData = fp.getRows();
            prediction = new int[y.length];
            for (int i = 0; i < arrListData.size(); i++) {
                String str = arrListData.get(i);
                prediction[i] = Integer.parseInt(str);
            }
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
