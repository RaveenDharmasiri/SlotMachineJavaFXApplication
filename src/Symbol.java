
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class Symbol implements ISymbol {
    // a reference variable from the Image data type to store the image object that represent the symbol. this reference will be reffering to a unique image object.
    private Image imageObj;

    // three variables of Reel data type to represent Reel objects.
    private Reel reelObj1;
    private Reel reelObj2;
    private Reel reelObj3;

    // overladding the constructor so that we can create objects from this class that does not accept any arguments.
    public Symbol(){

    }

    // another overloaded constructor where we can pass three Reel object parameters.
    public Symbol(Reel reelObj1, Reel reelObj2, Reel reelObj3){
        this.reelObj1 = reelObj1;
        this.reelObj2 = reelObj2;
        this.reelObj3 = reelObj3;
    }

    // this variable holds the value of each image...
    private int imgValue;

    // this method is used to set the image
    @Override
    public void setImage(Image imageObj) {
        this.imageObj = imageObj;
    }

    // this method will return the image of the Symbol object that involkes it.
    @Override
    public Image getImage() {
        return this.imageObj;
    }

    // this method is used to set the value of each symbol object.
    @Override
    public void setValue(int value) {
        this.imgValue = value;
    }

    // this method returns the value of the symbol object that invokes it.
    @Override
    public int getValue() {
        return this.imgValue;
    }

    // the purpose of this method is to get the value of each image that stops at the reel and then compare those values and tell the player whether they won or not.
    public void imageValueCount(){
        int reel_1_ImageValue;// used to store the value of the symbol that the reel 1 stop.
        int reel_2_ImageValue;// used to store the value of the symbol that the reel 2 stop.
        int reel_3_ImageValue;// used to store the value of the symbol that the reel 3 stop.
        if(MainGUI.isReel1Interupted && MainGUI.isReel2Interupted && MainGUI.isReel3Interupted && MainGUI.isSpinClicked){
            // enabling the spin and bet max button once the spinning is over.
            MainGUI.btnSpin.setDisable(false);
            MainGUI.btnBetMax.setDisable(false);

            // the number of spins will be incremented only when all three reels have been stopped.
            MainGUI.noOfSpins++;

            // once we enter the if condition these three boolean variables that are checking the inturption of the threads is set to false.
            MainGUI.isReel1Interupted = false;
            MainGUI.isReel2Interupted = false;
            MainGUI.isReel3Interupted = false;

            // below three variables are used to store the symbol values.
            reel_1_ImageValue = reelObj1.getImageValue();

            reel_2_ImageValue = reelObj2.getImageValue();

            reel_3_ImageValue = reelObj3.getImageValue();

            // comparing the image values to calculate the winning score.
            if(reel_1_ImageValue == reel_2_ImageValue&& reel_1_ImageValue == reel_3_ImageValue){

                MainGUI.winScore = MainGUI.bidAmount * (reel_1_ImageValue);
                MainGUI.totalWinScore = MainGUI.totalWinScore + MainGUI.winScore;

            }else if(reel_1_ImageValue == reel_2_ImageValue || reel_1_ImageValue==reel_3_ImageValue){

                MainGUI.winScore = MainGUI.bidAmount * (reel_1_ImageValue);
                MainGUI.totalWinScore = MainGUI.totalWinScore + MainGUI.winScore;

            }else if(reel_2_ImageValue == reel_3_ImageValue){

                MainGUI.winScore = MainGUI.bidAmount * (reel_2_ImageValue);
                MainGUI.totalWinScore = MainGUI.totalWinScore + MainGUI.winScore;

            }

            // Displaying an alert message depending on the fact whether the user has won or not.
            if(MainGUI.winScore > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Round Summary");
                // Win message if the user won.
                alert.setHeaderText("You WON "+MainGUI.winScore+" credits");
                alert.show();

                // incrementing the number of wins when the user wins a round.
                MainGUI.noOfWins++;
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Round Summary");
                // lost message if the user has lost
                alert.setHeaderText("You lost "+MainGUI.bidAmount+" credit");
                alert.show();

                // incrementing the number of loses when the user lose.
                MainGUI.noOfLoses++;
            }

            // setting the isSpinClicked variable to false. this way the program will not try to compare symbols even when the spinning has not being done.
            MainGUI.isSpinClicked = false;

            // adding the winning score to the credit amount.
            MainGUI.creditAmount = MainGUI.creditAmount + MainGUI.winScore;
            MainGUI.lblCredit.setText("Credit Amount: \n       "+MainGUI.creditAmount);

            // setting the bidAmoung and the winScore to zero for the next round.
            MainGUI.bidAmount = 0;
            MainGUI.winScore = 0;
            // credits that the user spend is the credit that he use for the bet. therefore incrementing the total credits spend relative to the amount the user spend.
            MainGUI.totalCreditSpend = MainGUI.totalCreditSpend + MainGUI.bidAmount;

            // setting the bet amount on the gui to zero when the round ends.
            MainGUI.lblBid.setText("Bid Amount: \n        "+MainGUI.bidAmount);
        }
    }

}
