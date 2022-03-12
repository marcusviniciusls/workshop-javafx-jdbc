package br.com.udemy.workshopjavafxjdbc;

import br.com.udemy.workshopjavafxjdbc.db.DbException;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Alerts;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Constraints;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Utils;
import br.com.udemy.workshopjavafxjdbc.model.entities.Department;
import br.com.udemy.workshopjavafxjdbc.model.services.DepartmentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.action.Action;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department department;
    private DepartmentService departmentService;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    public void onBtSaveAction(ActionEvent actionEvent) {
        if (department == null){
            throw new IllegalStateException("Entity was null");
        }
        if (departmentService == null){
            throw new IllegalStateException("Service was null");
        }
        try{
            department = getFormData();
            departmentService.saveOrUpdateDepartment(department);
            Utils.currentStage(actionEvent).close();
        } catch (DbException dbException){
            Alerts.showAlert("Error saving object", null, dbException.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Department getFormData() {
        Department department = new Department();
        Integer id = Utils.tryParseToInt(txtId.getText());
        String name = txtName.getText();
        department.setId(id);
        department.setName(name);
        return department;
    }

    @FXML
    public void onBtCancelAction(ActionEvent actionEvent) {
        Utils.currentStage(actionEvent).close();
    }

    public void setDepartment(Department department){
        this.department = department;
    }

    public void setDepartmentService(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializerNodes();
    }

    private void initializerNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData(){
        if (this.department == null){
            throw new IllegalStateException("Department was null");
        }
        txtId.setText(String.valueOf(this.department.getId()));
        txtName.setText(this.department.getName());
    }
}
