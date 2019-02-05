package ass2;

import java.util.Random;

/**
 * Xinyu Chen 101031031
 */
public class AgentThread extends Thread {
	
	protected Ingredient[] ingredientsOnTable = new Ingredient[2];
	protected int sandwichesMade = 0;
	protected int maxSandwiches = 20;
	protected static Ingredient[] allIngredients = Ingredient.values();
	protected boolean ingredientsAreOnTable = false;

	public static void main(String[] args) {
		// Start and store the agent thread	
		AgentThread a= new AgentThread();
		a=ChefThread.agent;
		a.start();

		// Start the chefs (one for each ingredient)
		for (Ingredient ingredient : allIngredients) {
			ChefThread i = new ChefThread(ingredient);
			i.start();
		}
	}

	public void run() {
		while (!finishedMakingSandwiches()) {
			putIngredientsOnTable();
		}

		// Print out the total number of sandwiches that were made
		System.out.printf("A total of 20 sandwiches have been made.\n");
	}

	public synchronized void putIngredientsOnTable() {
		Ingredient[] ingredients = new Ingredient[2];
		Random rnd = new Random(); // generate a random number
		// Pick a random ingredient
		ingredients[0] = allIngredients[rnd.nextInt(3)];
		// Pick a second random ingredient
		ingredients[1] = allIngredients[rnd.nextInt(3)];
		while (ingredients[0] == ingredients[1]) {
			ingredients[1] = allIngredients[rnd.nextInt(3)];
		} 
		// Get the lock and put them on the table
		while (this.ingredientsAreOnTable==true) { //if is making sandwich
			try {
					wait();
				}		
		    catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			// Put the new ingredients on the table
			ingredientsOnTable[0] = ingredients[0];
			ingredientsOnTable[1] = ingredients[1];
			this.ingredientsAreOnTable=true;
			// Notify that we are releasing the lock
			notifyAll();		
	}

	
	/**
	 * Check if the limit for the number of sandwiches we can make has been
	 * reached
	 * 
	 * @return true if we have reached (or passed) maxSandwiches
	 */
	public boolean finishedMakingSandwiches() {
		return (sandwichesMade >= maxSandwiches);
	}

}
