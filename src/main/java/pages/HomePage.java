package pages;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;


public class HomePage extends BasePage{

    private final Locator cookieConsentButton;

    public HomePage(Page page) {
        super(page);
        cookieConsentButton = page.locator("#onetrust-accept-btn-handler");
    }
    public HomePage acceptCookies() {
        cookieConsentButton.click();
        return this;
    }

    public SearchResultsPage search(String text) {

        Locator searchBar = page.locator("[data-test-id='search-bar-input']");

        searchBar.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        for (int i = 0; i < 3; i++) {
            searchBar.fill("");
            page.keyboard().type(text, new Keyboard.TypeOptions().setDelay(50));

            String currentValue = searchBar.inputValue();
            if (text.equals(currentValue)) {
                break;
            }
            page.waitForTimeout(100);
        }

        searchBar.press("Enter");
        return new SearchResultsPage(page);
    }
}
