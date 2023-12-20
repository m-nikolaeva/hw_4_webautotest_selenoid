package selenide_task2;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import selenide_task2.pages.LoginPage;
import selenide_task2.pages.MainPage;
import selenide_task2.pages.ProfilePage;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStandGeekBrains {
    private static final String USERNAME = "Student-9";
    private static final String PASSWORD = "425c57255c";
    private static final String FULLNAME = "9 Student";
    private static final String urlBase = "https://test-stand.gb.ru/login";
    private static final String dateBirth = "15.07.1999";


    @BeforeEach
    void setupTest() {
        Selenide.open(urlBase);
    }

    @Test
    public void testLoginPasswordEmpty(){
        LoginPage loginPage = Selenide.page(LoginPage.class);
        loginPage.clickLoginButton();
        assertEquals("401\nInvalid credentials.",loginPage.getErrorText());
    }

    @Test
    public void testChangeNumberStudentsGroup(){
        login();
        MainPage mainPage = Selenide.page(MainPage.class);
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
        MainPage mainPage = Selenide.page(MainPage.class);
        String groupTestName = "NEWGROUP" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
    }

    @Test
    public void testArchiveGroupOnMainPage(){
        login();
        MainPage mainPage = Selenide.page(MainPage.class);
        String groupTestName = "NEWGROUP" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
    }

    @Test
    public void testFullNameOnProfilePage() {
        login();
        MainPage mainPage = Selenide.page(MainPage.class);
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        assertEquals(FULLNAME, profilePage.getFullNameFromAdditionalInfo());
        assertEquals(FULLNAME, profilePage.getFullNameFromAvatarSection());
    }

    @Test
    public void testAvatarOnEditingPopupOnProfilePage(){
        login();
        MainPage mainPage = Selenide.page(MainPage.class);
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.clickEditingProfile();
        assertEquals("", profilePage.getAvatarInputValue());
        profilePage.uploadPictureFileToAvatarField(new File("src/test/resources/Avatar.PNG"));
        assertEquals("Avatar.PNG", profilePage.getAvatarInputValue());
    }

    @Test
    public void checkAddBirthdate() {
        login();
        MainPage mainPage = Selenide.page(MainPage.class);
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.clickEditingProfile();
        profilePage.setDateOfBirth(dateBirth);
        profilePage.saveEditedProfile();
        profilePage.closeEditedProfile();
        profilePage.checkDateOfBirth(dateBirth);
    }

    private void login() {
        LoginPage loginPage = Selenide.page(LoginPage.class);
        loginPage.login(USERNAME, PASSWORD);
        MainPage mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    @AfterEach
    public void closeTest() {
        WebDriverRunner.closeWebDriver();
    }
}
