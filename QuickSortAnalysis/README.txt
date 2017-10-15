Comments:
Based on the data visualization, the general trend is runtime will get slower as limit recursion increases.
When the array size is huge, quick sort is absolutely the wise choice for sorting the elements.
The data shows the most effective recursion limit is around 30.

Apache POI is used to import data to Excel Spreadsheet.

lib/
    - Apache POI API to import data from QuickSort analysis to Excel Spreadsheet.


resources/SORT_ANALYSIS.xlsx
    - Imported result of runtime from quickSort.QuickSortAnalysis.java and graph analysis
      for each size of array in each different Excel Sheet.
    - Array sizes: 20000, 80000, 320000, 1280000, 5120000

resources/Array_Size_20000_snap.PNG
    - Array sized 20000 runtime graph snapshot

resources/Array_Size_320000_snap.PNG
    - Array sized 320000 runtime graph snapshot

resources/RUN.txt
    - Output details of QuickSort runtime with array sized from 20000 to 5120000 and recursion limit
      from 2 to 300 in 2 steps.


src/cs1c/FHsort.java
    - Sorting algorithm collection.

src/cs1c/TimeConverter.java
    - Convert time in system into String output.

src/quickSort/QuickSortAnalysis.java
    - Testing the effectiveness of QuickSort with different recursion limit and array size.
    - After testing different array sizes, the maximum array size is ~5000000