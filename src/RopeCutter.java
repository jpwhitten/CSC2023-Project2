import java.util.ArrayList;
import java.util.Random;

/***
 * RopeCutter object, stores the orders and the factory
 * Cuts ropes based on either a First-Fit or Best-Fit algorithm
 * @author Joseeph Whitten
 * @version 2
 * @since 14/12/15
 */
public class RopeCutter {
	
	private ArrayList<Integer> factory; //The factory from which we import rope
	private ArrayList<Integer> orders; //the order list from which get the amount of rope to cut
	private ArrayList<Rope> stock = new ArrayList<Rope>(); //The current stock of rope
	
	private int SCRAP_LENGTH = 5; //Any rope below this length is scrapped
	
	private int ropeImports = 0; //Stores the amount of ropes imported
	
	//constructor
	public RopeCutter(ArrayList<Integer> factoryRopes, ArrayList<Integer> customerOrders) {
		factory = factoryRopes;
		orders = customerOrders;
	}
	
	/***
	 * Prints out each step of the algorithm for correctness testing
	 * Cuts the ropes using the First-Fit algorithm for all the orders, 
	 * imports a rope if First-Fit can not find a suitable rope
	 */
	public void testCutFirstFit() {
		
		int orderLength;
		int index;
		
		for(int i = 0; i < orders.size(); i++) {
			
			orderLength = orders.get(i); //Get the current order
			
			index = findFirstFit(orderLength); //Find the first suitable rope
			
			System.out.println("------------------------------------------------");
			System.out.println("Order Number: " + i + "\tOrder Length: " + orderLength);
			
			//If no rope is found
			if(index == -1) {
				
				int importLength = factory.get(ropeImports); //get new rope length for printing
				System.out.println("No suitable rope in stock!");
				System.out.println("Importing rope of length " + importLength); //Show length of rope being imported
				
				importRope(importLength); //Import a new rope
				index = stock.size() - 1; //Index to cut is the same as the index of the rope just imported
			}
			System.out.println("Current Stock: \n" );
			displayStock();
			System.out.println("Cutting Rope at index: " + index  + " for " + orderLength); //Show which ropeis being cut
			
			cut(index, orderLength); //Cut the selected rope
			
			int ropeLength = stock.get(index).getLength(); //Get the rope length for printing	
			System.out.println("Rope at index " + index + " should now be of length " + ropeLength); //Print new rope length
			System.out.println("Scrap rope at index " + index + " ?"); //Print if rope is being scrapped
			
			boolean removed = testShouldRemoveRope(index); //Get if the rope is being scrapped for printing
			System.out.println(removed);
			
			shouldRemoveRope(index); //Check if rope that has been cut should be removed and if do remove it
		}
		System.out.println("\n\nFIRST-FIT TEST COMPLETE\n");
	}
	
	/***
	 * Cuts the ropes using the First-Fit algorithm for all the orders, 
	 * imports a rope if First-Fit can not find a suitable rope
	 */
	public void cutFirstFit() {
		
		int orderLength; //Length of the current order
		int index; //Index of rope to be cut
		
		//Loop through the orders
		for(int i = 0; i < orders.size(); i++) {
			
			orderLength = orders.get(i); //store current order
			
			index = findFirstFit(orderLength); //Find index of rope to be cut
			
			//If no suitable rope is found
			if(index == -1) {
				
				importRope(factory.get(ropeImports)); //Import a rope
				index = stock.size() - 1; //Set index to that of rope just imported
				
			}
			
			cut(index, orderLength); //Cut rope at 'index' by the crrent order
			shouldRemoveRope(index); //Check if rope that has been cut should be removed and if do remove it
		}
	}
	
	/***
	 * Find the first suitable rope to cut and return its index
	 * @param orderLength the length of rope required by the order 
	 * @return Index of the first suitable rope in stock, or -1 if a suitable rope is not found
	 */
	private int findFirstFit(int orderLength) {
		//Loop through the ropes until a suitable rope is found
		for(int i = 0; i < stock.size(); i++) {
			if(canCut(i, orderLength)) {
				return i;
			}
		}
		return -1;
	}
	
	/***
	 * Prints out each step of the algorithm for correctness testing
	 * Cuts the ropes using the Best-Fit algorithm for all the orders, 
	 * imports a rope if Best-Fit can not find a suitable rope
	 */
	public void testCutBestFit() {
		int orderLength;
		int index;
		
		for(int i = 0; i < orders.size(); i++) {
			
			orderLength = orders.get(i); //Get the current order
			
			index = findBestFit(orderLength); //Find the first suitable rope
			
			System.out.println("------------------------------------------------");
			System.out.println("Order Number: " + i + "\tOrder Length: " + orderLength);
			
			//If no rope is found
			if(index == -1) {
				
				int importLength = factory.get(ropeImports); //get new rope length for printing
				System.out.println("No suitable rope in stock!");
				System.out.println("Importing rope of length " + importLength); //Show length of rope being imported
				
				importRope(importLength); //Import a new rope
				index = stock.size() - 1; //Index to cut is the same as the index of the rope just imported
			}
			System.out.println("Current Stock: \n" );
			displayStock();
			System.out.println("Cutting Rope at index: " + index  + " for " + orderLength); //Show which ropeis being cut
			
			cut(index, orderLength); //Cut the selected rope
			
			int ropeLength = stock.get(index).getLength(); //Get the rope length for printing	
			System.out.println("Rope at index " + index + " should now be of length " + ropeLength); //Print new rope length
			System.out.println("Scrap rope at index " + index + " ?"); //Print if rope is being scrapped
			
			boolean removed = testShouldRemoveRope(index); //Get if the rope is being scrapped for printing
			System.out.println(removed);
			
			shouldRemoveRope(index); //Check if rope that has been cut should be removed and if do remove it
		}
		System.out.println("\n\nBEST-FIT TEST COMPLETE\n");
	}
	
