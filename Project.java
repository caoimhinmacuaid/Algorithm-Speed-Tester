import java.text.DecimalFormat;


public class Project {
	
	public static void main(String a[]) {

		int[] testValues = { 100, 250, 500, 750, 1000, 1250, 2500, 3750, 5000, 6250, 7500, 8750, 10000 };//declares an array of different input values

		evaluateAndPrint("topOfTable", testValues); //prints top of table

		System.out.print("Insertion Sort "); //evaluates the average running time of insertion sort for each test value
		evaluateAndPrint("insertion", testValues);

		System.out.print("Quick Sort     ");// does the same for quick sort
		evaluateAndPrint("quick", testValues);

		System.out.print("Bucket Sort    ");// does the same for bucket sort
		evaluateAndPrint("bucket", testValues);

		System.out.print("Radix Sort     "); // does the same for radix sort
		evaluateAndPrint("radix", testValues);

		System.out.print("Bubble Sort    "); //does the same for bubble sort
		evaluateAndPrint("bubble", testValues);

	}


	public static void evaluateAndPrint(String identifier, int[] arrayValues) {

		if (identifier == "topOfTable") { //this section of code prints the top of the table with neat formatting
			System.out.print("Size           ");
			for (int i = 0; i < arrayValues.length; i++) {
				if (arrayValues[i] < 1000) {
					System.out.print(arrayValues[i] + "      ");
				} else {
					System.out.print(arrayValues[i] + "     ");
				}
			}
			System.out.println();

		} else {
			for (int i = 0; i < arrayValues.length; i++) { // benchmarks the corresponding sorting algorithm looping through differing test input values
				System.out.print(formatDouble(benchmark(identifier, arrayValues[i])) + "   ");// also formats the output so it is displayed neatly
			}
			System.out.println();
		}
	}
	public static String formatDouble(double value) {
		DecimalFormat df1 = new DecimalFormat("#0.0000");//declares 3 different decimal formats
		DecimalFormat df2 = new DecimalFormat("#00.000");//
		DecimalFormat df3 = new DecimalFormat("#000.00");//
		if (value < 10) {
			return df1.format(value);// returns second decimal format if value < 10
		} else if (value >= 10 && value < 100) {
			return df2.format(value);// returns second decimal format if 10 <= value < 100
		} else if (value >= 100) {
			return df3.format(value);// returns third decimal format if value >= 100
		} else
			throw new IllegalArgumentException();
	}

	public static void sortSelector(String identifier, int[] myArray) {
		switch (identifier) { //switch statement that checks identifier and calls corresponding sorting algorithm
		case "insertion":
			insertionSort(myArray);
			break;
		case "quick":
			quickSort(myArray, 0, myArray.length - 1);
			break;
		case "bucket":
			bucketSort(myArray, maxValue(myArray));
			break;
		case "radix":
			radixSort(myArray, myArray.length);
			break;
		case "bubble":
			bubbleSort(myArray);
			break;
		}
	}

	public static double benchmark(String identifier, int numOfElements) {
		int[] myArray ;
		double sum = 0;
		double average = 0;
		int warmup = 100; // number of warmup iterations that are completed before data collection begins
		for (int i = 0; i < warmup; i++) {

			myArray = randomArray(numOfElements);
			sortSelector(identifier, myArray);

		}
		for (int i = 0; i < 10; i++) {
			myArray = randomArray(numOfElements); //generates a random array
			long startTime = System.nanoTime();//records start time
			sortSelector(identifier, myArray);// calls method whose switch statement determines what sorting algorithm to call
			long endTime = System.nanoTime();// records end time
			long timeElapsed = endTime - startTime;// calculates time elapsed
			sum = sum + (double) timeElapsed / 1000000; // convert to ms

		}

		average = sum / 10;// divide by number of iterations
		return average;

	}

	public static int[] randomArray(int n) {// generates a random array between 0 and 100
		int[] array = new int[n];
		for (int i = 0; i < n; i++) {
			array[i] = (int) (Math.random() * 100);
		}
		return array;
	}

	public static void insertionSort(int arr[]) 
	{
		int n = arr.length;

		for (int i = 1; i < n; i++) {
			int key = arr[i]; //adjacent elements to be compared
			int j = i - 1; 	  //compared

			while ((j > -1) && (arr[j] > key)) { 
				//if element at index j is > than element adjacent to it then the elements are swapped
				arr[j + 1] = arr[j]; 
				j--;
			}
			arr[j + 1] = key;

		}
	}

	public static void quickSort(int[] arr, int start, int end) {

		int partition = partition(arr, start, end);

		if (partition - 1 > start) {
			quickSort(arr, start, partition - 1);
		}
		if (partition + 1 < end) {
			quickSort(arr, partition + 1, end);
		}
	}

	public static int partition(int[] arr, int start, int end) {
		int pivot = arr[end];

		for (int i = start; i < end; i++) {
			if (arr[i] < pivot) {
				int temp = arr[start];
				arr[start] = arr[i];
				arr[i] = temp;
				start++;
			}
		}

		int temp = arr[start];
		arr[start] = pivot;
		arr[end] = temp;

		return start;
	}

	public static int[] bucketSort(int[] arr, int max_value) 
	{
		int[] bucket = new int[max_value + 1];
		int[] sorted_arr = new int[arr.length];

		for (int i = 0; i < arr.length; i++)
			bucket[arr[i]]++;

		int pos = 0;
		for (int i = 0; i < bucket.length; i++)
			for (int j = 0; j < bucket[i]; j++)
				sorted_arr[pos++] = i;

		return sorted_arr;
	}

	static int maxValue(int[] arr) { // determines maximum value of array
		int max_value = 0;
		for (int i = 0; i < arr.length; i++)
			if (arr[i] > max_value)
				max_value = arr[i];
		return max_value;
	}

	static void countingSort(int array[], int size, int place) { // part of the radix sort algorithm
		int[] output = new int[size + 1];						 // sorts elements in buckets
		int max = array[0];
		for (int i = 1; i < size; i++) {
			if (array[i] > max)
				max = array[i];
		}
		int[] count = new int[max + 1];

		for (int i = 0; i < max; ++i)
			count[i] = 0;

		// Counts the number of elements
		for (int i = 0; i < size; i++)
			count[(array[i] / place) % 10]++;

		// Calculates the cumulative count
		for (int i = 1; i < 10; i++)
			count[i] += count[i - 1];

		// Places elements in sorted order
		for (int i = size - 1; i >= 0; i--) {
			output[count[(array[i] / place) % 10] - 1] = array[i];
			count[(array[i] / place) % 10]--;
		}

		for (int i = 0; i < size; i++)
			array[i] = output[i];
	}

	// Determines largest element of array
	static int getMax(int array[], int n) {
		int max = array[0];
		for (int i = 1; i < n; i++)
			if (array[i] > max)
				max = array[i];
		return max;
	}

	
	static void radixSort(int array[], int size) {
		// Determine max element
		int max = getMax(array, size);

		// Call counting sort to sort elements in the buckets
		for (int place = 1; max / place > 0; place *= 10)
			countingSort(array, size, place);
	}

	public static void bubbleSort(int[] arr) {
		int n = arr.length;
		for (int outer = n - 1; outer > 0; outer--) { // outer loop which decrements from the index of the last value of the array
			for (int inner = 0; inner < outer; inner++) {
				if (arr[inner] > arr[inner + 1]) {
					// compare adjacent elements and swap if out of order
					int temp = arr[inner];
					arr[inner] = arr[inner + 1];
					arr[inner + 1] = temp;
				}
			}

		}

	}

	static void printArray(int[] array) {

		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();

	}
}
