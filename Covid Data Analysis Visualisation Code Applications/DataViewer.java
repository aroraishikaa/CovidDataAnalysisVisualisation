import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
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

import javafx.collections.FXCollections;

import javafx.scene.chart.*;

import java.io.File;
import java.util.*;

/**
 * Write a description of JavaFX class DataViewer here.
 *
 * @author (Onyi, Ishika, Zoya, Hamnah)
 * @version (15.03.2023)
 */
public class DataViewer extends Application
{

    private Label alert1;
    private Label alert2;
    private Label alert3;

    private Button previousButton;
    private Button nextButton;
    private Button leftButton;
    private Button rightButton;
    private DatePicker startDate;
    private DatePicker endDate;
    private BorderPane contentPane;
    private ArrayList<CovidData> records;
    private ArrayList<BMapData> boroughs;
    private ChoiceBox boroughChoice;
    private ChoiceBox gmrChoice;
    private LocalDate start;
    private LocalDate end;
    private String boroughName;
    private String gmrName;
    private Label welcomeLabel;
    private GridPane map;

    private int index = 1;
    private ArrayList<Node> panels = new ArrayList<Node>();

    private Stats sLoader;
    private CovidDataLoader covidData;
    private CovidData cData;

    private Stats stats;
    private BMapLoader b_data;
    private BorderPane pane;


    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        //load the covid data 
        covidData = new CovidDataLoader();
        records = covidData.load();
        
        b_data = new BMapLoader();
        boroughs = b_data.load();

        //create HBox to contain the combo boxes so the user can select date range 
        HBox dateRanges = new HBox();

