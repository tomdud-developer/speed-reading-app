package com.src.speedreadingapp.jpa.pdfuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PdfUser {
        @Id
        @GeneratedValue
        Long id;

        @Lob
        String content;

        String name;


        String getNwords(Integer nWords) {
                Random rand = new Random();
                String[] splittedStrings = content.split(" ");
                int totalSize = splittedStrings.length;
                int minBound = 10;
                int maxBound = totalSize - nWords - 10;
                int startPoint = rand.nextInt(maxBound-minBound) + minBound;

                String[] constrainSplittedString =
                        Arrays.stream(splittedStrings)
                                .skip(startPoint)
                                .limit(nWords)
                                .toArray(String[]::new);

                return String.join(" ", constrainSplittedString);
        }

        public static String pdfToString(byte[] pdfBytes, int pageFrom, int pageTo) {
                PDFParser pdfParser = null;
                PDDocument pdDocument = null;
                COSDocument cosDocument = null;
                PDFTextStripper pdfTextStripper = null;
                RandomAccessBuffer randomAccessBuffer;
                String parsedText = null;
                try {
                        randomAccessBuffer = new RandomAccessBuffer(pdfBytes);
                        pdfParser = new PDFParser( randomAccessBuffer);
                        pdfParser.parse();
                        cosDocument = pdfParser.getDocument();
                        pdfTextStripper = new PDFTextStripper();
                        pdDocument = new PDDocument(cosDocument);
                        PDDocument newPdf = new PDDocument();
                        System.out.println(pdDocument.getNumberOfPages());
                        for(int i = pageFrom; i <= pageTo; i++) {
                                PDPage page = pdDocument.getPage(i);
                                newPdf.addPage(page);
                        }
                        parsedText = pdfTextStripper.getText(newPdf);
                        parsedText =  (parsedText.replaceAll("[^A-Za-z0-9. ]+", " "));
                } catch (Exception e) {
                        e.printStackTrace();
                        try {
                                if (cosDocument != null)
                                        cosDocument.close();
                                if (pdDocument != null)
                                        pdDocument.close();
                        } catch (Exception e1) {
                                e1.printStackTrace();
                        }
                }
                return parsedText;
        }
}
