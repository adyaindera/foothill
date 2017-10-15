/**
 * Card simulation including card, hand, and deck. A deck consists of
 * 52 pack of standard cards. User can input number of desired packs
 * in every Deck object, then deal the cards from the Deck
 * to n Hand, in normal order and shuffled order.
 *
 * @author Adya Putra Indera
 */

import java.util.Scanner;

public class Cards
{
   public static void main(String[] args)
   {
      //------PHASE 1--------------
      //instantiate Deck object with 2 packs
      Deck deck1 = new Deck(2);
      
      int totalCards;  
      totalCards = deck1.getTopCard();
      
      System.out.println("---------------------PHASE 1--------------------");
      System.out.println("Instantiate 2 packs of cards: \n");
      
      //remove the card and display it
      for (int i = 0; i < totalCards; i++)
         System.out.print(deck1.dealCard() + " / ");
      
      //store cards into deck randomly and display again
      deck1.init(2);
      deck1.shuffle();
      
      System.out.println("\n\nInitializing 2 packs and shuffle: \n");
      
      for (int i = 0; i < totalCards; i++)
         System.out.print(deck1.dealCard() + " / ");
      
      //make it 1 pack
      deck1.init(1);
      
      System.out.println("\n\nInitializing 1 pack: \n");
      
      totalCards = deck1.getTopCard();
      
      for (int i = 0; i < totalCards; i++)
         System.out.print(deck1.dealCard() + " / ");
      
      deck1.init(1);
      deck1.shuffle();
      
      System.out.println("\n\nInitializing 1 packs and shuffle: \n");
      
      for (int i = 0; i < totalCards; i++)
         System.out.print(deck1.dealCard() + " / ");

      //-----------PHASE 2-------------
      String inputString;
      int numHands = 0;
      Scanner input = new Scanner(System.in);
      
      System.out.println("\n\n-------------------PHASE 2-------------------");
      
      // validity for loop
      boolean valid = false;
      // input value from user, retry if the value is not valid
      do
      {
         System.out.print("How many hands? (1 - 10, please): ");
         
         inputString = input.nextLine();
         
         try
         {
            numHands = Integer.parseInt(inputString);
         }
         
         catch (NumberFormatException error)
         {  
            continue;
         }
         
         if (numHands < 1 || numHands > 10)
            continue;
         
         System.out.println("Here are our hands, from unshuffled deck: ");
         valid = true; 
      }
      while (!valid);
      
      // instantiate n Hand objects as input by user
      Hand[] player = new Hand[numHands];
      Deck deck = new Deck(); 
      
      for (int i = 0; i < numHands; i++)
      {
         player[i] = new Hand();
      }
      
      // loop counter
      int j = 0;

      // deal cards from Deck to Hands' objects and display it
      while (deck.getTopCard() > 0)
      {
         player[j++].takeCard(deck.dealCard());
            
         if (j == numHands)
            j = 0;
      }

      System.out.println();
      
      for (int i = 0; i < numHands; i++)
         System.out.println(player[i] + "\n");
         
      // restore a pack of cards and sort it randomly
      deck.init(1);
      deck.shuffle();
      
      // reset Hands' objects
      for (int i = 0; i < numHands; i++)
         player[i].resetHand();
      
      System.out.println("Here are our hands, from shuffled deck: \n");
      
      // loop counter
      j = 0;
      
      while (deck.getTopCard() > 0)
      {
         player[j++].takeCard(deck.dealCard());
            
         if (j == numHands)
            j = 0;
      }
      
      for (int i = 0; i < numHands; i++)
         System.out.println(player[i] + "\n");
   }
}

/**
 * a standard playing card with suit and value
 */
class Card
{
   // enum type for Card's Suit
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   }
   
   private char value;
   private Suit suit;
   private boolean errorFlag;

    /**
     * create card with input value and suit
     * @param value value of card
     * @param suit suit of card
     */
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

    /**
     * create default card: Ace Spades
     */
   public Card()
   {
      this('A', Suit.spades);
   }

    /**
     * mutator of value and suit
     * @param value value of card
     * @param suit suit of card
     * @return true or false
     */
   public boolean set(char value, Suit suit)
   {
      boolean valid = true;
      this.suit = suit;

      // make errorFlag true if not valid and vice versa
      if (!isValid(value, suit))
      {
         errorFlag = true;
         valid = false;
      }
      else
      {
         this.value = value;
         errorFlag = false;
      }
      
      return valid;
   }

    /**
     * create string representation of card
     */
   public String toString()
   {
      String retVal;

      if (errorFlag == true)
         retVal = "** illegal **";
      else
         retVal = String.valueOf(value) + " of " + suit;
      
      return retVal;
   }

   public char getValue()
   {
      return value;
   }
   
   public Suit getSuit()
   {
      return suit;
   }
   
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

    /**
     * check if Cards' members are identical to each other
     */
   public boolean equals(Card card)
   {
      if ((value == card.value) && (suit == card.suit) 
            && (errorFlag == card.errorFlag))
         return true;
      
      return false;      
   }

    /**
     * value validity test, no Suit test needed
     */
   private boolean isValid(char value, Suit suit)
   {
      char upVal;
      
      upVal = Character.toUpperCase(value);
      
      if (upVal == 'A' || upVal == 'T' || upVal == 'J' || upVal == 'Q' 
            || upVal == 'K' || (upVal >= '2' && upVal <= '9'))
         return true;
      
      return false;
   }
}

