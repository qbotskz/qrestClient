package com.akimatBot.utils.pdfDocument;

import com.akimatBot.entity.custom.PDFFilesToPrint;
import com.akimatBot.repository.repos.PDFFilesToPrintRepo;
import com.akimatBot.repository.repos.PropertiesRepo;
import com.akimatBot.services.OrderService;
import com.akimatBot.utils.DateUtil;
import com.akimatBot.web.dto.PaymentTypeReportDTO;
import com.akimatBot.web.dto.ReportDailyWithTaxDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service
public class PDFGenerator {

    @Autowired
    OrderService orderService;

    @Autowired
    PropertiesRepo propertiesRepo;

    @Autowired
    PDFFilesToPrintRepo pdfFilesToPrintRepo;
    private final String path = "D:/qrestoran/printerDocuments/";

    public  File getReportDaily(ReportDailyWithTaxDTO reportDaily) {
        try {
            String printerName = propertiesRepo.findFirstById(5).getValue1();
            int y = 0;
            int x = 0;
            float height = calculateHeight(reportDaily);
//            ReportDailyWithTaxDTO reportDaily = orderService.getReportDailyWithTaxDTO(code);

            // Создаем новый документ
            PDDocument document = new PDDocument();

            // Создаем страницу с заданной шириной (80 мм)
            PDPage page = new PDPage(new PDRectangle(226, height)); // Ширина: 80 мм, Высота: 200 мм
//            float dynamicHeight = calculateDynamicHeight(); // Ваш расчет высоты страницы

//            page.setMediaBox(new PDRectangle(140, dynamicHeight));

            // Добавляем страницу в документ
            document.addPage(page);

            // Создаем объект для записи контента на страницу
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Начинаем запись текста на страницу
            contentStream.beginText();
            contentStream.newLineAtOffset(x, height - 20 ); // Начальная позиция текста (10 мм, 180 мм)


/////////////////////////////////////////////////////////////////////////////////////////////

            // Выполните здесь необходимую обработку или расчет содержимого,
            // чтобы определить высоту страницы на основе содержимого

/////////////////////////////////////////////////////////////////////////////////////////////

            // Задаем размер и позицию текста на странице


//            addText(document, contentStream,10 , 0, 10 ,"ИП “Мустафаали”", 10);
            addText(document, contentStream,10 , 0, 10 ,"ИП “Мұстафаәли”", 10);

            addText(document, contentStream,10 , 0, 10 ,"ИНН ", 10);

            addText(document, contentStream,10 , x, 10 ,"----------------------------------------------", 30);
//            addText(document, contentStream,10 , x, 10 ," ", 20);
            addText(document, contentStream,10 , x, 10 ," 041 Выручка по типам с налогами ", 30);
            addText(document, contentStream,10 , x, 10 ,"Терминал: №1(Алтын Алтай Рыскулова)", 10);
            addText(document, contentStream,10 , x, 10 ,"Смена открыта: " + reportDaily.getGeneralShift().getOpeningTime(), 10);
            addText(document, contentStream,10 , x, 10 ,"Кассовая смена: " + reportDaily.getGeneralShift().getId(), 10);
            addText(document, contentStream,10 , x, 10 ,"Текущее время: " + DateUtil.getDbMmYyyyHhMmSs(new Date()), 10);
            addText(document, contentStream,10 , x, 10 ,"Текущий пользователь: " + reportDaily.getCurrentEmployee().getFullName(), 25);
            addText(document, contentStream,10 , x, 10 ,"Фискальные типы оплат:", 10);
            addText(document, contentStream,10 , x, 10 ,"----------------------------------------------", 10);
            addText(document, contentStream,10 , x, 10 ,"Тип оплаты        Заказов    Сумма", 10);
            for (String row : getRows(reportDaily.getPaymentTypeReports())){
                addText(document, contentStream,10 , x, 10 ,row, 10);
            }
            addText(document, contentStream,10 , x, 10 ,"----------------------------------------------", 10);
            addText(document, contentStream,10 , x, 10 ,getTotal(reportDaily.getTotal()), 10);



            // Завершаем запись текста
            contentStream.endText();

            // Закрываем контентный поток
            contentStream.close();

            // Сохраняем документ на диск
            String fileName =  new Date().getTime() + "(" +printerName +")" + ".pdf";
            String absoluteFileName = path + fileName;

            document.save(absoluteFileName);

            // Закрываем документ
            document.close();

            System.out.println("PDF-документ успешно создан.");

            pdfFilesToPrintRepo.save(fileName);
            return new File(absoluteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTotal(Double total) {
        int rowSize = 33;
        String str = "Итого";
//        String tot = String.format("%.2f",total);
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        String tot = decimalFormat.format(total);

        int chars = rowSize - tot.length();
        while (str.length() <= chars){
            str += " ";
        }
        str += tot;
        return str;
    }

    private List<String> getRows(List<PaymentTypeReportDTO> paymentTypeReports) {
        List<String> rows = new ArrayList<>();
        for (PaymentTypeReportDTO paymentTypeReportDTO : paymentTypeReports){
            rows.addAll(getRow(paymentTypeReportDTO));
        }
        return rows;
    }

    private List<String> getRow(PaymentTypeReportDTO paymentTypeReportDTO) {
        int countChars = 33;
        int charsForName = countChars - 4 - String.valueOf(paymentTypeReportDTO.getTotal()).length() - String.valueOf(paymentTypeReportDTO.getQuantity()).length();
        List<String> row = new ArrayList<>();
        String name  = "";
        for (int i = 0; i <= paymentTypeReportDTO.getPaymentType().getName().length() ; i++) {
            if (name.length() == paymentTypeReportDTO.getPaymentType().getName().length()){
                if (name.length() < charsForName){
                    while (name.length() != charsForName){
                        name += " ";
                    }
                }
                name = name + " " + paymentTypeReportDTO.getQuantity() + "    " + paymentTypeReportDTO.getTotal();
                row.add(name);
            }
            else {
                name += paymentTypeReportDTO.getPaymentType().getName().charAt(i);
                if (name.length() == charsForName) {
                    row.add(name);
                }
            }
        }
        return row;
    }

    private float calculateHeight(ReportDailyWithTaxDTO reportDaily) {
        float h = 240;
        h += 20*reportDaily.getPaymentTypeReports().size();
        return h;
    }

    private void addText(PDDocument document, PDPageContentStream contentStream, int fontSize, int x, int y, String text, float  leading) throws IOException {
        File fontFile = new File("src/main/resources/templates/fonts/VCOURN.TTF");
        PDType0Font customFont = PDType0Font.load(document, fontFile);

        contentStream.setFont(customFont, fontSize);
        contentStream.newLine();
        contentStream.setLeading(leading);
//        contentStream.newLineAtOffset(x, y); // Начальная позиция текста (10 мм, 180 мм)

        // Записываем данные внутрь PDF-документа
        contentStream.showText(text);

    }


}