import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
/**
 * Write a description of class BMapLoader here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BMapLoader
{
    private ArrayList<BMapData> bRecord;
    /** 
     * Return an ArrayList containing the rows of the Boroughs and their coordinates on the map in the csv file.
     */
    public ArrayList<BMapData> load() {
        System.out.println("Begin loading Borough Coordinates....");
        bRecord = new ArrayList<BMapData>();
        try{
            URL url = getClass().getResource("borough_grid.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                
                String boroughCode    = line[0];
                String boroughName    = line[1];
                int row    = convertInt(line[2]);    
                int column    = convertInt(line[3]);                  

                BMapData record = new BMapData(boroughCode,boroughName,row,column);

                bRecord.add(record);
                
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
        }
        System.out.println("Number of Loaded Records: " + bRecord.size());
        return bRecord;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
    
    /**
     * returns the borough name associated with the borough code 
     * @param boroughCode the borough code 
     * @return boroughName the borough name
     */
    public String findBoroughName(String boroughCode){
        String boroughName = " ";
        for(BMapData bData: bRecord){
            if(bData.getBoroughCode().equals(boroughCode)){
                boroughName = bData.getBoroughName();
            }
        }
        return boroughName;
    }
}
