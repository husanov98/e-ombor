package uz.mh.eombor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mh.eombor.model.ClientInfo;
import uz.mh.eombor.model.OldStir;
import uz.mh.eombor.repository.ClientInfoRepository;
import uz.mh.eombor.repository.OldStirRepository;

import java.io.IOException;
import java.util.List;

@Service
public class AddOldStirsService {
    private final OldStirRepository repository;
    private final AddOldStirsSheetService addOldStirsSheetService;

    public AddOldStirsService(OldStirRepository repository, AddOldStirsSheetService addOldStirsSheetService) {
        this.repository = repository;
        this.addOldStirsSheetService = addOldStirsSheetService;
    }
    public void getDiffData(MultipartFile file) throws IOException {
        List<String> stirs = addOldStirsSheetService.readStirs(file);
        for (String stir : stirs) {
            boolean isExists = repository.isExists(stir);
            if (!isExists){
                OldStir oldStir = new OldStir(stir);
                repository.save(oldStir);
            }else {
                System.out.println("already added");
            }
        }
    }
}
