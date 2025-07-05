module com.clinic.project.medclinic {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.clinic.project.medclinic;
    exports controller;
    opens controller to javafx.fxml;
    opens com.clinic.project.medclinic to javafx.fxml;
}