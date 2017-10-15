package quickSort;

import cs1c.FHsort;
import cs1c.TimeConverter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;


/**
 * Test QuickSort algorithm with recursion limit varies from 2 to 300 in 2 steps,
 * from array sized 20000 to 5120000.
 * The result (runtime of every limit and array size) will be imported to Excel
 * spreadsheet format using Apache POI API.
 *
 * @author CS 1C, Foothill College, Adya Putra Indera
 */
public class QuickSortAnalysis
{
    private static XSSFWorkbook workbook = new XSSFWorkbook();
    private static final String FILE_NAME = "resources/SORT_ANALYSIS.xlsx";
    private static final int UPPER_LIMIT_RECURSION = 300;
    private static final int LOWER_LIMIT_RECURSION = 2;
    private static final int UPPER_ARRAY_SIZE = 5120000;
    private static final int LOWER_ARRAY_SIZE = 20000;

    /**
     * Main method to test the runtime of recursion limit with different array size
     * and import the data to Excel spreadsheet
     * @param testSize array size
     */
    private static void compareRecLimQuickSort(int testSize)
    {
        int randomInt, rowDex, cellDex;
        long startTime, estimatedTime;

        XSSFSheet sortAnalysisSheet = workbook.createSheet("Array Size " + testSize);
        rowDex = 0;
        cellDex = 0;

        // generate data to excel
        Row row = sortAnalysisSheet.createRow(rowDex++);
        row.createCell(cellDex++).setCellValue("Recursion Limit");
        row.createCell(cellDex++).setCellValue("Runtime in ms");

        Integer[] arrayOfInts = new Integer[testSize];
        Integer[] arrayCopy = new Integer[testSize];

        // build arrays for comparing recursion limit of quick sort
        generateRandomArray(arrayOfInts, arrayCopy, testSize);

        // sorting with different recursion limit
        for (int limit = LOWER_LIMIT_RECURSION; limit <= UPPER_LIMIT_RECURSION; limit += 2)
        {
            FHsort.setRecursionLimit(limit);

            startTime = System.nanoTime();  // ------------------ start
            FHsort.quickSort(arrayOfInts);
            estimatedTime = System.nanoTime() - startTime;    // ---------------------- stop

            displaySortDetails(limit, estimatedTime, testSize);

            row = sortAnalysisSheet.createRow(rowDex++);
            cellDex = 0;
            row.createCell(cellDex++).setCellValue(limit);

            long milliseconds = TimeConverter.getMilliseconds(estimatedTime);
            row.createCell(cellDex++).setCellValue(milliseconds);

            // reset sorted arrays to unsorted arrays
            resetSortedArray(arrayOfInts, arrayCopy, testSize);
        }
    }

    /**
     * generate random integer depending on the array size
     * @param arrayOfInts array to be filled with random ints
     * @param arrayCopy copy of the random ints array
     * @param testSize size of array
     */
    private static void generateRandomArray(Integer[] arrayOfInts,
                                            Integer[] arrayCopy, int testSize)
    {
        int randomInt;

        for (int k = 0; k < testSize; k++)
        {
            randomInt = (int) (Math.random() * testSize);
            arrayOfInts[k] = randomInt;
            arrayCopy[k] = randomInt;
        }
    }

    /**
     * renew the already sorted array back to the original random ints array
     * @param arrayOfInts sorted ints array
     * @param arrayCopy original unsorted array
     * @param testSize size of array
     */
    private static void resetSortedArray(Integer[] arrayOfInts,
                                         Integer[] arrayCopy, int testSize)
    {
        for (int j = 0; j < testSize; j++)
            arrayOfInts[j] = arrayCopy[j];
    }

    /**
     * provide output of the details of array size, recursion limit, and runtime
     * @param limit recursion limit
     * @param estimatedTime sorting runtime
     * @param testSize size of array
     */
    private static void displaySortDetails(int limit, long estimatedTime, int testSize)
    {
        System.out.println("Quick sort Elapsed Time: "
                + " recursion limit: " + limit
                + ", size: " + testSize + ", "
                + TimeConverter.convertTimeToString(estimatedTime)
                + " = " + estimatedTime + "ns");
    }

    /**
     * write all the data to Excel Spreadsheet and create the spreadsheet in computer
     */
    private static void outputToExcel()
    {
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testing runtime of QuickSort algorithm with array sized from 20000 to 512000
     * and import the data to Excel Spreadsheet
     *
     * @throws Exception error handling of writing Excel Spreadsheet
     */
    public static void main(String[] args) throws Exception
    {
        for (int arraySize = LOWER_ARRAY_SIZE; arraySize <= UPPER_ARRAY_SIZE; arraySize *= 4)
        {
            compareRecLimQuickSort(arraySize);
            System.out.println();
        }

        outputToExcel();
    }
}