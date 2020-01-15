package com.eliise.learningspringboot.service;

import com.eliise.learningspringboot.Dao.FakeDataDao;
import com.eliise.learningspringboot.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private FakeDataDao fakeDataDao;

    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid, "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");

        List<User> users = List.of(anna);

        given(fakeDataDao.selectAllUsers()).willReturn(users);
        List<User> allUsers = userService.getAllUsers();
        assertThat(allUsers).hasSize(1);

        User user = allUsers.get(0);

        new AssertUserFields(user).invoke();
    }

    @Test
    void shouldGetUser() throws Exception {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");
        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));

        Optional<User> userOptional = userService.getUser(annaUid);

        assertThat(userOptional.isPresent()).isTrue();
        User user = userOptional.get();

        new AssertUserFields(user).invoke();


    }

    @Test
    void shouldUpdateUser() throws Exception {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.updateUser(anna)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(anna);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).updateUser(captor.capture());

        User user = captor.getValue();
        new AssertUserFields(user);

        assertThat(updateResult).isEqualTo(1);

    }

    @Test
    void shouldRemoveUser() throws Exception {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.deleteUserByUserUid(annaUid)).willReturn(1);

        int deleteResult = userService.removeUser(annaUid);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).deleteUserByUserUid(annaUid);

        assertThat(deleteResult).isEqualTo(1);

    }

    @Test
    void shouldInsertUser() throws Exception {
        User anna = new User(null, "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");

        given(fakeDataDao.insertUser(any(UUID.class), eq(anna))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int insertResult = userService.insertUser(anna);

        verify(fakeDataDao).insertUser(any(UUID.class), captor.capture());

        User user = captor.getValue();

        new AssertUserFields(user);
        assertThat(insertResult).isEqualTo(1);
    }

    private class AssertUserFields {
        private User user;

        public AssertUserFields(User user) {
            this.user = user;
        }

        public void invoke() {
            assertThat(user.getAge()).isEqualTo(20);
            assertThat(user.getFirstName()).isEqualTo("Anna");
            assertThat(user.getLastName()).isEqualTo("Aser");
            assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
            assertThat(user.getEmail()).isEqualTo("asdfgh@gmail.com");
            assertThat(user.getUserUid()).isInstanceOf(UUID.class);
            assertThat(user.getUserUid()).isNotNull();
        }
    }
}