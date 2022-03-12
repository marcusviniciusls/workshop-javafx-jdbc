package br.com.udemy.workshopjavafxjdbc;

import br.com.udemy.workshopjavafxjdbc.db.DbIntegrityException;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Alerts;
import br.com.udemy.workshopjavafxjdbc.gui.utils.DataChangeListener;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Utils;
import br.com.udemy.workshopjavafxjdbc.model.entities.Seller;
import br.com.udemy.workshopjavafxjdbc.model.services.SellerService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService sellerService;
    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    @FXML
    private TableColumn<Seller, String> tableColumnEmail;
    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;
    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;
    @FXML
    private TableColumn<Seller, Seller> tableColumnEdit;
    @FXML
    private TableColumn<Seller, Seller> tableColumnDelete;
    @FXML
    private Button buttonNew;
    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction(ActionEvent actionEvent){
        Stage parentStage = Utils.currentStage(actionEvent);
        Seller seller = new Seller();
        createDialogForm(seller, parentStage, "SellerForm.fxml");
    }

    public void setSellerService(SellerService sellerService){
        this.sellerService = sellerService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
        Stage stage = (Stage) HelloApplication.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (sellerService == null){
            throw new IllegalArgumentException("Service Was null");
        }
        List<Seller> listSeller = sellerService.findAll();
        obsList = FXCollections.observableArrayList(listSeller);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller Seller, Stage parentStage, String absoluteName){
        //try{
            //FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            //Pane pane = loader.load();
            //SellerFormController sellerFormController = loader.getController();
            //sellerFormController.setSeller(seller);
            //sellerFormController.setSellerService(new SellerService());
           // sellerFormController.subscribeDataChangeListener(this);
            //sellerFormController.updateFormData();
            //Stage dialogStage = new Stage();
            //dialogStage.setTitle("Enter Seller data");
            //dialogStage.setScene(new Scene(pane));
            //dialogStage.setResizable(false);
            //dialogStage.initOwner(parentStage);
            //dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.showAndWait();
        //} catch (IOException ioException){
            //Alerts.showAlert("IO Exception", "Error loading view", ioException.getMessage(), Alert.AlertType.ERROR);
        //}
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event), "SellerForm.fxml"));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller Seller) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        if (result.get() == ButtonType.OK){
            try {
                sellerService.remove(Seller);
                updateTableView();
            } catch (DbIntegrityException dbIntegrityException){
                Alerts.showAlert("Error removing object", null, dbIntegrityException.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
