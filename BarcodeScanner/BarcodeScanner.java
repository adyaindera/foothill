/* This is a Barcode Scanner program. Barcode described as image is
 * a set of boolean drawn as '*' or '' which can be scanned and translated
 * into text. It works by translating the position character (like binary)
 * from int to char. This program is also capable of translating text into
 * "barcode" or the image.
 *
 * @author Adya Putra Indera
 */

import java.lang.Math;

public class BarcodeScanner
{
   public static void main(String[] args)
   {
      String[] sImageIn = 
         { 
               "                                      ",
               "                                      ",
               "* * * * * * * * * * * * * *           ",
               "*                          *          ",
               "***** **** **** ***** ****            ",
               "****************************          ",
               "*   *  * *  *   *  *  *  *            ",
               "**       *   **  **        *          ",
               "* **   *   * ** * * * *               ",
               "**     *   ***    **    ** *          ",
               "****  ****   **   ***  * **           ",
               "****************************          "
         };

      String[] sImageIn_2 = 
         { 
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    "
         };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();

      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create barcode
      dm.readText("CS 1B will lead me to the CASH!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
   }
}

/**
 * interface for DataMatrix to use to process image and text
 */
interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

/**
 * image of barcode, include boolean which later drawn as arterisk or blank
 */
class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   private boolean imageData[][];

    /**
     * create 2D array with max height and width
     */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];

      // assign the 2d array of imageData[][] all to false
      for (int row = 0; row < imageData.length; row++)
         for (int col = 0; col < imageData[row].length; col++)
            imageData[row][col] = false;
   }

    /**
     * generate barcode image with data
     * @param strData barcode data in string
     */
   public BarcodeImage(String[] strData)
   {
      this();

      if (!checkSize(strData))
         return;

      for (int row = 0; row < strData.length; row++)
      {
         for (int col = 0; col < strData[row].length(); col++)
         {
            if(strData[row].charAt(col) == '*') 
               setPixel(MAX_HEIGHT - (strData.length - row), col, true);
            else 
               setPixel(MAX_HEIGHT - (strData.length - row), col, false);
         }
      }
   }

   public boolean getPixel(int row, int col)
   {
      try 
      {
         return imageData[row][col];   
      }
      catch (ArrayIndexOutOfBoundsException e)
      {
         return false;
      }
   }

   public boolean setPixel(int row, int col, boolean value)
   {
      if (row < 0 || row >= MAX_HEIGHT || col >= MAX_WIDTH || col < 0)
         return false;

      imageData[row][col] = value;   
      return true;
   }

    /**
     * copy object
     * @return copied barcode image
     * @throws CloneNotSupportedException error
     */
   public Object clone() throws CloneNotSupportedException
   {
      BarcodeImage newBc = (BarcodeImage)super.clone();

      newBc.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for (int row = 0; row < MAX_HEIGHT; row++)
         for (int col = 0; col < MAX_WIDTH; col++)
            newBc.imageData[row][col] = this.imageData[row][col];

      return newBc;  
   }

    /**
     * helper method to check valid size
     */
   private boolean checkSize(String[] data)
   {
      if (data == null)
         return false;      
      if (data.length > MAX_HEIGHT)
         return false;
      return true;
   }
}

/**
 * Scan barcode to generate text or the other way around
 */
