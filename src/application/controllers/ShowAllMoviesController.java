package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.sqlite.SQLiteDataSource;

import application.utils.DBUtility;
import application.utils.Movie;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ShowAllMoviesController implements Initializable {

	@FXML private HBox HBoxpane;
	@FXML private TextField getMovieSearchInput;
	@FXML private GridPane grid;
	@FXML private ImageView movieSearchBtn;
	@FXML private Button newReleaseBtn, searchBtn, trendingMoviesBtn, upcomingsMoviesBtn;
	@FXML private ScrollPane scrollBar;

	private static final String DB_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";
	private static List<Movie> movieCache = null;
	private List<Movie> movieList = new ArrayList<>();
	private PauseTransition pause = new PauseTransition(Duration.millis(500));
	private int currentOffset = 0;
	private final int PAGE_SIZE = 20;
	private boolean hasMoreMovies = true;
	private int totalMovies = -1; // Sẽ được cập nhật khi load lần đầu

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Task<List<Movie>> loadMoviesTask = getListTask();
		new Thread(loadMoviesTask).start();

		getMovieSearchInput.textProperty().addListener((obs, oldVal, newVal) -> {
			pause.setOnFinished(event -> searchMovies(newVal.trim()));
			pause.playFromStart();
		});

		scrollBar.vvalueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.doubleValue() >= 0.8 && hasMoreMovies && movieList.size() < totalMovies) {
				loadMoreMovies();
			}
		});
	}

	private Task<List<Movie>> getListTask() {
		Task<List<Movie>> loadMoviesTask = new Task<>() {
			@Override
			protected List<Movie> call() {
				totalMovies = getTotalMovies(); // Lấy tổng số phim
				return readMoviesDate(currentOffset, PAGE_SIZE);
			}
		};
		loadMoviesTask.setOnSucceeded(event -> {
			movieList.addAll(loadMoviesTask.getValue());
			populateGrid(movieList);
		});
		loadMoviesTask.setOnFailed(event -> {
			event.getSource().getException().printStackTrace();
		});
		return loadMoviesTask;
	}

	private int getTotalMovies() {
		int count = 0;
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(DB_URL);
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM movies");
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	private void populateGrid(List<Movie> movies) {
		int col = 0, row = grid.getRowCount() + 1;
		try {
			for (Movie movie : movies) {
				FXMLLoader fxmlloder = new FXMLLoader(getClass().getResource("/application/fxml/MovieCard.fxml"));
				AnchorPane anchorPane = fxmlloder.load();
				MovieCardController cardController = fxmlloder.getController();
				cardController.setData(movie);
				if (col == 4) {
					col = 0;
					row++;
				}
				grid.add(anchorPane, col++, row);
				GridPane.setMargin(anchorPane, new Insets(20));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	List<Movie> readMoviesDate(int offset, int limit) {
		List<Movie> movieNames = new ArrayList<>();
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(DB_URL);
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("SELECT * FROM movies LIMIT ? OFFSET ?")) {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			try (ResultSet rs = ps.executeQuery()) {
				DBUtility.getMoviesData(rs, movieNames);
				if (offset == 0) {
					movieCache = new ArrayList<>(movieNames);
				} else if (!movieNames.isEmpty()) {
					movieCache.addAll(movieNames);
				}
				if (movieNames.size() < limit) {
					hasMoreMovies = false; // Không còn phim để tải
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movieNames;
	}

	private void loadMoreMovies() {
		if (!hasMoreMovies) {
			return;
		}
		currentOffset += PAGE_SIZE;
		Task<List<Movie>> loadMoreTask = new Task<>() {
			@Override
			protected List<Movie> call() {
				return readMoviesDate(currentOffset, PAGE_SIZE);
			}
		};
		loadMoreTask.setOnSucceeded(event -> {
			List<Movie> newMovies = loadMoreTask.getValue();
			if (!newMovies.isEmpty()) {
				movieList.addAll(newMovies);
				populateGrid(newMovies); // Chỉ thêm các phim mới
			}
			if (newMovies.size() < PAGE_SIZE) {
				hasMoreMovies = false;
			}
		});
		loadMoreTask.setOnFailed(event -> {
			event.getSource().getException().printStackTrace();
		});
		new Thread(loadMoreTask).start();
	}

	private void searchMovies(String searchQuery) {
		List<Movie> searchResults = searchMoviesInDatabase(searchQuery);
		refreshGrid(searchResults);
	}

	List<Movie> searchMoviesInDatabase(String searchQuery) {
		List<Movie> movieNames = new ArrayList<>();
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(DB_URL);
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("SELECT * FROM movies WHERE name LIKE ? OR gener LIKE ?")) {
			ps.setString(1, "%" + searchQuery + "%");
			ps.setString(2, "%" + searchQuery + "%");
			try (ResultSet rs = ps.executeQuery()) {
				DBUtility.getMoviesData(rs, movieNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movieNames;
	}

	void refreshGrid(List<Movie> searchResults) {
		grid.getChildren().clear();
		populateGrid(searchResults);
		currentOffset = 0; // Reset offset khi tìm kiếm
		hasMoreMovies = true; // Reset trạng thái
		totalMovies = getTotalMovies(); // Cập nhật lại tổng số phim
	}

	@FXML
	void showReleaseMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("New Release");
		refreshGrid(releaseMovies);
	}

	@FXML
	void showTrendingMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("Trending");
		refreshGrid(releaseMovies);
	}

	@FXML
	void showUpcomingMovies(ActionEvent event) {
		List<Movie> releaseMovies = getMoviesByStatus("Upcoming");
		refreshGrid(releaseMovies);
	}

	private List<Movie> getMoviesByStatus(String status) {
		List<Movie> movieNames = new ArrayList<>();
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(DB_URL);
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("SELECT * FROM movies WHERE movieStatus = ?")) {
			ps.setString(1, status);
			try (ResultSet rs = ps.executeQuery()) {
				DBUtility.getMoviesData(rs, movieNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movieNames;
	}

	@FXML
	void refreshContent(MouseEvent event) {
		movieCache = null;
		grid.getChildren().clear();
		currentOffset = 0;
		hasMoreMovies = true;
		totalMovies = -1;
		initialize(null, null);
	}
}