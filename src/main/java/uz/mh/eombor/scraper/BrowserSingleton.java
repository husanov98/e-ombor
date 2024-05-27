package uz.mh.eombor.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
@Configuration
public class BrowserSingleton {
    private static BrowserContext browserContext;
    private static Playwright playwright;

    public BrowserSingleton() {}
    public static BrowserContext getBrowserInstance(){
        if (browserContext == null){
            playwright = Playwright.create();
            String userDataDir = "/home/mukhammad/.config/google-chrome";
            BrowserType.LaunchPersistentContextOptions options = new BrowserType
                    .LaunchPersistentContextOptions()
                    .setHeadless(false)
                    .setChannel("chrome")
                    .setExecutablePath(Paths.get("/usr/bin/google-chrome"))
                    .setViewportSize(1920,1080);
            browserContext = playwright.chromium().launchPersistentContext(Paths.get(userDataDir),options);
        }
        return browserContext;
    }
    public static void close(){
        if (browserContext != null) {
            browserContext.close();
            playwright.close();
        }
    }
}
