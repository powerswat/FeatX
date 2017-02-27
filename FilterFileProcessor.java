import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Filter;

/**
 * Created by powerswat on 2/26/17.
 */
public class FilterFileProcessor {

    public HashMap<String, Integer> readHashMapCSV(String filename){
        HashMap<String, Integer> res = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String str = br.readLine();
            String[] tokens = str.split(",");
            if (!tokens[0].equals("String") || !tokens[1].equals("Integer"))
                return null;
            while ((str = br.readLine()) != null){
                if (str.equals(""))
                    continue;
                tokens = str.split(",");
                res.put(tokens[0], Integer.parseInt(tokens[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void writeHashMapCSV(HashMap data, String filename, String keyType, String valueType) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filename));
            br.write(keyType + "," + valueType + "\n");
            Set keySet = data.keySet();

            for (Iterator it = keySet.iterator(); it.hasNext();)
                if (keyType.equals("String") && valueType.equals("Integer")) {
                    String key = (String) it.next();
                    String value = Integer.toString((Integer) data.get(key));
                    br.write(key + "," + value + "\n");
                }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FilterList> readArrayList2dCSV(String filterFiltername, String type){
        ArrayList<FilterList> res = new ArrayList<FilterList>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filterFiltername));

            String str = "";
            FilterList fl = new FilterList();;
            while ((str = br.readLine()) != null){
                if (str.equals(""))
                    continue;
                String[] tokens = str.split(",");

                if (tokens[0].equals("signature")) {
                    fl = new FilterList();
                    fl.signature = tokens[2];
                }
                else if (tokens[0].equals("toggleIdx"))
                    fl.toggleIdx = Integer.parseInt(tokens[2]);
                else if (tokens[0].equals("filterIdcs")){
                    Integer[] filterIdcs = new Integer[tokens.length-2];
                    for (int i = 0; i < filterIdcs.length; i++)
                        filterIdcs[i] = Integer.parseInt(tokens[i+2]);
                    fl.filterIdcs = filterIdcs;
                 } else if (tokens[0].equals("occurrence")){
                    int[] occurrences = new int[10];
                    for (int i = 0; i < occurrences.length; i++)
                        occurrences[i] = Integer.parseInt(tokens[i+2]);
                    fl.occurrences = occurrences;
                }  else if (tokens[0].equals("probabilities")){
                    double[] probabilities = new double[10];
                    for (int i = 0; i < probabilities.length; i++)
                        probabilities[i] = Double.parseDouble(tokens[i + 2]);
                    fl.probabilities = probabilities;
                    res.add(fl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void writeArrayList2dCSV(ArrayList data, String filename, String elementType){
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < data.size(); i++) {
                if (elementType.equals("FilterList")) {
                    FilterList fl = (FilterList) data.get(i);
                    br.write("signature,String," + fl.signature + "\n");
                    br.write("toggleIdx,int," + fl.toggleIdx + "\n");
                    br.write("filterIdcs,Integer");
                    for (int j = 0; j < fl.filterIdcs.length; j++)
                        br.write("," + fl.filterIdcs[j]);
                    br.write("\noccurrence,int");
                    for (int j = 0; j < fl.occurrences.length; j++)
                        br.write("," + fl.occurrences[j]);
                    br.write("\nprobabilities,double");
                    for (int j = 0; j < fl.probabilities.length; j++)
                        br.write("," + fl.probabilities[j]);
                    br.write("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
