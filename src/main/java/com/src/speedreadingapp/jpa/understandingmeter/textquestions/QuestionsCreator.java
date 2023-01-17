package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionsCreator {
    private final UnderstandingLevelQuestionRepository understandingLevelQuestionRepository;
    private final UnderstandingLevelTextRepository understandingLevelTextRepository;

    public void createAllQuestions(UnderstandingLevelText understandingLevelText) {
        createQuestion1(understandingLevelText);
        createQuestion2(understandingLevelText);
        createQuestion3(understandingLevelText);
        createQuestion4(understandingLevelText);
        createQuestion5(understandingLevelText);
        createQuestion6(understandingLevelText);
        createQuestion7(understandingLevelText);
        createQuestion8(understandingLevelText);
        createQuestion9(understandingLevelText);
        createQuestion10(understandingLevelText);
    }
    @Transactional
    protected UnderstandingLevelQuestion createQuestion1(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                1L,
                0,
                "Jak należy przyzwyczajać ptaka dzikiego złowionego zimą?",
                "Trzymać w ciepłym pokoju.",
                "Najpierw w sieni, potem w nieco cieplejszym korytarzu, a następnie w pokoju",
                "Trzymać w zimnym pokoju.",
                "Przenosić od razu do ciepłego pokoju.",
            'B',
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
                "Co powinno być najważniejszym elementem oswajania ptaszka?",
                "Spokój i cisza.",
                "Sztuczne ciepło.",
                "Wstrząśnienia.",
                "Hałas.",
                'A',
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
                "Czym należy wysypać podłogę klatki dla śpiewaka?",
                "Ziemniakami.",
                "Mącznikami i poczwarkami mrówek.",
                "Ziarnami zbóż.",
                "Warzywami.",
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
                "Jakiego środka należy użyć w ostateczności, jeśli ptak nie chce jeść?",
                "Kąpiel w letniej wodzie.",
                "Zasłonić klatkę zieloną chustką.",
                "Zamienić pożywienie na zboże.",
                "Umieścić klatkę przy oknie.",
                'A',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion5(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                5L,
                4,
                "Jakie wrażenie wywiera na ptaku człowiek?",
                "Złość.",
                "Strach.",
                "Zaufanie.",
                "Odraza.",
                'C',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion6(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                6L,
                5,
                "Jakie skutki może mieć raptowna zmiana temperatury dla ptaka?",
                "Wzmocni jego odporność.",
                "Zlikwiduje stres.",
                "Osłabi jego odporność.",
                "Ptaszek umrze.",
                'D',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion7(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                7L,
                6,
                "Jak powinien wyglądać mostek u zdrowo odżywionego ptaka?",
                "Okrągły.",
                "Kątowy.",
                "Kończasty-ostro zakończony.",
                "Długi.",
                'C',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion8(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                8L,
                7,
                "Co powoduje, że ptaki wędrowne opuszczają nas wszystkie w porze odlotu?",
                "Brak pożywienia.",
                "Niegościnna pora roku.",
                "Instynkt do przelotu na południe.",
                "Wszystkie powyższe.",
                'D',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion9(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                9L,
                8,
                "Kogo należy szukać, gdy chce się kupić najlepszego ptaszka?",
                "Przyjaciela ptaków.",
                "Barbarzyńskiego dręczyciela.",
                "Znawcy ptaków.",
                "Handlarza ptaków.",
                'A',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    protected UnderstandingLevelQuestion createQuestion10(UnderstandingLevelText understandingLevelText) {
        UnderstandingLevelQuestion question = new UnderstandingLevelQuestion(
                10L,
                9,
                "Jak długo trwa niepokój ptaszka więzionego?",
                "Kilka godzin.",
                "Kilka dni.",
                "Kilka tygodni.",
                "Nie da się przewidzieć. Dopóki nie osłabnie.",
                'D',
                understandingLevelText
        );
        UnderstandingLevelQuestion savedQuestion = understandingLevelQuestionRepository.save(question);
        return savedQuestion;
    }

}
