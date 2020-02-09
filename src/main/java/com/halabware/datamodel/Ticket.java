package com.halabware.datamodel;

public class Ticket {

    public String userName;
    public int reservationNumber;
    public int performanceId;

    public Ticket(String userName, int reservationNumber, int performanceId) {
        this.userName = userName;
        this.reservationNumber = reservationNumber;
        this.performanceId = performanceId;
    }

    @Override
    public String toString() {
        return "userName='" + userName + '\'' +
                        ", reservationNumber=" + reservationNumber +
                        ", performanceId=" + performanceId;
    }
}
