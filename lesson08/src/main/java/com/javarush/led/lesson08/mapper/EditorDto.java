package com.javarush.led.lesson08.mapper;

import com.javarush.led.lesson08.model.editor.Editor;
import com.javarush.led.lesson08.model.editor.EditorIn;
import com.javarush.led.lesson08.model.editor.EditorOut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditorDto {
    EditorOut out(Editor entity);

    Editor in(EditorIn inputDto);
}
