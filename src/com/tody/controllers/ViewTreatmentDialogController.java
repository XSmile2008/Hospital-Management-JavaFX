package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.Doctor;
import com.tody.datamodel.Patient;
import com.tody.datamodel.TreatmentDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Created by Olha Dovhal on 19/07/2017.
 */
public class ViewTreatmentDialogController {

    @FXML
    ListView<TreatmentDetails> recordsListView;
    @FXML
    Label docLabel, dateLabel;
    @FXML
    TextArea detailsTxtFd;

    private Patient patient;

    public void initialize() {
        recordsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreatmentDetails details = recordsListView.getSelectionModel().getSelectedItem();
                int id = details.getDoc_id();
                Doctor doc = Datasource.getInstance().queryDoctor(Integer.toString(id));
                docLabel.setText(doc.getName() + " " + "(" + doc.getSpeciality() + ")");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMMM, yyyy");
                dateLabel.setText(dtf.format(details.getDate()));
                detailsTxtFd.setText(details.getDetails());
            }
        });
    }

    protected void setPatient(String id) {
        this.patient = Datasource.getInstance().queryPatient(id);
        SortedList<TreatmentDetails> sortedList;
        if (patient != null) {
            sortedList = new SortedList<>(patient.getTreatmentDetails(), Comparator.comparing(TreatmentDetails::getDate));

            recordsListView.setItems(sortedList);
            recordsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } else {
            System.out.println("No Patient received!");
        }

        recordsListView.setCellFactory(new Callback<ListView<TreatmentDetails>, ListCell<TreatmentDetails>>() {
            @Override
            public ListCell<TreatmentDetails> call(ListView<TreatmentDetails> param) {
                return new ListCell<TreatmentDetails>() {
                    @Override
                    protected void updateItem(TreatmentDetails item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item != null ? item.getDate().toString() : null);
                    }
                };
            }
        });
    }
}