package com.javarush.led.lesson10.service;

import com.javarush.led.lesson10.mapper.TagDto;
import com.javarush.led.lesson10.model.tag.TagIn;
import com.javarush.led.lesson10.model.tag.TagOut;
import com.javarush.led.lesson10.repository.TagRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    public final TagRepo
            tagRepo;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public final TagDto mapper;

    public List<TagOut> getAll() {
        return tagRepo
                .getAll()
                .map(mapper::out)
                .toList();
    }

    public TagOut get(Long id) {
        return tagRepo
                .get(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public TagOut create(TagIn input) {
        return tagRepo
                .create(mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public TagOut update(TagIn input) {
        return tagRepo
                .update(input.getId(), mapper.in(input))
                .map(mapper::out)
                .orElseThrow();
    }

    public boolean delete(Long id) {
        return tagRepo.delete(id);
    }
}