	/***
	 * Cuts the ropes using the Best-Fit algorithm for all the orders, 
	 * imports a rope if Best-Fit can not find a suitable rope
	 */
	public void cutBestFit() {
		
		int orderLength; //Length of the current order
		int index; //Index of rope to be cut
		
		//Loop through the orders
		for(int i = 0; i < orders.size(); i++) {
			
			orderLength = orders.get(i); //Store the current order
			index = findBestFit(orderLength); //Find the best rope for this order
			
			//If no rope is found
			if(index == -1) {
				
				importRope(factory.get(ropeImports)); //Import a new rope
				index = stock.size() - 1; //Set index to that of rope just imported
				
			}
			
			cut(index, orderLength); //Cut rope at 'index' by the crrent order
			shouldRemoveRope(index); //Check if rope that has been cut should be removed and if do remove it
		}
	}
	
	/***
	 * Find the most suitable rope to cut then return its index
	 * @param orderLength the length of rope required by the order 
	 * @return Index of the first suitable rope in stock, or -1 if a suitable rope is not found
	 */
	private int findBestFit(int orderLength) {
		
		int bestLength = Integer.MAX_VALUE; //Value larger then MAX_ROPE required for algorithm to guarantee it will work
		int bestIndex = -1; //set best index to be -1
		Rope r;
		
		//Loop through all ropes in stock
		for(int i = 0; i < stock.size(); i++) {
			
			r = stock.get(i);
			
			//Select the rope that is closest to the size of the order while still being long enough to cut
			if(canCut(i, orderLength) && r.getLength() < bestLength) {
				
				if(r.getLength() == orderLength) {
					return i;
				}
				bestLength = r.getLength();
				bestIndex = i;
			}
		}
		return bestIndex;
	}
	
	/***
	 * Import a new rope from the factory, increment the import count by 1
	 * @param length An integer from the factory list
	 */
	private void importRope(int length) {
		stock.add(new Rope(length));
		ropeImports++;
	}
	
	/***
	 * Cut a length of rope from the rope at 'index' in 'stock'
	 * @param index Index of the rope in 'stock'
	 * @param cutLength Length being cut from the rope at 'index'
	 */
	public void cut(int index, int cutLength) {
		Rope r = stock.get(index);
		r.setLength(r.getLength() - cutLength);
	}
	
	/***
	 * Check to see if a rope at 'index' in 'stock' is long enough to have 'cutLength' removed from it
	 * @param index Index of rope in 'stock'
	 * @param cutLength Length of rope required by the order
	 * @return Boolean (is rope longer then the order?)
	 */
	public boolean canCut(int index, int cutLength) {
		return stock.get(index).getLength() >= cutLength;
	}
	
	/***
	 * Check if a rope is small enough to be removed from stock and remove it so
	 * @param index Index of rope in 'stock'
	 */
	private void shouldRemoveRope(int index) {
		if(stock.get(index).getLength() < SCRAP_LENGTH) {
			stock.remove(index);
		}
	}
	
	/***
	 * Check if a rope is small enough to be removed from stock and remove it so
	 * @param index Index of rope in 'stock'
	 */
	private boolean testShouldRemoveRope(int index) {
		return stock.get(index).getLength() < SCRAP_LENGTH;
	}
	
	/***
	 * Display the order integers
	 */
	public void displayOrders() {
		for(int i = 0; i < orders.size(); i++) {
			System.out.print(orders.get(i) + ", ");
		}
		System.out.println("\n");
	}
	
	/***
	 * Display the ropes in stock
	 */
	public void displayStock() {
		for(int i = 0; i < stock.size(); i++) {
			System.out.print(stock.get(i).getLength() + ", ");
		}
		System.out.println("\n");
	}
	
	/***
	 * Display the factory integers
	 */
	public void disp1ayFactory() {
		for(int i = 0; i < factory.size(); i++) {
			System.out.print(factory.get(i) + ", ");
		}
		System.out.println("\n");
	}
	
	/***
	 * Returns the import count
	 * @return ImportCount
	 */
	public int getImportCount() {
		return this.ropeImports;
	}
	
	/***
	 * Returns the current Stock
	 * @return Stock
	 */
	public ArrayList<Rope> getStock() {
		return this.stock;
	}
	
}
