package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.common.AlreadyAllocateException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

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
}
