package uz.mh.eombor.service;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.mapper.ClientMapper;
import uz.mh.eombor.model.ClientInfo;
import uz.mh.eombor.repository.ClientInfoRepository;

import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class OmborService {
    private final ClientInfoRepository repository;
    private final ClientMapper clientMapper;

    public OmborService(ClientInfoRepository repository, ClientMapper clientMapper) {
        this.repository = repository;
        this.clientMapper = clientMapper;
    }



    public void ishtiQilSuka(List<String> numbers) throws ParseException{
        List<ClientInfoDto> dtos = scrapeOmbor(numbers);
        saveOmbors(dtos);
    }
    private void saveOmbors(List<ClientInfoDto> dtos){
        for (ClientInfoDto dto : dtos) {
            ClientInfo clientInfo = clientMapper.mapDtoToClient(dto);
            repository.save(clientInfo);
        }
    }
    public List<ClientInfoDto> scrapeOmbor(List<String> numbers) throws ParseException{

        try (Playwright playwright = Playwright.create()){
            List<ClientInfoDto> dtos = new ArrayList<>();
            String userDataDir = "/home/mukhammad/.config/google-chrome";
            BrowserType.LaunchPersistentContextOptions options = new BrowserType
                    .LaunchPersistentContextOptions()
                    .setHeadless(false)
                    .setChannel("chrome")
                    .setExecutablePath(Paths.get("/usr/bin/google-chrome"))
                    .setViewportSize(1920,1080);
            BrowserContext context = playwright.chromium().launchPersistentContext(Paths.get(userDataDir),options);
//            Page page1 = context.newPage();
//            page1.navigate("http://127.0.0.1:64646");
            Page page = context.newPage();
            page.navigate("https://e-ombor.customs.uz/");
            page.locator("#lload").click();
            Thread.sleep(1000L);
            page.locator("#every-thing-ok > div:nth-child(3) > a:nth-child(1)").click();
            Thread.sleep(4000L);
            page.locator("#menu > li:nth-child(2) > a > div.menu-title").click();
            Thread.sleep(1000L);
            page.locator("#menu > li.submenu.mm-active.active > ul > li:nth-child(3) > a > div.menu-title").click();
            Thread.sleep(1000L);
            for (String number : numbers) {
                List<Locator> locators = new ArrayList<>();
                Locator transportNumber = page.locator("#transport_raqami");
                Thread.sleep(1000L);
                transportNumber.fill(number);
                Thread.sleep(1000L);
                page.locator("#asosiyKontentUzOmbor > div > div:nth-child(3) > div > div > div > div > div > div.col-1 > button").click();
                Thread.sleep(2000L);
                page.waitForSelector("tbody");
                Thread.sleep(4000L);
                List<ElementHandle> all = page.querySelectorAll("tbody tr");
                System.out.println(all.size());
                for (int i = 1; i <= all.size(); i++) {
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(4)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(5)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(6)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(7)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(8)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(9)"));
                    locators.add(page.locator("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(10)"));
                    System.out.println(i);
                    ClientInfoDto dto = fromLocatorToDto(locators);
                    dtos.add(dto);
                    locators = new ArrayList<>();
                }
                Thread.sleep(5000L);
            }
            page.close();
            context.close();
            return dtos;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientInfoDto fromLocatorToDto(List<Locator> locators) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ClientInfoDto dto = new ClientInfoDto();
        int count = 1;
        System.out.println("------------------------------------");
        for (Locator locator : locators) {
            if (count == 1){
                dto.setTEBNH(locator.innerText());
            }else if (count == 2){
                dto.setTransportNumber(locator.innerText());
            }else if (count == 3){
                String ayirish = ayirish(locator.innerText());
                String pizdes = ayirish.replace("," , ".");
                Locale locale = Locale.FRANCE;
                NumberFormat numberFormat = NumberFormat.getInstance(locale);
                Number number = numberFormat.parse(pizdes);
                Float floatNumber = number.floatValue();
                dto.setBrutto(floatNumber);
            }else if (count == 4){
                dto.setReceiver(locator.innerText());
            }else if (count == 5){
                dto.setStir(locator.innerText());
            }else if (count == 6){
                dto.setPost(Integer.valueOf(locator.innerText()));
            }else if (count == 7 && !locator.innerText().equals("-")){
                dto.setDeliveryDate(new Date(dateFormat.parse(locator.innerText()).getTime()));
            }else if (count == 8){
                dto.setPlaceOfArrival(locator.innerText());
            }else if (count == 9 && locator.innerText().contains("Назоратдан ечилган")){
                String jalap = locator.innerText();
                String[] lines = jalap.split("\\r?\\n");
                if (lines.length >= 2){
                    String secondLine = lines[1];
                    java.util.Date date = dateFormat1.parse(secondLine);
                    Timestamp timestamp = new Timestamp(date.getTime());
                    dto.setSolvedTime(timestamp);
                    dto.setAllowed(true);
                }else {
                    System.out.println("omini yedi");
                }
            }
            count++;
        }
        return dto;
    }
    private String ayirish(String son){
        String newRaqam = "";
        for (int i = 0; i < son.length(); i++) {
            if (son.charAt(i) != 160){
                newRaqam = newRaqam + son.charAt(i);
            }
        }
        return newRaqam;
    }
}
