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
    private final QuestionsCreator questionsCreator;
    @Transactional
    public UnderstandingLevelText getTextNumberX(Long x) {
        return understandingLevelTextRepository.getTextByIndex(
                Integer.parseInt(String.valueOf(x))
        );
    }

    @Transactional
    public void createTextAnswerQuestion1() {
        try {
            understandingLevelTextRepository.deleteAll();
            understandingLevelQuestionRepository.deleteAll();
            UnderstandingLevelText understandingLevelText = understandingLevelTextRepository.save(new UnderstandingLevelText(1L, 1, createText1()));
            questionsCreator.createAllQuestions(understandingLevelText);
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



    public List<UnderstandingLevelQuestion> getQuestionsToTextNumberX(Integer number) {
        return understandingLevelQuestionRepository.getQuestionsToTextNumberX(number);
    }
}
