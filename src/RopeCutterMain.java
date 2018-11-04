import java.util.ArrayList;
import java.util.Random;

/***
 * RopeCutterMain perform correctness and performance tests on the First-Fit and Best-Fit algorithms
 * @author Joseeph Whitten
 * @version 2
 * @since 14/12/15
 */
public class RopeCutterMain {
	
	private static final int ROPE_MAX = 200; //Maximum length of rope from factory
	private static final int ROPE_MIN = 100; // Minimum length of rope from factory
	private static final int ORDER_MAX = 100; //Maximum length of rope for an order
	private static final int ORDER_MIN = 1; //Minimum length of rope for an order
	
	private static final int TEST_ORDER_NUMBER = 10; //Amount of orders to perform a correctness test on
	
	//Array containing the sizes of the sets of orders we want to test
	private static final int[] ORDER_SIZES = {100, 200, 300, 500, 1000, 2000, 3000, 5000, 10000, 20000, 30000, 50000, 100000, 200000, 300000, 500000, 1000000};
	
	private final static int TEST_COUNT = 10000000; //Amount of tests to perform on a order set of size 1
	
	private final int SEED = 1000; //Seed for the random generator
	
	public static void main(String[] args) {
		Random r = new Random(); //Random Object for seed
		//runCorrectnessTest(); //Run correctness test
		runComparisonPerformanceTest(r); //Run performance test
	}
	
	/***
	 * Run tests for each set size in ORDER_SIZES
	 * Store the Time Taken, Import Count and Remaining Ropes for each test of each algorithm
	 * @param r The random object
	 */
	public static void runComparisonPerformanceTest(Random r) {
		Random rand = r;
	
		//Declare the factory and order list
		ArrayList<Integer> factoryRopes;
		ArrayList<Integer> customerOrders;
		
		long startTime; //Declare the start time variable for timing runs of the algorithm
		
		/*
		 * Declare multidimensional arrays that will store the time taken, the import count and the remaining ropes
		 * for each test of the First Fit algorithm
		 * [order size being tested][current test iteration]
		 */
		long[][] timesFF = new long[ORDER_SIZES.length][0];
		int[][] importCountsFF = new int[ORDER_SIZES.length][0];
		int[][] remainingRopesFF = new int[ORDER_SIZES.length][0];
		
		/*
		 * (First Fit)Store the average the time taken, the import count and the remaining ropes respectively 
		 * for each order size
		 */
 		long[] averageTimesFF = new long[ORDER_SIZES.length];
		int[] averageImportCountFF = new int[ORDER_SIZES.length];
		int[] averageRemainingRopeFF = new int[ORDER_SIZES.length];
		
		//(Best Fit)Create the jagged arrays to store the results of each test
		for(int i = 0; i < ORDER_SIZES.length; i++) {
			timesFF[i] = new long[TEST_COUNT/ORDER_SIZES[i]];
			importCountsFF[i] = new int[TEST_COUNT/ORDER_SIZES[i]];
			remainingRopesFF[i] = new int[TEST_COUNT/ORDER_SIZES[i]];
		}
		
		/*
		 * Declare multidimensional arrays that will store the time taken, the import count and the remaining ropes
		 * for each test of the Best Fit algorithm
		 * [order size being tested][current test iteration]
		 */
		long[][] timesBF = new long[ORDER_SIZES.length][0];
		int[][] importCountsBF = new int[ORDER_SIZES.length][0];
		int[][] remainingRopesBF = new int[ORDER_SIZES.length][0];
		
		/*
		 * (Best Fit)Store the average the time taken, the import count and the remaining ropes respectively 
		 * for each order size
		 */
		long[] averageTimesBF = new long[ORDER_SIZES.length];
		int[] averageImportCountBF = new int[ORDER_SIZES.length];
		int[] averageRemainingRopeBF = new int[ORDER_SIZES.length];
		
		//(Best Fit)Create the jagged arrays to store the results of each test
		for(int i = 0; i < ORDER_SIZES.length; i++) {
			timesBF[i] = new long[TEST_COUNT/ORDER_SIZES[i]];
			importCountsBF[i] = new int[TEST_COUNT/ORDER_SIZES[i]];
			remainingRopesBF[i] = new int[TEST_COUNT/ORDER_SIZES[i]];
		}
		
		//Declare the rope cutter
		RopeCutter ropeCutter;
		
		//For every element in ORDER_SIZE
		for(int k = 0; k < ORDER_SIZES.length; k++) {
			
			//For the amount of times we want to test an element of the current order size
			for(int i = 0; i < TEST_COUNT/ORDER_SIZES[k]; i++) {
				
				//Create a new factory and order list
				factoryRopes = new ArrayList<Integer>();
				customerOrders = new ArrayList<Integer>();
				
				//Fill the factory and order lists with random integers (of size equal to the current order size)
				generateRandomIntegerList(factoryRopes, rand, ORDER_SIZES[k],  ROPE_MIN, ROPE_MAX);
				generateRandomIntegerList(customerOrders, rand, ORDER_SIZES[k],   ORDER_MIN, ORDER_MAX);
				
				//*****FIRST FIT******
				
				//Create a new rope cutter using the new factory and order list
				ropeCutter = new RopeCutter(factoryRopes, customerOrders);
				
				//Start the timer
				startTime = System.nanoTime();
				//Run first fit algorithm
				ropeCutter.cutFirstFit();
				//Stop the timer, store the result
				timesFF[k][i] = (System.nanoTime() - startTime);
				//Store the remaining ropes
				remainingRopesFF[k][i] = ropeCutter.getStock().size();
				//Store the import count
				importCountsFF[k][i] = ropeCutter.getImportCount();
				
				//Add results to their respective averages
				averageTimesFF[k] += timesFF[k][i];
				averageImportCountFF[k] += importCountsFF[k][i];
				averageRemainingRopeFF[k] += remainingRopesFF[k][i];
				
				//*****BEST FIT******
				
				//Create a new ropeCutter using the same factory and order list
				ropeCutter = new RopeCutter(factoryRopes, customerOrders);
				//Reset and start the timer
				startTime = System.nanoTime();
				//Run the best fit algorithm
				ropeCutter.cutBestFit();
				//Stop the timer, store the result
				timesBF[k][i] = (System.nanoTime() - startTime);
				//Store the remaining ropes
				remainingRopesBF[k][i] = ropeCutter.getStock().size();
				//Store the import count
				importCountsBF[k][i] = ropeCutter.getImportCount();
				
				//Add results to their respective averages
				averageTimesBF[k] += timesBF[k][i];
				averageImportCountBF[k] += importCountsBF[k][i];
				averageRemainingRopeBF[k] += remainingRopesBF[k][i];
			}
			
			/*
			 * Store the test count for the current order size (we are testing First Fit and Best Fit equally so can
			 * use either timesFF or timesBF
			 */
			int currentTestCount = timesFF[k].length;
			
			//(First Fit)Average the times, import counts and remaining ropes for each order size
			averageTimesFF[k] = averageTimesFF[k] /currentTestCount;
			averageImportCountFF[k] = averageImportCountFF[k] / currentTestCount;
			averageRemainingRopeFF[k] = averageRemainingRopeFF[k] / currentTestCount;	
			
			//(Best Fit)Average the times, import counts and remaining ropes for each order size
			averageTimesBF[k] = averageTimesBF[k] / currentTestCount;
			averageImportCountBF[k] = averageImportCountBF[k] / currentTestCount;
			averageRemainingRopeBF[k] = averageRemainingRopeBF[k] / currentTestCount;
		}
		
		//Make First Fit table
		System.out.println("-------------------FIRST-FIT-------------------\n");
		System.out.println("Order Count\tTime Taken(ns)\tTime Taken(ms)\tRopes Imported\tRopes Remaining");
		
		//Print results
		for(int j = 0; j < ORDER_SIZES.length; j++) {
			double timeMS = averageTimesFF[j] / (double)1000000;
			System.out.print(ORDER_SIZES[j]);
			System.out.print("\t\t" + averageTimesFF[j]);
			System.out.printf("    \t%.2f", timeMS);
			System.out.print("\t\t" + averageImportCountFF[j]);
			System.out.println("\t\t" + averageRemainingRopeFF[j]);
		}
		
		//Make Best Fit table
		System.out.println("\n-------------------BEST-FIT-------------------\n");
		System.out.println("Order Count\tTime Taken(ns)\tTime Taken(ms)\tRopes Imported\tRopes Remaining");
		
		//Print results
		for(int j = 0; j < ORDER_SIZES.length; j++) {
			double timeMS = averageTimesBF[j] / (double)1000000;
			System.out.print(ORDER_SIZES[j]);
			System.out.print("\t\t" + averageTimesBF[j]);
			System.out.printf("    \t%.2f", timeMS);
			System.out.print("\t\t" + averageImportCountBF[j]);
			System.out.println("\t\t" + averageRemainingRopeBF[j]);
		}
	}
		
