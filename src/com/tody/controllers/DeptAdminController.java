package com.tody.controllers;

import com.tody.datamodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Olha Dovhal on 12/06/2017.
 */
public class DeptAdminController {

    //Left
    @FXML
    Label userId, userName;

    @FXML
    ToggleGroup tgSidebar;
    @FXML
    ToggleButton tgBtnNewReg;
    @FXML
    ToggleButton tgBtnViewPatients;
    @FXML
    ToggleButton tgBtnViewOutPatients;

    @FXML
    AnchorPane registrationView, mainView;
    @FXML
    TabPane viewPatient;

    @FXML
    Label userNameWelcome, nameLabel, reasonLabel, phoneLabel, addressLabel, referLabel, ipidLabel, opidLabel, ageLabel;

    @FXML
    TextField nameTxtFd, reasonTxtFd, phoneTxtFd, addressTxtFd, ipidTxtFd, opidTxtFd, ageTxtFd, usrInput,
            viewID, viewName, viewAge, viewAddress, viewPhone;
    @FXML
    ChoiceBox<String> referBox;

    @FXML
    GridPane gridPane;

    @FXML
    TableView<Patient> tvPatients;

    @FXML
    TableView<Op> tvOp;

    private String id;

    public void initialize() {

    }

    @FXML
    private void onNewRegistrationClicked() {
        mainView.setVisible(false);
        registrationView.setVisible(true);
        viewPatient.setVisible(false);
        tvOp.setVisible(false);
    }

    @FXML
    private void onViewPatientsClicked() {
        mainView.setVisible(false);
        registrationView.setVisible(false);
        viewPatient.setVisible(true);
        tvOp.setVisible(false);
    }

    @FXML
    private void onViewOutPatientsClicked() {
        mainView.setVisible(false);
        registrationView.setVisible(false);
        viewPatient.setVisible(false);
        tvOp.setVisible(true);
    }

    @FXML
    private void validateOPID() {
        Op op = Datasource.getInstance().queryOpById(Integer.parseInt(opidTxtFd.getText()));
        if (op != null) {
            nameLabel.setVisible(true);
            nameTxtFd.setVisible(true);
            nameTxtFd.setText(op.name.getValue());
            nameTxtFd.setEditable(false);
            ageLabel.setVisible(true);
            ageTxtFd.setVisible(true);
            reasonLabel.setVisible(true);
            reasonTxtFd.setVisible(true);
            reasonTxtFd.setText(op.reason.getValue());
            reasonTxtFd.setEditable(false);

            phoneLabel.setVisible(true);
            phoneTxtFd.setVisible(true);
            addressLabel.setVisible(true);
            addressTxtFd.setVisible(true);
            referLabel.setVisible(true);
            referBox.setVisible(true);

            phoneTxtFd.requestFocus();
            referBox.getItems().add("Select Doctor");
            referBox.getSelectionModel().selectFirst();
            List<Doctor> doctors = Datasource.getInstance().queryDoctors();
            for (Doctor doc : doctors) {
                referBox.getItems().addAll(doc.getName() + " (" + doc.getSpeciality() + ")");
            }
        } else {
            Alert notFound = new Alert(Alert.AlertType.ERROR, "Patient with OPID : " + opidTxtFd.getText() + " is not in the database");
            notFound.setHeaderText(null);
            notFound.setTitle(null);
            notFound.show();
        }
    }

    @FXML
    private void onSelect() {
        if (!"Select Doctor".equals(referBox.getSelectionModel().getSelectedItem())) {
            generateIPID();
        }
    }

    private void generateIPID() {
        if (ipidTxtFd.getText().isEmpty()) {
            ipidLabel.setVisible(true);
            ipidTxtFd.setVisible(true);

            ArrayList<Character> list = new ArrayList<>();
            for (char i = 'A'; i <= 'Z'; i++) {
                list.add(i);
            }
            ArrayList<Integer> list1 = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                list1.add(i);
            }
            Collections.shuffle(list);
            Collections.shuffle(list1);
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                id.append(list1.get(i));
            }
            for (int i = 0; i < 2; i++) {
                id.append(list.get(i));
            }

