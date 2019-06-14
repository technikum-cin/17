import at.technikumwien.Employee;
import at.technikumwien.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//import static org.junit.jupiter.api.Assertions;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Erstellt die Mockito new classen, mockito befüllt die classen
public class EmployeeServiceTest {

    @Mock //mockito steckt hier eine Implementierung mit Abfagen hinein, reine Pseudoabfragen, gemockte methoden liefern immer null zurück
    private EntityManager em; //gemocked mit mockito

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testMocks(){
        assertNotNull(em);
        assertNotNull(employeeService);
        assertSame(em, employeeService.getEm()); // assert same überprüft ob es das same object ist
        assertNull(em.merge(null));
    }


    private Employee emp;

    @BeforeEach
    public void setUp(){
        emp = new Employee();
    }


    @Test
    public void testSaveWithGradeWithoutID(){
        employeeService.save(emp); //Führen die Funktion im Mockito mock auf

        // überprüfen ob die Funktion em.persist aufgerufen wurde.
        verify(em, times(1)).persist(emp); // persist wurde genau einmal aufgerufen
        verifyNoMoreInteractions(em); // und sonst wurde nichts gemacht

        verify(em, never()).merge(emp); // redundant, überprüft ob merge nicht aufgerufen wurde


    }

    @Test
    public void testSaveWithGradeWithID(){
        emp.setId(1L);

        // gemockte methode liefert immer null zurück, hier sagen wir was die methode returnen soll
        when(em.merge(emp)).thenReturn(emp);

        var empSaved =  employeeService.save(emp); //Führen die Funktion im Mockito mock auf

        assertSame(emp, empSaved); // schaut ob es wirklich die gleiche klasse ist
        verify(em, times(1)).merge(emp); // persist wurde genau einmal aufgerufen
        verifyNoMoreInteractions(em); // und sonst wurde nichts gemacht

    }

}

// tests über maven starten
// set JAVA_HOME="C:\Progra~1\Java\jdk-11.0.2"
// mvn clean test