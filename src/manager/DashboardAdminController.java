package manager;

import manager.model.BookedTicket;
import manager.model.Movie;
import manager.model.User;
import manager.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable {

    @FXML private Button dashboardButton;
    @FXML private Button movieManagementButton;
    @FXML private Button customerManagementButton;
    @FXML private Button ticketManagementButton;
    @FXML private Button logoutButton;

    @FXML private AnchorPane dashboardView;
    @FXML private AnchorPane movieManagementView;
    @FXML private AnchorPane customerManagementView;
    @FXML private AnchorPane ticketManagementView;

    @FXML private Label revenueLabel;
    @FXML private Label totalMoviesLabel;
    @FXML private Label totalTicketsLabel;
    @FXML private Label totalUsersLabel;

    // Dashboard Carousel Elements
    @FXML private ImageView dashboardImageView;
    @FXML private Button prevImageButton;
    @FXML private Button nextImageButton;

    @FXML private ImageView moviePosterImageView;
    @FXML private Button selectMovieImageButton;
    @FXML private TextField movieNameField;
    @FXML private TextField movieGenreField;
    @FXML private TextField movieDescriptionField;
    @FXML private TextField movieTotalSeatsField;
    @FXML private DatePicker movieReleaseDatePicker;
    @FXML private TextField movieShowTimeField;
    @FXML private TextField movieTagsField;
    @FXML private ComboBox<String> movieStatusComboBox;
    @FXML private TextField movieActorsListField;
    @FXML private TextField moviePerPriceField; // Sẽ hiển thị String
    @FXML private TextField movieRatingsField;
    @FXML private TextField movieShowDatesAndTimingsField;
    @FXML private TextField movieBookedSeatsCountField;
    @FXML private Button addMovieButton;
    @FXML private Button editMovieButton;
    @FXML private Button deleteMovieButton;
    @FXML private TableView<Movie> movieTableView;
    @FXML private TableColumn<Movie, Integer> colMovieId;
    @FXML private TableColumn<Movie, String> colMovieName;
    @FXML private TableColumn<Movie, String> colMovieGenre;
    @FXML private TableColumn<Movie, String> colMovieDescription;
    @FXML private TableColumn<Movie, String> colMovieStatus;
    @FXML private TableColumn<Movie, Integer> colMovieTotalSeats;
    @FXML private TableColumn<Movie, String> colMovieShowTime;
    @FXML private TableColumn<Movie, String> colMoviePerPrice; // Đã đổi sang String
    @FXML private TableColumn<Movie, Double> colMovieRatings;
    @FXML private TableColumn<Movie, LocalDateTime> colMovieReleaseDate;
    @FXML private TableColumn<Movie, Integer> colMovieBookedSeats;

    @FXML private TextField userFirstNameField;
    @FXML private TextField userLastNameField;
    @FXML private TextField userEmailField;
    @FXML private TextField userPhoneNumberField;
    @FXML private TextField userCityNameField;
    @FXML private TextField userRoleField;
    @FXML private Button updateUserButton;
    @FXML private Button deleteUserButton;
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserFirstName;
    @FXML private TableColumn<User, String> colUserLastName;
    @FXML private TableColumn<User, String> colUserEmail;
    @FXML private TableColumn<User, String> colUserPhoneNumber;
    @FXML private TableColumn<User, String> colUserCityName;
    @FXML private TableColumn<User, String> colUserRole;

    @FXML private TextField ticketSearchField;
    @FXML private RadioButton searchByMovieIdRadioButton;
    @FXML private RadioButton searchByUserIdRadioButton;
    @FXML private ToggleGroup searchGroup;
    @FXML private Button ticketSearchButton;
    @FXML private TableView<BookedTicket> bookedTicketTableView;
    @FXML private TableColumn<BookedTicket, Integer> colBookedTicketId;
    @FXML private TableColumn<BookedTicket, String> colBookedTicketNo;
    @FXML private TableColumn<BookedTicket, Integer> colBookedTicketUserId;
    @FXML private TableColumn<BookedTicket, Integer> colBookedTicketMovieId;
    @FXML private TableColumn<BookedTicket, String> colBookedTicketSeatNumbers;
    @FXML private TableColumn<BookedTicket, Integer> colBookedTicketSeatClass;
    @FXML private TableColumn<BookedTicket, String> colBookedTicketShowTime;
    @FXML private TableColumn<BookedTicket, Double> colBookedTicketPerPrice;
    @FXML private TableColumn<BookedTicket, Double> colBookedTicketTotalPrice;
    @FXML private TableColumn<BookedTicket, String> colBookedTicketCurrentStatus;
    @FXML private TableColumn<BookedTicket, LocalDate> colBookedTicketBookingDate;
    @FXML private TableColumn<BookedTicket, String> colBookedTicketBookingTime;
    @FXML private Button refreshTicketsButton;
    @FXML private Button deleteBookedTicketButton;

    private ObservableList<Movie> movieList;
    private ObservableList<User> userList;
    private ObservableList<BookedTicket> bookedTicketList;

    // For Dashboard Image Carousel
    private List<String> dashboardImages;
    private int currentImageIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDashboardCarousel(); // Initialize dashboardImages BEFORE showView() is called

        showView(dashboardView);

        dashboardButton.setOnAction(event -> showView(dashboardView));
        movieManagementButton.setOnAction(event -> showView(movieManagementView));
        customerManagementButton.setOnAction(event -> showView(customerManagementView));
        ticketManagementButton.setOnAction(event -> showView(ticketManagementView));
        logoutButton.setOnAction(event -> handleLogout());

        loadDashboardData();
        prevImageButton.setOnAction(event -> showPreviousImage());
        nextImageButton.setOnAction(event -> showNextImage());

        initializeMovieTable();
        loadMovies();
        movieStatusComboBox.setItems(FXCollections.observableArrayList("Available", "Upcoming", "Released", "Cancelled"));
        addMovieButton.setOnAction(event -> handleAddMovie());
        editMovieButton.setOnAction(event -> handleEditMovie());
        deleteMovieButton.setOnAction(event -> handleDeleteMovie());
        selectMovieImageButton.setOnAction(event -> handleSelectMovieImage());
        movieTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMovieDetails(newValue));

        initializeUserTable();
        loadUsers();
        updateUserButton.setOnAction(event -> handleUpdateUser());
        deleteUserButton.setOnAction(event -> handleDeleteUser());
        userTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showUserDetails(newValue));

        initializeBookedTicketTable();
        loadBookedTickets();
        ticketSearchButton.setOnAction(event -> handleTicketSearch());
        refreshTicketsButton.setOnAction(event -> loadBookedTickets());
        deleteBookedTicketButton.setOnAction(event -> handleDeleteBookedTicket());
    }

    private void showView(AnchorPane viewToShow) {
        dashboardView.setVisible(false);
        movieManagementView.setVisible(false);
        customerManagementView.setVisible(false);
        ticketManagementView.setVisible(false);

        viewToShow.setVisible(true);
        resetButtonStyles();
        if (viewToShow == dashboardView) {
            dashboardButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold;");
            loadDashboardData();
            updateDashboardImage();
        } else if (viewToShow == movieManagementView) {
            movieManagementButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold;");
            loadMovies();
            clearMovieForm();
        } else if (viewToShow == customerManagementView) {
            customerManagementButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-border-color: #34495e; -fx-border-radius: 8;");
            loadUsers();
            clearUserForm();
        } else if (viewToShow == ticketManagementView) {
            ticketManagementButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-border-color: #34495e; -fx-border-radius: 8;");
            loadBookedTickets();
        }
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-border-color: #34495e; -fx-border-radius: 8;";
        dashboardButton.setStyle(defaultStyle);
        movieManagementButton.setStyle(defaultStyle);
        customerManagementButton.setStyle(defaultStyle);
        ticketManagementButton.setStyle(defaultStyle);
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn đăng xuất?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.out.println("Đăng xuất thành công!");
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void loadDashboardData() {
        try {
            double revenue = DBUtil.getTotalRevenue();
            int totalMovies = DBUtil.getCount("movies");
            int totalUsers = DBUtil.getCount("users");
            int totalTickets = DBUtil.getCount("booked_ticket");

            revenueLabel.setText(String.format("%,.0f VNĐ", revenue));
            totalMoviesLabel.setText(String.valueOf(totalMovies));
            totalUsersLabel.setText(String.valueOf(totalUsers));
            totalTicketsLabel.setText(String.valueOf(totalTickets));

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể tải dữ liệu Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Dashboard Carousel Logic ---
    private void setupDashboardCarousel() {
        dashboardImages = new ArrayList<>();
        // Make sure these paths are correct and the images exist in your resources folder.
        // E.g., src/main/resources/manager/images/movie_banner1.jpg
        dashboardImages.add("/manager/images/phim-dien-anh-he-2019-elle-man-feature-11 (1).jpg");
        dashboardImages.add("/manager/images/rap-chieu-phim-sai-gon-3.jpg");
        dashboardImages.add("/manager/images/131042796_754748555134099_4053885803321312559_n.jpg");
        // Add more images as needed.
    }

    private void updateDashboardImage() {
        if (dashboardImages != null && !dashboardImages.isEmpty()) {
            String imagePath = dashboardImages.get(currentImageIndex);
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                dashboardImageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading dashboard image: " + imagePath + " - " + e.getMessage());
                dashboardImageView.setImage(null);
            }
        } else {
            dashboardImageView.setImage(null);
        }
    }

    private void showNextImage() {
        if (dashboardImages != null && !dashboardImages.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % dashboardImages.size();
            updateDashboardImage();
        }
    }

    private void showPreviousImage() {
        if (dashboardImages != null && !dashboardImages.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + dashboardImages.size()) % dashboardImages.size();
            updateDashboardImage();
        }
    }


    // --- Movie Management Logic ---
    private void initializeMovieTable() {
        colMovieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMovieName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMovieGenre.setCellValueFactory(new PropertyValueFactory<>("gener"));
        colMovieDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMovieStatus.setCellValueFactory(new PropertyValueFactory<>("movieStatus"));
        colMovieTotalSeats.setCellValueFactory(new PropertyValueFactory<>("totalNumberOfSeats"));
        colMovieShowTime.setCellValueFactory(new PropertyValueFactory<>("showDatesAndTimings"));
        colMoviePerPrice.setCellValueFactory(new PropertyValueFactory<>("perPrices")); // Đã đổi sang String
        colMovieRatings.setCellValueFactory(new PropertyValueFactory<>("ratings"));
        colMovieBookedSeats.setCellValueFactory(new PropertyValueFactory<>("bookedSeatsCount"));

        colMovieReleaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        colMovieReleaseDate.setCellFactory(column -> new TableCell<Movie, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
    }

    private void loadMovies() {
        try {
            movieList = FXCollections.observableArrayList(DBUtil.getAllMovies());
            movieTableView.setItems(movieList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể tải danh sách phim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            movieNameField.setText(movie.getName());
            movieGenreField.setText(movie.getGener());
            movieDescriptionField.setText(movie.getDescription());
            movieTotalSeatsField.setText(String.valueOf(movie.getTotalNumberOfSeats()));
            
            if (movie.getReleaseDate() != null) {
                movieReleaseDatePicker.setValue(movie.getReleaseDate().toLocalDate());
                movieShowTimeField.setText(movie.getReleaseDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                movieReleaseDatePicker.setValue(null);
                movieShowTimeField.clear();
            }

            movieTagsField.setText(movie.getTags());
            movieStatusComboBox.setValue(movie.getMovieStatus());
            movieActorsListField.setText(movie.getActorsList());
            moviePerPriceField.setText(movie.getPerPrices()); // Hiển thị giá là String
            movieRatingsField.setText(String.valueOf(movie.getRatings()));
            movieBookedSeatsCountField.setText(String.valueOf(movie.getBookedSeatsCount()));

            // HIỂN THỊ ẢNH POSTER KHI CHỌN PHIM
            if (movie.getPosterImage() != null && !movie.getPosterImage().isEmpty()) {
                try {
                    Image image;
                    // Check if it's an absolute file URI (from FileChooser) or a classpath resource
                    if (movie.getPosterImage().startsWith("file:///")) {
                         image = new Image(movie.getPosterImage());
                    } else if (movie.getPosterImage().startsWith("http://") || movie.getPosterImage().startsWith("https://")) {
                        // Tải ảnh từ URL bên ngoài
                        image = new Image(movie.getPosterImage(), true); // true for background loading
                    }
                    else { // Assume it's a classpath resource relative to manager package
                         image = new Image(getClass().getResourceAsStream(movie.getPosterImage()));
                    }
                    moviePosterImageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading movie poster image: " + movie.getPosterImage() + " - " + e.getMessage());
                    moviePosterImageView.setImage(null);
                }
            } else {
                moviePosterImageView.setImage(null);
            }

        } else {
            clearMovieForm();
        }
    }

    private void clearMovieForm() {
        movieNameField.clear();
        movieGenreField.clear();
        movieDescriptionField.clear();
        movieTotalSeatsField.clear();
        movieReleaseDatePicker.setValue(null);
        movieShowTimeField.clear();
        movieTagsField.clear();
        movieStatusComboBox.getSelectionModel().clearSelection();
        movieActorsListField.clear();
        moviePerPriceField.clear();
        movieRatingsField.clear();
        movieBookedSeatsCountField.clear();
        moviePosterImageView.setImage(null);
    }

    private void handleSelectMovieImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh Poster Phim");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                String fileUri = selectedFile.toURI().toString();
                Image image = new Image(fileUri);
                moviePosterImageView.setImage(image);
                
                // Cập nhật trường posterImage của Movie object hiện tại
                // LƯU Ý QUAN TRỌNG: Đây chỉ là cách tạm thời để hiển thị ảnh.
                // Trong ứng dụng thực tế, bạn cần sao chép file ảnh này vào thư mục tài nguyên của dự án
                // và lưu đường dẫn TƯƠNG ĐỐI vào DB (ví dụ: "/manager/images/movie_posters/filename.jpg").
                // Nếu không, ảnh sẽ không hiển thị khi ứng dụng được di chuyển hoặc chạy trên máy khác.
                Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
                if (selectedMovie != null) {
                    selectedMovie.setPosterImage(fileUri); // Tạm thời lưu URI tuyệt đối
                } else {
                    showAlert(Alert.AlertType.WARNING, "Lưu ý", "Ảnh đã chọn sẽ được hiển thị nhưng chưa được lưu vào phim nào. Vui lòng chọn một phim hoặc thêm phim mới để lưu.");
                }

                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Ảnh đã chọn: " + selectedFile.getName() + "\nĐường dẫn tạm thời (sẽ lưu): " + fileUri + "\n(Cần xử lý sao chép file và lưu đường dẫn tương đối để ứng dụng hoạt động ổn định)");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải ảnh: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleAddMovie() {
        try {
            String name = movieNameField.getText();
            String description = movieDescriptionField.getText();
            String genre = movieGenreField.getText();
            // Lấy đường dẫn ảnh từ ImageView. Sẽ là URI tuyệt đối nếu chọn từ FileChooser.
            String posterImage = (moviePosterImageView.getImage() != null) ? moviePosterImageView.getImage().getUrl() : "";


            String tags = movieTagsField.getText();
            String status = movieStatusComboBox.getValue();
            String actors = movieActorsListField.getText();
            String perPrices = moviePerPriceField.getText(); // Lấy giá là String
            int bookedSeats = Integer.parseInt(movieBookedSeatsCountField.getText());
            double ratings = Double.parseDouble(movieRatingsField.getText());

            LocalDate selectedDate = movieReleaseDatePicker.getValue();
            String timeText = movieShowTimeField.getText();
            LocalDateTime releaseDateTime = null;

            if (selectedDate != null) {
                if (!timeText.isEmpty()) {
                    try {
                        LocalTime selectedTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
                        releaseDateTime = LocalDateTime.of(selectedDate, selectedTime);
                    } catch (DateTimeParseException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi định dạng giờ", "Giờ chiếu không hợp lệ. Vui lòng nhập theo định dạng HH:mm (ví dụ: 14:30).");
                        return;
                    }
                } else {
                    releaseDateTime = selectedDate.atStartOfDay();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng chọn ngày chiếu.");
                return;
            }

            int totalSeats = Integer.parseInt(movieTotalSeatsField.getText());
            String showTimes = movieShowDatesAndTimingsField.getText();

            Movie newMovie = new Movie(name, description, genre, posterImage, tags, status, actors, perPrices, bookedSeats, ratings, releaseDateTime, totalSeats, showTimes);
            DBUtil.addMovie(newMovie);
            loadMovies();
            clearMovieForm();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phim đã được thêm mới.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập số hợp lệ cho số ghế và rating. Giá vé phải là chuỗi (ví dụ: '100000;120000;150000').");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể thêm phim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEditMovie() {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            try {
                selectedMovie.setName(movieNameField.getText());
                selectedMovie.setDescription(movieDescriptionField.getText());
                selectedMovie.setGener(movieGenreField.getText());
                selectedMovie.setPosterImage((moviePosterImageView.getImage() != null) ? moviePosterImageView.getImage().getUrl() : "");


                selectedMovie.setTags(movieTagsField.getText());
                selectedMovie.setMovieStatus(movieStatusComboBox.getValue());
                selectedMovie.setActorsList(movieActorsListField.getText());
                selectedMovie.setPerPrices(moviePerPriceField.getText()); // Cập nhật giá là String
                selectedMovie.setBookedSeatsCount(Integer.parseInt(movieBookedSeatsCountField.getText()));
                selectedMovie.setRatings(Double.parseDouble(movieRatingsField.getText()));
                
                LocalDate selectedDate = movieReleaseDatePicker.getValue();
                String timeText = movieShowTimeField.getText();
                LocalDateTime releaseDateTime = null;

                if (selectedDate != null) {
                    if (!timeText.isEmpty()) {
                        try {
                            LocalTime selectedTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
                            releaseDateTime = LocalDateTime.of(selectedDate, selectedTime);
                        } catch (DateTimeParseException e) {
                            showAlert(Alert.AlertType.ERROR, "Lỗi định dạng giờ", "Giờ chiếu không hợp lệ. Vui lòng nhập theo định dạng HH:mm (ví dụ: 14:30).");
                            return;
                        }
                    } else {
                        releaseDateTime = selectedDate.atStartOfDay();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng chọn ngày chiếu.");
                    return;
                }
                selectedMovie.setReleaseDate(releaseDateTime);

                selectedMovie.setTotalNumberOfSeats(Integer.parseInt(movieTotalSeatsField.getText()));
                selectedMovie.setShowDatesAndTimings(movieShowDatesAndTimingsField.getText());

                DBUtil.updateMovie(selectedMovie);
                loadMovies();
                clearMovieForm();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin phim đã được cập nhật.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập số hợp lệ cho số ghế và rating. Giá vé phải là chuỗi (ví dụ: '100000;120000;150000').");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể cập nhật phim: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Không có lựa chọn", "Vui lòng chọn một phim để sửa.");
        }
    }

    private void handleDeleteMovie() {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa phim này?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DBUtil.deleteMovie(selectedMovie.getId());
                        loadMovies();
                        clearMovieForm();
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phim đã được xóa.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể xóa phim: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Không có lựa chọn", "Vui lòng chọn một phim để xóa.");
        }
    }

    private void initializeUserTable() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colUserLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        colUserPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colUserCityName.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadUsers() {
        try {
            userList = FXCollections.observableArrayList(DBUtil.getAllUsers());
            userTableView.setItems(userList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể tải danh sách người dùng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showUserDetails(User user) {
        if (user != null) {
            userFirstNameField.setText(user.getFirstName());
            userLastNameField.setText(user.getLastName());
            userEmailField.setText(user.getEmailAddress());
            userPhoneNumberField.setText(user.getPhoneNumber());
            userCityNameField.setText(user.getCityName());
            userRoleField.setText(user.getIsSuperUser() ? "Admin" : "User");
        } else {
            clearUserForm();
        }
    }

    private void clearUserForm() {
        userFirstNameField.clear();
        userLastNameField.clear();
        userEmailField.clear();
        userPhoneNumberField.clear();
        userCityNameField.clear();
        userRoleField.clear();
    }

    private void handleUpdateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                selectedUser.setFirstName(userFirstNameField.getText());
                selectedUser.setLastName(userLastNameField.getText());
                selectedUser.setEmailAddress(userEmailField.getText());
                selectedUser.setPhoneNumber(userPhoneNumberField.getText());
                selectedUser.setCityName(userCityNameField.getText());
                selectedUser.setIsSuperUser(userRoleField.getText().equalsIgnoreCase("Admin"));

                DBUtil.updateUser(selectedUser);
                loadUsers();
                clearUserForm();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin người dùng đã được cập nhật.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể cập nhật người dùng: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Không có lựa chọn", "Vui lòng chọn một người dùng để cập nhật.");
        }
    }

    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa người dùng này?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DBUtil.deleteUser(selectedUser.getId());
                        loadUsers();
                        clearUserForm();
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Người dùng đã được xóa.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể xóa người dùng: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Không có lựa chọn", "Vui lòng chọn một người dùng để xóa.");
        }
    }

    private void initializeBookedTicketTable() {
        colBookedTicketId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookedTicketNo.setCellValueFactory(new PropertyValueFactory<>("ticketNo"));
        colBookedTicketUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colBookedTicketMovieId.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        colBookedTicketSeatNumbers.setCellValueFactory(new PropertyValueFactory<>("seatNumbers"));
        colBookedTicketSeatClass.setCellValueFactory(new PropertyValueFactory<>("seatClass"));
        colBookedTicketShowTime.setCellValueFactory(new PropertyValueFactory<>("showTime"));
        colBookedTicketPerPrice.setCellValueFactory(new PropertyValueFactory<>("perPrice"));
        colBookedTicketTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colBookedTicketCurrentStatus.setCellValueFactory(new PropertyValueFactory<>("currentStatus"));
        colBookedTicketBookingDate.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        colBookedTicketBookingTime.setCellValueFactory(new PropertyValueFactory<>("bookingTime"));
    }

    private void loadBookedTickets() {
        try {
            bookedTicketList = FXCollections.observableArrayList(DBUtil.getAllBookedTickets());
            bookedTicketTableView.setItems(bookedTicketList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể tải danh sách vé đã đặt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleTicketSearch() {
        String query = ticketSearchField.getText();
        if (query.isEmpty()) {
            loadBookedTickets();
            return;
        }

        boolean searchByMovieId = searchByMovieIdRadioButton.isSelected();
        try {
            bookedTicketList = FXCollections.observableArrayList(DBUtil.searchBookedTickets(query, searchByMovieId));
            bookedTicketTableView.setItems(bookedTicketList);
            if (bookedTicketList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Tìm kiếm", "Không tìm thấy vé nào với tiêu chí tìm kiếm này.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng nhập ID hợp lệ (chỉ số).");
        }
        catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể tìm kiếm vé: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteBookedTicket() {
        BookedTicket selectedTicket = bookedTicketTableView.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa vé này?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DBUtil.deleteBookedTicket(selectedTicket.getId());
                        loadBookedTickets();
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Vé đã được xóa.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi cơ sở dữ liệu", "Không thể xóa vé: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Không có lựa chọn", "Vui lòng chọn một vé để xóa.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}