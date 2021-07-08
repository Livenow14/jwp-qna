package qna.domain;

import qna.common.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void deleteAnswers(User writer) throws Exception {
        if (deletedFalseAnswerHasDifferentOwner(writer).isPresent()) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        answers.forEach(it -> it.toDeleted(true));
    }

    private Optional<Answer> deletedFalseAnswerHasDifferentOwner(User writer) {
        return answers.stream()
                .filter(it -> !it.isOwner(writer))
                .findFirst();
    }

    public List<DeleteHistory> toDeleteHistory(Long questionId, User writer) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, writer));
        for (Answer answer : answers) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.writer()));
        }
        return deleteHistories;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
    }
}
