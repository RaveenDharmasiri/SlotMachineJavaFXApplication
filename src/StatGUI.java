import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// this gui is to represent the window which is going to display statistics.
public class StatGUI extends Application {

    // this is the name of the file in which we are going to save the information.
    private String fileName;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinWidth(1550);
        primaryStage.setMinHeight(800);
        primaryStage.setTitle("statistics");

        // everytime the statistics window is opened the statistics button is disabled. Therefore, giving the command to the program to enable the
        // statistics button when the statistics window is closed.
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                MainGUI.btnStatistics.setDisable(false);
            }
        });

        // the gridpane where all the components of this page will be added.
        GridPane statGrid = new GridPane();
        statGrid.setAlignment(Pos.CENTER);
        statGrid.setGridLinesVisible(true);


        Label statistics = new Label("Statistics");
        statistics.setStyle("-fx-font-weight:bold; -fx-font-size:40px; -fx-font-family: Tele-Marines;");
        statistics.setPadding(new Insets(10,10,10,540));

        // displaying the total number of wins using a label.
        Label lblWins  = new Label("Wins : \n   "+ MainGUI.noOfWins);
        lblWins.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblWins.setPadding(new Insets(10,10,10,100));

        // displaying the total number of loses using a label
        Label lblLoses = new Label("Loses : \n   "+MainGUI.noOfLoses);
        lblLoses.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblLoses.setPadding(new Insets(10,100,10,100));

        // displaying the total number of spins
        Label lblNoOfSpins = new Label("Total Number of spins : \n               "+MainGUI.noOfSpins);
        lblNoOfSpins.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblNoOfSpins.setPadding(new Insets(10,100,10,10));

        // displaying the average credit the user won.
        Label lblAvgCredit = new Label("AVG. Credits Won: \n            "+MainGUI.creditPerGame);
        lblAvgCredit.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblAvgCredit.setPadding(new Insets(10,100,10,10));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(lblWins, lblLoses, lblNoOfSpins, lblAvgCredit);

        /* using the pie chart since it is a better graphical representation of the information. In an instance looking at the pie chart the user can realize whether they have more
         * wins than loses or the other way around.
         */
        PieChart pieChartObj  = new PieChart();
        PieChart.Data winSlice = new PieChart.Data("No of Wins",MainGUI.noOfWins);
        PieChart.Data loseSlice = new PieChart.Data("No of Loses",MainGUI.noOfLoses);
        pieChartObj.getData().addAll(winSlice, loseSlice);

        HBox saveFileBtnContainer = new HBox();
        saveFileBtnContainer.setPadding(new Insets(10,0,10,620));


        Button btnSaveToFile = new Button("Save to File");
        btnSaveToFile.setPadding(new Insets(20,50,20,50));

        // adding css to the button.
        btnSaveToFile.setStyle("-fx-background-color: \n" +
                "linear-gradient(#f2f2f2, #d6d6d6),\n" +
                "linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%),\n" +
                "linear-gradient(#dddddd 0%, #f6f6f6 50%);\n" +
                "-fx-background-radius: 8,7,6;\n" +
                "-fx-background-insets: 0,1,2;\n" +
                "-fx-text-fill: black;\n" +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");

        // the purpose of this button is to create the file and then save the relavent information into that file.
        btnSaveToFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event)  {
                try {
                    // invoking the method that creates the file and save the information into the file.
                    createFile();
                } catch (FileNotFoundException e) {
                    // display an error message to let the user know if the file which save the data could not be made.
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("File could not be created");
                    alert.show();
                    // along with the error message the exception error is also printed for the user to get a clear idea about the error.
                    e.printStackTrace();
                }
            }
        });

        saveFileBtnContainer.getChildren().add(btnSaveToFile);

        // setting the position of the components on the grid.
        statGrid.add(statistics, 3, 0);
        statGrid.add(hBox, 3,1);
        statGrid.add(pieChartObj, 3, 3);
        statGrid.add(saveFileBtnContainer, 3, 4);

        // creating the scene object of this window.
        Scene statScene = new Scene(statGrid, 1550, 800);

        // making the statScene the scene of the statistics window.
        primaryStage.setScene(statScene);
        primaryStage.show();

    }

    // each time the btnSaveToFile is clicked this method is invoked which will create the file and write the data into that created file.
    private void createFile() throws FileNotFoundException {
        //since we are going to save the information frequently we are going to use the date and time as the name of the files in which are saving the information.

        // therefore we are using the dateObj to store the current date and time.
        Date dateObj = new Date();

        // the we are using the simple date format object to format the date object according to our preferred format.
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");

        // since we are using the date and time as the name of the file, everytime when we click the btnSaveToFile button
        // new file will be created with a unique name.
        fileName = (dateFormat.format(dateObj)).toString()+".txt";
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            // writing the necessary information into the file.
            writer.println("No of Wins: "+MainGUI.noOfWins);
            writer.println("No of Loses: "+MainGUI.noOfLoses);
            writer.println("Average Credit Scored: "+MainGUI.creditPerGame);

            // sending an alert message to inform the user that the information have been saved to file. the name of the file will also be displayed in the message.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved");
            alert.setHeaderText("You statistics have been saved to the file "+fileName);
            alert.show();

            // closing the PrintWriter object.
            writer.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