	/***
	 * Run a correctness test for TEST_ORDER_NUMBER orders
	 * @param r
	 */
	public static void runCorrectnessTest(Random r) {
		Random rand = r;
		
		//Create a factory and order list
		ArrayList<Integer> factoryRopes = new ArrayList<Integer>();
		ArrayList<Integer> customerOrders = new ArrayList<Integer>();
		
		//Fill the factory and order list with random integers
		generateRandomIntegerList(factoryRopes, rand, TEST_ORDER_NUMBER,  ROPE_MIN, ROPE_MAX);
		generateRandomIntegerList(customerOrders, rand, TEST_ORDER_NUMBER,   ORDER_MIN, ORDER_MAX);
		
		//Create a rope cutter
		RopeCutter ropeCutter = new RopeCutter(factoryRopes, customerOrders);
		
		//Show the factory and order list
		System.out.println("CORRECTNESS TEST");
		System.out.println("Factory Ropes: ");
		
		ropeCutter.disp1ayFactory();
		
		System.out.println("Orders Placed: ");
		
		ropeCutter.displayOrders();
		
		System.out.println("---------------TESTING FIRST-FIT----------------");
		
		ropeCutter.testCutFirstFit(); //run first fit
		
		//reset the rope cutter
		ropeCutter = new RopeCutter(factoryRopes, customerOrders);
		
		System.out.println("\n---------------TESTING BEST-FIT-----------------");
		
		ropeCutter.testCutBestFit(); //run best fit
	}
	
	/***
	 * Fill a list with an amount of random integers within a specified bounds 
	 * @param list The list to generate integers for
	 * @param rand The random object used to generate the integers
	 * @param size The amount of integers to generate
	 * @param min The minimum value a generated integer can be
	 * @param max The maximum value a generated integer can be
	 */
	private static void generateRandomIntegerList(ArrayList<Integer> list, Random rand, int size, int min, int max) {
		for(int i = 0; i < size; i++) {
			list.add(min + rand.nextInt(max - min));
		}
	}
	
	
}
