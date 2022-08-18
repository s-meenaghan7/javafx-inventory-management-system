package controller;

import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import utils.SceneChanger;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 FXML Add Part Screen Controller class.
 @author Sean
 */
public class AddPartController implements Initializable {

    @FXML private final ToggleGroup partTypeToggleGroup = new ToggleGroup();
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;

//    @FXML private TextField partIdTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partStockTextField;
    @FXML private TextField partPriceTextField;
    @FXML private TextField partMaxTextField;
    @FXML private TextField partMinTextField;

    @FXML private TextField partTypeField;
    @FXML private Label partTypeLabel;

//    @FXML private Button addPartCancelButton;
//    @FXML private Button addPartSaveButton;

    @FXML private Label nameExceptionText;
    @FXML private Label stockExceptionText;
    @FXML private Label priceExceptionText;
    @FXML private Label maxExceptionText;
    @FXML private Label minExceptionText;
    @FXML private Label partSubclassExceptionText;


    /**
     Called to initialize the Add Part controller class after its root element has been completely processed.
     Also sets up the ToggleGroup for the two RadioButtons. Sets the In-House RadioButton as the default selection (arbitrary).
     @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.inHouseRadioButton.setToggleGroup(partTypeToggleGroup);
        this.outsourcedRadioButton.setToggleGroup(partTypeToggleGroup);

        // Sets inHouse as default radio button selection
        this.inHouseRadioButton.setSelected(true);
    }

    /**
     This method controls changing the partTypeField, depending on the radio button selection made.
     Depending on the selection, the partTypeField will read "Machine ID" or "Company Name."
     The selection of the radio button is used to indicate the subclass of added Parts.
     */
    public void radioButtonChanged() {
        if (this.partTypeToggleGroup.getSelectedToggle().equals(this.inHouseRadioButton)) {
            partTypeField.clear();
            partSubclassExceptionText.setText("");
            partTypeLabel.setText("Machine ID");
        }

        else if (this.partTypeToggleGroup.getSelectedToggle().equals(this.outsourcedRadioButton)) {
            partTypeField.clear();
            partSubclassExceptionText.setText("");
            partTypeLabel.setText("Company Name");
        }
    }

    /**
     This method listens for the Save button on the Add Part screen being pressed.
     The method first validates user input via the validateUserInputParts() method. Next, this method will check for the RadioButton selection made by the user (InHouse or Outsourced).
     A new Part of either Part subclass is created based on the selection, and the Part is then added to the Inventory's list of Parts.
     Finally, the scene is changed back to the Main screen by calling changeSceneTo() method.
     @param event the ActionEvent object representing the Save button on the Add Part screen being pressed.
     @throws IOException the potential IOException that must be caught or declared to be thrown.
     */
    public void addPartSaveButtonPushed(ActionEvent event) throws IOException {
        if(!validateUserInputParts()) return; // If user input !valid, do not continue.

        if (partTypeToggleGroup.getSelectedToggle().equals(inHouseRadioButton)) {
            // create inHousePart from text fields, static method for ID
            InHousePart newInHousePart = new InHousePart(createPartId(),
                    partNameTextField.getText().trim(),
                    Double.parseDouble(partPriceTextField.getText()),
                    Integer.parseInt(partStockTextField.getText()),
                    Integer.parseInt(partMinTextField.getText()),
                    Integer.parseInt(partMaxTextField.getText()),
                    Integer.parseInt(partTypeField.getText()));

            // add the newInHousePart to the allParts ObservableList
            Inventory.addPart(newInHousePart);

            // switch back to mainScreen
            SceneChanger.changeSceneTo(event, "mainScreen.fxml");
        }

        if (partTypeToggleGroup.getSelectedToggle().equals(outsourcedRadioButton)) {
            //create outsourcedPart from text fields, static method for ID
            OutsourcedPart newOutsourcedPart = new OutsourcedPart(createPartId(),
                    partNameTextField.getText(),
                    Double.parseDouble(partPriceTextField.getText()),
                    Integer.parseInt(partStockTextField.getText()),
                    Integer.parseInt(partMinTextField.getText()),
                    Integer.parseInt(partMaxTextField.getText()),
                    partTypeField.getText());

            // add the newInHousePart to the allParts ObservableList
            Inventory.addPart(newOutsourcedPart);

            //switch back to mainScreen
            SceneChanger.changeSceneTo(event, "mainScreen.fxml");
        }
    }

