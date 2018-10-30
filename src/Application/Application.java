package Application;

import Application.Scraper.Scraper;

public class Application {

    public static void main(String[] args) {
        Scraper scraper = new Scraper();
        scraper.startScraping();
    }
}
