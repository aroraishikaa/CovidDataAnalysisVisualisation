import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.time.LocalDate;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
/**
 * Write a description of class statsDataLoader here.
 *
 * @author (Hamnah, Zoya, Ishika, Onyi)
 * @version (a version number or a date)
 */
public class Stats
{   
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<CovidData> records;
    private ArrayList<String> headerLabels = new ArrayList<String>();
    private BorderPane contentPane;

    private int index = 0;
    private int totalDeaths = 0;
    private int aveTotalCases = 0;
    private int numOfRecords ;
    private int aveParkGMR;
    private int aveResidentialGMR;
    private int aveGroceryGMR;
    private String highestDeathDate = "";
    private String label = "";
    private Label headerLabel;

    /**
     * Constructor for objects of class statsDataLoader
     */
    //Passing through parameters through constructor
    public Stats(BorderPane contentPane_m, ArrayList<CovidData> records, LocalDate startDate, LocalDate endDate)
    {
        this.records = records;
        //Variable to store number of records
        //numOfRecords = records.size();
        contentPane = contentPane_m;
        //records are filtered by the start and end date
        filterByDates(startDate, endDate);
        //First label displayed on the stats panel
        headerLabel = new Label("Welcome to the statistics panel!\nClick the left & right buttons to view statistics");

        //calling stats calculation methods
        getAveTotalCases();
        getAveResidentialGMR();
        getAveParkGMR();
        //highestTotalDeath();
        getNumOfTotalDeaths();
        //calling method to create labels
        createLabels();

        //converting LocalDate into String data type to be appended to headerLabels
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        highestDeathDate = formatter.format(endDate);
        System.out.println(endDate);
        System.out.println(highestDeathDate);
    }

    /**
     * Loads up statsPanel GUI window
     */

    public Pane showStatsPanel(){
        BorderPane statsPane = new BorderPane();
        //Creating HBox to hold buttons & labels & positioning to centre of panel
        HBox statsButtonBar = new HBox();
        statsButtonBar.setAlignment(Pos.CENTER);

        //Creation of buttons to scroll through stats
        Button leftButton = new Button("<");
        leftButton.setTranslateX(-75);
        Button rightButton = new Button(">");
        rightButton.setTranslateX(100);

        //Adding all elements to HBox buttonBar
        statsButtonBar.getChildren().addAll(leftButton, headerLabel, rightButton);
        statsPane.setCenter(statsButtonBar);

        //Jumps to referenced methods when button is clicked
        leftButton.setOnAction(this::leftButtonClicked);
        rightButton.setOnAction(this::rightButtonClicked);

        return statsPane;

    }

    /**
     * Method to filter covidData records by startDate & endDate inputted by user in welcome panel
     * Takes in startDate & endDate as parameters
     * Stores number of filtered records into another variable called numOfRecords
     */

    private void filterByDates(LocalDate startDate, LocalDate endDate){

        ArrayList<CovidData> filteredList = new ArrayList<CovidData>();
        for(CovidData covidData : records){
            covidData.getDate();
            LocalDate Date = LocalDate.parse(covidData.getDate());
            if(Date.isBefore(endDate.plusDays(1)) && Date.isAfter(startDate.minusDays(1))){
                filteredList.add(covidData);
            }
        }
        records = filteredList;
        numOfRecords = filteredList.size();
        System.out.println("end of filter by dates");
        System.out.println("records size: "+numOfRecords);
    }

    /**
     * Method that is called when rightButton is clicked on statsPanel GUI
     * Index variable which is incremented when rightButton is clicked
     * Index is used to display corresponding label and statistic
     */
    private void rightButtonClicked(ActionEvent event){
        if(index == 4 ){
            index = 0;
        }
        else {index += 1;}
        setLabels();
    }

    /**
     * Method that is called when leftButton is clicked on statsPanel GUI
     * Index variable which is decremented when leftButton is clicked
     * Index is used to display corresponding label and statistic
     */
    private void leftButtonClicked(ActionEvent event){
        if(index == 0){
            index = 4;
        }
        else{
            index -= 1;}
        setLabels();
    }

    /**
     * Method used to create labels to display in statsPanel according to index
     * headerLabels is an arraylist of strings which is then iterated through using index to display correct label
     */
    public void createLabels(){

        headerLabels.add("Total number of deaths: " + totalDeaths);
        headerLabels.add("Average of total cases: " + aveTotalCases);
        headerLabels.add("Average of residential GMR: " + aveResidentialGMR);
        headerLabels.add("Average of parks GMR: " + aveParkGMR);
        headerLabels.add("Date of highest total deaths: " + highestDeathDate);

    }

    /**
     * Method to set label in statsPanel GUI using index
     */
    public void setLabels(){
        this.headerLabel.setText(headerLabels.get(index));
    }

    /**
     * Returns all records from CovidDataLoader to filter through
     */
    public ArrayList<CovidData> getDataList(){
        return records;
    }

    /**
     * Method that calculates number of total deaths for given time period
     * Uses getter methods from CovidData class
     */
    public void getNumOfTotalDeaths(){
        for(int i = 0; i < numOfRecords; i++){
            this.totalDeaths = totalDeaths + records.get(i).getTotalDeaths();
        }
    }

    /**
     * Method that calculates average of total deaths for given time period
     * Uses getter methods from CovidData class
     */
    public void getAveTotalCases(){
        int totalCases = 0;
        for(int i = 0 ; i < numOfRecords; i++){
            totalCases = totalCases + records.get(i).getTotalCases();
        }

        this.aveTotalCases = totalCases/numOfRecords; 
    }

    /**
     * Method that calculates average of residential GMR for given time period
     * Uses getter methods from CovidData class
     */

    public void getAveResidentialGMR(){
        int totalResidentialGMR = 0;
        for(int i = 0; i < numOfRecords; i++){
            totalResidentialGMR = totalResidentialGMR + records.get(i).getResidentialGMR();
        }
        this.aveResidentialGMR = totalResidentialGMR/numOfRecords;

    }

    /**
     * Method that calculates average of park GMR for given time period
     * Uses getter methods from CovidData class
     */

    public void getAveParkGMR(){
        int totalParkGMR = 0;
        for(int i = 0; i < numOfRecords; i++){
            totalParkGMR = totalParkGMR + records.get(i).getParksGMR();
        }
        this.aveParkGMR = totalParkGMR/numOfRecords;

    }

    /**
     * Method that calculates and returns date of highest total deaths during given period of time
     * Uses getter methods from CovidData class
     */
    public void highestTotalDeath(){
        int currentTotalDeath;
        int nextTotalDeath;
        for(int i = 0; i < records.size(); i++){
            if(!(i == (records.size()-1))){
                currentTotalDeath = records.get(i).getTotalDeaths();
                nextTotalDeath = records.get(i+1).getTotalDeaths();
                if(nextTotalDeath > currentTotalDeath){
                    highestDeathDate = records.get(i+1).getDate();
                }
                else{
                    highestDeathDate = records.get(i).getDate();
                }
            }
        }
    }
}

