package com.halabware.gui;

import com.halabware.datamodel.CurrentUser;
import com.halabware.datamodel.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

// controller for both the top tabs and login tab!

public class LoginTab {
	
	@FXML
	private Text actiontarget;
	@FXML
	private TextField username;

	private BookingTab bookingTabCtrl;

	private UserTicketsTab userTicketsCtrl;

	private Database db;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {

		if (!db.isConnected()) {
			// inform the user that there is no check against the database
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login fail");
			alert.setHeaderText(null);
			alert.setContentText("No database connection! Cannot check user credentials.");
			alert.showAndWait();
		} else {
			String uname = username.getText();

			/* --- TODO: add code to query the database credentials --- */
			if (!db.userExists(uname)) {
				// inform the user that there is no check against the database
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Login fail");
				alert.setHeaderText(null);
				alert.setContentText("You have to sign up before you can log in!");
				alert.showAndWait();
				/* --- END TODO --- */
			} else {
				// setting the user name
				CurrentUser.instance().loginAs(uname);

				// inform the user about logging in
				actiontarget.setText("Sign in user " + uname);

				// inform booking tab of user change
				this.bookingTabCtrl.userChanged();
				this.userTicketsCtrl.userChanged();

			}

		}
	}

	public void initialize() {
		System.out.println("Initializing LoginTab.");
	}

	// helpers
	// use this pattern to send data down to controllers at initialization
	public void setBookingTab(BookingTab bookingTabCtrl) {
		System.out.println("LoginTab sets bookingTab:" + bookingTabCtrl);
		this.bookingTabCtrl = bookingTabCtrl;
	}

	public void setUserTicketsTab(UserTicketsTab userTicketsCtrl) {
		System.out.println("LoginTab sets userTicketsTab:" + userTicketsCtrl);
		this.userTicketsCtrl = userTicketsCtrl;
	}

	public void setDatabase(Database db) {
		this.db = db;
	}

}