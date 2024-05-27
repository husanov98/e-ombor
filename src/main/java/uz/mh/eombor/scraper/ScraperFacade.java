package uz.mh.eombor.scraper;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class ScraperFacade {
    private Page page;
    public ScraperFacade(BrowserContext browserContext){
        this.page = browserContext.newPage();
    }
    public void navigateTo(String url){
        page.navigate(url);
    }
    public String extractText(String selector){
        return page.locator(selector).innerText();
    }
    public void clickButton(String selector){
        page.locator(selector).click();
    }
    public void fill(Locator locator, String text){
        locator.fill(text);
    }
    public Locator getLocator(String selector){
        return page.locator(selector);
    }
    public void waitForSelector(String selector){
        page.waitForSelector(selector);
    }
    public List<ElementHandle> querySelectorAll(String selector){
        return page.querySelectorAll(selector);
    }
    public void close(){
        page.close();
    }
}
