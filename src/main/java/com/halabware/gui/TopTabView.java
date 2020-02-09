package com.halabware.gui;

import com.halabware.datamodel.Database;
import javafx.fxml.FXML;
import javafx.scene.Parent;

public class TopTabView {
	@FXML
	private Parent aLoginTab;
	@FXML
	private LoginTab aLoginTabController;

	@FXML
	private Parent aBookingTab;
	@FXML
	private BookingTab aBookingTabController;

	@FXML
	private Parent aUserTicketsTab;
	@FXML
	private UserTicketsTab aUserTicketsTabController;

	public void initialize() {
		System.out.println("Initializing TopTabView");

		// send the booking controller reference to the login controller
		// in order to pass data between the two
		aLoginTabController.setBookingTab(aBookingTabController);
		aLoginTabController.setUserTicketsTab(aUserTicketsTabController);
		aBookingTabController.setUserTicketsTab(aUserTicketsTabController);
	}

	public void setDatabase(Database db) {
		aLoginTabController.setDatabase(db);
		aBookingTabController.setDatabase(db);
		aUserTicketsTabController.setDatabase(db);
	}
}
