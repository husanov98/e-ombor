package uz.mh.eombor.scraper;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class PageBuilder {
    private Page page;
    public PageBuilder(BrowserContext browserContext) {
        this.page = browserContext.newPage();
    }

    public PageBuilder navigate(String url) {
        page.navigate(url);
        return this;
    }

    public PageBuilder click(String selector) {
        page.click(selector);
        return this;
    }

    public PageBuilder type(String selector, String text) {
        page.fill(selector, text);
        return this;
    }

    public PageBuilder waitForSelector(String selector) {
        page.waitForSelector(selector);
        return this;
    }

    public Page build() {
        return page;
    }
}
