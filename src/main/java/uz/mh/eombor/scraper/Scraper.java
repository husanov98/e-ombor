package uz.mh.eombor.scraper;

import com.microsoft.playwright.BrowserContext;
import uz.mh.eombor.dto.ClientInfoDto;

import java.util.List;

public abstract class Scraper {
    protected BrowserContext browserContext;
    public Scraper(BrowserContext browserContext){
        this.browserContext = browserContext;
    }
    public abstract List<ClientInfoDto> scrape(List<String> numbers) throws Exception;

}