class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';  

   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;

    /**
     * create barcode image
     */
   public DataMatrix()
   {
      actualWidth = 0;
      actualHeight = 0;
      text = "";
      image = new BarcodeImage();
   }

    /**
     * create barcode image using input
     * @param image BarcodeImage input
     */
   public DataMatrix(BarcodeImage image)
   {
      this();
      scan(image);
   }

    /**
     * create barcode using text input
     * @param text String input
     */
   public DataMatrix(String text)
   {
      this();
      readText(text);
   }

    /**
     * scan the height and widht of an image
     * @param bc barcode image input
     * @return error or true
     */
   public boolean scan(BarcodeImage bc)
   {
      try 
      {
         this.image = (BarcodeImage) bc.clone();
      } 
      catch(CloneNotSupportedException e)
      {
      }

      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();

      return true;
   }

    /**
     * read the input text
     * @param text String input
     * @return true
     */
   public boolean readText(String text)
   {
      this.text = text;
      return true;
   }

    /**
     * generate barcode image from text
     * @return true
     */
   public boolean generateImageFromText() 
   {
      for(int i = 0; i < 10; i++)
      {
         image.setPixel(image.MAX_HEIGHT - i, 0, true);
      }

      for(int i = 0; i < text.length(); i++)
      {
         writeCharToCol(i + 1, (int)text.charAt(i));
      }

      for(int i = 0; i < 10; i++)
      {
         if(i % 2 == 0)
         {
            image.setPixel(image.MAX_HEIGHT - i, text.length() + 1, true);
         }
      }

      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();
      return true;
   }

    /**
     * generate String from barcode image
     * @return true
     */
   public boolean translateImageToText()
   {
      int row, col, digit;
      char temp;

      readText("");

      for (col = 1; col < actualWidth - 1; col++)
      {
         temp = 0;
         for (row = BarcodeImage.MAX_HEIGHT - 2, digit = 0; 
              row > BarcodeImage.MAX_HEIGHT - (actualHeight); row--, digit++)
         {
            if (image.getPixel(row, col) == true)
               temp += (int)Math.pow(2, digit);
         }
         text += temp;
      }
      return true;
   }

    /**
     * convert char to boolean in column
     */
   private boolean writeCharToCol(int col, int num)
   {
      String binary = Integer.toBinaryString(num);
      image.setPixel(image.MAX_HEIGHT, col, true);

      if (col % 2 == 0) 
      {
         image.setPixel(image.MAX_HEIGHT - (binary.length() + 1), col, true);
      }

      for (int i = 0; i < binary.length(); i++)
      {
         if (binary.charAt(i) == '1')
            image.setPixel((image.MAX_HEIGHT - 1) - (i + 1), col, true);
         else
            image.setPixel((image.MAX_HEIGHT - 1) - (i + 1), col, false);
      }

      return true;
   }

    /**
     * display the text result from scanned barcode
     */
   public void displayTextToConsole()
   {
      System.out.println(text);
   }

    /**
     * display the barcode image result from scanned String
     */
   public void displayImageToConsole()
   {
      int row, col;
      
      // top border
      System.out.println();
      for(col = 0; col < actualWidth + 2; col++)
         System.out.print("-");
      System.out.println();

      // side border and image
      for(row = image.MAX_HEIGHT - actualHeight; row < image.MAX_HEIGHT; row++)
      {
         System.out.print("|");
         for(col = 0; col < actualWidth; col++)
         {
            if(image.getPixel(row, col) == true) 
               System.out.print("*");
            else
               System.out.print(" ");
         }
         System.out.println("|");
      }

      // bottom border
      for(col = 0; col < actualWidth + 2; col++)
      {
         System.out.print("-");
      }
      System.out.println();
   }

    /**
     * calculate height of barcode image
     * @return height of barcode image
     */
   private int computeSignalHeight()
   {
      int height = 0;

      for (int row = 0; row < image.MAX_HEIGHT; row++)
      {
         if (image.getPixel(row, 0) == true)
            height++;
      }

      return height;
   }

    /**
     * calculate width of barcode image
     * @return width of barcode image
     */
   private int computeSignalWidth()
   {
      int width = 0;

      for(int col = 0; col < image.MAX_WIDTH; col++)
      {
         if (image.getPixel(image.MAX_HEIGHT - 1, col) == true)
            width++;
      }

      return width;
   }

   // accessors
   public int getActualHeight()
   {
      return actualHeight;
   }
   public int getActualWidth()
   {
      return actualWidth;
   }
}