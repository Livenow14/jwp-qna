package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.common.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

class AnswersTest {

    private Answers answers;

    @BeforeEach
    void init() {
        answers = new Answers();
        answers.addAnswer(new Answer(A1.getId(), A1.writer(), A1.question(), A1.contents()));
    }

    @DisplayName("답변 삭제 - 성공")
    @Test
    void deleteAnswers() {
        //given
        //when
        //then
        assertThatCode(() -> answers.deleteAnswers(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }

    @DisplayName("답변 삭제 - 실패 - 다른 사용자의 답변있음")
    @Test
    void deleteAnswersFail() {
        //given
        answers.addAnswer(new Answer(A2.getId(), A2.writer(), A2.question(), A2.contents()));
        //when
        //then
        assertThatThrownBy(() -> answers.deleteAnswers(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}