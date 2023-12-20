package selenide_task2.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    @FindBy(xpath = "//h3/following-sibling::div" +
            "//div[contains(text(), 'Full name')]/following-sibling::div")
    private SelenideElement fullNameInAdditionalInfo;
    @FindBy(css = "div.mdc-card h2")
    private SelenideElement fullNameInAvatarSection;
    private final SelenideElement editingProfile = $("button[title='More options']");
    private final SelenideElement formEdit = $("form#update-item");
    private final SelenideElement inputAvatar = formEdit.$("input[type='file']");
    private final SelenideElement dateOfBirthInfo
            = $x("//h3/following-sibling::div//div[contains(text(), 'Date of birth')]/following-sibling::div");
    private final SelenideElement editDateOfBirth = formEdit.$("input[type='date']");
    private final SelenideElement saveEditedProfileButton = formEdit.$("button[type='submit']");
    private final SelenideElement closeEditedProfileButton = $x("//button[text() = 'close']");

    public String getFullNameFromAdditionalInfo() {
        return fullNameInAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameFromAvatarSection() {
        return fullNameInAvatarSection.shouldBe(visible).text();
    }

    public void clickEditingProfile(){
        editingProfile.should(visible).click();
    }

    public void uploadPictureFileToAvatarField(File file){
        inputAvatar.should(visible).uploadFile(file);
    }

    public String getAvatarInputValue(){
        String inputValue = inputAvatar.should(visible).getValue();
        return Objects.requireNonNull(inputValue).substring(inputValue.lastIndexOf("\\") + 1);
    }

    public void setDateOfBirth(String dateOfBirth) {
        editDateOfBirth.should(Condition.visible).setValue(dateOfBirth);
    }

    public void saveEditedProfile() {
        saveEditedProfileButton.should(Condition.visible).click();
    }

    public void closeEditedProfile() {
        closeEditedProfileButton.should(Condition.visible).click();
    }

    public void checkDateOfBirth(String dateInfo) {
        dateOfBirthInfo.should(Condition.text(dateInfo), Duration.ofSeconds(20));
    }
}
