package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BasePage {

    Locator accessoriesCategory = page.locator("span:has-text('Telefonlar & Aksesuarlar')");
    Locator phoneCategory = page.locator("span:has-text('Cep Telefonları')");
    Locator reviewsTab = page.locator("button:has-text('Değerlendirmeler')");
    Locator defaultSort = page.locator("div[class*='hermes-Sort-module']:has-text('Varsayılan')").first();
    Locator newestReview = page.locator("div[class*='hermes-Sort-module']:has-text('En yeni değerlendirme')").first();
    Locator thumbsUp = page.locator("div.thumbsUp.hermes-ReviewCard-module-lOsa4wAwncdp3GgzpaaV").first();
    Locator thankYouMessage = page.locator("span[class*='hermes-ReviewCard-module']:has-text('Teşekkür Ederiz.')").first();
    Locator productPrice = page.locator("div[data-test-id='default-price'] span");
    Locator addToCartButton = page.locator("button[data-test-id='addToCart']:has-text('Sepete ekle')");
    Locator goToCartButton = page.locator("button:has-text('Sepete git')");

    public ProductPage(Page page) {
        super(page);
    }

    public boolean isPageLoaded() {
        accessoriesCategory.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        phoneCategory.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return accessoriesCategory.isVisible() && phoneCategory.isVisible();
    }

    public String getProductTitle() {
        Locator h1 = page.locator("h1").first();
        waitForVisible(h1, 10);
        System.out.println(h1.textContent().trim());
        return h1.textContent().trim();
    }

    public boolean isOtherSellersSectionDisplayed() {
         Locator otherSellers = page.locator("div >> span:has-text('Diğer satıcılar')");
         try {
             otherSellers.waitFor(new Locator.WaitForOptions()
                     .setTimeout(15000)
                     .setState(WaitForSelectorState.VISIBLE));
             return true;
         } catch (TimeoutError e) {
             return false;
         }
     }

     public ProductPage clickReviews() {
         reviewsTab.scrollIntoViewIfNeeded();
         reviewsTab.click();
         return this;
     }

     public ProductPage clickLatestReviews(){
         defaultSort.scrollIntoViewIfNeeded();
         defaultSort.click();
         newestReview.scrollIntoViewIfNeeded();
         newestReview.click();
         return this;
     }

    public ProductPage clickThumbsUp(){
        thumbsUp.scrollIntoViewIfNeeded();
        thumbsUp.click();
        return this;
    }

    public boolean isThankYouMessageDisplayed(){
        thankYouMessage.scrollIntoViewIfNeeded();
        return thankYouMessage.isVisible();
    }

    public String getProductPrice(){
        String priceText = productPrice.textContent();
        System.out.println("Ürün fiyatı: " + priceText);
        return priceText;
    }

    public ProductPage clickAddToCard(){
        addToCartButton.scrollIntoViewIfNeeded();
        addToCartButton.click();
        return this;
    }

    public List<String> getOtherPrices() {
        List<String> prices = new ArrayList<>();
        Locator priceElements = page.locator("div[data-test-id='price-current-price']");

        int count = priceElements.count();
        if (count == 0) {
            return prices;
        }

        for (int i = 0; i < count; i++) {
            String priceText = priceElements.nth(i).textContent().trim();
            prices.add(priceText);
        }
        return prices;
    }

    public ProductPage addMinPriceProductToBasket(){

         int minIndex = getMinPriceIndex(getOtherPrices());
         Locator goToProductButton = page.locator("button:has-text('Ürüne git')").nth(minIndex);
         goToProductButton.scrollIntoViewIfNeeded();
         goToProductButton.click();
         addToCartButton.scrollIntoViewIfNeeded();
         addToCartButton.click();
         return this;
    }

    public boolean isProductAddedToCart() {
        try {
            goToCartButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            return true;
        } catch (PlaywrightException e) {
            return false;
        }
    }

    public static int getMinPriceIndex(List<String> prices) {
        if (prices == null || prices.isEmpty()) {
            return -1;
        }

        double minPrice = Double.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < prices.size(); i++) {
            String priceStr = prices.get(i)
                    .replace(".", "")
                    .replace(",", ".")
                    .replaceAll("[^0-9.]", "");

            double price = Double.parseDouble(priceStr);

            if (price < minPrice) {
                minPrice = price;
                minIndex = i;
            }
        }

        return minIndex;
    }
}