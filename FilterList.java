import java.util.InputMismatchException;

public class FilterList {
    public String signature;
    public int toggleIdx;
    public Integer[] filterIdcs;
    public int[] occurrences = new int[10];
    public double[] probabilities = new double[10];

    public FilterList(int toggleIdx, Integer[] filterIdcs){
        this.toggleIdx = toggleIdx;
        this.filterIdcs = filterIdcs;
        generateSignature(this.toggleIdx, this.filterIdcs);
    }

    public void generateSignature(int toggleIdx, Integer[] filterIdcs){
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(toggleIdx) + "_");

        for (int i = 0; i < filterIdcs.length-1; i++)
            sb.append(filterIdcs[i].toString() + "_");
        sb.append(filterIdcs[filterIdcs.length-1].toString());

        this.signature = sb.toString();
    }

    public void setOccurrence(int digit, int occurrence) {
        this.occurrences[digit] = occurrence;
    }

    public void setProbabilities(int numDigit) {
        int numRule = 0;
        for (int i = 0; i < 10; i++)
            numRule += occurrences[i];
        double pRule = (double)numRule / (double)numDigit;
        for (int i = 0; i < 10; i++)
            this.probabilities[i] = ((double)occurrences[i] / (double)numRule) / pRule;
    }
}
