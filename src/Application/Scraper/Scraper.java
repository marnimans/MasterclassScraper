package Application.Scraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;

import java.util.List;

public class Scraper {

    public static WebClient client = new WebClient();

    public void startScraping() {
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://www.megekko.nl/sitemap";
            HtmlPage page = client.getPage(searchUrl);
            List<HtmlElement> items = page.getByXPath("//a[@class='sitemap_prodvan']"); //Sitemap links
            if (items.isEmpty()) {
                System.out.println("No items found !");
            } else {
                for (HtmlElement subClass : items) {
                    scrapeSubclass(subClass);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    public void scrapeSubclass(HtmlElement subClass) throws Exception {
        String subclassUrl = subClass.getAttribute("href");  //Subclass links
        HtmlPage subPage = client.getPage("https://www.megekko.nl" + subclassUrl);
        List<HtmlElement> subItems = subPage.getByXPath("//main/div[@class='teksten']/ul/li/a"); //subItem links
        if (subItems.isEmpty()) {
            System.out.println("No Subitems found !");
        } else {
            for (HtmlElement product : subItems) {
                scrapeProductPage(product);
            }
        }
    }

    public void scrapeProductPage(HtmlElement product) throws Exception {
        String productUrl = product.getAttribute("href");
        HtmlPage productPage = client.getPage("https://www.megekko.nl" + productUrl);
        getData(productPage);
    }

    public void getData(HtmlPage productPage) {
        Product product = new Product();

        HtmlElement price = ((HtmlElement) productPage.getFirstByXPath("//div[@class='pricer large']/div[@class='pricecontainer']"));
        String itemPrice = price == null ? "no price" : price.asText();
        itemPrice = removeHtmlTags(itemPrice);
        product.setProductPrice(itemPrice);

        HtmlElement name = ((HtmlElement) productPage.getFirstByXPath("//a/h1[@class='title']"));
        String itemName = name == null ? "no name" : name.asText();
        itemName = removeHtmlTags(itemName);
        product.setProductName(itemName);

        HtmlElement description = ((HtmlElement) productPage.getFirstByXPath("//div[@id='proddesccontainer']"));
        String itemDescription = description == null ? "no description" : description.asText();
        itemDescription = removeHtmlTags(itemDescription);
        product.setProductDescription(itemDescription);

        generateJsonObject(product);
    }

    public void generateJsonObject(Product product) {
        Gson g = new Gson();
        String JsonProduct = g.toJson(product);
        System.out.println(JsonProduct);
    }

    public String removeHtmlTags(String str) {
        return str.replaceAll("\\r\\n", "");
    }
}
