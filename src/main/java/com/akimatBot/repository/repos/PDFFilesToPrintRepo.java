package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.PDFFilesToPrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface PDFFilesToPrintRepo extends JpaRepository<PDFFilesToPrint, Long> {

    @Transactional
    @Modifying
    @Query("update PDFFilesToPrint pdf set pdf.printed = true where pdf.fileName = ?1")
    void printedByName(String name);

    @Query(value = "select * from pdffiles_to_print pdf where pdf.printed = false order by id limit 1", nativeQuery = true)
    PDFFilesToPrint getNotPrintedFirst();

    default void save(String fileName) {
        PDFFilesToPrint pdfFilesToPrint = new PDFFilesToPrint();
        pdfFilesToPrint.setFileName(fileName);
        pdfFilesToPrint.setDate(new Date());
        pdfFilesToPrint.setPrinted(false);
        this.save(pdfFilesToPrint);
    }

}