        //create region so that the combo boxes and labels are always in the right corner 
        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);

        //create choice boxes and their respective labels 
        Label from = new Label("From: ");
        startDate = new DatePicker();
        Label to = new Label("To: ");
        endDate = new DatePicker();
        endDate.setDisable(true);

        endDate.setOnAction(this::endDateSelected);
        startDate.setOnAction(this::startDateSelected);

        //add components to Hbox 
        dateRanges.getChildren().addAll(spacer,from,startDate,to,endDate);

        //create a new Welcome Label to give basic instructions to the user
        welcomeLabel = new Label("Welcome to the Application! \n Once you enter a data range, you will be shown a visual representation of the COVID data. ");
        //panels.add(welcomeLabel);

        //create alert labels that notify the user if there is an error.
        alert1 = new Label(" ");
        alert2 = new Label(" ");
        alert3 = new Label(" ");

        //create a VBox to contain all the labels
        VBox labels = new VBox();
        Region topSpace = new Region();
        VBox.setVgrow(topSpace, Priority.ALWAYS);
        Region bottomSpace = new Region();
        VBox.setVgrow(bottomSpace, Priority.ALWAYS);
        labels.getChildren().addAll(topSpace,welcomeLabel,alert1,alert2,alert3,bottomSpace);
        
        panels.add(labels);

        //create a Hbox to contain navigation buttons 
        HBox buttonBar = new HBox();

        //create navigation buttons 
        previousButton = new Button ("Previous");
        nextButton = new Button("Next");

        //create region to to set constant space between navigation buttons 
        Region space = new Region();
        HBox.setHgrow(space,Priority.ALWAYS);

        //add buttons to hbox
        buttonBar.getChildren().addAll(previousButton,space,nextButton);

        //hide the button from the user.
        previousButton.setVisible(false);
        nextButton.setVisible(false);

        // Create a new Border Pane
        // Add the Label in center and Hboxs' at the bottom into the pane  
        contentPane = new BorderPane(labels, dateRanges, null, buttonBar, null);
        //center,top, right, bottom, left

        //set an action on the button using method reference
        nextButton.setOnAction(this::nextButtonClickedFirst);
        previousButton.setOnAction(this::previousbuttonClick);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(contentPane, 500,500);
        stage.setTitle("Welcome");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();

    }

    /**
     * This method is executed when the next button is pressed from the borough map window 
     */
    public void nextButtonClickedFirst(ActionEvent event){
        //initialise an instance of statsDataLoader
        BorderPane contentPane_m = new BorderPane();
        stats = new Stats(contentPane_m, records, startDate.getValue(), endDate.getValue());//nothing is being returned here, contentPnaem is empty !!!!!!
        Pane statsPane = stats.showStatsPanel();
        //contentPane.setCenter(statsPane);
        panels.add(statsPane);
        setUpGraph();
        index = index+1;
        setPanel();
        
        nextButton.setOnAction(this::nextbuttonClick);
    }
    

    /**
     * This method will be executed when the next button is clicked
     * Index variable which is incremented when the next Button is clicked
     * Index is used to display corresponding panel
     */
    private void nextbuttonClick(ActionEvent event)
    {
        System.out.println("in nextbuttonClick index = "+index);
        
        if(index == 2){
            nextButton.setDisable(true);
        }
        
        if(index == 3 ){
            index = 0;
        }
        else {
            index = index+1;
        }
        setPanel();//indexy out of bouuhnds
    }

    /**
     * This method will be executed when the previous button is clicked
     * Index variable which is decremented when teh previous button is clicked
     * Index is used to display corresponding panel
     * 
     */
    private void previousbuttonClick(ActionEvent event)
    {
        if(index == 0){
            index = 3;
        }

        else{
            index -= 1;
        }
        setPanel();
    }
    
    /**
     * Method to set panel in the window
     */
    public void setPanel(){
        contentPane.setCenter(panels.get(index));
    }

    /**
     * executed when the first date is picked 
     * allows the user to pick a second date once they have chosen a start date.
     */
    private void startDateSelected(ActionEvent event){
        LocalDate date = startDate.getValue();
        if(!(date == null)){
            endDate.setDisable(false);
        }
    }

    /**
     * this will be executed when the end date combo box is used 
     * makes the navigational buttons visible if a correct date range has been chosen. 
     * shows the next panel (borough map) to the user if a correct date range has been chosen 
     */
    private void endDateSelected(ActionEvent event){

        boolean invalid = false;
        LocalDate fromDate = startDate.getValue();
        LocalDate toDate = endDate.getValue();

        if(fromDate.isBefore(earliestDate(records))){
            System.out.println("in start date is before earliest date");
            invalid = true;
            alert1.setText("there is no data for this range, please pick new dates");
            startDate.getEditor().clear();
            endDate.getEditor().clear();
        }else if(toDate.isAfter(lastDate(records))){
            invalid = true;
            alert2.setText("there is no data for this range., please pick new dates ");
            startDate.getEditor().clear();
            endDate.getEditor().clear();
        }else if(fromDate.isAfter(toDate)){
            invalid = true;
            alert3.setText("the chosen start date is after the end date, please pick new dates");
            startDate.getEditor().clear();
            endDate.getEditor().clear();
        }

        if(invalid == false){
            previousButton.setVisible(true);
            nextButton.setVisible(true);
            showMap();
        }

    }

    /**
     * @returns the last data on covid data was recorded.  
     */
    private LocalDate lastDate(ArrayList<CovidData> records){
        String lastDate = records.get(0).getDate();
        LocalDate date = LocalDate.parse(lastDate);
        return date;
    }

    /**
     * @returns the earliest date covid data was recorded for 
     */
    private LocalDate earliestDate(ArrayList<CovidData> records){
        int recordSize = records.size();
        String earliestDate = records.get(recordSize-1).getDate();
        LocalDate date = LocalDate.parse(earliestDate);
        return date;
    }

    /**
     * Displays the map to the user 
     */
    private void showMap(){
        //setting up grid pane, making buttons in grid pane, relate data to each borough, putting grid pane in center of borderpane
        start = startDate.getValue();
        end = endDate.getValue();
        map = new GridPane();
        b_data = new BMapLoader();
        boroughs = b_data.load();

        for(int i = 0;i<boroughs.size();i++){
            Button b = new Button(boroughs.get(i).getBoroughCode());
            b = MarkColor(b);
            b.setOnAction(this::boroughClicked);
            map.add(b,boroughs.get(i).getColumn(),boroughs.get(i).getRow());
        }

        contentPane.setCenter(map);
        panels.add(map);
    }

    /**
     * Changes the colour of each borough button based on the total deaths in that time period
     * @param b the button being changed 
     */
    private Button MarkColor(Button b){
        String boroughCode = b.getText();
        String boroughName = b_data.findBoroughName(boroughCode);

        if ((covidData.getTotalBoroughDeaths(boroughName,start,end,records) < 25000)&&(covidData.getTotalBoroughDeaths(boroughName,start,end,records)<100000)){
            b.setStyle("-fx-background-color: Green");
        }

        if (covidData.getTotalBoroughDeaths(boroughName,start,end,records)> 100000 && covidData.getTotalBoroughDeaths(boroughName,start,end,records) < 300000){
            b.setStyle("-fx-background-color: Yellow");
        }

        if (covidData.getTotalBoroughDeaths(boroughName,start,end,records) > 300000){
            b.setStyle("-fx-background-color: Red");
        }
        return b;
    }

    /**
     * This method will be executed when a button in the grid pane is clicked.
     * The covid data gets filtered by data and borough and the window for the borough data to be displayed on is created.
     */
    public void boroughClicked(ActionEvent event){
        ArrayList<CovidData> covidDataForBorough = new ArrayList<CovidData> ();

        String buttonName = ((Button) event.getSource()).getText(); 

        String boroughName = "";

        start = startDate.getValue();
        end = endDate.getValue();

        for (BMapData bourough : boroughs) {
            if(buttonName.equalsIgnoreCase(bourough.getBoroughCode())) {
                boroughName = bourough.getBoroughName();
                for(CovidData covidData : records) {
                    if(boroughName.equalsIgnoreCase(covidData.getBorough())) {
                        LocalDate date = LocalDate.parse(covidData.getDate());
                        if(date.isBefore(end.plusDays(1)) && date.isAfter(start.minusDays(1))){
                            covidDataForBorough.add(covidData);
                        }
                    }
                }
            }
        }

        // create a new stage and scene
        Stage boroughStage = new Stage();

        VBox root = new VBox();
        StackPane stackPane = new StackPane();
        makeTableView(covidDataForBorough, root);

        Scene boroughScene = new Scene(root, 400, 300);
        boroughStage.setTitle(boroughName);
        boroughStage.setScene(boroughScene);

        boroughStage.show();
    }

    /**
     * This method creates a table view displaying the following details about the borough over the date range that was entered: 
     * date, google mobility data, new COVID cases, total COVID cases, new COVID deaths.
     * @param covidDataForBorough the covid data filtered by date and borough
     * @param parent the parent layout pane which the table view is added to
     */
    private void makeTableView(ArrayList<CovidData> covidDataForBorough, Pane parent) {
        TableView tableView = new TableView<TableColumn>();
        parent.getChildren().add(tableView);

        //Create columns and name them
        TableColumn<CovidData, String> column1 = new TableColumn<>("date");
        column1.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<CovidData, String> column2 = new TableColumn<>("borough");
        column2.setCellValueFactory(new PropertyValueFactory<>("borough"));

        TableColumn<CovidData, String> column3 = new TableColumn<>("retailRecreationGMR");
        column3.setCellValueFactory(new PropertyValueFactory<>("retailRecreationGMR"));

        TableColumn<CovidData, String> column4 = new TableColumn<>("groceryPharmacyGMR");
        column4.setCellValueFactory(new PropertyValueFactory<>("groceryPharmacyGMR"));

        TableColumn<CovidData, String> column5 = new TableColumn<>("parksGMR");
        column5.setCellValueFactory(new PropertyValueFactory<>("parksGMR"));

        TableColumn<CovidData, String> column6 = new TableColumn<>("transitGMR");
        column6.setCellValueFactory(new PropertyValueFactory<>("transitGMR"));

        TableColumn<CovidData, String> column7 = new TableColumn<>("workplacesGMR");
        column7.setCellValueFactory(new PropertyValueFactory<>("workplacesGMR"));

        TableColumn<CovidData, String> column8 = new TableColumn<>("residentialGMR");
        column8.setCellValueFactory(new PropertyValueFactory<>("residentialGMR"));

        TableColumn<CovidData, String> column9 = new TableColumn<>("newCases");
        column9.setCellValueFactory(new PropertyValueFactory<>("newCases"));

        TableColumn<CovidData, String> column10 = new TableColumn<>("totalCases");
        column10.setCellValueFactory(new PropertyValueFactory<>("totalCases"));

        TableColumn<CovidData, String> column11 = new TableColumn<>("newDeaths");
        column11.setCellValueFactory(new PropertyValueFactory<>("newDeaths"));

        TableColumn<CovidData, String> column12 = new TableColumn<>(" ");
        column12.setCellValueFactory(new PropertyValueFactory<>("totalDeaths"));

        //Hide borough column and totalDeaths column
        column2.setVisible(false);
        column12.setVisible(false);

        //Add columns to table view
        tableView.getColumns().addAll(column1,column2,column3,column4,column5,column6,column7,column8,column9,column10,column11,column12);

        //make columns sortable
        List<TableColumn> tableColumns = tableView.getColumns();
        for (TableColumn column : tableColumns) {
            column.setSortable(true);
            column.setSortType(TableColumn.SortType.ASCENDING);  
        }

        //Add COVID data to tables
        tableView.getItems().addAll(covidDataForBorough);

    } 

    /**
     * Creates and displays the panel that allows users to choose what graph they want to display
     */
    private void setUpGraph(){
        //nextButton.setDisable(true);
        HBox choices = new HBox();
        Label explain = new Label("select a borough and gmr  to display an area chart: "); 
        boroughChoice = createBoroughChoices();
        gmrChoice = createGMRChoices();

        Region space1 = new Region();
        HBox.setHgrow(space1,Priority.ALWAYS);
        Region space2 = new Region();
        HBox.setHgrow(space2,Priority.ALWAYS);

        choices.getChildren().addAll(space1,boroughChoice,gmrChoice,space2); 

        Button graphs = new Button("display graph");
        graphs.setOnAction(this::graphButtonClicked);  

        VBox choicePanel = new VBox();
        choicePanel.getChildren().addAll(explain,choices,graphs);

        //contentPane.setCenter(choicePanel);
        
        panels.add(choicePanel);
        
    }

    /**
     * creates populates the choice box with borough names 
     * @return boroughChoice the choice box that allows user to pick a borough
     */
    private ChoiceBox createBoroughChoices(){
        ArrayList<String> options = new ArrayList<String>();
        for(int i = 0;i<boroughs.size();i++){
            options.add(boroughs.get(i).getBoroughName());
        }

        ChoiceBox boroughChoice = new ChoiceBox<Object>(FXCollections.observableArrayList(options));
        return boroughChoice;
    }

    /**
     * creates and populates the choice box with different types of gmr data
     * @retur gmrChoice the choice box that allows the user to specify what type of gmr data they want to chart
     */
    private ChoiceBox createGMRChoices(){
        String [] gmrList = {"retail","parks","grocery","residential","transit","workplaces"};
        ChoiceBox gmrChoice = new ChoiceBox<Object>(FXCollections.observableArrayList(gmrList));
        return gmrChoice;
    }

    /**
     * called when the display graph button is clicked
     * creates the area chart 
     */
    private void graphButtonClicked(ActionEvent event){
        gmrName = gmrChoice.getSelectionModel().getSelectedItem().toString();
        boroughName = boroughChoice.getSelectionModel().getSelectedItem().toString();
        Chart areaChart = new Chart();
        AreaChart<String,Number> gmrChart = areaChart.createAreaChart(start,end,gmrName,boroughName,records);
        showGraph(gmrChart);
    }

    /**
     * displays the graph 
     * @param graph the graph displayed
     */
    private void showGraph(AreaChart<String,Number> graph){
        nextButton.setDisable(false);
        Stage graphStage = new Stage();
        pane = new BorderPane();
        pane.setCenter(graph);
        contentPane.setCenter(pane);
        panels.add(pane);
    }

}
