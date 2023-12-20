package selenoid_task1;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenoid_task1.pages.LoginPage;
import selenoid_task1.pages.MainPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStandGeekBrains {
    private static WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;
    private static final String USERNAME = "Student-9";
    private static final String PASSWORD = "425c57255c";
    private static final String urlBase = "https://test-stand.gb.ru/login";


    @BeforeAll
    public static void initSelenoid() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("119");
        Map<String, Object> options = new HashMap<>();
        options.put("enableVnc", true);
        options.put("enableLog", true);
        capabilities.setCapability("selenoid:options", options);
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);

    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
        driver.get(urlBase);
        loginPage = new LoginPage(driver, wait);
    }

    // Авторизация c пустыми полями: логин и пароль
    @Test
    public void testLoginPasswordEmpty(){
        loginPage.clickLoginButton();
        assertEquals("401\nInvalid credentials.",loginPage.getErrorText());
    }

    // Манипуляции со студентами в группе
    @Test
    public void testChangeNumberStudentsGroup(){
        login();
        String groupTestName = "NEWGROUP" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        int studentCount = 3;
        mainPage.clickAddStudentIconOnGroupWithTitle(groupTestName);
        mainPage.typeAmountOfStudentsInForm(studentCount);
        mainPage.clickSaveBtnOnCreateStudentsForm();
        mainPage.closeCreateStudentsModalWindow();
        mainPage.waitStudentCount(groupTestName, studentCount);
        mainPage.clickZoomIconOnGroupWithTitle(groupTestName);
        String firstStudent = mainPage.getNameByIndex(0);
        assertEquals("active", mainPage.getStatusOfStudent(firstStudent));
        mainPage.clickTrashIconOnStudentWithName(firstStudent);
        assertEquals("block", mainPage.getStatusOfStudent(firstStudent));
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstStudent);
        assertEquals("active", mainPage.getStatusOfStudent(firstStudent));
    }

    @Test
    public void testAddingGroupOnMainPage() {
        login();
        String groupTestName = "NEWGROUP" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
    }

    @Test
    public void testArchiveGroupOnMainPage(){
        login();
        String groupTestName = "NEWGROUP" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
    }

    private void login() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    @AfterEach
    public void closeTest() {
        driver.quit();
    }
}
