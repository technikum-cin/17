package at.technikumwien;

import lombok.*;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Data                   // Macht Datanbanktabelle draus
@AllArgsConstructor     //Macht Constructor mit allen Werten
@NoArgsConstructor      //Macht Constructor ohne ARgumente
@RequiredArgsConstructor // Kann zu atributen dazuschreiben ob sie null sein dürfen
@Entity                 // braucht einen leeren Konstruktor wie fast alles in diesem Bereich
@Table(name = "t_employee")
@NamedQuery(
        name = "Employee.selectAll",
        query = "SELECT e FROM Employee e ORDER BY e.lastName ASC, e.firstName ASC"
) // JPQL wird zu sql umgewandelt, Datenbankunabhängig. Die Spalten können in der DAtenbank anders heißen.
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull // wirkt nur auf den Setter, nicht auf Konstruktor
    @Column(length = 50, nullable = false)
    @JsonbProperty(value = "first-name", nillable = false)
    private String firstName;

    @NonNull
    @JsonbProperty(value = "last-name", nillable = false)
    @Column(length = 50, nullable = false)
    private String lastName;

    @NonNull
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    @JsonbProperty(nillable = false)
    private Sex sex;

    @NonNull
    @Column(nullable = false)
    @JsonbProperty(value = "birth-name", nillable = false)
    private LocalDate birthDate;

    @NonNull
    @Column(nullable = false)
    @JsonbTransient
    private boolean active;

    // Transient private clock only needed for unit test, can use for clockmanipulation in unit tests
    @Transient
    private Clock clock = Clock.systemDefaultZone();


    @JsonbTransient
    public String getDescription() {
        return getFullName() + " (" + getAgeInYears() + " Jahre, " + (active ? "aktiv" : "inaktiv") + ")";
    }

    @JsonbTransient
    public String getFullName() {
        Objects.requireNonNull(sex, "sex must not be null");
//        Objects.requireNonNull(firstName, "firstName must not be null");
//        Objects.requireNonNull(lastName, "lastName must not be null");

        return (sex == Sex.FEMALE ? "Frau" : "Herr") + " " + firstName + " " + lastName;
    }

    @JsonbTransient
    public int getAgeInYears() {
//        Objects.requireNonNull(birthDate, "birthDate must not be null");

        return Period.between(birthDate, LocalDate.now(clock)).getYears();
    }

}

