import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Prob1 {
    final static int NUM_PIXELS = 256;
    final static int NUM_CUR_DIGIT = 5000;

    private static BigInteger numFilterOnOff;
    private static BigInteger numFilterMap;
    private static ArrayList<FilterList> validFilters = new ArrayList<FilterList>();
    private static BigInteger numRules;
    private static HashMap<String, Integer> filterIdxMap = new HashMap<>();

    public void countNumRules(int p){
        if (p > NUM_PIXELS) {
            System.out.println("Can't exceed NUM_PIXELS");
            return;
        }
        BigInteger denom = BigInteger.valueOf(1);
        BigInteger numer = BigInteger.valueOf(1);
        numFilterOnOff = BigInteger.valueOf((int)Math.pow(2, p));

        int asdSubtr = 0;
        for (int i = NUM_PIXELS; i > (NUM_PIXELS-p); i--) {
            denom = denom.multiply(BigInteger.valueOf(i));
            numer = numer.multiply(BigInteger.valueOf(p - asdSubtr++));
        }

        numFilterMap = denom.divide(numer);
        numRules = numFilterOnOff.multiply(numFilterMap);
    }

    private BigInteger[] extractCurDigitTbl(BigInteger[] X, int digit){
        BigInteger[] res = new BigInteger[NUM_CUR_DIGIT];
        for (int i = 0; i < NUM_CUR_DIGIT; i++)
            res[i] = X[(NUM_CUR_DIGIT * digit) + i];
        return res;
    }

    private int makeSignature(BigInteger num, Integer[] filter, int p) {
        int signature = 0;
        BigInteger base = BigInteger.valueOf(1);
        for (int i = 0; i < p; i++) {
            if (i > 0)
                signature <<= 1;
            signature |= num.and(base.shiftLeft(filter[i])).shiftRight(filter[i]).intValue();
        }
        return signature;
    }

    private int[][] countMatchToggle(int[][] statCurDigit, BigInteger num, Integer[] filter,
                                         int p, int filterID){
        int signature = makeSignature(num, filter, p);
        statCurDigit[filterID][signature]++;
        return statCurDigit;
    }

    private int[][] calcRuleMatches(BigInteger[] X, int digit, int p, int limit,
                                 int[] filterToggleSW, ArrayList<Integer[]> filters){
        int[][] statCurDigit = new int[numFilterMap.intValue()][numFilterOnOff.intValue()];
        // Extract a table for the current digit
        BigInteger[] curDigitTbl = extractCurDigitTbl(X, digit);

        for (int i = 0; i < numFilterMap.intValue(); i++) {
            for (int k = 0; k < NUM_CUR_DIGIT; k++)
                statCurDigit = countMatchToggle(statCurDigit, curDigitTbl[k], filters.get(i), p, i);
//            if (i % 1000 == 0)
//                System.out.println(i);
        }

        return statCurDigit;
    }

    private int[] generateToggleSW(){
        int[] res = new int[numFilterOnOff.intValue()];
        for (int i = 0; i < res.length; i++)
            res[i] = i;
        return res;
    }

    private ArrayList<Integer[]> generateIndexMap(int p){
        ArrayList<Integer[]> res = new ArrayList<Integer[]>();
        for (int i = 0; i < NUM_PIXELS; i++) {
            if (p == 1) {
                Integer[] indexOf1 = new Integer[p];
                indexOf1[p - 1] = NUM_PIXELS - (i + 1);
                res.add(indexOf1);
            }
            else if (p == 2){
                for (int j = i+1; j < NUM_PIXELS; j++) {
                    Integer[] indexOf1 = new Integer[p];
                    indexOf1[p - 2] = NUM_PIXELS - (i + 1);
                    indexOf1[p - 1] = NUM_PIXELS - (j + 1);
                    res.add(indexOf1);
                }
            }
        }
        return res;
    }

    private void filterValidRules(ArrayList<int[][]> statistics,
                                                   ArrayList<Integer[]> filters, int limit){
        for (int k = 0; k < numFilterMap.intValue(); k++) {
            for (int j = 0; j < numFilterOnOff.intValue(); j++) {
                FilterList fl = new FilterList(j, filters.get(k));
                for (int i = 0; i < statistics.size(); i++) {
                    int[][] statCurDigit = statistics.get(i);
                    if (statCurDigit[k][j] > limit)
                        fl.setOccurrence(i, statCurDigit[k][j]);
                }
                fl.setProbabilities(NUM_CUR_DIGIT);
                validFilters.add(fl);
                filterIdxMap.put(fl.signature, validFilters.size()-1);
            }
        }
    }

    public void selectValidRules(BigInteger[] X, int[] y, int p, int limit){
        ArrayList<int[][]> statistics = new ArrayList<int[][]>();

        // Generate a set of toggle switches for the filter
        int[] filterToggleSW = generateToggleSW();

        // Generate a set of filters
        ArrayList<Integer[]> filters = generateIndexMap(p);

        for (int i = 0; i < 10; i++)
            statistics.add(calcRuleMatches(X, i, p, limit, filterToggleSW, filters));

        filterValidRules(statistics, filters, limit);
    }

    public void solve(BigInteger[] X, int[] y, int p){
        countNumRules(p);

        selectValidRules(X, y, p, 0);
    }

    public static ArrayList<FilterList> getValidFilters() {
        return validFilters;
    }

    public static HashMap<String, Integer> getFilterIdxMap() {
        return filterIdxMap;
    }

    public static BigInteger getNumRules() {
        return numRules;
    }
}
