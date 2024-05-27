package uz.mh.eombor.controller;


import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mh.eombor.service.ScraperService;
import uz.mh.eombor.service.SheetService;


import java.io.IOException;
import java.util.List;

@RestController
public class OmborController {
    private final ScraperService scraperService;
    private final SheetService sheetService;

    public OmborController(ScraperService scraperService, SheetService sheetService) {
        this.scraperService = scraperService;
        this.sheetService = sheetService;
    }

    @PostMapping(value = "/getOmbor",consumes = {"multipart/form-data"})
    public void getOmbor(@RequestPart(name = "moshinNomer") MultipartFile moshinNomer) throws Exception {
        long begin = System.currentTimeMillis();
        
        List<String> numbers = sheetService.readNumbers(moshinNomer);
        scraperService.scrapeOmbor(numbers);
        long end = System.currentTimeMillis();
        long time = ((end - begin)/1000)/60;
        System.out.println(time + " minut vaqt ketdi");
    }
    @PostMapping(value = "getExcel",consumes = {"multipart/form-data"})
    public void get(@RequestPart(name = "excel") String fileName) throws IOException{
        scraperService.getExcel(fileName);
    }
}
