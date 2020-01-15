package com.eliise.learningspringboot.Dao;

import com.eliise.learningspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @BeforeEach
    public void setUp() throws Exception {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    public void shouldSelectAllUsers() throws Exception {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("joe.jones@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    public void shouldSelectUserByUserUid() throws Exception {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(UUID.randomUUID(), "Anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");
        fakeDataDao.insertUser(annaUserUid, anna);
        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);
        Optional<User> annaOptional = fakeDataDao.selectUserByUserUid(annaUserUid);
        assertThat(annaOptional.isPresent()).isTrue();
        assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);
    }

    @Test
    public void shouldNotSelectUserByUserUid() throws Exception {
        Optional<User> user = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        User newJoe = new User(joeUserUid, "anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");
        fakeDataDao.updateUser(newJoe);
        Optional<User> optionalUser = fakeDataDao.selectUserByUserUid(joeUserUid);
        assertThat(optionalUser.isPresent()).isTrue();

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(optionalUser.get()).isEqualToComparingFieldByField(newJoe);

    }

    @Test
    public void deleteUserByUserUid() throws Exception {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();

        fakeDataDao.deleteUserByUserUid(joeUserUid);

        assertThat(fakeDataDao.selectUserByUserUid(joeUserUid).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers().isEmpty());
    }

    @Test
    public void insertUser() throws Exception {
        UUID userUid = UUID.randomUUID();
        User user = new User(UUID.randomUUID(), "anna", "Aser",
                User.Gender.FEMALE, 20, "asdfgh@gmail.com");
        fakeDataDao.insertUser(userUid, user);
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
        assertThat(fakeDataDao.selectUserByUserUid(userUid).get()).isEqualToComparingFieldByField(user);
    }
}