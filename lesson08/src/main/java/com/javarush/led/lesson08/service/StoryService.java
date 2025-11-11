package com.javarush.led.lesson08.service;

import com.javarush.led.lesson08.mapper.StoryDto;
import com.javarush.led.lesson08.model.story.StoryIn;
import com.javarush.led.lesson08.model.story.StoryOut;
import com.javarush.led.lesson08.repository.StoryRepoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StoryService {

    public final StoryRepoImpl repoImpl;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public final StoryDto mapper;

    public List<StoryOut> getAll() {
        return repoImpl
                .getAll()
                .map(mapper::out)
                .toList();
    }

    public StoryOut get(Long id) {
        return repoImpl
                .get(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public StoryOut create(StoryIn input) {
        return repoImpl
                .create(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public StoryOut update(StoryIn input) {
        return repoImpl
                .update(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public boolean delete(Long id) {
        return repoImpl.delete(id);
    }
}
