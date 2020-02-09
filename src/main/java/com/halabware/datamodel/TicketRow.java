package com.halabware.datamodel;

public class TicketRow {
    public int res_number;
    public String customer, title, theatre, showDate;

    public TicketRow(int res_number, String customer, String title,
                     String theatre, String showDate) {
        this.res_number = res_number;
        this.customer = customer;
        this.title = title;
        this.theatre = theatre;
        this.showDate = showDate;
    }

    @Override
    public String toString() {
        return
                title +
                        "\n" + theatre + "           " + showDate;
    }
}
