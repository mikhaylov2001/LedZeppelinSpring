package com.javarush.lesson19.service;

import com.javarush.lesson19.entity.User;
import com.javarush.lesson19.repository.Repo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final Repo repo;

    public Optional<User> get(Long id) {
        return repo.findById(id);
    }

    public List<User> findAll() {
        Sort sort=Sort.sort(User.class).by(User::getId);
        return repo.findAll(sort);
    }

    @Transactional
    public User save(User user) {
        String original = user.getPassword();
        String encode = passwordEncoder.encode(original);
        user.setPassword(encode);
        return repo.saveAndFlush(user);
    }

    @Transactional
    public void delete(User user) {
        repo.delete(user);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }


}
