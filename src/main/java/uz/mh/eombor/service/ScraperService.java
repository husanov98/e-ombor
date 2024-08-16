package uz.mh.eombor.service;

import com.microsoft.playwright.BrowserContext;
import org.springframework.stereotype.Service;
import uz.mh.eombor.dto.ClientInfoDto;
import uz.mh.eombor.mapper.ClientMapper;
import uz.mh.eombor.model.ClientInfo;
import uz.mh.eombor.model.NewClientInfo;
import uz.mh.eombor.model.OldStir;
import uz.mh.eombor.repository.ClientInfoRepository;
import uz.mh.eombor.repository.NewClientInfoRepository;
import uz.mh.eombor.repository.OldStirRepository;
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
    private final NewClientInfoRepository newClientInfoRepository;
    private final OldStirRepository oldStirRepository;

    public ScraperService(ClientInfoRepository repository,
                          ClientMapper clientMapper,
                          SheetService sheetService,
                          NewClientInfoRepository newClientInfoRepository,
                          OldStirRepository oldStirRepository) {
        this.repository = repository;
        this.clientMapper = clientMapper;
        this.sheetService = sheetService;
        this.newClientInfoRepository = newClientInfoRepository;
        this.oldStirRepository = oldStirRepository;
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
            boolean exists = oldStirRepository.isExists(clientInfo.getStir());
            if (!exists){
                NewClientInfo newClientInfo = mapFromClientInfoToNewClientInfo(clientInfo);
                newClientInfoRepository.save(newClientInfo);
                oldStirRepository.save(new OldStir(newClientInfo.getStir()));
            }
        }
    }
    private NewClientInfo mapFromClientInfoToNewClientInfo(ClientInfo clientInfo){
        return new NewClientInfo(
                clientInfo.getTEBNH(),
                clientInfo.getTransportNumber(),
                clientInfo.getBrutto(),
                clientInfo.getReceiver(),
                clientInfo.getStir(),
                clientInfo.getPost(),
                clientInfo.getDeliveryDate(),
                clientInfo.getPlaceOfArrival(),
                clientInfo.isAllowed(),
                clientInfo.getTransportType(),
                clientInfo.getSolvedTime());
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
