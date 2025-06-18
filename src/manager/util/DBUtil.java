package manager.util;

import manager.model.BookedTicket;
import manager.model.Movie;
import manager.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String JDBC_URL = "jdbc:sqlite:src/application/database/movie_ticket_booking.db";

    private static final DateTimeFormatter[] DATE_TIME_READ_FORMATTERS = {
        DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("M/d/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
    };

    private static final DateTimeFormatter[] DATE_ONLY_READ_FORMATTERS = {
        DateTimeFormatter.ofPattern("M/d/yyyy"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    private static final DateTimeFormatter DB_WRITE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DB_WRITE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static int getCount(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(totalPrice) FROM booked_ticket WHERE currentStatus = 'Completed'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public static List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String releaseDateStr = rs.getString("releaseDate");
                LocalDateTime parsedReleaseDate = null;

                if (releaseDateStr != null && !releaseDateStr.trim().isEmpty()) {
                    releaseDateStr = releaseDateStr.trim();

                    boolean parsedSuccessfully = false;

                    for (DateTimeFormatter formatter : DATE_TIME_READ_FORMATTERS) {
                        try {
                            parsedReleaseDate = LocalDateTime.parse(releaseDateStr, formatter);
                            parsedSuccessfully = true;
                            break;
                        } catch (DateTimeParseException e) {
                            // Continue
                        }
                    }

                    if (!parsedSuccessfully) {
                        for (DateTimeFormatter formatter : DATE_ONLY_READ_FORMATTERS) {
                            try {
                                parsedReleaseDate = LocalDate.parse(releaseDateStr, formatter).atStartOfDay();
                                parsedSuccessfully = true;
                                break;
                            } catch (DateTimeParseException e) {
                                // Continue
                            }
                        }
                    }

                    if (!parsedSuccessfully) {
                        System.err.println("Warning: Could not parse releaseDate '" + releaseDateStr + "' for movie ID " + rs.getInt("id") + ". Setting date to null.");
                        parsedReleaseDate = null;
                    }
                }

                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("gener"),
                        rs.getString("posterImage"),
                        rs.getString("tags"),
                        rs.getString("movieStatus"),
                        rs.getString("actorsList"),
                        rs.getString("perPrices"), // Đã đọc là String
                        rs.getInt("bookedSeatsCount"),
                        rs.getDouble("ratings"),
                        parsedReleaseDate,
                        rs.getInt("totalNumberOfSeats"),
                        rs.getString("showDatesAndTimings"),
                        rs.getString("createdAt") != null ? LocalDateTime.parse(rs.getString("createdAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                        rs.getString("updatedAt") != null ? LocalDateTime.parse(rs.getString("updatedAt"), DB_WRITE_DATE_TIME_FORMATTER) : null
                ));
            }
        }
        return movies;
    }

    public static void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (name, description, gener, posterImage, tags, movieStatus, actorsList, perPrices, bookedSeatsCount, ratings, releaseDate, totalNumberOfSeats, showDatesAndTimings, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movie.getName());
            pstmt.setString(2, movie.getDescription());
            pstmt.setString(3, movie.getGener());
            pstmt.setString(4, movie.getPosterImage());
            pstmt.setString(5, movie.getTags());
            pstmt.setString(6, movie.getMovieStatus());
            pstmt.setString(7, movie.getActorsList());
            pstmt.setString(8, movie.getPerPrices()); // Đã ghi là String
            pstmt.setInt(9, movie.getBookedSeatsCount());
            pstmt.setDouble(10, movie.getRatings());
            pstmt.setString(11, movie.getReleaseDate() != null ? movie.getReleaseDate().format(DB_WRITE_DATE_TIME_FORMATTER) : null);
            pstmt.setInt(12, movie.getTotalNumberOfSeats());
            pstmt.setString(13, movie.getShowDatesAndTimings());
            pstmt.setString(14, LocalDateTime.now().format(DB_WRITE_DATE_TIME_FORMATTER));
            pstmt.setString(15, LocalDateTime.now().format(DB_WRITE_DATE_TIME_FORMATTER));
            pstmt.executeUpdate();
        }
    }

    public static void updateMovie(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET name = ?, description = ?, gener = ?, posterImage = ?, tags = ?, movieStatus = ?, actorsList = ?, perPrices = ?, bookedSeatsCount = ?, ratings = ?, releaseDate = ?, totalNumberOfSeats = ?, showDatesAndTimings = ?, updatedAt = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movie.getName());
            pstmt.setString(2, movie.getDescription());
            pstmt.setString(3, movie.getGener());
            pstmt.setString(4, movie.getPosterImage());
            pstmt.setString(5, movie.getTags());
            pstmt.setString(6, movie.getMovieStatus());
            pstmt.setString(7, movie.getActorsList());
            pstmt.setString(8, movie.getPerPrices()); // Đã ghi là String
            pstmt.setInt(9, movie.getBookedSeatsCount());
            pstmt.setDouble(10, movie.getRatings());
            pstmt.setString(11, movie.getReleaseDate() != null ? movie.getReleaseDate().format(DB_WRITE_DATE_TIME_FORMATTER) : null);
            pstmt.setInt(12, movie.getTotalNumberOfSeats());
            pstmt.setString(13, movie.getShowDatesAndTimings());
            pstmt.setString(14, LocalDateTime.now().format(DB_WRITE_DATE_TIME_FORMATTER));
            pstmt.setInt(15, movie.getId());
            pstmt.executeUpdate();
        }
    }

    public static void deleteMovie(int movieId) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.executeUpdate();
        }
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("emailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                        rs.getString("cityName"),
                        rs.getInt("isSuperUser") == 1,
                        rs.getString("createdAt") != null ? LocalDateTime.parse(rs.getString("createdAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                        rs.getString("updatedAt") != null ? LocalDateTime.parse(rs.getString("updatedAt"), DB_WRITE_DATE_TIME_FORMATTER) : null
                ));
            }
        }
        return users;
    }

    public static void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, emailAddress = ?, phoneNumber = ?, cityName = ?, isSuperUser = ?, updatedAt = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmailAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getCityName());
            pstmt.setInt(6, user.getIsSuperUser() ? 1 : 0);
            pstmt.setString(7, LocalDateTime.now().format(DB_WRITE_DATE_TIME_FORMATTER));
            pstmt.setInt(8, user.getId());
            pstmt.executeUpdate();
        }
    }

    public static void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    public static List<BookedTicket> getAllBookedTickets() throws SQLException {
        List<BookedTicket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM booked_ticket";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(new BookedTicket(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("movieId"),
                        rs.getString("seatNumbers"),
                        rs.getInt("seatClass"),
                        rs.getInt("numberOfSeats"),
                        rs.getString("showTime"),
                        rs.getDouble("perPrice"),
                        rs.getDouble("totalPrice"),
                        rs.getString("currentStatus"),
                        rs.getString("createdAt") != null ? LocalDateTime.parse(rs.getString("createdAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                        rs.getString("updatedAt") != null ? LocalDateTime.parse(rs.getString("updatedAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                        rs.getString("ticketNo"),
                        rs.getString("bookingDate") != null ? LocalDate.parse(rs.getString("bookingDate"), DB_WRITE_DATE_FORMATTER) : null,
                        rs.getString("bookingTime")
                ));
            }
        }
        return tickets;
    }

    public static List<BookedTicket> searchBookedTickets(String query, boolean searchByMovieId) throws SQLException {
        List<BookedTicket> tickets = new ArrayList<>();
        String sql;
        int id = -1;
        try {
            id = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            // Not a valid ID, if searching by ID, this will result in no matches
        }

        if (searchByMovieId) {
            sql = "SELECT * FROM booked_ticket WHERE movieId = ?";
        } else {
            sql = "SELECT * FROM booked_ticket WHERE userId = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id); // Use parsed ID for search
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new BookedTicket(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getInt("movieId"),
                            rs.getString("seatNumbers"),
                            rs.getInt("seatClass"),
                            rs.getInt("numberOfSeats"),
                            rs.getString("showTime"),
                            rs.getDouble("perPrice"),
                            rs.getDouble("totalPrice"),
                            rs.getString("currentStatus"),
                            rs.getString("createdAt") != null ? LocalDateTime.parse(rs.getString("createdAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                            rs.getString("updatedAt") != null ? LocalDateTime.parse(rs.getString("updatedAt"), DB_WRITE_DATE_TIME_FORMATTER) : null,
                            rs.getString("ticketNo"),
                            rs.getString("bookingDate") != null ? LocalDate.parse(rs.getString("bookingDate"), DB_WRITE_DATE_FORMATTER) : null,
                            rs.getString("bookingTime")
                        ));
                    }
                }
            }
            return tickets;
        }

    public static void deleteBookedTicket(int ticketId) throws SQLException {
        String sql = "DELETE FROM booked_ticket WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            pstmt.executeUpdate();
        }
    }
}