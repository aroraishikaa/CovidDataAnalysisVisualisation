
import javafx.scene.control.Button;
import javafx.scene.paint.*;
import java.util.ArrayList;

/**
 * Write a description of class BMapData here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BMapData
{
    // instance variables - replace the example below with your own
    private String boroughCode;
    private String boroughName;
    private int row;
    private int column;
    /**
     * Constructor for objects of class BMapData
     */
    public BMapData(String boroughCode, String boroughName, int row, int column)
    {
        // initialise instance variables
        this.boroughCode = boroughCode;
        this.boroughName = boroughName;
        this.row = row;
        this.column = column;
    }

    /**
     * @return the borough code 
     */
    public String getBoroughCode(){
        return boroughCode;
    }

    /**
     * @return the borough name 
     */
    public String getBoroughName(){
        return boroughName;
    }

    /**
     * @return the row the borough is in on the map grid 
     */
    public int getRow(){
        return row;
    }

    /**
     * @return the column the borough is in on the map grid 
     */
    public int getColumn(){
        return column;
    }
    
    /**
     * @param the borough code of borough
     * @return boroughName the borough name
     */
    public String matchBoroughName(String boroughCode){
        return boroughName;
    }
    
    

    // public void MarkerColor(ArrayList<CovidData[]> records, Button button)  { 
        // for (CovidData[] row: records){
            // if (row[1].equals(button.getText())){ 

                // if(data.getTotalDeaths()< 10000){
                    // button.setStyle("-fx-background-color: Green");
                // }

                // if (data.getTotalDeaths()> 10000 && data.getTotalDeaths() < 70000){
                    // button.setStyle("-fx-background-color: Yellow");
                // }

                // if (data.getTotalDeaths()> 70000){
                    // button.setStyle("-fx-background-color: Red");
                // }

            // }
        // }

    // }
}