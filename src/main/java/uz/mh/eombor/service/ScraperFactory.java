package uz.mh.eombor.service;

import com.microsoft.playwright.BrowserContext;
import uz.mh.eombor.scraper.OmborScraper;
import uz.mh.eombor.scraper.Scraper;

public class ScraperFactory {
    public static Scraper getScraper(String type, BrowserContext browserContext){
        if (type.equals("ombor")) {
            return new OmborScraper(browserContext);
        }
        throw new IllegalArgumentException("Unknown scraper type: " + type);
    }
}
