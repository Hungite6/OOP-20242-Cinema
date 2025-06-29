package application.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JSONUtility {

	static String path_userdata = "src/application/database/userdata.json";
	String path_moviedata = "src/application/database/moviedata.json";

	// take ResultSet and store in userdata.json file
	public static void storeUserDataFromResultSet(ResultSet rs) throws IOException, SQLException {
		// Extract user specific data from the ResultSet
		int userId = rs.getInt("id");
		String phoneNumber = rs.getString("phoneNumber");
		String firstName = rs.getString("firstName");
		String lastName = rs.getString("lastName");
		String cityName = rs.getString("cityName");
		String userEmail = rs.getString("emailAddress");
		boolean isSuperUser = rs.getInt("isSuperUser") == 1;

		// Check for null or empty values and replace with empty string
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			phoneNumber = "";
		}
		if (firstName == null || firstName.isEmpty()) {
			firstName = "";
		}
		if (lastName == null || lastName.isEmpty()) {
			lastName = "";
		}
		if (cityName == null || cityName.isEmpty()) {
			cityName = "";
		}
		if (userEmail == null || userEmail.isEmpty()) {
			userEmail = "";
		}

		// Create Gson object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Create User object
		User user = new User(userId, firstName, lastName, userEmail, phoneNumber, cityName);

		// Convert User object to JSON string
		String jsonString = gson.toJson(user);

		// Write JSON string to file
		try (FileWriter writer = new FileWriter(path_userdata)) {
			writer.write(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Inner class representing User
	static class User {
		public int userId; // Make the field public
		String firstName, lastName, email, phoneNumber, cityName;
		public boolean isSuperUser;

		// Getters and setters
		public boolean isSuperUser() {
			return isSuperUser;
		}

		public void setSuperUser(boolean superUser) {
			isSuperUser = superUser;
		}
		// Constructor
		public User(int id, String fname, String lname, String email, String phoneNumber, String cityName) {
			this.userId = id;
			this.firstName = fname;
			this.lastName = lname;
			this.email = email;
			this.phoneNumber = phoneNumber;
			this.cityName = cityName;
		}
	}

	// check if the userdata.json has userId and email values for auto-login from
	// welcome screen
	public static boolean userIsLoggedIn() {
		// Path to the userdata.json file
		String filePath = "src/application/database/userdata.json";

		// Try-with-resources to automatically close the FileReader
		try (FileReader reader = new FileReader(filePath)) {
			// Parse JSON file
			JsonElement jsonElement = JsonParser.parseReader(reader);
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			// Check if JSON contains userId and email fields
			return jsonObject.has("userId") && !jsonObject.get("userId").isJsonNull() && jsonObject.has("email")
					&& !jsonObject.get("email").isJsonNull() && !jsonObject.get("userId").getAsString().isEmpty()
					&& !jsonObject.get("email").getAsString().isEmpty();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If user is not found
		return false;
	}

	// remove user details from userdata.json for logout from dashboard
	public static boolean removeValuesAndSave() {
		// Path to the userdata.json file
		String filePath = "src/application/database/userdata.json";

		try {
			// Read JSON file
			FileReader reader = new FileReader(filePath);
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			reader.close();

			// Modify JSON object
			jsonObject.addProperty("userId", "");
			jsonObject.addProperty("email", "");
			jsonObject.addProperty("firstName", "");
			jsonObject.addProperty("lastName", "");
			jsonObject.addProperty("phoneNumber", "");
			jsonObject.addProperty("cityName", "");

			// Write modified JSON back to file
			FileWriter writer = new FileWriter(filePath);
			Gson gson = new Gson();
			gson.toJson(jsonObject, writer);
			writer.close();

			return true; // Modification successful
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false; // Modification failed
	}

	public static class MovieData {
		public int id, price, basePrice, totalPrice = 0;
		public String name, timing, booked, selected;
		public String[] bookedSeats = {}, selectedSeats = {};

		// Constructor
		public MovieData(int id, String name, String timing, String[] booked, int basePrice) {
			this.id = id;
			this.name = name;
			this.timing = timing;
			this.bookedSeats = booked;
			this.basePrice = basePrice;
		}
	}

	// Create and store new movie data into JSON
	public boolean createMovieJson(int id, String name, String timing, String booked, int basePrice) {
		try {
			FileWriter writer = new FileWriter(path_moviedata);
			Gson gson = new Gson();
			MovieData movieData = new MovieData(id, name, timing, booked.split(","), basePrice);
			gson.toJson(movieData, writer);
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println("Error storing movie data: " + e.getMessage());
			return false;
		}
	}

	// Get movie data from JSON
	public MovieData getMovieJson() {
		try {
			FileReader reader = new FileReader(path_moviedata);
			// MovieData movieData = gson.fromJson(reader, MovieData.class);
			JsonElement jsonElement = JsonParser.parseReader(reader);
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			JsonArray arr = jsonObject.getAsJsonArray("bookedSeats");
			String[] booked = new String[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				booked[i] = arr.get(i).getAsString();
			}
			// Lấy selectedSeats
			JsonArray selectedArray = jsonObject.getAsJsonArray("selectedSeats");
			String[] selectedSeats = new String[selectedArray != null ? selectedArray.size() : 0];
			if (selectedArray != null) {
				for (int i = 0; i < selectedArray.size(); i++) {
					selectedSeats[i] = selectedArray.get(i).getAsString();
				}
			}

			// Lấy các trường khác với giá trị mặc định nếu null
			int id = jsonObject.has("id") && !jsonObject.get("id").isJsonNull() ? jsonObject.get("id").getAsInt() : 0;
			String name = jsonObject.has("name") && !jsonObject.get("name").isJsonNull() ? jsonObject.get("name").getAsString() : "";
			String timing = jsonObject.has("timing") && !jsonObject.get("timing").isJsonNull() ? jsonObject.get("timing").getAsString() : "";
			int basePrice = jsonObject.has("basePrice") && !jsonObject.get("basePrice").isJsonNull() ? jsonObject.get("basePrice").getAsInt() : 0;
			int totalPrice = jsonObject.has("totalPrice") && !jsonObject.get("totalPrice").isJsonNull() ? jsonObject.get("totalPrice").getAsInt() : 0;
			String selected = jsonObject.has("selected") && !jsonObject.get("selected").isJsonNull() ? jsonObject.get("selected").getAsString() : "";

			// Tạo đối tượng MovieData
			MovieData movieData = new MovieData(id, name, timing, booked, basePrice);
			movieData.totalPrice = totalPrice;
			movieData.selectedSeats = selectedSeats;
			movieData.selected = selected;

			return movieData;
		} catch (IOException e) {
			System.out.println("Error getting movie data: " + e.getMessage());
			return null;
		}
	}

	// Update movie data in JSON
	public void updateMovieJson(String[] seats, int totalPrice, int basePrice, String name) {
		try {
			FileReader reader = new FileReader(path_moviedata);
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // For pretty printing JSON
			MovieData movieData = gson.fromJson(reader, MovieData.class);
			reader.close();

			// Update fields
			movieData.selected = String.join(", ", seats);
			movieData.selectedSeats = seats;
			movieData.totalPrice = totalPrice;
			movieData.basePrice = basePrice;
			movieData.name = name;

			FileWriter writer = new FileWriter(path_moviedata);
			gson.toJson(movieData, writer);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error updating movie data: " + e.getMessage());
		}
	}

	public static User getUserData(){
		try {
			FileReader reader = new FileReader(path_userdata);
			JsonElement jsonElement = JsonParser.parseReader(reader);
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			reader.close();
            return new User(jsonObject.get("userId").getAsInt(), jsonObject.get("firstName").getAsString(), jsonObject.get("lastName").getAsString(), jsonObject.get("email").getAsString(), jsonObject.get("phoneNumber").getAsString(), jsonObject.get("cityName").getAsString());
		} catch (IOException e) {
			System.out.println("Error getting user data: " + e.getMessage());
			return null;
		}
	}
}
