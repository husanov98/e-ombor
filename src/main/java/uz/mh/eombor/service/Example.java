package uz.mh.eombor.service;

import com.microsoft.playwright.BrowserContext;
import uz.mh.eombor.scraper.BrowserSingleton;
import uz.mh.eombor.scraper.Scraper;

import java.util.Arrays;
import java.util.List;

public class Example {
    public static void main(String[] args) throws Exception{
        List<String> list = Arrays.asList("01499ZFA","01500NGA");
        BrowserContext browserContext = BrowserSingleton.getBrowserInstance();
        Scraper ombor = ScraperFactory.getScraper("ombor", browserContext);
        ombor.scrape(list);
    }
}
