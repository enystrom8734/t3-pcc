package userinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

//==============================================================================
public class ColorTableModel {
    private final SimpleStringProperty barcodePrefix;
    private final SimpleStringProperty description;
    private final SimpleStringProperty alphaCode;
    private final SimpleStringProperty status;


    public ColorTableModel(Vector<String> atData) {
        barcodePrefix = new SimpleStringProperty(atData.elementAt(2));
        description = new SimpleStringProperty(atData.elementAt(1));
        alphaCode = new SimpleStringProperty(atData.elementAt(3));
        status = new SimpleStringProperty(atData.elementAt(4));
    }


    public String getBarcodePrefix() {
        return barcodePrefix.get();
    }


    public void setBarcodePrefix(String pref) {
        barcodePrefix.set(pref);
    }


    public String getDescription() {
        return description.get();
    }


    public void setDescription(String desc) {
        description.set(desc);
    }


    public String getAlphaCode() {
        return alphaCode.get();
    }


    public void setAlphaCode(String code) {
        alphaCode.set(code);
    }


    public String getStatus() {
        return status.get();
    }


    public void setStatus(String stat) {
        status.set(stat);
    }
}
