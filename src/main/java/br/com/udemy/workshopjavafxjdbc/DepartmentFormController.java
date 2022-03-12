package br.com.udemy.workshopjavafxjdbc;

import br.com.udemy.workshopjavafxjdbc.db.DbException;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Alerts;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Constraints;
import br.com.udemy.workshopjavafxjdbc.gui.utils.DataChangeListener;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Utils;
import br.com.udemy.workshopjavafxjdbc.model.entities.Department;
import br.com.udemy.workshopjavafxjdbc.model.exception.ValidationException;
import br.com.udemy.workshopjavafxjdbc.model.services.DepartmentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.action.Action;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department department;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListenerList = new ArrayList<>();

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
            notifyDataChangeListeners();
            Utils.currentStage(actionEvent).close();
        } catch (ValidationException validationException){
            setErrorMessages(validationException.getErrors());
        } catch (DbException dbException){
            Alerts.showAlert("Error saving object", null, dbException.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener dataChangeListener : this.dataChangeListenerList){
            dataChangeListener.onDataChanged();
        }
    }

    public void subscribeDataChangeListener(DataChangeListener dataChangeListener){
        this.dataChangeListenerList.add(dataChangeListener);
    }

    private Department getFormData() {
        Department department = new Department();
        ValidationException validationException = new ValidationException("Validation Error");
        Integer id = Utils.tryParseToInt(txtId.getText());
        department.setId(id);
        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            validationException.addError("name", "Field can't be empty");
        }
        String name = txtName.getText();
        department.setName(name);
        if (validationException.getErrors().size() > 0){
            throw validationException;
        }
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

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();
        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}
