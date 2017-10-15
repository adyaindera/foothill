import java.util.Scanner;

/**
 * Inspired by Game of Life, new line generation will be generated
 * based on 3 chars of boolean "*" or " "
 * The rule is 0 - 255 and will be processed to generate new generation
 * suitable to binary number
 *
 * @author Adya Putra Indera
 */
public class CellularAutomata
{
   public static void main(String[] args)
   {
      int rule, k;
      String strUserIn;
      
      Scanner inputStream = new Scanner(System.in);
      Automaton aut;

      // get rule from user
      do
      {
         System.out.print("Enter Rule (0 - 255): ");
         // get the answer in the form of a string:
         strUserIn = inputStream.nextLine();
         // and convert it to a number so we can compute:
         rule = Integer.parseInt(strUserIn);
      } while (rule < 0 || rule > 255);

      // create automaton with this rule and single central dot
      aut = new Automaton(rule);

      // now show it
      System.out.println("   start");
      for (k = 0; k < 100; k++)
      {
         System.out.println( aut.toStringCurrentGen() );
         aut.propagateNewGeneration();
      }
      System.out.println("   end");
      inputStream.close();
   }
}

/**
 * compute new generation based on rule
 */
class Automaton
{
   public final static int MAX_DISPLAY_WIDTH = 121;
   
   private boolean rules[];
   private String thisGen;  
   String extremeBit; // bit, "*" or " ", implied everywhere "outside"
   int displayWidth;  // an odd number so it can be perfectly centered

    /**
     * constructor set default rule and width
     * @param newRule input rule number
     */
   public Automaton(int newRule)
   {
      rules = new boolean[8];
      
      if (!setRule(newRule))
         setRule(0);
      
      //set width to default of 79 chars
      setDisplayWidth(79);
      resetFirstGen();
   }

    /**
     * reset generation to the beginning
     */
   public void resetFirstGen()
   {
      thisGen = "*";
      extremeBit = " ";
   }

    /**
     * mutator convert the number to a byte of true or false each bit
     * @param newRule input rule number
     */
   public boolean setRule(int newRule)
   {
      if (newRule < 0 || newRule > 255)
         return false;
      
      //shift right and bitwise AND, if equals 1 then true, otherwise false
      for (int i = 0; i < 8; i++)
      {
         if (((newRule >> i) & 1) == 1)
            rules[i] = true;
         else
            rules[i] = false;
      }      
      return true;
   }

    /**
     * mutator set the width by parameter according to the rule
     * @param width input width number
     */
   public boolean setDisplayWidth(int width)
   {
      if (width < 1 || width > MAX_DISPLAY_WIDTH || width % 2 == 0)
         return false;
      
      displayWidth = width;
      return true;
   }

    /**
     * set the formation on screen based on thisGen's length
     */
   public String toStringCurrentGen()
   {
      String returnString = thisGen;
      int substringStart, substringEnd;
      
      if (thisGen.length() < displayWidth)
      {
         // pad extremeBit until the same length with displayWidth
         while(returnString.length() < displayWidth)
            returnString = extremeBit + returnString + extremeBit;
      }
      else
      {
         //make the generation centered and truncate the excess width
         substringStart = (thisGen.length() - displayWidth) / 2;
         substringEnd = thisGen.length() - substringStart;
         
         returnString = thisGen.substring(substringStart, substringEnd);
      }
      
      return returnString;
   }

    /**
     * create the next generation based on rule
     */
   public void propagateNewGeneration()
   {
      //pad thisGen with 2 extremeBits each side
      String tempGen = extremeBit + extremeBit + thisGen
            + extremeBit + extremeBit;
      String nextGen = "";
      String template;
      
      boolean apply = false;
      
      // loop for temporarily padded with extremeBit generation
      for (int i = 1; i < (tempGen.length() - 1); i++)
      {
         // template for rule
         template = "   *   * **   ** * *****";
         
         // loop for 1 byte of rule[]
         for (int j = 0; j < 8; j++)
         {
            // check if each bit equals the template rule
            if (tempGen.charAt(i - 1) == template.charAt(2)
                  && tempGen.charAt(i) == template.charAt(1)
                  && tempGen.charAt(i + 1) == template.charAt(0))
               apply = rules[j];
            
            template = template.substring(3);
         }
         
         if (apply)
            nextGen += "*";
         else
            nextGen += " ";
      }
      
      // applying next generation to current generation
      thisGen = nextGen;
      
      if (extremeBit == " ")
         apply = rules[0];
      else
         apply = rules[7];
      
      if (apply)
         extremeBit = "*";
      else 
         extremeBit = " ";
   }
}