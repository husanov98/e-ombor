package uz.mh.eombor.service;

import com.microsoft.playwright.BrowserContext;
import org.springframework.stereotype.Service;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.mapper.ClientMapper;
import uz.mh.eombor.model.ClientInfo;
import uz.mh.eombor.repository.ClientInfoRepository;
import uz.mh.eombor.scraper.BrowserSingleton;
import uz.mh.eombor.scraper.Scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {
    private final ClientInfoRepository repository;
    private final ClientMapper clientMapper;
    private final SheetService sheetService;

    public ScraperService(ClientInfoRepository repository, ClientMapper clientMapper, SheetService sheetService) {
        this.repository = repository;
        this.clientMapper = clientMapper;
        this.sheetService = sheetService;
    }

    public void scrapeOmbor(List<String> numbers)throws Exception{
        BrowserContext browserContext = BrowserSingleton.getBrowserInstance();
        Scraper ombor = ScraperFactory.getScraper("ombor", browserContext);
        List<ClientInfoDto> clientInfoDtos = ombor.scrape(numbers);
        saveOmbors(clientInfoDtos);
        BrowserSingleton.close();
    }
    private void saveOmbors(List<ClientInfoDto> dtoList){
        for (ClientInfoDto dto : dtoList) {
            ClientInfo clientInfo = clientMapper.mapDtoToClient(dto);
            repository.save(clientInfo);
        }
    }

    public void getExcel(String fileName) throws IOException {
        List<ClientInfoDto> dtoList = new ArrayList<>();
        List<ClientInfo> all = repository.findAll();
        for (ClientInfo clientInfo : all) {
            ClientInfoDto dto = clientMapper.mapToClientInfoDto(clientInfo);
            dtoList.add(dto);
        }
        sheetService.generateExcel(fileName,dtoList);
    }
}
