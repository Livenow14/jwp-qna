package qna.domain;

import qna.common.AlreadyAllocateException;
import qna.common.CannotDeleteException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "question_id"))
public class Question extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    private final Answers answers = new Answers();

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super.changeId(id);
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void deleteAnswers() throws Exception {
        answers.deleteAnswers(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.toAnswer(answer);
    }

    public User writer() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void toDeleted(User loginUser) throws Exception {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if (deleted) {
            throw new AlreadyAllocateException("이미 삭제 처리된 질문입니다.");
        }
        this.deleted = true;
    }

    public List<DeleteHistory> toDeleteHistory() throws Exception {
        if (!deleted) {
            throw new CannotDeleteException("삭제되지 않은 질문입니다.");
        }
        return answers.toDeleteHistory(this.getId(), this.writer);
    }

    public void removeAnswer(Answer answer) {
        this.answers.removeAnswer(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Question question = (Question) o;
        return Objects.equals(this.getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + super.getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }

    private boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }
}
