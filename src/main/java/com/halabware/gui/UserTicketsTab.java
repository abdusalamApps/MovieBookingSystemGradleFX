package com.halabware.gui;

import com.halabware.datamodel.CurrentUser;
import com.halabware.datamodel.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;


public class UserTicketsTab {

    Database db;
    String userName;

    List<String> tickets;

    @FXML
    private ListView<String> ticketsListView;

    public void initialize() {
        System.out.println("Initializing UserTicketsTab");
        tickets = new ArrayList<>();

        fillTicketsList();
    }

    public void fillTicketsList() {
        if (userName == null || userName.equals("")) {
            tickets.add("Please Log In to See Your Tickets!");
        } else {
            tickets = db.getUserTickets(userName);
        }

        ticketsListView.setItems(FXCollections.observableList(tickets));
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public void userChanged() {
        userName = CurrentUser.instance().getCurrentUserId();
        fillTicketsList();
        System.out.println("userName in UserTicketsTab: " + userName);
    }

}
