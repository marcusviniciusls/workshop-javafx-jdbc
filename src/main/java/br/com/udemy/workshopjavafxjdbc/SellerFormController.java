package br.com.udemy.workshopjavafxjdbc;

import br.com.udemy.workshopjavafxjdbc.db.DbException;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Alerts;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Constraints;
import br.com.udemy.workshopjavafxjdbc.gui.utils.DataChangeListener;
import br.com.udemy.workshopjavafxjdbc.gui.utils.Utils;
import br.com.udemy.workshopjavafxjdbc.model.entities.Department;
import br.com.udemy.workshopjavafxjdbc.model.entities.Seller;
import br.com.udemy.workshopjavafxjdbc.model.exception.ValidationException;
import br.com.udemy.workshopjavafxjdbc.model.services.DepartmentService;
import br.com.udemy.workshopjavafxjdbc.model.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller seller;
    private SellerService sellerService;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListenerList = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtSaveAction(ActionEvent actionEvent) {
        if (seller == null){
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null){
            throw new IllegalStateException("Service was null");
        }
        try{
            seller = getFormData();
            sellerService.saveOrUpdateSeller(seller);
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

    private Seller getFormData() {
        Seller Seller = new Seller();
        ValidationException validationException = new ValidationException("Validation Error");
        Integer id = Utils.tryParseToInt(txtId.getText());
        Seller.setId(id);
        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            validationException.addError("name", "Field can't be empty");
        }
        String name = txtName.getText();
        Seller.setName(name);
        if (validationException.getErrors().size() > 0){
            throw validationException;
        }
        return Seller;
    }

    @FXML
    public void onBtCancelAction(ActionEvent actionEvent) {
        Utils.currentStage(actionEvent).close();
    }

    public void setSeller(Seller Seller){
        this.seller = Seller;
    }

    public void setServices(SellerService SellerService, DepartmentService departmentService){
        this.sellerService = SellerService;
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializerNodes();
    }

    private void initializerNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 80);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData(){
        if (this.seller == null){
            throw new IllegalStateException("Seller was null");
        }
        txtId.setText(String.valueOf(this.seller.getId()));
        txtName.setText(this.seller.getName());
        txtEmail.setText(seller.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
        if (seller.getBirthDate() != null){
            dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        if (seller.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        } else {
            comboBoxDepartment.setValue(seller.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if (departmentService == null){
            throw new IllegalStateException("DepartmentService Was null");
        }
        List<Department> listDepartment = departmentService.findAll();
        obsList = FXCollections.observableArrayList(listDepartment);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();
        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
