package uz.mh.eombor.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.mh.eombor.service.AddOldStirsService;

@RestController
public class AddOldStirsController {
    private final AddOldStirsService addOldStirsService;

    public AddOldStirsController(AddOldStirsService addOldStirsService) {
        this.addOldStirsService = addOldStirsService;
    }

    @PostMapping(value = "/getDiffData",consumes = {"multipart/form-data"})
    public void getDiffData(@RequestPart(name = "stirs") MultipartFile stirs) throws Exception{
        addOldStirsService.getDiffData(stirs);
    }
}
