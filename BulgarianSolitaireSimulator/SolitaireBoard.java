import java.util.Random;
import java.util.Scanner;

/*
  class SolitaireBoard
  The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
  by changing NUM_FINAL_PILES, below.  There are only some values for this that result in a game that terminates.
  (See comments below next to named constant declarations for more details on this.)
*/

public class SolitaireBoard {

    public static final int NUM_FINAL_PILES = 9;
    // number of piles in a final configuration
    // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)

    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
    // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
    // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
    // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

    /**
     * Representation invariant: 
     * 1. numPiles is the number of the cardPiles 
     * 2. 0 < numPiles <= CARD_TOTAL 
     * 3. cardPiles are in cardPiles[0]-cardPiles[numPiles - 1] 
     * 4. for any integer i, 0 <= i < numPiles, cardPiles[i] must satisfy that 0 < cardPiles[i] <= CARD_TOTAL
     * 5. cardPiles[0] + cardPiles[1] + ... + cardPiles[numPiles - 1] = CARD_TOTAL 
     * <put rep. invar. comment here>
     */
    private int[] cardPiles;
    private int numPiles;

    /**
     * Initialize the board with the given configuration.
     * PRE:SolitaireBoard.isValidConfigString(numberString)
     */
    public SolitaireBoard(String numberString) {
	// assert isValidConfigString(numberString);

	cardPiles = new int[CARD_TOTAL];
	numPiles = constructConfig(cardPiles, numberString);

	assert isValidSolitaireBoard();
    }

    /**
     * Create a random initial configuration.
     */
    public SolitaireBoard() {
	Random generator = new Random();

	cardPiles = new int[CARD_TOTAL];
	numPiles = 0;
	int surplus = CARD_TOTAL; // total cards left

	while (surplus > 0) {
	    cardPiles[numPiles] = generator.nextInt(surplus) + 1;
	    surplus -= cardPiles[numPiles];
	    numPiles++;
	}

	assert isValidSolitaireBoard();
    }

    /**
     * Play one round of Bulgarian solitaire. Updates the configuration
     * according to the rules of Bulgarian solitaire: Takes one card from each
     * pile, and puts them all together in a new pile.
     */
    public void playRound() {
	for (int i = 0; i < numPiles; i++) {
	    cardPiles[i]--;
	}
	int temp = numPiles;
	deleteTarget(0);
	cardPiles[numPiles] = temp;
	numPiles++;

	assert isValidSolitaireBoard();
    }

    /**
     * Return true iff the current board is at the end of the game. That is,
     * there are NUM_FINAL_PILES piles that are of sizes 1, 2, 3, . . . ,
     * NUM_FINAL_PILES, in any order.
     */
    public boolean isDone() {
	// check if the numPiles equals to NUM_FINAL_PILES
	if (numPiles != NUM_FINAL_PILES) {
	    return false;
	}

	// count how many times 1 ~ NUM_FINAL_PILES exits separately in cardPile[i] (-1 < i < NUM_FINAL_PILES)
	int[] count = new int[NUM_FINAL_PILES];
	for (int i = 0; i < NUM_FINAL_PILES; i++) {
	    if (cardPiles[i] > 0 && cardPiles[i] <= NUM_FINAL_PILES) {
		count[ cardPiles[i] - 1 ]++;
	    }
	}

	for (int i = 0; i < NUM_FINAL_PILES; i++) {
	    if (count[i] != 1) {
		return false;
	    }
	}
		
	assert isValidSolitaireBoard();
	return true;
    }

    /**
     * Returns current board configuration as a string with the format of a
     * space-separated list of numbers with no leading or trailing spaces. The
     * numbers represent the number of cards in each non-empty pile.
     */
    public String configString() {
	String pilesString = "";
	for (int i = 0; i < numPiles - 1; i++) {
	    pilesString += (cardPiles[i] + " ");
	}
	pilesString += cardPiles[numPiles - 1];

	assert isValidSolitaireBoard();
	return pilesString;

    }

    /**
     * Returns true iff configString is a space-separated list of numbers that
     * is a valid Bulgarian solitaire board assuming the card total SolitaireBoard.CARD_TOTAL 
     * PRE: configString must only contain numbers and whitespace
     */
    public static boolean isValidConfigString(String configString) {
	int[] piles = new int[CARD_TOTAL];
	int newNumPiles = constructConfig( piles, configString);

	if (newNumPiles > CARD_TOTAL){
	    return false;
	}

	return isValidConfiguration(piles, newNumPiles);

    }

    /**
     * Returns true iff the solitaire board data is in a valid state 
     * (See representation invariant comment for more details.)
     */
    private boolean isValidSolitaireBoard() {

	return isValidConfiguration(this.cardPiles, this.numPiles);

    }

    // <add any additional private methods here>

    /**
     * Return number of valid piles in the array after remove the target.
     * Remove all the elements in the array which equal to the target integer,
     * modify the "size" of the array.
     * @param target the target integer to be removed 
     */
    private void deleteTarget(int target) {
	int[] newPiles = new int[CARD_TOTAL];
	int newNumPiles = 0;
	int i = 0;
	while (i < this.numPiles) {
	    if (cardPiles[i] != target) {
		newPiles[newNumPiles] = cardPiles[i];
		newNumPiles++;
	    }
	    i++;
	}

	this.cardPiles = newPiles;
	this.numPiles = newNumPiles;
    }

    /**
     * Return true iff the given array and integer is in a valid state for instance variables
     * (cardPiles and numPiles)
     * @param piles a partial filled array that represents the piles of the cards
     * @param numPiles an integer that represents number of valid piles
     */
    private static boolean isValidConfiguration(int[] piles, int numPiles) {
	// check if numPiles satisfies that 0 < numPiles <= CARD_TOTAL
	if (numPiles <= 0 || numPiles > CARD_TOTAL) {
	    return false;
	}

	// check if any element cardPiles[i] satisfies that 0 < cardPiles <= CARD_TOTAL
	for (int i = 0; i < numPiles; i++) {
	    if (piles[i] <= 0 || piles[i] > CARD_TOTAL) {
		return false;
	    }
	}

	// check if the total number of cards always equals to CARD_TOTAL
	int cardsTotal = 0;
	for (int i = 0; i < numPiles; i++) {
	    cardsTotal += piles[i];
	}
	if (cardsTotal != CARD_TOTAL) {
	    return false;
	}

	return true;
    }

    /**
     * Return number of integers in the String, convert a given String to an array
     * @param piles the array converted to
     * @param configString the String to be converted
     * @return number of valid elements in the array
     */
    private static int constructConfig(int[] piles, String configString){
	Scanner lineScanner = new Scanner(configString);
	int newNumPiles = 0;
	int waste = 0;

	while (lineScanner.hasNextInt()){
	    if (newNumPiles < CARD_TOTAL){
		piles[newNumPiles] = lineScanner.nextInt();
	    }
	    else { waste = lineScanner.nextInt();}
	    
	    newNumPiles++;
	}
		
	return newNumPiles;
    }
}
