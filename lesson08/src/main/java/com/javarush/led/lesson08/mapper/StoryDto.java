package com.javarush.led.lesson08.mapper;

import com.javarush.led.lesson08.model.story.Story;
import com.javarush.led.lesson08.model.story.StoryIn;
import com.javarush.led.lesson08.model.story.StoryOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoryDto {
    StoryOut out(Story entity);

    Story in(StoryIn inputDto);
}
