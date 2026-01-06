package com.javarush.lesson17.service;

import com.javarush.lesson17.entity.User;
import com.javarush.lesson17.repository.Repo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService {

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
