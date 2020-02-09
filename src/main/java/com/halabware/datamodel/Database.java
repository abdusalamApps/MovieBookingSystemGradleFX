package com.halabware.datamodel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {

    /**
     * The database connection.
     */
    private Connection conn;

    /**
     * Create the database interface object. Connection to the database is performed
     * later.
     */
    public Database() {
        conn = null;
    }

    /**
     * Open a connection to the database, using the specified user name and
     * password.
     *
     * @param userName The user name.
     * @param password The user's password.
     * @return true if the connection succeeded, false if the supplied user name and
     * password were not recognized. Returns false also if the JDBC driver
     * isn't found.
     */
    public boolean openConnection(String url, String database, String userName, String password) {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + database, userName, password);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;

        System.err.println("Database connection closed.");
    }

    /**
     * Check if the connection to the database has been established
     *
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    public Show getShowData(String mTitle, String mDate) {
        Show show = new Show(mTitle, mDate, null, null);
        System.out.print(mTitle + " " + mDate + "\n");
        try {
            /* --- TODO: add code for database query --- */
            String query = "select Movies.name as Movie," + " Performances.pdate as Show_Date,"
                    + " Theatres.name as Theatre," + " Performances.available_seats as Available_Seats\n"
                    + "from Movies join Performances\n" + "	on Movies.id = Performances.movie_id\n" + "join Theatres\n"
                    + "	on Performances.theatre_name = Theatres.name\n" + "where Movies.name = '" + mTitle
                    + "' and Performances.pdate = '" + mDate + "';";

            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                show = new Show(mTitle, mDate, set.getString("Theatre"), set.getInt("Available_Seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return show;
    }

    /* --- TODO: insert more own code here --- */

    /*
     * This method returns all the available shows
     */
    public List<String> getMovies() {
        ArrayList<String> movies = new ArrayList<>();

        String query = "select Movies.name as Movie\n" + "from Movies join Performances\n"
                + "on Performances.movie_id = Movies.id;";

        try {
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                movies.add(set.getString("Movie"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /*
     * This method returns the dates a specific movies is shown
     */
    public List<String> getShowDates(String movie) {
        ArrayList<String> dates = new ArrayList<>();
        String query = "select Performances.pdate as Show_Date\n" + "from Performances join Movies\n"
                + "on Performances.movie_id = Movies.id\n" + "where Movies.name = " + "'" + movie + "'";

        try {
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                dates.add(set.getString("Show_Date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dates;

    }

    /*
     * Inserts a new User
     */
    public boolean userExists(String username) {
        String query = "select * from Users " + "where user_name = '" + username + "'";
        int size = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                size++;
                System.out.println(set.getString("user_name"));
            }
            if (size > 0)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Makes a new booking
     */
    public boolean bookATicket(String userName, String showDate, int availableSeats, String theatreName) {

        int performanceId = getPerformanceId(showDate, availableSeats, theatreName);

        System.out.println(performanceId);

        String query = "insert into Tickets (performance_id, user_name) values(" + performanceId + ", '" + userName
                + "')";

        decreaseAvailableSeats(performanceId);
        int update = 0;
        try {
            Statement statement = conn.createStatement();
            update = statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /*
     * Returns all tickets' res numbers based on PerformanceId and a userName
     */
    public List<String> getTickets(int performanceId, String userName) {
        ArrayList<String> resNumbers = new ArrayList<>();

        String query = "select res_number from "
                + "Tickets where Tickets.performance_id = " + performanceId
                + " and Tickets.user_name = '" + userName
                + "' order by Tickets.res_number desc;";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                resNumbers.add(resultSet.getString("res_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return resNumbers;
    }

    public List<String> getTickets(String userName) {
        ArrayList<String> tickets = new ArrayList<>();
        String query = "select * from Tickets where Tickets.user_name = '" + userName + "' ";

        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                tickets.add(new Ticket(
                        resultSet.getString("user_name"),
                        resultSet.getInt("res_number"),
                        resultSet.getInt("performance_id")
                ).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return tickets;
    }

    public List<String> getUserTickets(String userName) {
        ArrayList<String> tickets = new ArrayList<>();
        String query = "select Tickets.res_number as Reservation_Number, Users.name as Customer,  Movies.name as Title, Theatres.name as Theatre, Performances.pdate as Show_Date " +
                "from Tickets join Performances " +
                "on Tickets.performance_id = Performances.id " +
                "join Movies on Movies.id = Performances.movie_id " +
                "join Users on Users.user_name = Tickets.user_name " +
                "join Theatres on Theatres.name = Performances.theatre_name " +
                "where Tickets.user_name = '"+ userName +"';";

        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                tickets.add(new TicketRow(
                        resultSet.getInt("Reservation_Number"),
                        resultSet.getString("Customer"),
                        resultSet.getString("Title"),
                        resultSet.getString("Theatre"),
                        resultSet.getString("Show_Date")
                        ).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /*
     * Decrease the available seats
     */
    public boolean decreaseAvailableSeats(int performanceId) {

        String query = "update Performances set available_seats = available_seats - 1" + " where Performances.id = "
                + performanceId + " and available_seats > 0;";
        int update = 0;
        try {
            Statement st = conn.createStatement();
            update = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return update > 0;
    }

    /*
     * A helper method that returns a specific Performance id
     * based on a show date, available seats and theatre name.
     */
    public int getPerformanceId(String showDate, int availableSeats, String theatreName) {
        String query = "select Performances.id\n" + "from Performances\n" + "where Performances.pdate = '" + showDate
                + "' and\n" + "	  Performances.available_seats = '" + availableSeats + "' and\n"
                + "      Performances.theatre_name = '" + theatreName + "';";
        int id = -1;
        try {
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                id = set.getInt("Performances.id");
            }
            if (id < 0)
                throw new IllegalArgumentException();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    /*
     * Check if there are seats available
     */
}
