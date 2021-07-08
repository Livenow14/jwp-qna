package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.common.AlreadyAllocateException;
import qna.common.UnAuthorizedException;

import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    private User javajigi;
    private User targetUser;

    @BeforeEach
    void init() {
        javajigi = new User(JAVAJIGI.getId(), JAVAJIGI.getAccount(), JAVAJIGI.getPassword(), JAVAJIGI.getName(), JAVAJIGI.getEmail());
        targetUser = new User(javajigi.getAccount(), javajigi.getPassword(), "자바지기네!", "자바지기네@넥스트레벨.net");
    }

    @DisplayName("유저 수정 - 성공")
    @Test
    void updateUser() {
        //given
        //when
        javajigi.update(JAVAJIGI, targetUser);
        //then
        assertThat(javajigi.getName()).isEqualTo(targetUser.getName());
        assertThat(javajigi.getEmail()).isEqualTo(targetUser.getEmail());
    }

    @DisplayName("유저 수정 - 실패 - 로그인한 유저와 같지 않음")
    @Test
    void updateUserFailNotLogin() {
        //given
        //when
        //then
        assertThatThrownBy(() -> javajigi.update(SANJIGI, targetUser))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("유저 수정 - 실패 - 타겟 유저의 패스워드가 기존 유저와 같지 않음")
    @Test
    void updateUserFailUnMatchPassword() {
        //given
        //when
        targetUser.changePassword("changedPassword");
        //then
        assertThatThrownBy(() -> javajigi.update(SANJIGI, targetUser))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("유저 비밀번호 변경 - 성공")
    @Test
    void changePassword() {
        //given
        //when
        //then
        assertThatCode(() -> targetUser.changePassword("changedPassword"))
                .doesNotThrowAnyException();
    }

    @DisplayName("유저 비밀번호 변경 - 실패 - 동일한 비밀번호")
    @Test
    void changePasswordFail() {
        //given
        //when
        //then
        assertThatThrownBy(() -> targetUser.changePassword(targetUser.getPassword()))
                .isInstanceOf(AlreadyAllocateException.class);
    }

    @DisplayName("게스트유저 생성 - 성공")
    @Test
    void createGuestUser() {
        //given
        User guestUser = User.GUEST_USER;
        //when
        boolean isGuestUser = guestUser.isGuestUser();
        //then
        assertThat(isGuestUser).isEqualTo(true);
    }

    @DisplayName("게스트유저 수정 - 실패")
    @Test
    void updateGuestUserFail() {
        //given
        User guestUser = User.GUEST_USER;
        //when
        //then
        assertThatThrownBy(() -> guestUser.update(guestUser, JAVAJIGI))
                .isInstanceOf(UnAuthorizedException.class);

        assertThatThrownBy(() -> guestUser.changePassword(targetUser.getPassword()))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
