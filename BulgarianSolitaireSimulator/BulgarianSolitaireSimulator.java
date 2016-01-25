import java.util.Scanner;

/**
 * class BulgarianSolitaireSimulator
 * A class to model the game Bulgarian Solitaire. Game starts with a given number
 * of cards (always equals to n * (n + 1) / 2 and n > 0). The starting configuration
 * can be random or specific according to whether the user initialize it (by using 
 * command line arg -u). Game ends with a static final configuration. Playing procedure 
 * can be showed directly or with stops (by using command line arg -s). 
 */

public class BulgarianSolitaireSimulator {

    public static void main(String[] args) {
     
	boolean singleStep = false;
	boolean userConfig = false;

	for (int i = 0; i < args.length; i++) {
	    if (args[i].equals("-u")) {
		userConfig = true;
	    }
	    else if (args[i].equals("-s")) {
		singleStep = true;
	    }
	}

	playGame(userConfig, singleStep);
    }
   
    //********************private methods*****************************************

    /**
     * Play the Bulgarian Solitaire game with a starting configuration. Whether 
     * the starting configuration is random or given and whether play each roud 
     * with a stop depends on the command line arguments
     * @param userConfig if true, initialize the starting configuration with given
     * array, otherwise use a random one
     * @param singleStep if true, stop after each round, otherwise play until is done
     */
    private static void playGame(boolean userConfig, boolean singleStep){
	SolitaireBoard newGame;
	if ( userConfig ) { 
	    newGame = new SolitaireBoard( userInput() );
	}
	else { newGame = new SolitaireBoard(); }

	System.out.println("Initial configuration: " + newGame.configString());
	   
	boolean isDone = newGame.isDone();
	int rounds = 0;
	String line = "";
	Scanner in = new Scanner(System.in);

	// check if the current configuration state is the end
	while( !isDone ){
	    rounds++;
	    newGame.playRound();
	    System.out.println("[" + rounds + "] Current configuration: " + newGame.configString());

	    if ( singleStep ){
		System.out.print("<Type return to continue>");
		line = in.nextLine();
	    }

	    isDone = newGame.isDone();
	}
	System.out.println("Done!");
    }
   
    /**
     * Return the String the user input, only if the String is valid.
     * Allow the user to initialize the configuration, redo it until the String entered is valid.
     */
    private static String userInput() {
	   
	System.out.println("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
	System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
	System.out.println("Please enter a space-separated list of positive integers followed by newline:");
	   
	Scanner in = new Scanner(System.in);
	String configString = in.nextLine();
	
	// check if the given String is valid for the our configuration
	while( !SolitaireBoard.isValidConfigString(configString) ){
	    System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be " 
			       + SolitaireBoard.CARD_TOTAL);
	    System.out.println("Please enter a space-separated list of positive integers followed by newline:");
	    configString = in.nextLine();
	}
	   
	return configString;
    }
   
}
