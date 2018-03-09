
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Reel implements Runnable {
    // the array Symbol type which is used to store all the Symbol object.
    public static Symbol[] mainSymbolList = new Symbol[6];

    // a variable to store the random number
    private int randomNumber = 0;

    // this imageview reference is to store the imageview object that we are passing into this class with the help of the constructor.
    private ImageView imageViewObj;

    // this variable is used to store the previous random number that is generated inside the run method.
    private int randomNumberTemp = 0;

    // since the image value is changing consistently i used the key word volatile so that it can catch the value that are being assigned to it.
    private volatile int imageValue;

    // this method will return the value of the symbol that was last called to be displayed in the application.
    public int getImageValue(){
        return  this.imageValue;
    }

    // the constructor of this class has been overloaded
    public Reel(ImageView imgViewObj) {
        this.imageViewObj = imgViewObj;
    }

    // this void method will create the array that contains the symbols.
    public static void spin() {
        // creating the symbol objects.
        Symbol symbol1 = new Symbol();
        // using the setImage() method from the symbol class to set the image of the object.
        symbol1.setImage(new Image(Reel.class.getResourceAsStream("/bell.png")));
        // using the setValue() method from the symbol class to set the value of the symbol.
        symbol1.setValue(6);
        // adding the symbol to the array.
        mainSymbolList[0] = symbol1;

                                                    // the same procedure has been used for the rest of the symbol objects.

        Symbol symbol2 = new Symbol();
        symbol2.setImage(new Image(Reel.class.getResourceAsStream("/cherry.png")));
        symbol2.setValue(2);
        mainSymbolList[1] = symbol2;

        Symbol symbol3 = new Symbol();
        symbol3.setImage(new Image(Reel.class.getResourceAsStream("/lemon.png")));
        symbol3.setValue(3);
        mainSymbolList[2] = symbol3;

        Symbol symbol4 = new Symbol();
        symbol4.setImage(new Image(Reel.class.getResourceAsStream("/plum.png")));
        symbol4.setValue(4);
        mainSymbolList[3] = symbol4;

        Symbol symbol5 = new Symbol();
        symbol5.setImage(new Image(Reel.class.getResourceAsStream("/redseven.png")));
        symbol5.setValue(7);
        mainSymbolList[4] = symbol5;

        Symbol symbol6 = new Symbol();
        symbol6.setImage(new Image(Reel.class.getResourceAsStream("/watermelon.png")));
        symbol6.setValue(5);
        mainSymbolList[5] = symbol6;
    }

    // this is the method that is invoked when the threads are started.
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                randomNumber = (int) (Math.random() * 6);
                //if the current random number is equal to the random number that was generated before a new value is assigned to it. for this
                // we are comparing the current random to the previous random number(randomNumberTemp). if the previous random number is equal
                // to the current random number a new value between 0 and 5 will be assigned to it.
                if (randomNumber == this.randomNumberTemp) {
                    if (randomNumber >= 0 && randomNumber <= 4) {
                        // incrementing the randomNumber by 1 if the random number is between 0 and 4
                        randomNumber++;
                    } else {
                        // if the randomNumber is 5 then 0 is assigned to the randomNumber
                        randomNumber = 0;
                    }
                } else {

                }
                // using synchronized block to make sure that the images are accessed by one thread at a time. if not for the Synchronized block
                // at one point the spinning animation of the images or symbols will stops. that is because more than one thread is trying to
                // access the images at the same time.
                synchronized (Reel.class) {
                    this.imageViewObj.setImage(mainSymbolList[randomNumber].getImage());
                    this.imageValue = mainSymbolList[randomNumber].getValue();
                }

                // the current random number is assigned to this variable. Later this variable will compared against the next generated random number.
                this.randomNumberTemp = randomNumber;
                Thread.sleep(75);
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }


}

