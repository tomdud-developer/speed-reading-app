package com.speedreadingapp.service;

import com.speedreadingapp.dto.PDFNamesResponseDTO;
import com.speedreadingapp.dto.PDFPagesRequestDTO;
import com.speedreadingapp.dto.PDFRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.PDF;
import com.speedreadingapp.exception.PDFServiceException;
import com.speedreadingapp.repository.PDFRepository;
import com.speedreadingapp.service.pdf.HTMLPageFromPDF;
import com.speedreadingapp.service.pdf.HTMLPageGeneratorImpl;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PDFService {

    private final PDFRepository pdfRepository;
    private final ApplicationUserService applicationUserService;

    @Transactional
    public void savePDF(PDFRequestDTO pdfRequestDTO) {

        MultipartFile multipartFile = pdfRequestDTO.getFile();
        ApplicationUser applicationUser = applicationUserService.getUserFromAuthContext();

        checkExtensionIfItsNotPDFThrowException(
                Objects.requireNonNull(multipartFile.getOriginalFilename())
        );

        checkIfFileWithThatNameIsInDatabaseAndThrowExceptionIfIts(
                pdfRequestDTO.getName(), applicationUser);

        byte[] bytes = getBytesFromFile(multipartFile);

        PDF pdf = new PDF(
                0L,
                pdfRequestDTO.getName(),
                loadBytesToPDDocumentAndReturnNumberOfPages(bytes),
                bytes,
                applicationUser
        );

        pdfRepository.save(pdf);
    }

    @Transactional(readOnly = true)
    public List<PDFNamesResponseDTO> getListOfPDFsNames() {

        ApplicationUser applicationUser = applicationUserService.getUserFromAuthContext();

        return pdfRepository.findAllByUserId(applicationUser.getId())
                .stream().map(pdf -> new PDFNamesResponseDTO(pdf.getName(), pdf.getNumberOfPages())).toList();
    }

    @Transactional(readOnly = true)
    public List<HTMLPageFromPDF> getListOfPagesInHTML(PDFPagesRequestDTO pdfPagesRequestDTO) {

        ApplicationUser applicationUser = applicationUserService.getUserFromAuthContext();

        Optional<PDF> optionalPDF = pdfRepository.findPDFByNameAndUserId(
                applicationUser.getId(), pdfPagesRequestDTO.getPdfName());

        if (optionalPDF.isEmpty()) throw new PDFServiceException(
                String.format("File with name %s not found in repository", pdfPagesRequestDTO.getPdfName()));

        try (HTMLPageGeneratorImpl htmlPageGenerator =
                     HTMLPageGeneratorImpl.createFrom(optionalPDF.get().getContent())) {
            return htmlPageGenerator.generatePages(
                    pdfPagesRequestDTO.getDesiredWords(),
                    pdfPagesRequestDTO.getFromPage()
            );
        } catch (IOException exception) {
            throw new PDFServiceException("Exception when generating HTML pages");
        }
    }

    private void checkIfFileWithThatNameIsInDatabaseAndThrowExceptionIfIts(
            String name, ApplicationUser applicationUser) {
        Optional<PDF> optionalPDF = pdfRepository.findPDFByNameAndUserId(applicationUser.getId(), name);
        if (optionalPDF.isPresent())
            throw new PDFServiceException(
                    String.format("File with name %s is already present in your repository", name)
            );
    }

    private void checkExtensionIfItsNotPDFThrowException(String fullName) {
        String[] partsOfName = fullName.split("\\.");

        if (!(partsOfName.length == 2 && partsOfName[1].equals("pdf")))
            throw new PDFServiceException("Wrong extension");
    }

    private byte[] getBytesFromFile(MultipartFile multipartFile) throws PDFServiceException {
        try {
            return multipartFile.getBytes();
        } catch (IOException exception) {
            throw new PDFServiceException("Failed when getting bytes from file");
        }
    }

    private int loadBytesToPDDocumentAndReturnNumberOfPages(byte[] bytes) {
        try (PDDocument pdDocument = PDDocument.load(bytes);) {
            return pdDocument.getNumberOfPages();
        } catch (IOException e) {
            throw new PDFServiceException("Exception when load bytes to PDDocument");
        }
    }

    public void generateHTMLFromPDF(File file) throws IOException {
        PDDocument pdf = PDDocument.load(file);
        Path path = Files.createFile(Path.of("pdf.html"));
        Writer output = new PrintWriter(path.toString(), "utf-8");
        new PDFDomTree().writeText(pdf, output);

        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

