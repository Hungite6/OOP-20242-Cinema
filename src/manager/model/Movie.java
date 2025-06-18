package manager.model;

import java.time.LocalDateTime;

public class Movie {
    private int id;
    private String name;
    private String description;
    private String gener;
    private String posterImage;
    private String tags;
    private String movieStatus;
    private String actorsList;
    private String perPrices; // Đã thay đổi từ int sang String
    private int bookedSeatsCount;
    private double ratings;
    private LocalDateTime releaseDate;
    private int totalNumberOfSeats;
    private String showDatesAndTimings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Movie(int id, String name, String description, String gener, String posterImage, String tags, String movieStatus, String actorsList, String perPrices, int bookedSeatsCount, double ratings, LocalDateTime releaseDate, int totalNumberOfSeats, String showDatesAndTimings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gener = gener;
        this.posterImage = posterImage;
        this.tags = tags;
        this.movieStatus = movieStatus;
        this.actorsList = actorsList;
        this.perPrices = perPrices; // Cập nhật constructor
        this.bookedSeatsCount = bookedSeatsCount;
        this.ratings = ratings;
        this.releaseDate = releaseDate;
        this.totalNumberOfSeats = totalNumberOfSeats;
        this.showDatesAndTimings = showDatesAndTimings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Movie(String name, String description, String gener, String posterImage, String tags, String movieStatus, String actorsList, String perPrices, int bookedSeatsCount, double ratings, LocalDateTime releaseDate, int totalNumberOfSeats, String showDatesAndTimings) {
        this.name = name;
        this.description = description;
        this.gener = gener;
        this.posterImage = posterImage;
        this.tags = tags;
        this.movieStatus = movieStatus;
        this.actorsList = actorsList;
        this.perPrices = perPrices; // Cập nhật constructor
        this.bookedSeatsCount = bookedSeatsCount;
        this.ratings = ratings;
        this.releaseDate = releaseDate;
        this.totalNumberOfSeats = totalNumberOfSeats;
        this.showDatesAndTimings = showDatesAndTimings;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGener() { return gener; }
    public void setGener(String gener) { this.gener = gener; }
    public String getPosterImage() { return posterImage; }
    public void setPosterImage(String posterImage) { this.posterImage = posterImage; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getMovieStatus() { return movieStatus; }
    public void setMovieStatus(String movieStatus) { this.movieStatus = movieStatus; }
    public String getActorsList() { return actorsList; }
    public void setActorsList(String actorsList) { this.actorsList = actorsList; }
    public String getPerPrices() { return perPrices; } // Đã đổi sang String
    public void setPerPrices(String perPrices) { this.perPrices = perPrices; } // Đã đổi sang String
    public int getBookedSeatsCount() { return bookedSeatsCount; }
    public void setBookedSeatsCount(int bookedSeatsCount) { this.bookedSeatsCount = bookedSeatsCount; }
    public double getRatings() { return ratings; }
    public void setRatings(double ratings) { this.ratings = ratings; }
    public LocalDateTime getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }
    public int getTotalNumberOfSeats() { return totalNumberOfSeats; }
    public void setTotalNumberOfSeats(int totalNumberOfSeats) { this.totalNumberOfSeats = totalNumberOfSeats; }
    public String getShowDatesAndTimings() { return showDatesAndTimings; }
    public void setShowDatesAndTimings(String showDatesAndTimings) { this.showDatesAndTimings = showDatesAndTimings; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}