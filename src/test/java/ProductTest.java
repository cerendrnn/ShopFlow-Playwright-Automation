
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ProductPage;
import pages.SearchResultsPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductTest extends BaseTest {

    SearchResultsPage searchResultsPage;
    ProductPage productPage;

    @Test
    @DisplayName("Thank you message should be displayed after rating thumb is filled")
    void verifyThankYouMessageDisplayed(){

        searchResultsPage= homePage
                .acceptCookies()
                .search("iphone");

        productPage = searchResultsPage.clickRandomProduct();
        String selectedProduct = searchResultsPage.getSelectedProductTitle();
        String productNameOnPage = productPage.getProductTitle();
        assertTrue(productPage.isPageLoaded()," page is not loaded");
        assertEquals(selectedProduct,productNameOnPage, "products are not same");

        productPage
                .clickReviews()
                .clickLatestReviews()
                .clickThumbsUp();

        assertTrue(productPage.isThankYouMessageDisplayed());

    }
    @Test
    @DisplayName("User can add min. price product to the card")
    void verifyMinPriceProductAddedToCard() {

        searchResultsPage= homePage
                    .acceptCookies()
                    .search("iphone");

        productPage = searchResultsPage.clickRandomProduct();
        String productPrice = productPage.getProductPrice();
        assertTrue(productPage.isOtherSellersSectionDisplayed());

        List<String> otherPrices = productPage.getOtherPrices();

        if(otherPrices.isEmpty()){
            productPage.clickAddToCard();
            assertTrue(productPage.isProductAddedToCart());
        }
        else{
            productPage.addMinPriceProductToBasket();
            assertTrue(productPage.isProductAddedToCart());
        }

    }
}