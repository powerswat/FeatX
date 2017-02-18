import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by youngsukcho on 2017. 2. 17..
 */
public class FileProcessor {
    public ArrayList<ArrayList<String>> readCSV(String filename){
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        ArrayList<String> row = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str = "";
            while ((str = br.readLine()) != null){
                row.add(str.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void removeEmpty(){

    }

    public void writeCSV(){

    }
}
