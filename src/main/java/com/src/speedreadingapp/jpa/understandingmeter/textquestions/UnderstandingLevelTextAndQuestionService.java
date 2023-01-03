package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class UnderstandingLevelTextAndQuestionService {

    private final UnderstandingLevelQuestionRepository understandingLevelQuestionRepository;
    private final UnderstandingLevelTextRepository understandingLevelTextRepository;

    @Transactional
    public UnderstandingLevelText getTextNumberX(Long x) {
        return understandingLevelTextRepository.getReferenceById(x);
    }

    @Transactional
    public void createTextAnswerQuestion1() {
        try {
            UnderstandingLevelText understandingLevelText = understandingLevelTextRepository.save(new UnderstandingLevelText(1L, 1, createText1()));
            UnderstandingLevelQuestion understandingLevelQuestion1 = createQuestion1(understandingLevelText);
            UnderstandingLevelQuestion understandingLevelQuestion2 = createQuestion2(understandingLevelText);
            UnderstandingLevelQuestion understandingLevelQuestion3 = createQuestion3(understandingLevelText);
            UnderstandingLevelQuestion understandingLevelQuestion4 = createQuestion4(understandingLevelText);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String createText1() throws IOException {
        String str = "";
        StringBuffer buf = new StringBuffer();
        InputStream is = UnderstandingLevelTextAndQuestionService.class.getClassLoader().getResourceAsStream("understandingleveltext.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n" );
                }
            }
        } finally {
            try { is.close(); } catch (Throwable ignore) {}
        }
        str = buf.toString();
        return str;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion1(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                1L,
                0,
                "Gdzie należy postawić klatkę z ptakiem?",
                "w pobliżu pieca",
                "w słoneczne miejsce",
                "w miejscu zacienionym i przysłoniętym",
                "w miejscu bezpośrednio nasłonecznionym",
                'C',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion2(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                2L,
                1,
                "Jakiego miejsca powinno się unikać, gdy postawimy klatkę z ptakiem?",
                "miejsca zacienionego",
                "przeciągów",
                "miejsca słonecznego",
                "powały",
                'B',
                understandingLevelText
        );

        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion3(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                3L,
                2,
                "Jakie szkody może spowodować postawienie klatki w słonecznym miejscu?",
                "ptak może zachorować",
                "ptak może wyemigrować",
                "ptak może zaziębić się i stracić życie",
                "ptak może zostać porwany",
                'C',
                understandingLevelText
        );

        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion4(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                4L,
                3,
                "Czy ptaki na wolności wystawiają swoje ciała na działanie promieni słonecznych?",
                "tak",
                "częsciowo",
                "nie",
                "zależy od pogody",
                'C',
                understandingLevelText
        );

        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    public List<UnderstandingLevelQuestion> getQuestionsToTextNumberX(Long number) {
        return understandingLevelQuestionRepository.getQuestionsToTextNumberX(number);
    }
}
