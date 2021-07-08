package qna.domain;

import qna.common.AlreadyAllocateException;
import qna.common.UnAuthorizedException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Column(length = 20, nullable = false)
    private String account;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50)
    private String email;

    protected User() {
    }

    public User(String account, String password, String name, String email) {
        this(null, account, password, name, email);
    }

    public User(Long id, String account, String password, String name, String email) {
        super.changeId(id);
        this.account = account;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        validateUser(loginUser, target);
        this.name = target.name;
        this.email = target.email;
    }

    private void validateUser(User loginUser, User target) {
        if (isGuestUser()) {
            throw new UnAuthorizedException();
        }

        if (!matchUser(loginUser.account)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }
    }

    private boolean matchUser(String account) {
        return this.account.equals(account);
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public void changePassword(String password) {
        if (isGuestUser()) {
            throw new UnAuthorizedException();
        }

        if (matchPassword(password)) {
            throw new AlreadyAllocateException("동일한 비밀번호입니다");
        }
        this.password = password;
    }

    public boolean isGuestUser() {
        return false;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", userId='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        User user = (User) o;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, password, name, email);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