            ipidTxtFd.setText(id.toString());
            System.out.println("IPID : " + id);
        }


    }

    @FXML
    private void newPatient() {
        String ip_id = ipidTxtFd.getText();
        String name = nameTxtFd.getText();
        int age = Integer.parseInt(ageTxtFd.getText());
        String address = addressTxtFd.getText();
        String phone = phoneTxtFd.getText();

        if (!ip_id.isEmpty() && !name.isEmpty()) {
            if (address.isEmpty()) {
                address = "N/A";
            }
            if (Datasource.getInstance().storePatient(ip_id, name, age, address, phone)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "New Patient added!\n" +
                        "Details :\n" +
                        "IPID : " + ip_id + "\n" +
                        "Name : " + name + "\n" +
                        "Age  : " + age + "\n" +
                        "Address : " + address + "\n" +
                        "Phone   : " + phone);
                success.setHeaderText(null);
                success.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Error adding patient to the database!\n" +
                        "Check necessary field");
                error.setHeaderText(null);
                error.show();
            }
        }
    }

    @FXML
    protected void viewPatient() {
        String id = usrInput.getText().toUpperCase().trim();
        if (!id.isEmpty() && id.length() == 5) {
            Patient patient = Datasource.getInstance().queryPatient(id);
            if (patient != null) {
                String ip_id = patient.getIp_id();
                String name = patient.getName();
                int age = patient.getAge();
                String address = patient.getAddress();
                String phone = patient.getPhone();

                viewID.setText(ip_id);
                viewName.setText(name);
                viewAge.setText(String.valueOf(age));
                viewAddress.setText(address);
                viewPhone.setText(phone);
                gridPane.setVisible(true);
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Check IPID");
                error.setHeaderText(null);
                error.show();
            }
        }


    }

    @FXML
    private void updatePatients() {
        tvPatients.getItems().setAll(Datasource.getInstance().queryAllPatients());
        //        Task<ObservableList<Patient>> task = new GetAllPatientsTask();
//        tvPatients.itemsProperty().bind(task.valueProperty());
//        new Thread(task).start();
    }

    @FXML
    void reset() {
        opidTxtFd.clear();
        nameTxtFd.clear();
        ageTxtFd.clear();
        nameLabel.setVisible(false);
        nameTxtFd.setVisible(false);
        ageLabel.setVisible(false);
        ageTxtFd.setVisible(false);
        reasonTxtFd.clear();
        reasonLabel.setVisible(false);
        reasonTxtFd.setVisible(false);

        phoneTxtFd.clear();
        phoneLabel.setVisible(false);
        phoneTxtFd.setVisible(false);
        addressTxtFd.clear();
        addressLabel.setVisible(false);
        addressTxtFd.setVisible(false);
        referBox.getSelectionModel().selectFirst();
        referLabel.setVisible(false);
        referBox.setVisible(false);
        referBox.getItems().clear();

        ipidTxtFd.clear();
        ipidLabel.setVisible(false);
        ipidTxtFd.setVisible(false);
    }

    @FXML
    void resetPatientView() {
        usrInput.clear();
        viewName.clear();
        viewID.clear();
        viewAge.clear();
        viewAddress.clear();
        viewPhone.clear();
        gridPane.setVisible(false);
        usrInput.requestFocus();
    }

    public void setUser(String id) {
        this.id = id;
        DeptAdmin user = Datasource.getInstance().queryDeptAdmin(id);
        userName.setText(user.getName());
        userNameWelcome.setText(user.getName());
        userId.setText(id);

    }

    @FXML
    private void logOut() {
        MainController controller = new MainController();
        controller.logOut();
    }

    class GetAllPatientsTask extends Task {
        @Override
        public ObservableList call() throws Exception {
            return FXCollections.observableArrayList(Datasource.getInstance().queryAllPatients());
        }
    }
}
