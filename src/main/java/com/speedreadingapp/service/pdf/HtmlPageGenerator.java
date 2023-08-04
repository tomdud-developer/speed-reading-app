package com.speedreadingapp.service.pdf;

import java.util.List;

public interface HtmlPageGenerator {
    List<HTMLPageFromPDF> generatePages(int desiredNumberOfWords, int fromPage);
    HTMLPageFromPDF generatePage(int pageNumber);
}
