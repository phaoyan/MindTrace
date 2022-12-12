package pers.juumii.MindTrace.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;
import pers.juumii.MindTrace.utils.algorithm.MathUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Data
@ToString
@NoArgsConstructor
@InstantData
public class Knowledge implements Persistent{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishTime = Constants.timeAnchor;
    private long id, superKnowledgeId;
    private String description;
    private List<LearningCard> learningCards = new ArrayList<>();
    private List<QuizCard> quizCards = new ArrayList<>();

    @InstantData
    public int getQuizScale(){
        AtomicInteger res = new AtomicInteger();
        quizCards.forEach(card-> res.addAndGet(card.getScale()));
        return res.get();
    }

    @InstantData
    public List<QuizRecord> getQuizRecords(){
        List<QuizRecord> res = new ArrayList<>();
        quizCards.forEach(card->res.addAll(card.getQuizRecords()));
        return res;
    }

    @InstantData
    public List<QuizCard> getReviewedQuizCards(){
        return DataUtils.getAllIf(getQuizCards(), quizCard -> !quizCard.getQuizRecords().isEmpty());
    }

    @InstantData
    public List<QuizCard> getUnreviewedQuizCards(){
        return DataUtils.getAllIf(getQuizCards(), quizCard -> quizCard.getQuizRecords().isEmpty());
    }

    @InstantData
    public Duration getReviewTimeSpent(){
        Duration res = Duration.ZERO;
        for(QuizCard card: getQuizCards())
            res = res.plusMinutes((long) card.getScale() * card.getQuizRecords().size());
        return res;
    }

    @InstantData
    public double getAverageQuizCardCompletion(){
        return quizCards.isEmpty() ? 0 : DataUtils.destructureAll(getQuizCards(), QuizCard::getQuizRecords).stream().mapToDouble(group->group.isEmpty() ? 0 : group.get(group.size()-1).getCompletion()).sum() / quizCards.size();
    }

    @InstantData
    public List<QuizCard> getPerfectRecordQuizCards(){
        return DataUtils.getAllIf(quizCards, quizCard -> !quizCard.getQuizRecords().isEmpty() && quizCard.getQuizRecords().get(quizCard.getQuizRecords().size()-1).getCompletion() >= 90);
    }

    @InstantData
    public List<QuizCard> getPoorRecordQuizCards(){
        return DataUtils.getAllIf(quizCards, quizCard -> !quizCard.getQuizRecords().isEmpty() && quizCard.getQuizRecords().get(quizCard.getQuizRecords().size()-1).getCompletion() <= 30);
    }

    @InstantData
    public double getRate(){
        AtomicReference<Double> res = new AtomicReference<>((double) 0);
        quizCards.forEach(card-> res.updateAndGet(v -> v + card.getRate()));
        return quizCards.size() == 0 ? 0 : res.get() / quizCards.size();
    }

    @InstantData
    public String getOverviewMarkdown(){
        return """
                # Quiz
                 - *%d* quiz cards registered.
                 - *%d* minutes expected to review all quiz cards.
                # Review
                 - *%d* quiz cards reviewed.
                 - *%d* quiz cards never reviewed.
                 - *%d* hours *%d* minutes spent on reviewing
                # Mastery
                 - *%.2f* is the average completion of quiz cards.
                 - *%d* quiz cards have perfect completion.
                 - *%d* quiz cards have poor completion.
                # Rating
                 - *%.2f* is the average rating of quiz cards.
                 
                """.formatted(
                        getQuizCards().size(),
                        getQuizScale(),
                        getReviewedQuizCards().size(),
                        getUnreviewedQuizCards().size(),
                        getReviewTimeSpent().toHoursPart(),getReviewTimeSpent().toMinutesPart(),
                        getAverageQuizCardCompletion(),
                        getPerfectRecordQuizCards().size(),
                        getPoorRecordQuizCards().size(),
                        getRate()
        );
    }

    public static Knowledge protoType() {
        Knowledge res = new Knowledge();
        res.setId(MathUtils.unique());
        res.setSuperKnowledgeId(-1);
        res.setDescription("");
        res.setQuizCards(new ArrayList<>());
        res.setLearningCards(new ArrayList<>());
        res.setEstablishTime(LocalDateTime.now());
        return res;
    }

}
