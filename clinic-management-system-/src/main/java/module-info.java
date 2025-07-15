module com.clinic.project.medclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    exports clinicamed.mainapp;
    exports clinicamed.controller;

   opens clinicamed.mainapp to javafx.fxml;
   opens clinicamed.controller to javafx.fxml;
   opens clinicamed.model to javafx.base;
}
