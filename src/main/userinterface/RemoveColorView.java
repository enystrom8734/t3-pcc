// specify the package
package userinterface;

// system imports

import impresario.IModel;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

// project imports

/**
 * The class containing the Remove Color View for the Professional Clothes
 * Closet application
 */
//==============================================================
public class RemoveColorView extends View {

    protected PccButton submitButton;
    protected PccButton cancelButton;
    protected PccText prompt;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object

    public RemoveColorView(IModel at) {
        super(at, "RemoveColorView");

        // create a container for showing the contents
        container.getChildren().add(createActionArea());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog(""));

        //Add container to our BorderPane
        bp.setCenter(container);

        // Add BorderPane to our view
        getChildren().add(bp);

        populateFields();

        myModel.subscribe("TransactionError", this);
    }


    @Override
    protected String getActionText() {
        return "Remove a Color";
    }

    // Create the main form content

    private VBox createFormContent() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        PccText prompt1 = new PccText("Are you sure you with to remove this Color?");
        prompt1.setWrappingWidth(WRAPPING_WIDTH);
        prompt1.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(prompt1);

        prompt = new PccText("");
        prompt.setWrappingWidth(WRAPPING_WIDTH);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFont(Font.font(APP_FONT, 20));

        vbox.getChildren().add(prompt);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new PccButton("Yes");
        submitButton.setOnAction(e -> {
            myModel.stateChangeRequest("RemoveColor", null);
            myModel.stateChangeRequest("CancelRemoveCT", "");
        });
        doneCont.getChildren().add(submitButton);

        cancelButton = new PccButton("No");
        cancelButton.setOnAction(e -> {
            clearErrorMessage();
            myModel.stateChangeRequest("CancelRemoveCT", null);
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(doneCont);

        return vbox;
    }


    // Create the status log field

    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    public void populateFields() {
        prompt.setText(
                (String) myModel.getState("Description") + "\n"
        );

    }

    /**
     * Update method
     */

    public void updateState(String key, Object value) {
        clearErrorMessage();

        if (key.equals("TransactionError")) {
            String val = (String) value;
            if (val.startsWith("ERR")) {
                displayErrorMessage(val);
            } else {
                displayMessage(val);
            }

        }
    }

    /**
     * Display error message
     */

    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */

    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */

    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

}


//	Revision History:
//
