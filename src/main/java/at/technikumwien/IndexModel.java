package at.technikumwien;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

// Das ist die Enterprise Java bean
@Data // hier damit wir getter und setter setzen
@Named("index") // Wenn im index.xthml ein index. aufgerufen wird sucht er get Funktion hier | Name über den zugegriffen wird
@RequestScoped // gilt nur solange der Aufruf aktiv ist.
public class IndexModel {

    // CDI erstellt das Objekt damit es funktioniert, selbst wird nie eine Instanz erzeugt sonst würde es nicht funktionieren. Der Container muss es machen.
    @Getter(AccessLevel.NONE) // hier wollen wir keine Getter und Setter
    @Setter(AccessLevel.NONE)
    @Inject
    private EmployeeService employeeService; // wird von @Inject ge-new()ed

    private boolean showActive = true; // Getter & Setter automatisch      -- Model Bean das auf die Index.html verknüpft ist, wenn im html auf das die Verknüpfung gedrückt wird wird der bool gesetzt
    private boolean showNonActive = true;

    // Muss gleich heißen wie in der index.xhtml und wird dorta aufgerufen   <ui:repeat var="employee" value="#{index.employees}">
    public List<Employee> getEmployees() {
        if (showActive && showNonActive) {
            return employeeService.findAll();
        }
        else {
            return employeeService.findAll(
                    employee -> (employee.isActive() && showActive) || (!employee.isActive() && showNonActive)
            );
        }
    }

}
