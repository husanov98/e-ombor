package uz.mh.eombor.scraper;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.dto.SelectorDto;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OmborScraper extends Scraper {
    public OmborScraper(BrowserContext browserContext) {
        super(browserContext);
    }

    @Override
    public List<ClientInfoDto> scrape(List<String> numbers) throws Exception {
        int am = 1;
        List<ClientInfoDto> dtoList = new ArrayList<>();
        ScraperFacade facade = new ScraperFacade(browserContext);

            facade.navigateTo("https://e-ombor.customs.uz/");
            facade.clickButton("#lload");
            Thread.sleep(1000L);
            facade.clickButton("#every-thing-ok > div:nth-child(3) > a:nth-child(1)");
            Thread.sleep(4000L);
            facade.clickButton("#menu > li:nth-child(2) > a > div.menu-title");
            Thread.sleep(1000L);
            facade.clickButton("#menu > li.submenu.mm-active.active > ul > li:nth-child(3) > a > div.menu-title");
            Thread.sleep(1000L);
            for (String number : numbers) {
                try {
                    System.out.println("=====================================================================");
                    System.out.println(number);
                    Locator locator = facade.getLocator("#transport_raqami");
                    Thread.sleep(1000L);
                    facade.fill(locator, number);
                    Thread.sleep(1000L);
                    facade.clickButton("#asosiyKontentUzOmbor > div > div:nth-child(3) > div > div > div > div > div > div.col-1 > button");
                    Thread.sleep(2000L);
                    facade.waitForSelector("tbody");
                    Thread.sleep(4000L);
                    List<ElementHandle> all = facade.querySelectorAll("tbody tr");
                    List<SelectorDto> selectorDtos = setDto(all, facade);
                    for (SelectorDto selectorDto : selectorDtos) {
                        ClientInfoDto dto = new ClientInfoDto();
                        try {
                            dto = fromLocator(selectorDto, facade);
                        } catch (Exception e) {
                            dto.setTransportNumber(number);
                            System.out.println("Ma'lumot topilmadi");
                        }
                        dtoList.add(dto);
                        System.out.println("Clients count = " + am);
                        am++;
                    }
//            selectorDtoList.addAll(selectorDtos);
                }catch (Exception e){
                    System.out.println("Web page is not responding");
                }
            }
//        for (SelectorDto dto : selectorDtoList) {
//            ClientInfoDto clientInfoDto = fromLocator(dto,facade);
//            dtoList.add(clientInfoDto);
//            System.out.println(clientInfoDto.getStir());
//        }

        return dtoList;
    }
    private ClientInfoDto fromLocator(SelectorDto selectorDto,ScraperFacade facade) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ClientInfoDto dto = new ClientInfoDto();
        System.out.println("------------------------------------");
        for (int i = 2; i <= 10; i++) {
            if (i == 2){
                dto.setTEBNH(facade.extractText(selectorDto.getSelector2()));
            }else if (i == 3){
                dto.setTransportNumber(facade.extractText(selectorDto.getSelector3()));
            }else if (i == 4){
                String ayirish = ayirish(facade.extractText(selectorDto.getSelector4()));
                String pizdes = ayirish.replace("," , ".");
                Locale locale = Locale.FRANCE;
                NumberFormat numberFormat = NumberFormat.getInstance(locale);
                Number number = numberFormat.parse(pizdes);
                Float floatNumber = number.floatValue();
                dto.setBrutto(floatNumber);
            }else if (i == 5){
                dto.setReceiver(facade.extractText(selectorDto.getSelector5()));
            }else if (i == 6){
                dto.setStir(facade.extractText(selectorDto.getSelector6()));
            }else if (i == 7){
                dto.setPost(Integer.valueOf(facade.extractText(selectorDto.getSelector7())));
            }else if (i == 8 && !facade.extractText(selectorDto.getSelector8()).equals("-")){
                dto.setDeliveryDate(new Date(dateFormat.parse(facade.extractText(selectorDto.getSelector8())).getTime()));
            }else if (i == 9){
                dto.setPlaceOfArrival(facade.extractText(selectorDto.getSelector9()));
            }else if (i == 10 && facade.extractText(selectorDto.getSelector10()).contains("Назоратдан ечилган")){
                String jalap = facade.extractText(selectorDto.getSelector10());
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
        }
        return dto;
    }
    private List<SelectorDto> setDto(List<ElementHandle> allElements,ScraperFacade facade){
        List<SelectorDto> dtos = new ArrayList<>();
        for (int i = 1; i <= allElements.size(); i++) {
            SelectorDto dto = new SelectorDto();
            dto.setOrder(i);
            dto.setSelector2("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)");
            dto.setSelector3("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(3)");
            dto.setSelector4("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(4)");
            dto.setSelector5("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(5)");
            dto.setSelector6("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(6)");
            dto.setSelector7("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(7)");
            dto.setSelector8("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(8)");
            dto.setSelector9("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(9)");
            dto.setSelector10("#win > table > tbody > tr:nth-child(" + i + ") > td:nth-child(10)");
            dtos.add(dto);
        }
        return dtos;
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
