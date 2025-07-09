module com.clinic.project.medclinic {
    requires javafx.controls;
    requires javafx.fxml;

    exports clinicamed.mainapp;
    exports clinicamed.controller;

   opens clinicamed.mainapp to javafx.fxml;
    opens clinicamed.controller to javafx.fxml;
}
