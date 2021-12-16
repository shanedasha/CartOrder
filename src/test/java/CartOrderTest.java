import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartOrderTest {

    WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSetForm() {
        driver.findElement(By.cssSelector("[ data-test-id='name'] input")).sendKeys("Вахрушева Анна-Мария");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79693845678");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetFormWithoutName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79693845678");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetFormWithoutPhone() {
        driver.findElement(By.cssSelector("[ data-test-id='name'] input")).sendKeys("Вахрушев Василий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetFormWithoutCheck() {
        driver.findElement(By.cssSelector("[ data-test-id='name'] input")).sendKeys("Вахрушев Василий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79693845678");
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetEmptyForm() {
        driver.findElement(By.cssSelector("[ data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getText().trim();
        String expected = "Фамилия и имя\n" + "Поле обязательно для заполнения";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetFormWithWrongName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petrov Andrew");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79693845678");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expected, text);
    }

    @Test
    public void shouldSetFormWithWrongPhone() {
        driver.findElement(By.cssSelector("[ data-test-id='name'] input")).sendKeys("Вахрушев Василий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("8945642304");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, text);
    }
}