/**
 * Simulates a person with maximum of 2 set of standard pack cards
 * and has the ability to play out the card or get a new card
 */
class Hand
{
   private Card[] myCards;
   private int numCards;
   
   //maximum 2 set of standard pack cards
   public static final int MAX_CARDS = 104;

    /**
     * create array of cards
     */
   public Hand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

    /**
     * remove all cards
     */
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

    /**
     * insert a new card to the hand
     * @param card input card
     * @return true if there is space for a card
     */
   public boolean takeCard(Card card)
   {
      if (numCards == MAX_CARDS)
         return false;
      
      Card copyCard = new Card(card.getValue(), card.getSuit());
      
      myCards[numCards++] = copyCard;
      
      return true;
   }

    /**
     * remove the latest Card object in array from the Hand
     * @return the removed card
     */
   public Card playCard()
   {
      if (numCards == 0)
         return null;
      
      Card returnCard = myCards[numCards - 1];
      myCards[numCards - 1] = null;
      numCards--;
      
      return returnCard;
   }

    /**
     * create string representation of the hand of cards
     */
   public String toString()
   {
      String handString = "Hand = ( ";
      
      for (int i = 0; i < numCards; i++)
      {
         if (i != 0)
           handString += ", ";
        
         handString += myCards[i].toString();
      }
      
      handString += " )";
      
      return handString;
   }

    /**
     * find number of cards on hand
     * @return the number of cards
     */
   public int getNumCards()
   {
      return numCards;
   }

    /**
     * get a card from hand based on position without removing from hand
     * @param k index position
     * @return the inspected card
     */
   public Card inspectCard(int k)
   {
      if (k < 0 || k >= numCards)
      {
         Card illegalCard = new Card('I', Card.Suit.diamonds);
         
         return illegalCard;
      }
         
      return myCards[k];
   }
}

/**
 * consist set of several standard pack cards
 */
class Deck
{
   public static final int MAX_CARDS = 6 * 52; 
   
   private static Card[] masterPack;
   private Card[] cards;
   private int topCard;
   private int numPacks;
   
   // test if method has already been invoked
   private static boolean masterPackAllocated = false;

    /**
     * create desired standard pack of cards in 1 deck
     * @param numPacks number of packs
     */
   public Deck(int numPacks)
   {      
         allocateMasterPack();
         init(numPacks);
   }

    /**
     * create 1 standard pack of cards
     */
   public Deck()
   {
      this(1);
   }

    /**
     * create desired packs of 52-standard-card Deck
     * @param numPacks number of packs
     */
   public void init(int numPacks)
   {
      if (numPacks <= 0 || numPacks > 6)
         return;
      
      // masterPack loop counter
      int j = 0;
      
      this.numPacks = numPacks;
      
      topCard = 52 * numPacks;
      cards = new Card[topCard];
      
      for (int i = 0; i < topCard; i++)
      {
         cards[i] = masterPack[j++];
         
         if (j == 52)
            j = 0;
      }
   }

    /**
     * create standard 1 deck of 52 cards
     */
   private static void allocateMasterPack()
   {
      if (!masterPackAllocated)
      {
         Card.Suit st;
         int k, j;
         char val;
         
         masterPack = new Card[52];
         
         for (int i = 0; i < masterPack.length; i++)
            masterPack[i] = new Card();
         
         for (k = 0; k < 4; k++)
         {
            // set the suit for this loop pass
            switch(k)
            {
            case 0:
               st = Card.Suit.clubs; break;
            case 1:
               st = Card.Suit.diamonds; break;
            case 2:
               st = Card.Suit.hearts; break;
            case 3:
               st = Card.Suit.spades; break;
            default:
               st = Card.Suit.spades; break;
            }

            // now set all the values for this suit
            masterPack[13 * k].set('A', st);
            for (val = '2', j = 1; val <= '9'; val++, j++)
               masterPack[13 * k + j].set(val, st);
            masterPack[13 * k + 9].set('T', st);
            masterPack[13 * k + 10].set('J', st);
            masterPack[13 * k + 11].set('Q', st);
            masterPack[13 * k + 12].set('K', st);
         }
         
         masterPackAllocated = true;
      }
      else
         return;
   }

    /**
     * shuffle cards randomly
     */
   public void shuffle()
   {
      Card tempCard;
      int randomPosition;
      
      for (int i = 0; i < topCard; i++)
      {
         randomPosition = (int) ((Math.random() * topCard));
         tempCard = cards[i];
         cards[i] = cards[randomPosition];
         cards[randomPosition] = tempCard;
      }
         
   }

    /**
     * remove latest card from the deck
     * @return the removed card
     */
   public Card dealCard()
   {
      if (topCard == 0)
         return null;
      
      Card removedCard = cards[topCard - 1];
      cards[topCard - 1] = null;
      topCard--;
      
      return removedCard;
   }

    /**
     * get a card from deck based on position without removing from deck
     * @param k index position
     * @return the inspected card
     */
   public Card inspectCard(int k)
   {
      if (k < 0 || k >= topCard)
      {
         Card illegalCard = new Card('I', Card.Suit.diamonds);
         
         return illegalCard;
      }
      
      return cards[k];
   }

    /**
     * find total number of cards in the deck
     * @return the total cards
     */
   public int getTopCard()
   {
      return topCard;
   }
}