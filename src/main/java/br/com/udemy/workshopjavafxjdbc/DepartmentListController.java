package br.com.udemy.workshopjavafxjdbc;

import br.com.udemy.workshopjavafxjdbc.HelloApplication;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Alerts;
import br.com.udemy.workshopjavafxjdbc.gui.utils.DataChangeListener;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Utils;
import br.com.udemy.workshopjavafxjdbc.model.entities.Department;
import br.com.udemy.workshopjavafxjdbc.model.services.DepartmentService;
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
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmentService departmentService;
    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private TableColumn<Department, Department> tableColumnEdit;
    @FXML
    private Button buttonNew;
    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent actionEvent){
        Stage parentStage = Utils.currentStage(actionEvent);
        Department department = new Department();
        createDialogForm(department, parentStage, "DepartmentForm.fxml");
    }

    public void setDepartmentService(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Stage stage = (Stage) HelloApplication.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (departmentService == null){
            throw new IllegalArgumentException("Service Was null");
        }
        List<Department> listDepartment = departmentService.findAll();
        obsList = FXCollections.observableArrayList(listDepartment);
        tableViewDepartment.setItems(obsList);
        initEditButtons();
    }

    private void createDialogForm(Department department, Stage parentStage, String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            DepartmentFormController departmentFormController = loader.getController();
            departmentFormController.setDepartment(department);
            departmentFormController.setDepartmentService(new DepartmentService());
            departmentFormController.subscribeDataChangeListener(this);
            departmentFormController.updateFormData();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException ioException){
            Alerts.showAlert("IO Exception", "Error loading view", ioException.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, Utils.currentStage(event), "DepartmentForm.fxml"));
            }
        });
    }

}
