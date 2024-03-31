package ru.ibs.appline.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Первое задание")
public class TrainingApplineSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor executor;
    private final String baseUrl=  "http://training.appline.ru/user/login";
    private final String login = "Irina Filippova";
    private final String password = "testing";

    @BeforeEach
    public void before() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/webdriver/chromedriver121.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().setSize(new Dimension(1920,1080));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15, 1000));

        //Шаг 1. Переход на страницу авторизации
        driver.get(baseUrl);
    }

    @Test()
    @DisplayName("Оформление заявки на командировку")
    @Tag("Test_Selenium_1")
    public void ApplyingForBusinessTrip() {
        //Шаг 2. Авторизация
        waitForElementBeVisible(driver.findElement(By.xpath("//form[@id='login-form']")));
        findElementByXpath("//input[@name='_username']").sendKeys(login);
        findElementByXpath("//input[@name='_password']").sendKeys(password);
        findElementByXpath("//button[normalize-space('Войти')]").click();


        //Шаг 3. Проверить наличие на странице заголовка 'Панель быстрого запуска'
        String titleXpath = "//h1[text()='Панель быстрого запуска']";
        assertTrue(findElementByXpath(titleXpath).isDisplayed(), "На странице отсутствует заголовок 'Панель быстрого запуска'");


        //Шаг 4. В выплывающем окне раздела 'Расходы' нажать на 'Командировки'
        String costsButtonXpath = "//ul[contains(@class,'main-menu')]/li/a/span[@class='title' and text()='Расходы']";
        WebElement costsButton = findElementByXpath(costsButtonXpath);
        costsButton.click();

        String costsListXpath = "./ancestor::li//ul[@class='dropdown-menu menu_level_1']";
        waitForElementBeVisible(costsButton.findElement(By.xpath(costsListXpath)));

        String businessTripXpath = "//span[text()='Командировки']";
        findElementByXpath(businessTripXpath).click();

        loadingWindow();


        //Шаг 5. Нажать на 'Создать командировку'
        String createBusinessTripButtonXpath = "//a[@title='Создать командировку']";
        findElementByXpath(createBusinessTripButtonXpath).click();
        loadingWindow();


        //Шаг 6. Проверить наличие на странице заголовка 'Создать командировку'
        String createBusinessTripTitleXpath = "//div[@class='row']//h1[@class='user-name']";
        findElementByXpath(createBusinessTripTitleXpath);


        //Шаг 7. На странице создания командировки заполнить или выбрать поля:

        //Подразделение - выбрать Отдел внутренней разработки
        String divisionBlockXpath = "//select[contains(@id,'crm_business_trip_businessUnit')]";
        WebElement divisionBlock = driver.findElement(By.xpath(divisionBlockXpath));
        divisionBlock.click();

        String internalDevelopmentDepartmentButtonXpath = "//option[text()='Отдел внутренней разработки']";
        WebElement internalDevelopmentDepartmentButton = findElementByXpath(internalDevelopmentDepartmentButtonXpath);
        internalDevelopmentDepartmentButton.click();

        divisionBlock.click();

        //Принимающая организация - нажать 'Открыть список' и в поле 'Укажите организацию' выбрать любое значение
        String openListButtonXpath = "//a[text()='Открыть список']";
        WebElement openListButton = findElementByXpath(openListButtonXpath);
        openListButton.click();

        String organisationBlockXpath = "//div[contains(@class,'select2-container')]";
        WebElement organisationBlock = findElementByXpath(organisationBlockXpath);
        organisationBlock.click();

        String organisationDropDownListXpath = "//li[contains(@class,'select2-results')]/..";
        WebElement organisationDropDownList = findElementByXpath(organisationDropDownListXpath);

        String organisationNameXpath = "//div[contains(text(),'(Хром) Призрачная Организация Охотников')]";
        organisationDropDownList.findElement(By.xpath(organisationNameXpath)).click();

        //В задачах поставить чекбокс на 'Заказ билетов'
        String taskCheckBoxXpath = "//label[text()='Заказ билетов']//preceding-sibling::input";
        WebElement tasksCheckBox = findElementByXpath(taskCheckBoxXpath);
        tasksCheckBox.click();

        //Указать города выбытия и прибытия
        String departureCityInputFieldXpath = "//input[@data-ftid='crm_business_trip_departureCity']";
        WebElement departureCityInputField = findElementByXpath(departureCityInputFieldXpath);
        departureCityInputField.clear();
        departureCityInputField.sendKeys("Энгельс");

        String arrivalCityInputFieldXpath = "//input[@data-ftid='crm_business_trip_arrivalCity']";
        WebElement arrivalCityInputField = findElementByXpath(arrivalCityInputFieldXpath);
        arrivalCityInputField.sendKeys("Москва");

        //Указать даты выезда и возвращения
        String plannedDepartureDateInputFieldXpath = "//input[contains(@id,'departureDatePlan') and contains(@class,'datepicker-input')]";
        WebElement plannedDepartureDateInputField = findElementByXpath(plannedDepartureDateInputFieldXpath);
        plannedDepartureDateInputField.sendKeys("10.11.2023");

        String plannedReturnDateInputFieldXpath = "//input[contains(@id,'returnDatePlan') and contains(@class,'datepicker-input')]";
        WebElement plannedReturnDateInputField = findElementByXpath(plannedReturnDateInputFieldXpath);
        plannedReturnDateInputField.sendKeys("10.12.2023");

        //Шаг 8. Проверить, что все поля заполнены правильно
        //проверка, что подразделение выбрано
        assertTrue(internalDevelopmentDepartmentButton.getText().contains("Отдел внутренней разработки"),
                "Блок 'Подразделение' не содержит в себе текст 'Отдел внутренней разработки'");

        //проверка, что организация выбрана
        assertTrue(organisationBlock.getText().contains("(Хром) Призрачная Организация Охотников"),
                "Блок 'Укажите организацию' не содержит в себе текст '(Хром) Призрачная Организация Охотников'");

        //проверка, что чекбокс 'Заказ билетов' поставлен
        assertTrue(tasksCheckBox.isSelected(), "Чекбокс 'Заказ билетов' не поставлен");

        //проверка, что поля Выбытия и Прибытия заполнены
        assertTrue(departureCityInputField.getAttribute("value").contains("Энгельс"),
                "Поле 'Город выбытия' не содержит текст 'Энгельс'");
        assertTrue(arrivalCityInputField.getAttribute("value").contains("Москва"),
                "Поле 'Город прибытия' не содержит текст 'Москва'");

        //проверка, что поля Дата выезда и Дата возвращения заполнены
        assertFalse(plannedDepartureDateInputField.getText().contains("datepicker-input  hasDatepicker error"),
                "Поле 'Планируемая дата выезда' не содержит текст");
        assertFalse(plannedReturnDateInputField.getText().contains("datepicker-input  hasDatepicker error"),
                "Поле 'Планируемая дата возвращения' не содержит текст");


        //Шаг 9. Нажать 'Сохранить и закрыть'
        String saveAndCloseButtonXpath = "//button[@class='btn btn-success action-button']";
        WebElement saveAndCloseButton = findElementByXpath(saveAndCloseButtonXpath);
        clickElementByJS(saveAndCloseButton);

        loadingWindow();


        //Шаг 8. Проверить, что на странице появилось сообщение: 'Список командируемых сотрудников не может быть пустым'
        String errorMassageXpath = "//label[text()='Командированные сотрудники']/../..//span[text()='Список командируемых сотрудников не может быть пустым']";
        WebElement errorMassage = findElementByXpath(errorMassageXpath);
        assertTrue(errorMassage.getText().contains("Список командируемых сотрудников не может быть пустым"),
                "На странице не появилось сообщение: 'Список командируемых сотрудников не может быть пустым'");
    }

    @AfterEach
    public void after() {
        driver.quit();
        driver = null;
    }

    private WebElement waitForElementBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    private WebElement findElementByXpath(String xPath) {
        return waitForElementBeVisible(driver.findElement(By.xpath(xPath)));
    }

    private WebElement clickElementByJS(WebElement element) {
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        return element;
    }

    private void loadingWindow() {
        waitForElementBeVisible(driver.findElement(By.xpath("//div[@class='loader-mask shown']")));
    }
}
