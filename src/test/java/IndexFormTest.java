import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("e2e") // add tag to check in pom, control when plugin is activated
public class IndexFormTest {

    private WebDriver driver;

    @BeforeAll
    public static void setUpBeforeClass(){
        setSystemPropertyIfUnset("webdriver.chrome.driver", "C:/Users/Lucian/Technikum/CIN/webdrivers/chromedriver.exe"); // pfad zur chromedriver.exe
    }

    @BeforeEach
    public void setUp() throws Exception{
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // soll 3 Minuten warten bevor er abbricht und Fehler wirft
    }

    @AfterEach
    public void tearDown() throws Exception{
        driver.quit(); // schließt browser
    }

    @Test
    public void testShowActiveEmployees(){
        driver.get("http://localhost:8082/employee/index.xhtml");

        var checkboxShowActive = driver.findElement(By.id("form:showactive"));
        var checkboxShowNonActive = driver.findElement(By.id("form:shownonactive"));
        var buttonSubmit = driver.findElement(By.id("form:submit"));
        var hiddenViewState = driver.findElement(By.name("javax.faces.ViewState"));

        var viewStateValue = hiddenViewState.getAttribute("value");
        if (!checkboxShowActive.isSelected()) {
            checkboxShowActive.click();
        }
        if (checkboxShowNonActive.isSelected()) {
            checkboxShowNonActive.click();
        }
        buttonSubmit.submit();

        // waits for view state value to change
        var wait = new WebDriverWait(driver, 3);
        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.textToBePresentInElementValue(
                                By.name("javax.faces.ViewState"),
                                viewStateValue
                        )
                )
        );

        var liEmployeeTexts = driver.findElements(By.cssSelector("ul > li")).stream()
                .map(li -> li.getText())
                .collect(Collectors.toList());

        assertEquals(2, liEmployeeTexts.size());

        // hamcrest - not done in lesson
//        assertThat(liEmployeeTexts, hasItem(containsString("Frau Martina Musterfrau")));
//        assertThat(liEmployeeTexts, hasItem(containsString("Herr Markus Mustermann")));

//        checkboxShowActive.click(); //click aufs Element, hier kann man viel machen, auch TExteingabe simulieren
    }




    //Hilfsmethode die System Property für Pfad setzt wenn nicht gesetzt
    private static void setSystemPropertyIfUnset(String key, String value){
        System.getProperties().putIfAbsent(key, value);

    }
}

// tests über maven starten
// set JAVA_HOME="C:\Progra~1\Java\jdk-11.0.2"
// mvn clean test