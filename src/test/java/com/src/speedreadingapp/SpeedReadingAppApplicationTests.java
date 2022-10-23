package com.src.speedreadingapp;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

@SpringBootTest
class SpeedReadingAppApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void pdfToString() {

		PDFParser parser = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		PDFTextStripper pdfStripper;
		RandomAccessBufferedFileInputStream randomAccessBufferedFileInputStream;
		String parsedText;
		String fileName = "/home/tom/Desktop/Where-the-Crawdads-Sing.pdf";
		File file = new File(fileName);
		try {
			randomAccessBufferedFileInputStream = new RandomAccessBufferedFileInputStream(file);
			parser = new PDFParser( randomAccessBufferedFileInputStream);
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			PDDocument pd = new PDDocument();
			pd.addPage(pdDoc.getPage(122));
			parsedText = pdfStripper.getText(pd);
			System.out.println(parsedText.replaceAll("[^A-Za-z0-9. ]+", " "));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

}
