package com.speedreadingapp.service.pdf;

import com.speedreadingapp.exception.PDFServiceException;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HTMLPageGeneratorImpl implements HtmlPageGenerator, AutoCloseable {

    private PDDocument pdf;
    private PDPageTree pagesTree;

    private static final Pattern pattern = Pattern.compile("[^A-Za-z0-9łąńśćęóżźŁĄŃŚĆĘÓŻŹ.\",?!\\- ]+");


    private HTMLPageGeneratorImpl(byte[] bytes) {
        loadDocument(bytes);
    }

    public static HTMLPageGeneratorImpl createFrom(byte[] bytes) {
        return new HTMLPageGeneratorImpl(bytes);
    }

    private void loadDocument(byte[] bytes) {
        try {
            this.pdf = PDDocument.load(bytes);
            this.pagesTree = this.pdf.getPages();
        } catch (IOException e) {
            throw new PDFServiceException("Exception when load bytes to PDDocument");
        }
    }

    @Override
    public List<HTMLPageFromPDF> generatePages(int desiredNumberOfWords, int fromPage) {

        checkIfPageIsGreaterThanTotalPages(fromPage);

        List<HTMLPageFromPDF> listOfHTMLPages = new ArrayList<>();
        int wordCounter = 0;
        int currentPage = fromPage - 1;
        while (wordCounter < desiredNumberOfWords) {
            if (currentPage >= pdf.getNumberOfPages()) throw new PDFServiceException(String.format(
                        "Starting from %d page it is impossible to achieve %d words.", fromPage, desiredNumberOfWords));

            HTMLPageFromPDF htmlPageFromPDF = generatePage(currentPage);
            wordCounter += htmlPageFromPDF.getWordsOnPage();

            listOfHTMLPages.add(htmlPageFromPDF);

            currentPage++;
        }

        return listOfHTMLPages;
    }

    @Override
    public HTMLPageFromPDF generatePage(int pageNumber) {

        checkIfPageIsGreaterThanTotalPages(pageNumber);

        try(PDDocument newPFDWithOnePage = new PDDocument()) {
            newPFDWithOnePage.addPage(pagesTree.get(pageNumber));

            String html = new PDFDomTree().getText(newPFDWithOnePage);
            String unescapedHtml = StringEscapeUtils.unescapeJava(html);

            int numberOfWords = countNumberOfWordsInPDF(newPFDWithOnePage);

            return new HTMLPageFromPDF(unescapedHtml ,pageNumber + 1, numberOfWords);
        } catch (IOException e) {
            throw new PDFServiceException("Exception when creating html page from pdf");
        }
    }

    @Override
    public void close() throws IOException {
        pdf.close();
    }

    private int countNumberOfWordsInPDF(PDDocument pdf) {
        String text = getTextFromPDFDocumentAndReplaceUndesiredCharacters(pdf);
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
           if(text.charAt(i) == ' ' || text.charAt(i) == '\n')
               counter++;
        }

        return counter;
    }

    private String getTextFromPDFDocumentAndReplaceUndesiredCharacters(PDDocument pdf) {
        try{
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(pdf);
            return RegExUtils.replaceAll(text, pattern, " ");
        } catch (IOException exception) {
            throw new PDFServiceException("Exception when extracting text from pdf");
        }
    }

    private void checkIfPageIsGreaterThanTotalPages(int page) throws PDFServiceException {
        if (page > pdf.getNumberOfPages())
            throw new PDFServiceException(String.format("The page number %d is greater than numbers of page", page));
    }
}
