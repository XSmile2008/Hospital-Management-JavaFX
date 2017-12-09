package com.tody.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Op {

    final SimpleIntegerProperty id = new SimpleIntegerProperty();
    public final SimpleStringProperty name = new SimpleStringProperty();
    public final SimpleStringProperty reason = new SimpleStringProperty();

    public Op() {
    }

    public Op(int id, String name, String reason) {
        this.id.set(id);
        this.name.set(name);
        this.reason.set(reason);
    }
}
