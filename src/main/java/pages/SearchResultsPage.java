package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.Random;

public class SearchResultsPage extends BasePage {

    private String selectedProductTitle;

    public SearchResultsPage(Page page) {
        super(page);
    }

    public String getSelectedProductTitle(){

        return selectedProductTitle;
    }

    public ProductPage clickRandomProduct() {
        int previousCount = 0;
        while (true) {
            Locator productCards = page.locator("article[class^='productCard-module_article']");
            int currentCount = productCards.count();
            if (currentCount == previousCount) break;

            productCards.nth(currentCount - 1).scrollIntoViewIfNeeded();
            page.waitForTimeout(500);
            previousCount = currentCount;
        }

        Locator productCards = page.locator("article[class^='productCard-module_article']");
        int count = productCards.count();
        if (count == 0) throw new RuntimeException("No product cards found!");

        int index = new Random().nextInt(count);
        Locator randomCard = productCards.nth(index);

        int retries = 5;
        for (int i = 0; i < retries; i++) {
            try {
                randomCard.scrollIntoViewIfNeeded();
                randomCard.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(1000));
                break;
            } catch (PlaywrightException e) {
                page.waitForTimeout(500);
            }
        }
        Locator titleLocator = randomCard.locator("a.productCardLink-module_productCardLink__GZ3eU[title]").first();
        selectedProductTitle = titleLocator.getAttribute("title").trim();
        System.out.println("Tıklanan ürün: " + selectedProductTitle);

        Page newPage = page.waitForPopup(randomCard::click);

        newPage.waitForLoadState();

        return new ProductPage(newPage);
    }
}
