
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class MainGUI extends Application {

    Stage window;// the reference to the stage object of this gui.
    Label lblTitle;
    static Label lblCredit, lblBid;

    // creating references to the buttons that will used to add credit, bet coin and reset
    Button btnAddCoin, btnBetOne, btnReset;
    static Button btnSpin, btnBetMax;
    static Button btnStatistics;

    // this varibale is used to count the number of rounds the user has won.
    static int noOfWins = 0;
    // this variable is used to count the number of rounds the user has lost.
    static int noOfLoses = 0;
    // this varibale is used to count the total number of credits the user has spend for all the rounds.
    static int totalCreditSpend = 0;
    // counting the number of spins.
    static int noOfSpins = 0;
    // this variable store the credits the player scored for per game.
    static double creditPerGame = 0;

    // a variable that will be used to check whether the StatGui is open or not.
    static boolean isStatGuiOpen = true;

    // these static variables are used to check whether the threads have been interrupted or not.
    static boolean isReel1Interupted = false;
    static boolean isReel2Interupted = false;
    static boolean isReel3Interupted = false;

    static int creditAmount = 10;// this static variable will hold the amount of credit the player is going to have.
    static int bidAmount = 0;// this static varible will be used to store the amount the user is going to bet.
    static int winScore = 0;// this variable holds the amount of credit that the player won.
    static int totalWinScore = 0;

    // three Image view objects to display the images of the three reels.
    static ImageView imgView1 = new ImageView(); // this imageview will display the images of the first reel.
    static ImageView imgView2 = new ImageView(); // this imageview will display the images of the second reel.
    static ImageView imgView3 = new ImageView(); // this imageview will display the images of the third reel.


    private Reel reelObj1;
    private Reel reelObj2;
    private Reel reelObj3;

    // this variable is used to count how many times the threads have been stopped.
    private static int threadStopCount =0 ;

    // declaring the three threads that will be used for the reels.
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;

    // this Symbol object reference will be used to create a connection between this and the Symbol class.
    private Symbol symObj;

    private StatGUI statObj;

    // this boolean variable is used to check whether the spin button has been pressed.
    static boolean isSpinClicked = false;


    @Override
    public void start(Stage primaryStage){
        // assigning the stage object to the stage reference that I created
        window = primaryStage;

        // setting the title of the stage
        window.setTitle("Spin to win");

        // setting a maximum height and width to the stage.
        window.setMinHeight(660);
        window.setMinWidth(1550);

        // command the application to do something at its termination process.
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(threadStopCount == 0){
                    // just close the application but do not do anything since none of the threads have been ran.
                }else{
                    // interupting the threads when the application has been terminated. **sometimes the threads run in the background even when the application is terminated.
                    thread1.interrupt();
                    thread2.interrupt();
                    thread3.interrupt();
                    // commanding to close all the secondary windows when the main window is closed.
                    Platform.exit();
                }
            }
        });

        // using a credit as the main layout of this gui.
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:#e6e6e6;");
        grid.setGridLinesVisible(false);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);

        // creating a label to display the title of the application.
        lblTitle = new Label("spin to Win");
        lblTitle.setStyle("-fx-font-weight:bold; -fx-font-size:40px; -fx-font-family: Tele-Marines;");
        lblTitle.setPadding(new Insets(10,10,10,220));

        // adding the laybel to display the amount of credit.
        lblCredit = new Label("Credit Amount: \n       "+creditAmount);
        lblCredit.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblCredit.setPadding(new Insets(10,100,10,10));

        // adding a label to display the amount the user bid.
        lblBid = new Label("Bet Amount: \n        "+bidAmount);
        lblBid.setStyle("-fx-font-weight:bold; -fx-font-style:italic; -fx-font-size:34px;");
        lblBid.setPadding(new Insets(10,10,10,100));

        // declaring the image objects. these images will be displayed at the launch of the application.
        Image image1 = new Image(getClass().getResourceAsStream("/bell.png"));
        Image image2 = new Image(getClass().getResourceAsStream("/cherry.png"));
        Image image3 = new Image(getClass().getResourceAsStream("/lemon.png"));

        // adding the image to imageview
        imgView1.setImage(image1);
        imgView1.setPreserveRatio(true);

        imgView1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    // the thread1 will interrupted when the imgView1 is clicked.
                    thread1.interrupt();
                    isReel1Interupted = true;
                    symObj.imageValueCount();
                }catch (NullPointerException e){
                    System.out.println("Error: "+ e);
                }

            }
        });
        imgView1.setFitWidth(200);

        // adding the image to imageview
        imgView2.setImage(image2);
        imgView2.setPreserveRatio(true);
        imgView2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    // interrupting the thread2 with the click on imgView2
                    thread2.interrupt();
                    isReel2Interupted = true;
                    symObj.imageValueCount();
                }catch( NullPointerException e){
                    System.out.println("Error: "+e);
                }

            }
        });
        imgView2.setFitWidth(200);

        // adding the image to imageview.
        imgView3.setImage(image3);
        imgView3.setPreserveRatio(true);
        imgView3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    // thread3 will be interrupted when the imgView3 is clicked
                    thread3.interrupt();
                    isReel3Interupted = true;
                    symObj.imageValueCount();
                }catch(NullPointerException e){
                    System.out.println("Error: "+e);
                }
            }
        });
        imgView3.setFitWidth(200);

        // declaring an hbox to hold the imageviews that are going to display the symbols in the application or gui.
        HBox hbox = new HBox(100);
        hbox.setStyle("-fx-border-color: black;");


        hbox.getChildren().addAll(imgView1, imgView2, imgView3);

        // this hbox is specially made to hold the button that is used to spin the images.
        HBox hbox2 = new HBox();
        btnSpin = new Button("SPIN");
        btnSpin.setPadding(new Insets(20,60,20,60));
        btnSpin.setStyle("-fx-text-fill:white; -fx-font-family:Comic Sans MS; -fx-font-size:40px; -fx-background-color:black; -fx-background-radius:55px;" +
                "-fx-font-weight:bold;");
        btnSpin.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnSpin.setStyle("-fx-text-fill:black; -fx-font-family:Consolas; -fx-font-size:40px; -fx-background-color:#e6e6e6;" +
                        "-fx-font-weight:bold; -fx-border-color:black; -fx-border-width:5px; -fx-border-radius:50px;");
            }
        });
        btnSpin.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnSpin.setStyle("-fx-text-fill:white; -fx-font-family:Consolas; -fx-font-size:40px; -fx-background-color:black; -fx-background-radius:55px;" +
                        "-fx-font-weight:bold;");
            }
        });
        btnSpin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(bidAmount == 0){
                    // displaying an alert message to request the user to make a bid before spinning.
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Please make a bet before spinning!");
                    alert.show();

                }else {// after the bid has been made.


                    isReel1Interupted = false;
                    isReel2Interupted = false;
                    isReel3Interupted = false;



                    // during the spinning process the spinning button is disabled so that the user cannot click it while the spinning is happening.
                    btnSpin.setDisable(true);

                    /* threadStopCount is incremented when the spin button is pressed. this allows the program to know that the threads have been created and that they
                     *  have been started.
                     *  So that at the point when I am terminating the application the program send the message to interrupt the threads. Otherwise the threads will be
                     *  running in the background even after the application has been terminated.
                     */
                    threadStopCount++;

                    /* every time when we click the spin this variable will become true. This variable is used to help the program to keep a track whether the spin button
                     * has been pressed or not.
                     */

                    isSpinClicked = true;

                    // creating three Reel objects that will be passed into the thread constructor. also passing the image view object as an argument into the constructor so
                    // that the run method in the Reel class know on which image view the image should be displayed relative to each thread.
                    reelObj1 = new Reel(imgView1);
                    reelObj2 = new Reel(imgView2);
                    reelObj3 = new Reel(imgView3);

                    // a symbol object that accept
                    symObj = new Symbol(reelObj1, reelObj2, reelObj3);

                    // every time the spinning button is pressed three thread objects will be created.
                    thread1 = new Thread(reelObj1, "thread1");
                    thread2 = new Thread(reelObj2, "thread2");
                    thread3 = new Thread(reelObj3, "thread3");

                    // starting the threads.
                    thread1.start();
                    thread2.start();
                    thread3.start();
                }
            }
        });

        hbox2.setPadding(new Insets(10,0,0,280));
        hbox2.getChildren().add(btnSpin);

        HBox hbox3 = new HBox(10);
        hbox3.setPadding(new Insets(10,0,0,110));

        // declaring the button to add coins to the credit area.
        btnAddCoin= new Button("Add Coin");
        btnAddCoin.setPadding(new Insets(20,60,20,60));
        btnAddCoin.setStyle(buttonCss());

        // the function of this button is to increment the credit amount when it is being clicked.
        btnAddCoin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                creditAmount++;
                lblCredit.setText("Credit Amount: \n       "+creditAmount);

                // these lines are to make sure that the button to set the max is available only when the credit amount is greater than or equal to 3.
                if(creditAmount >= 3){
                    btnBetMax.setDisable(false);
                }
            }
        });

        // declaring the button that will be used to set the maximum bet.
        btnBetMax = new Button("Bet Max");
        btnBetMax.setPadding(new Insets(20,60,20,60));
        btnBetMax.setStyle(buttonCss());

        // this method is used to set an action to the button that set the maximum bet.
        btnBetMax.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    // when the credit amount is between 0 and 3 exclusively a warning message is showed that the user cannot bet the maximum value since there is not enough credit.
                    if(creditAmount<3 && creditAmount >= 1){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Not enough credit");
                        alert.setHeaderText("Not enough credit to bet a MAXIMUM amount!");
                        alert.show();
                        btnBetMax.setDisable(true);

                        // the user is given the chance to bet the maximum only if there is enough credit.
                    }else if(creditAmount >=3){
                        creditAmount = creditAmount - 3;
                        bidAmount = bidAmount + 3;
                        lblCredit.setText("Credit Amount: \n       "+creditAmount);
                        lblBid.setText("Bid Amount: \n        "+bidAmount);

                        // maximum bet setting button is disabled once it is has been used to set the maximum bet.
                        // which also means that the user can use this button to set the maximum bet only once per each round.
                        btnBetMax.setDisable(true);
                    }else if(creditAmount == 0){
                        // disabling the button used to set the maximum bet when the credit amount is zero.
                        btnBetMax.setDisable(true);
                        // prompting a message to let the user know that they cannot bet the maximum bet because there is not enough credit.
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Not enough credit");
                        alert.setHeaderText("There is not enough credit!");
                        alert.show();
                    }
            }
        });

        // declaring the button that will be used to bet a credit.
        btnBetOne = new Button("Bet One");
        btnBetOne.setPadding(new Insets(20,60,20,60));
        btnBetOne.setStyle(buttonCss());

        // below method is setting an action to the button that is used to bet a credit.
        btnBetOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // prompting a warning to message to warn the user that they cannot bet when there is not enough credit.
                if(creditAmount == 0){
                    // a warning message to tell the player that he cannot bet when there is no credit.
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Not enough Credit");
                    alert.setHeaderText("Sorry! You do not have enough credit to bet");
                    alert.show();
                }else{
                    // below is decrementing credit amount and incrementing the bet amount when the user press the btnBetOne button.
                    creditAmount--;
                    bidAmount++;
                    // displaying the current credit amount and the bid amount when a credit has been betted.
                    lblCredit.setText("Credit Amount: \n       "+creditAmount);
                    lblBid.setText("Bid Amount: \n        "+bidAmount);
                }

            }
        });

        // adding the button to the hbox
        hbox3.getChildren().addAll(btnAddCoin, btnBetOne, btnBetMax);

        // creating another hbox to hold the other buttons of the
        HBox hbox4 = new HBox(10);
        hbox4.setPadding(new Insets(10,0,0,200));

        // this button is used if the user want to reset the amount that he has bet. When this button is pressed all the values that the player bid before spinning will
        // be added to the credit area.
        btnReset = new Button("Reset");
        btnReset.setPadding(new Insets(20,60,20,60));
        btnReset.setStyle(buttonCss());

        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                creditAmount = creditAmount + bidAmount;
                bidAmount = 0;
                //betMaxCount = true;
                btnBetMax.setDisable(false);
                lblCredit.setText("Credit Amount: \n       "+creditAmount);
                lblBid.setText("Bid Amount: \n        "+bidAmount);
            }
        });

        btnStatistics = new Button("Statistics");
        btnStatistics.setPadding(new Insets(20,50,20,50));
        // applying css to the button.
        btnStatistics.setStyle(buttonCss());

        // the function of this button is to open the window which shows the statistics. this button will show information relating to how many rounds the user lost and
        // the amount of round the user won.
        btnStatistics.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    averageCredits();
                    // creating a new object from the StatGUI class.
                    statObj = new StatGUI();

                    // using the created object to invoke the start method.
                    statObj.start(new Stage());

                    // disabling the statistics button when the statistics button is opened in the background. This way we can avoid
                    // opening the same window more than once when the same button is clicked.
                    btnStatistics.setDisable(true);
                }catch(ArithmeticException e){
                    // this is disaplying an error message to let the user know that there can be faults in the calculations if they try to open
                    // the statics window without atleast playing one game.
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Statics cannot be shown until you spin atleast once since the information is not accurate.");
                    alert.show();
                }

            }
        });

        hbox4.getChildren().addAll(btnReset, btnStatistics);

        // adding the labels and the buttons to the grid.
        grid.add(lblTitle, 3,0);
        grid.add(hbox,3,1);
        grid.add(lblCredit, 1,1);
        grid.add(lblBid, 6,1);
        grid.add(hbox2, 3,2);
        grid.add(hbox3, 3,3);
        grid.add(hbox4, 3, 4);

        // declaring the scene that will be added to the window. also adding the created grid pane into the scene.
        Scene scene = new Scene(grid, 1550,660);

        // adding the scene into the window. this will make sure that everything that we added to the scene will be visible to the user.
        primaryStage.setScene(scene);

        // this is to make sure that the window is visible.
        primaryStage.show();
    }

    public static void main(String[] args) {
        // before launching the gui the array which contains the Symbol objects is created.
        Reel.spin();// this method is in the Reel class.
        launch(args);
    }

    // this method calculates the average amount of credit the player scored throughout the game.
    public static void averageCredits(){
        creditPerGame = (totalWinScore)/noOfSpins;
    }

    // this method returns the css that will be applied to a button.
    public String buttonCss(){
        String cssEffect = "-fx-background-color: \n" +
                "linear-gradient(#f2f2f2, #d6d6d6),\n" +
                "linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%),\n" +
                "linear-gradient(#dddddd 0%, #f6f6f6 50%);\n" +
                "-fx-background-radius: 8,7,6;\n" +
                "-fx-background-insets: 0,1,2;\n" +
                "-fx-text-fill: black;\n" +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );";
        return cssEffect;
    }
}
