package ass2;

public class ChefThread extends Thread {
	protected Ingredient unlimitedIngredient;
	protected int sandwichesMade = 0;
	protected static AgentThread agent = new AgentThread();

	public ChefThread(Ingredient unlimitedIngredient) {
		this.unlimitedIngredient = unlimitedIngredient;

	}

	/**
	 * Make sandwiches when possible :D
	 */
	public void run() {
		while (agent.finishedMakingSandwiches() == false) {
			synchronized (agent) {
				try {
					// Wait until there are ingredients on the table or we are
					// done making sandwiches
					while (agent.ingredientsAreOnTable == false && agent.finishedMakingSandwiches() == false) {
						agent.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Check if all sandwiches are done
				if (agent.finishedMakingSandwiches() == false && agent.ingredientsAreOnTable == true) {
					// Check if they are the ingredients we need
					boolean canMakeSandwich = true;
					for (Ingredient i : agent.ingredientsOnTable) {
						if (i == this.unlimitedIngredient) {
							canMakeSandwich = false;
						}
					}
					// Make a sandwich
					if (canMakeSandwich) {
						agent.sandwichesMade++;
						agent.ingredientsAreOnTable = false;
						sandwichesMade++;
					}
				}
				// Notify other threads that we are releasing the lock
				agent.notifyAll();
			}
		}
		// Output the total count of sandwiches we made
		System.out.printf("The %s chef made %d sandwiches.%n", unlimitedIngredient, sandwichesMade);
	}
}