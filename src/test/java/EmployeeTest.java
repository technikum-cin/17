import at.technikumwien.Employee;
import at.technikumwien.Sex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    private Employee emp;
    private Employee empFemale;
    private Employee empMale;


    @BeforeEach
    public void setUp() throws Exception{
        empFemale = new Employee("Martina", "Musterfrau", Sex.FEMALE, LocalDate.of(1991, 1, 1), true);
        empMale = new Employee("Markus", "Mustermann", Sex.MALE, LocalDate.of(1991, 1, 1), false);
        emp = new Employee();

        empFemale.setClock(getFixedClockWithDate(2009,12,31));
        empMale.setClock(getFixedClockWithDate(2010,1,1));

    }

//    @AfterEach
//    public void tearDown(){
//
//
//    }


    @Test
    public void testGetFullNameFemale(){

        assertEquals("Frau Martina Musterfrau", empFemale.getFullName());
    }

    @Test
    public void testGetFullNameMale(){

        assertEquals("Herr Markus Mustermann", empMale.getFullName());
    }

    @Test
    public void testGetFullNameSexNull(){

        assertThrows(NullPointerException.class, () -> emp.getFullName());
    }

    @Test
    public void testGetAgeInYearsWithYesterday(){

        assertEquals(18, empFemale.getAgeInYears());
    }

    @Test
    public void testGetAgeInYearsWithSameDay(){

        assertEquals(19, empMale.getAgeInYears());
    }

    @Test
    public void testGetAgeInYearsWithBirthDateNull() {
        assertThrows(NullPointerException.class, () -> emp.getAgeInYears());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Frau Martina Musterfrau (18 Jahre, aktiv)", empFemale.getDescription());
        assertEquals("Herr Markus Mustermann (19 Jahre, inaktiv)", empMale.getDescription());
    }


    // Hilfsmethode fixed clock die wir bekommen haben um die Zeit besser testen zu können, clock manipulieren
    private Clock getFixedClockWithDate(int year, int month, int dayOfMonth) {
        return Clock.fixed(
                LocalDateTime.of(year, month, dayOfMonth, 0, 0).toInstant(ZoneOffset.ofHours(0)),
                ZoneId.systemDefault()
        );
    }

}

// tests über maven starten
// set JAVA_HOME="C:\Progra~1\Java\jdk-11.0.2"
// mvn clean test