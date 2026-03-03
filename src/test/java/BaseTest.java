
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pages.HomePage;

public class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;

    protected String homePageUrl = "https://www.hepsiburada.com/";
    protected HomePage homePage;

    @BeforeAll
    static void globalSetup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setChannel("chrome")
                        .setSlowMo(50)
        );
    }

    @AfterAll
    static void globalTearDown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        page = browser.newPage();
        page.navigate(homePageUrl);
        homePage = new HomePage(page);
    }

    @AfterEach
    void tearDown() {
        page.close();
    }
}