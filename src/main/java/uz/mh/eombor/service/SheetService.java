package uz.mh.eombor.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.mh.eombor.dto.ClientInfoDto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SheetService {
    public List<String> readNumbers(MultipartFile file) throws IOException {
        List<String> numbers = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()){
            Row currentRow = rowIterator.next();
            Iterator<Cell> cellIterator = currentRow.cellIterator();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                String number = cell.getStringCellValue();
                numbers.add(number);
            }
        }
        return numbers;
    }

    public void generateExcel(String fileName, List<ClientInfoDto> dtoList) throws IOException{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        XSSFRow firstRow = sheet.createRow(0);

        XSSFCell clientId = firstRow.createCell(0);
        clientId.setCellValue("ID");

        XSSFCell TEBNH = firstRow.createCell(1);
        TEBNH.setCellValue("номер ДКДГ");

        XSSFCell transportNumber = firstRow.createCell(2);
        transportNumber.setCellValue("Номер транспортного средства");

        XSSFCell brutto = firstRow.createCell(3);
        brutto.setCellValue("Брутто, кг");

        XSSFCell receiver = firstRow.createCell(4);
        receiver.setCellValue("Грузополучатель");

        XSSFCell stir = firstRow.createCell(5);
        stir.setCellValue("ИНН");

        XSSFCell post = firstRow.createCell(6);
        post.setCellValue("Пост назначения");

        XSSFCell deliverDate = firstRow.createCell(7);
        deliverDate.setCellValue("Срок доставки");

        XSSFCell placeOfArrival = firstRow.createCell(8);
        placeOfArrival.setCellValue("Место доставки");

        XSSFCell solvedTime = firstRow.createCell(9);
        solvedTime.setCellValue("Статус");

        int row = 1;
        for (ClientInfoDto dto : dtoList) {
            XSSFRow currentRow = sheet.createRow(row);
            XSSFCell cellClientId = currentRow.createCell(0);
            if (dto.getId() != null){
                cellClientId.setCellValue(dto.getId());
            }


            XSSFCell cellTEBNH = currentRow.createCell(1);
            if (dto.getTEBNH() != null){
                cellTEBNH.setCellValue(dto.getTEBNH());
            }

            XSSFCell transportRaqami = currentRow.createCell(2);
            if (dto.getTransportNumber() != null){
                transportRaqami.setCellValue(dto.getTransportNumber());
            }

            XSSFCell Brutto = currentRow.createCell(3);
            if (dto.getBrutto() != null){
                Brutto.setCellValue(dto.getBrutto());
            }

            XSSFCell qabulQiluvchi = currentRow.createCell(4);
            if (dto.getReceiver() != null){
                qabulQiluvchi.setCellValue(dto.getReceiver());
            }

            XSSFCell inn = currentRow.createCell(5);
            if (dto.getStir() != null){
                inn.setCellValue(dto.getStir());
            }

            XSSFCell posti = currentRow.createCell(6);
            if (dto.getPost() != null){
                posti.setCellValue(dto.getPost());
            }

            XSSFCell yetkazishVaqti = currentRow.createCell(7);
            if (dto.getDeliveryDate() != null){
                yetkazishVaqti.setCellValue(dto.getDeliveryDate().toString());
            }


            XSSFCell joyi = currentRow.createCell(8);
            if (dto.getPlaceOfArrival() != null){
                joyi.setCellValue(dto.getPlaceOfArrival());
            }

            XSSFCell time = currentRow.createCell(9);
            if (dto.getSolvedTime() != null){
                time.setCellValue(dto.getSolvedTime().toString());
            }
            row++;
        }
        FileOutputStream file = new FileOutputStream(fileName + ".xlsx");
        workbook.write(file);
        file.flush();
        file.close();
    }
}