    /**
     This method listens for the Cancel button being pressed.
     Pressing the Cancel button will change the scene back to the main screen.
     @param event the ActionEvent object representing the Cancel button on the Add Part screen being pressed.
     @throws IOException the potential IOException that must be caught or declared to be thrown.
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        SceneChanger.changeSceneTo(event, "mainScreen.fxml");
    }

    /**
     This method validates the user's input on the Add and Modify Part forms.
     The method is called within the button handlers for the Save buttons, on the Add Part and Modify Part forms.
     The method checks each TextField individually, ensuring input is valid. If any of the TextFields inputs are invalid, an error message will be displayed to the user and the method will return false.
     @return returns true if there are no error messages present. If any error messages are still present, returns false.
     */
    public boolean validateUserInputParts() {
        //check that the partNameTextField is valid input
        if (partNameTextField.getText().isBlank() ||
                Utils.isNumber(partNameTextField.getText()) ||
                (!partNameTextField.getText().trim().matches("^[a-zA-Z\\s]+$"))) { //spaces only allowed between words

            nameExceptionText.setText("Name field input invalid. Numbers and special characters are forbidden.");

        } else nameExceptionText.setText("");

        //check that the partStockTextField is valid input; should be an int and between the max and min values
        if (partStockTextField.getText().isBlank() ||
                (!Utils.isNumber(partStockTextField.getText()))) {

            stockExceptionText.setText("Inventory field input invalid. Value must be a whole number.");

        } else if (!partStockTextField.getText().isBlank()) {
            try {
                if (partMaxTextField.getText().isBlank() || partMinTextField.getText().isBlank()) {
                    stockExceptionText.setText("Inventory field cannot be checked against blank min and/or max fields.");

                } else if ((Integer.parseInt(partStockTextField.getText()) > Integer.parseInt(partMaxTextField.getText())) ||
                        (Integer.parseInt(partStockTextField.getText()) < Integer.parseInt(partMinTextField.getText()))) {

                    stockExceptionText.setText("Inventory field value must be a number between the min and max field values.");

                } else stockExceptionText.setText("");

            } catch (NumberFormatException e) {
                stockExceptionText.setText("Error: Inventory field cannot be checked against blank min and/or max fields.");
            }
        } else stockExceptionText.setText("");

        //check that the partPriceTextField is valid input. Must be a double/float (int OK)
        if (partPriceTextField.getText().isBlank() ||
                (!partPriceTextField.getText().matches("[+-]?([0-9]*[.])?[0-9]+"))) {

            priceExceptionText.setText("Price field value must be a number (decimal points are OK).");

        } else priceExceptionText.setText("");

        //check the partMaxTextField. Value must be a number, greater than partMinTextField.
        if (partMaxTextField.getText().isBlank() ||
                (!Utils.isNumber(partMaxTextField.getText()))) {

            maxExceptionText.setText("Max field value must be a number. Value must be greater than Min field value.");

        } else maxExceptionText.setText("");

        //check the partMinTextField. Value must be a number, less than partMaxTextField.
        if (partMinTextField.getText().isBlank() ||
                (!Utils.isNumber(partMinTextField.getText()))) {

            minExceptionText.setText("Min field value must be a number. Value must be lesser than Max field value.");

        } else minExceptionText.setText("");

        //check the partTypeField. If InHouse is selected, value must be int. If Outsourced selected, value must be String.
        if (partTypeToggleGroup.getSelectedToggle().equals(inHouseRadioButton)) {
            if (partTypeField.getText().isBlank() || (!Utils.isNumber(partTypeField.getText()))) {

                partSubclassExceptionText.setText("Machine ID field value must be a whole number.");

            } else partSubclassExceptionText.setText("");

        } else if (partTypeToggleGroup.getSelectedToggle().equals(outsourcedRadioButton)) {
            if (partTypeField.getText().isBlank() || Utils.isNumber(partTypeField.getText())) {

                partSubclassExceptionText.setText("Company Name field value should not be blank or only contain numbers.");

            } else partSubclassExceptionText.setText("");

        }

        //Returns false if errors are still present, true if all exceptionText's are "" (empty)
        return nameExceptionText.getText().equals("") &&
                stockExceptionText.getText().equals("") &&
                priceExceptionText.getText().equals("") &&
                maxExceptionText.getText().equals("") &&
                minExceptionText.getText().equals("") &&
                partSubclassExceptionText.getText().equals("");
    }

    private static int partsIdCounter = 0;

    /**
     This static method is responsible for generating a unique Part ID each time a Part object is instantiated.
     This method returns the value of the ++partsIdCounter, a static int variable.
     @return ++partsIdCounter the value of the partsIdCounter incremented by 1 is returned each time this method is called.
     */
    public static int createPartId() {
        return ++partsIdCounter;
    }

}
