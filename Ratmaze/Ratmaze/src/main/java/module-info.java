module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens lab to javafx.fxml;
    exports lab;
    exports lab.interfaces;
    opens lab.interfaces to javafx.fxml;
    exports lab.entity;
    opens lab.entity to javafx.fxml;
    exports lab.enums;
    opens lab.enums to javafx.fxml;
    exports lab.logic;
    opens lab.logic to javafx.fxml;
    exports lab.logic.controllers;
    opens lab.logic.controllers to javafx.fxml;
    exports lab.others;
    opens lab.others to javafx.fxml;
}