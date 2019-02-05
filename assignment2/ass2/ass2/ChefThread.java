package ass2;

public class ChefThread extends Thread { // one chef thread
	protected Ingredient Ingredient;
	protected int Made = 0;
	protected static AgentThread agent = new AgentThread();

	public ChefThread(Ingredient unlimitedIngredient) {
		this.Ingredient = unlimitedIngredient;
	}

	/**
	 * Make sandwiches at right condition with one chef
	 */
	public void run() {
		while (agent.finishedMakingSandwiches() == false) {// 20 sandwich are done
			synchronized (agent) {
				try {
					while (agent.ingredientsAreOnTable == false) {
						agent.wait(); // until ingredient is on table
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (agent.ingredientsAreOnTable == true) {
					// Check if they are the ingredients we need
					boolean canMakeSandwich = true;
					if (agent.ingredientsOnTable[0] == this.Ingredient
							|| agent.ingredientsOnTable[1] == this.Ingredient) {
						canMakeSandwich = false;
					}
					// Make a sandwich with the chef we needs
					else if (canMakeSandwich) {
						agent.sandwichesMade++;
						agent.ingredientsAreOnTable = false;
						Made++;
					}
				}
				agent.notifyAll(); // release lock
			}
		}
		System.out.printf("The %s chef made %d sandwiches.%n", Ingredient, Made);
		// Output the total count of sandwiches we made
	}
}
