package manager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookedTicket {
    private int id;
    private int userId;
    private int movieId;
    private String seatNumbers;
    private int seatClass;
    private String showTime;
    private double perPrice;
    private double totalPrice;
    private String currentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ticketNo;
    private LocalDate bookingDate;
    private String bookingTime;
    private int numberOfSeats; // Added based on DB schema

    public BookedTicket(int id, int userId, int movieId, String seatNumbers, int seatClass, int numberOfSeats, String showTime, double perPrice, double totalPrice, String currentStatus, LocalDateTime createdAt, LocalDateTime updatedAt, String ticketNo, LocalDate bookingDate, String bookingTime) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.seatNumbers = seatNumbers;
        this.seatClass = seatClass;
        this.numberOfSeats = numberOfSeats;
        this.showTime = showTime;
        this.perPrice = perPrice;
        this.totalPrice = totalPrice;
        this.currentStatus = currentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ticketNo = ticketNo;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }
    public int getSeatClass() { return seatClass; }
    public void setSeatClass(int seatClass) { this.seatClass = seatClass; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public double getPerPrice() { return perPrice; }
    public void setPerPrice(double perPrice) { this.perPrice = perPrice; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getTicketNo() { return ticketNo; }
    public void setTicketNo(String ticketNo) { this.ticketNo = ticketNo; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
}