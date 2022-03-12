package br.com.udemy.workshopjavafxjdbc.gui.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

    public static Stage currentStage(ActionEvent actionEvent){
        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }
    
    public static Integer tryParseToInt(String number){
        try{
            return Integer.parseInt(number);
        } catch (NumberFormatException numberFormatException){
            return null;
        }
    }
}