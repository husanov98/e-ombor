package uz.mh.eombor.service;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;
import uz.mh.eombor.model.OldStir;
import uz.mh.eombor.model.OrgInfoData;
import uz.mh.eombor.repository.OldStirRepository;
import uz.mh.eombor.repository.OrgInfoDataRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrgInfoService {
    private final OrgInfoDataRepository dataRepository;
    private final OldStirRepository oldStirRepository;

    public OrgInfoService(OrgInfoDataRepository dataRepository,
                          OldStirRepository oldStirRepository) {
        this.dataRepository = dataRepository;
        this.oldStirRepository = oldStirRepository;
    }

    public void getOrgInfoData(Long startId,Long endId) throws IOException, InterruptedException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        List<OldStir> stirFromExcel = getStirFromExcel(startId, endId);
        System.out.println(stirFromExcel.size());
        for (OldStir oldStir : stirFromExcel) {
            Thread.sleep(5000L);
            scrapeOrgInfo(oldStir,webClient);
        }
    }

    private void scrapeOrgInfo(OldStir oldStir,WebClient webClient) throws IOException {

        String url = "https://orginfo.uz/uz/search/all/?q=" + oldStir.getStir();
        HtmlPage page = webClient.getPage(url);
        System.out.println(page.getTitleText());
        // Locate the input element using its attributes
        HtmlElement inputElement = page.getFirstByXPath("//a[contains(@class, 'text-decoration-none text-body-hover')]");

        if (inputElement != null) {
            try {
                HtmlPage organisations = inputElement.click();
                HtmlElement element = organisations.getFirstByXPath("//div[contains(@class, 'card bg-body-tertiary border rounded-3')]");
                HtmlPage organisation = element.click();
                HtmlElement mainData = (HtmlElement) organisation.getByXPath("//div[contains(@class, 'col-12')]").get(2);
                List<HtmlElement> datas = mainData.getByXPath("//div[contains(@class, 'row border-bottom py-3')]");
                OrgInfoData infoData = new OrgInfoData();
                int count = 0;
                for (HtmlElement data : datas) {
                    HtmlElement span = data.getElementsByTagName("span").get(1);
                    if (count == 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = dateFormat.parse(span.asNormalizedText());
                        infoData.setDateOfRegistration(date);
                    } else if (count == 1) {
                        if (span.asNormalizedText().contains("Hozirda mavjud")) {
                            infoData.setStatus(true);
                        }
                    } else if (count == 2) {
                        infoData.setRegisteringAuthority(span.asNormalizedText());
                    } else if (count == 3) {
                        infoData.setStir(span.asNormalizedText());
                    } else if (count == 4) {
                        infoData.setTHSHT(span.asNormalizedText());
                    } else if (count == 5) {
                        infoData.setDBIBT(span.asNormalizedText());
                    } else if (count == 6) {
                        infoData.setIFUT(span.asNormalizedText());
                    }
                    count++;
                }
                datas = mainData.getByXPath("//div[contains(@class, 'row pt-3')]");
                HtmlElement income = datas.get(0).getElementsByTagName("span").get(1);
                infoData.setCharterOfFoundations(income.asNormalizedText());
                mainData = (HtmlElement) organisation.getByXPath("//div[contains(@class, 'col-12')]").get(3);
                datas = mainData.getByXPath("div[contains(@class, 'row border-bottom py-3')]");
                count = 0;
                for (HtmlElement data : datas) {
                    HtmlElement span = data.getElementsByTagName("span").get(1);
                    if (count == 0) {
                        infoData.setEmail(span.asNormalizedText());
                    } else if (count == 1) {
                        infoData.setPhoneNumber(span.asNormalizedText());
                    } else if (count == 2) {
                        infoData.setAddress(span.asNormalizedText());
                    }
                    count++;
                }
                datas = mainData.getByXPath("//div[contains(@class, 'row py-3')]");
                HtmlElement address = datas.get(1).getElementsByTagName("span").get(1);
                infoData.setAddress(address.asNormalizedText());
                infoData.setOldStir(oldStir);
                dataRepository.save(infoData);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.println("Input element not found!");
        }

    }
    private List<OldStir> getStirFromExcel(Long startId, Long secondId){
        return oldStirRepository.getStirs(startId, secondId);
    }
}
