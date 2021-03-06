package svd;

import java.util.ArrayList;
import java.util.HashMap;

public class SingularValueDecomposition {
	private Parameters parameters = new Parameters();
	private ArrayList<Rating> ratings = new ArrayList<Rating>();
	private HashMap<Integer, User> users = new HashMap<Integer, User>();
	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public SingularValueDecomposition () {
		super();
	}

	public SingularValueDecomposition(HashMap<Integer, User> users, HashMap<Integer, Item> items, ArrayList<Rating> ratings) {
		this.ratings = ratings;
		this.items = items;
		this.users = users;
	}

	/**
	 * Train SVD using Stochastic Gradient Descent
	 */
	public void train() {
		stochasticGradientDescent();
	}

	private void stochasticGradientDescent() {
		//TODO: randomly shuffle the training set to optimize convergence

		// features in a vector
		for (int rank = 0; rank < parameters.rank; rank++) {
			// iterations
			for (int epoch = 0; epoch < parameters.iterations; epoch++) {
				// all ratings
				for (int i = 0; i < ratings.size(); i++) {
					User user = users.get(ratings.get(i).getUserId());
					Item item = items.get(ratings.get(i).getItemId());

					double error = parameters.learningRate * (ratings.get(i).getRating() - predictRating(user.getId(), item.getId()));

					double userVector = user.getFeatureVector().getFeatures()[rank];
					double itemVector = item.getFeatureVector().getFeatures()[rank];

					user.getFeatureVector().getFeatures()[rank] += (error * itemVector);
					item.getFeatureVector().getFeatures()[rank] += (error *  userVector);
				}
			}
		}
	}

	public double predictRating(int userId, int itemId) {
		User user = users.get(userId);
		Item item = items.get(itemId);
		return user.getFeatureVector().dotProduct(item.getFeatureVector());
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public HashMap<Integer, User> getUsers() {
		return users;
	}

	public HashMap<Integer, Item> getItems() {
		return items;
	}

	public class Parameters {
		// Default values
		private int iterations = 5000;
		private int rank = 5;
		private double learningRate = 0.001;
		private double minError = 0.0001;
	}
}

