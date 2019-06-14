package at.technikumwien;

import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Stateless
public class EmployeeService {

    @Getter @Setter // Für Unit Tests, damit wir setter nutzen können und simulieren
    @PersistenceContext // wird auf wildfly erstellt
    private EntityManager em;

    public long count(){
        return findAll().size();
    }

    public List<Employee> findAll(){
        return em.createNamedQuery("Employee.selectAll", Employee.class).getResultList();
    }

    public void deleteById(long id){
        // muss ein Object (optional) übergeben werden || wirft eine Exception wenn nicht vorhanden
        var employee = findById(id).orElseThrow();
        em.remove(employee);
    }

    public List<Employee> findAll(Predicate<Employee> predicate){
        // simple if not null
        Objects.requireNonNull(predicate, "predicate must not be null");

        return findAll().stream().filter(predicate).collect(Collectors.toList());
    }

    public Optional<Employee> findById(long id){
        // Optional returned immer object, entwedet objekt oder optional
        return Optional.ofNullable(em.find(Employee.class, id)); // Optional da es nichti n Datenbank existieren muss

    }

    public Employee save(Employee employee){

        if(employee.getId() == null){
            em.persist(employee);
            return employee;
        } else {
            return em.merge(employee);
        }
    }

    public List<Employee> saveAll(List<Employee> employees){
        // stream makes if possible to use map as foreach this::save = employee -> save(employee)
        return employees.stream().map(this::save).collect(Collectors.toList());
    }
}
