package com.speedreadingapp.controller;

import com.speedreadingapp.dto.PDFPagesRequestDTO;
import com.speedreadingapp.dto.PDFRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.PDF;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.repository.PDFRepository;
import com.speedreadingapp.service.PDFService;
import com.speedreadingapp.util.MockApplicationUserFactory;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PDFControllerTest {

    private static final String PDF_ENDPOINT_URL = "/api/v2/pdfs";
    private static final String PDF_LIST_ENDPOINT_URL = "/api/v2/pdfs/list";
    private static final String PDF_PAGES_ENDPOINT_URL = "/api/v2/pdfs/pages";

    @InjectMocks
    private PDFController pdfController;

    @MockBean
    private PDFRepository pdfRepository;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;



    @BeforeEach
    public void setUp() {
     //   this.mockMvc = MockMvcBuilders.standaloneSetup(this.pdfController).build();
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void uploadPdf() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF = new PDF(
                12L,
                "Sample book",
                5,
                sampleFile.getBytes(),
                applicationUser
        );

        //then
        when(pdfRepository.findPDFByNameAndUserId(any(), any())).thenReturn(Optional.empty());
        when(pdfRepository.save(any())).thenReturn(returnedPDF);
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(
                multipart(PDF_ENDPOINT_URL)
                        .file(sampleFile)
                        .param("name", "Sample book")
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void uploadPdfFailedBecauseOfExistingPDFWithThatName() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF = new PDF(
                12L,
                "Sample book",
                5,
                sampleFile.getBytes(),
                applicationUser
        );

        //then
        when(pdfRepository.findPDFByNameAndUserId(any(), any())).thenReturn(Optional.of(returnedPDF));
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(
                multipart(PDF_ENDPOINT_URL)
                        .file(sampleFile)
                        .param("name", "Sample book")
            ).andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage")
                                .value(String.format("File with name %s is already present in your repository", returnedPDF.getName()))
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void getListOfPDFsNames() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF1 = new PDF(
                12L,
                "Sample book1",
                5,
                sampleFile.getBytes(),
                applicationUser
        );

        PDF returnedPDF2 = new PDF(
                13L,
                "Sample book2",
                5,
                sampleFile.getBytes(),
                applicationUser
        );

        //then
        when(pdfRepository.findAllByUserId(any())).thenReturn(List.of(returnedPDF1, returnedPDF2));
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(PDF_LIST_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.results").isArray()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(2))
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void getListOfHTMLPages() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF = new PDF(
                12L,
                "Sample book",
                5,
                sampleFile.getBytes(),
                applicationUser
        );


        PDFPagesRequestDTO pdfPagesRequestDTO = PDFPagesRequestDTO.builder()
                .fromPage(1)
                .pdfName(returnedPDF.getName())
                .desiredWords(1000)
                .build();

        //then
        when(pdfRepository.findPDFByNameAndUserId(any(), eq(pdfPagesRequestDTO.getPdfName()))).thenReturn(Optional.of(returnedPDF));
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(PDF_PAGES_ENDPOINT_URL)
                        .content(Objects.requireNonNull(ObjectToJsonAsStringConverter.convert(pdfPagesRequestDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.results").isArray()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(3))
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void getListOfHTMLPagesFailedBecauseTooMuchWordsRequested() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF = new PDF(
                12L,
                "Sample book",
                5,
                sampleFile.getBytes(),
                applicationUser
        );


        PDFPagesRequestDTO pdfPagesRequestDTO = PDFPagesRequestDTO.builder()
                .fromPage(1)
                .pdfName(returnedPDF.getName())
                .desiredWords(100000)
                .build();

        //then
        when(pdfRepository.findPDFByNameAndUserId(any(), eq(pdfPagesRequestDTO.getPdfName()))).thenReturn(Optional.of(returnedPDF));
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(PDF_PAGES_ENDPOINT_URL)
                        .content(Objects.requireNonNull(ObjectToJsonAsStringConverter.convert(pdfPagesRequestDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage")
                                .value(String.format("Starting from %d page it is impossible to achieve %d words.",
                                        pdfPagesRequestDTO.getFromPage(), pdfPagesRequestDTO.getDesiredWords()))
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void getListOfHTMLPagesFailedBecauseTooHighPageRequested() throws Exception {
        //given
        MockMultipartFile sampleFile = getTestPdfFile();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        PDF returnedPDF = new PDF(
                12L,
                "Sample book",
                5,
                sampleFile.getBytes(),
                applicationUser
        );


        PDFPagesRequestDTO pdfPagesRequestDTO = PDFPagesRequestDTO.builder()
                .fromPage(100)
                .pdfName(returnedPDF.getName())
                .desiredWords(500)
                .build();

        //then
        when(pdfRepository.findPDFByNameAndUserId(any(), eq(pdfPagesRequestDTO.getPdfName()))).thenReturn(Optional.of(returnedPDF));
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(PDF_PAGES_ENDPOINT_URL)
                        .content(Objects.requireNonNull(ObjectToJsonAsStringConverter.convert(pdfPagesRequestDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage")
                                .value(String.format("The page number %d is greater than numbers of page",
                                        pdfPagesRequestDTO.getFromPage()))
                );
    }


    private MockMultipartFile getTestPdfFile() {
        try (InputStream inputStream = PDFControllerTest.class
                .getClassLoader().getResourceAsStream("sample_pdf_5_pages.pdf")) {
            return new MockMultipartFile(
                    "file",
                    "sample_pdf_5_pages.pdf",
                    "application/pdf",
                    inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}