package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.mapper.StoryDto;
import com.javarush.led.lesson10.model.story.StoryIn;
import com.javarush.led.lesson10.model.story.StoryOut;
import com.javarush.led.lesson10.repository.StoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StoryService {

    public final StoryRepo storyRepo;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public final StoryDto mapper;

    public List<StoryOut> getAll() {
        return storyRepo
                .getAll()
                .map(mapper::out)
                .toList();
    }

    public StoryOut get(Long id) {
        return storyRepo
                .get(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public StoryOut create(StoryIn input) {
        return storyRepo
                .create(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public StoryOut update(StoryIn input) {
        return storyRepo
                .update(input.getId(), mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public boolean delete(Long id) {
        return storyRepo.delete(id);
    }
}
