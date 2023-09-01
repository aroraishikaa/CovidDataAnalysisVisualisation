import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage; 
import javafx.scene.chart.NumberAxis; 
import javafx.scene.chart.XYChart;
import javafx.scene.chart.*;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Write a description of JavaFX class LineChart here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Chart 
{
    // We keep track of the count, and label displaying the count:
    private int count = 0;
    private Label myLabel = new Label("0");    
    /**
     * Creates an Area Chart based on the gmr data from the covid_london csv file within a specific date range for a specific borough 
     * @param firstDate the first date in the date range
     * @param lastDate the last date in the date range 
     * @param dataName the specific gmr data that is being plotted
     * @param name the name of the borough 
     * @data the list of all the covid data recorded 
     * @return areaChart the area chart 
     */
    public AreaChart createAreaChart(LocalDate firstDate, LocalDate lastDate,String dataName,String name, ArrayList data){
        LocalDate startDate = firstDate;
        LocalDate endDate = lastDate;
        String googleMobility = dataName;
        String boroughName = name;
        ArrayList<CovidData> records = data;
        
        ArrayList<String> dateX = getDates(startDate,endDate,records,boroughName);
        ArrayList<Integer> gmrY = getGMR(startDate, endDate, records,boroughName,dataName);
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(-50,50,10);
        
        xAxis.setLabel("date");
        yAxis.setLabel("traffic");
        
        AreaChart<String,Number> areaChart = new AreaChart<String,Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName(boroughName);
        
        for(int i =0;i<dateX.size();i++){
            String date = dateX.get(i);
            int gmr = gmrY.get(i);
            series.getData().add(new XYChart.Data(date,gmr));
        }
        
        areaChart.getData().addAll(series);
        
        return areaChart;
    }

    /**
     * Returns an ArrayList of all the dates recorded within a given date range for a specific borough
     * @param start the beginning date of the range 
     * @param end the last date of the range 
     * @param data all the recorded covid data 
     * @param name the name of the borough 
     * @return datePoints all the dates recorded within a given date range for a specific borough
     */
    private ArrayList<String> getDates(LocalDate start, LocalDate end, ArrayList data, String name){
        LocalDate startDate = start;
        LocalDate endDate = end;
        ArrayList<CovidData> records = data;
        String boroughName = name;
        ArrayList<String> datePoints = new ArrayList<String>();
        for(CovidData covidData : records) {
                    if(boroughName.equalsIgnoreCase(boroughName)) {
                        LocalDate Date = LocalDate.parse(covidData.getDate());
                        if(Date.isBefore(endDate.plusDays(1)) && Date.isAfter(startDate.minusDays(1))){
                            String dateRetrieved = covidData.getDate();
                            datePoints.add(dateRetrieved);
                        }
                    }
                }
        return datePoints;
    }
    
    /**
     * Returns an ArrayList of all the gmr data (as specified) recorded within a given date range for a specific borough
     * @param start the beginning date of the range 
     * @param end the last date of the range 
     * @param data all the recorded covid data 
     * @param name the name of the borough 
     * @param dataName the name of the specific gmr data to be returned
     * @return gmrPoints all the gmr data  recorded within a given date range for a specific borough
     */
    private ArrayList<Integer> getGMR(LocalDate start, LocalDate end, ArrayList data, String name,String dataName){
        LocalDate startDate = start;
        LocalDate endDate = end;
        ArrayList<CovidData> records = data;
        String boroughName = name;
        String gmrName = dataName;
        ArrayList<Integer> gmrPoints = new ArrayList<Integer>();
        for(CovidData covidData : records) {
                    if(boroughName.equalsIgnoreCase(boroughName)) {
                        LocalDate Date = LocalDate.parse(covidData.getDate());
                        if(Date.isBefore(endDate.plusDays(1)) && Date.isAfter(startDate.minusDays(1))){
                            switch (dataName){
                                case "retail":
                                    gmrPoints.add(covidData.getRetailRecreationGMR());
                                    break;
                                case "parks":
                                    gmrPoints.add(covidData.getParksGMR());
                                    break;
                                case "grocery":
                                    gmrPoints.add(covidData.getGroceryPharmacyGMR());
                                    break;
                                case "residential":
                                    gmrPoints.add(covidData.getResidentialGMR());
                                    break;
                                case "transit":
                                    gmrPoints.add(covidData.getTransitGMR());
                                    break;
                                case "workplaces":
                                    gmrPoints.add(covidData.getWorkplacesGMR());
                                    break;
                            }
                        }
                    }
                }
        return gmrPoints;
    }
    
}
