package uz.mh.eombor.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mh.eombor.service.OrgInfoService;
import uz.mh.eombor.service.ScraperService;
import uz.mh.eombor.service.SheetService;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class OmborController {
    private final ScraperService scraperService;
    private final SheetService sheetService;
    private final OrgInfoService orgInfoService;

    public OmborController(ScraperService scraperService,
                           SheetService sheetService,
                           OrgInfoService orgInfoService) {
        this.scraperService = scraperService;
        this.sheetService = sheetService;
        this.orgInfoService = orgInfoService;
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
    @PostMapping(value = "/getOrgInfo")
    public void getOrgInfo(@RequestParam(name = "startId") Long startId, @RequestParam(name = "endId") Long endId) throws IOException, InterruptedException {
        long begin = System.currentTimeMillis();
        orgInfoService.getOrgInfoData(startId, endId);
        long end = System.currentTimeMillis();
        long time = ((end - begin)/1000)/60;
        System.out.println(time + " minut vaqt ketdi");
    }
}
