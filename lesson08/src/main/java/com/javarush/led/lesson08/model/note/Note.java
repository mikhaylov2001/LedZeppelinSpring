package com.javarush.led.lesson08.model.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    Long id;
    Long storyId;
    String content;


}
