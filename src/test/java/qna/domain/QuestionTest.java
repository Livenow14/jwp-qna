package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.common.AlreadyAllocateException;
import qna.common.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @DisplayName("삭제 상태로 변경 - 성공")
    @Test
    void setDelatedTest() throws Exception {
        //give
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        question.toDeleted(UserTest.JAVAJIGI);
        //then
        assertThat(question.isDeleted()).isEqualTo(true);
    }

    @DisplayName("삭제 상태로 변경 - 실패 - 이미 삭제처리됨")
    @Test
    void setDeletedTestFail() throws Exception {
        //give
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        question.toDeleted(UserTest.JAVAJIGI);
        //then
        assertThatThrownBy(() -> question.toDeleted(UserTest.JAVAJIGI))
                .isInstanceOf(AlreadyAllocateException.class);
    }

    @DisplayName("삭제 상태로 변경 - 실패 - 질문 작성자가 아님")
    @Test
    void setDeletedTestFailUnAuthorizeTest() {
        //give
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        //then
        assertThatThrownBy(() -> question.toDeleted(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("삭제 상태 히스토리 출력 - 성공")
    @Test
    void toHistoryTest() throws Exception {
        //give
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        question.toDeleted(UserTest.JAVAJIGI);
        List<DeleteHistory> deleteHistories = question.toDeleteHistory();
        //then
        assertThat(deleteHistories).hasSize(1);
    }

    @DisplayName("삭제 상태 히스토리 출력 - 실패 - 삭제되지 않은 질문")
    @Test
    void toHistoryFailTest() {
        //give
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        //when
        //then
        assertThatThrownBy(question::toDeleteHistory)
                .isInstanceOf(CannotDeleteException.class);
    }
}
