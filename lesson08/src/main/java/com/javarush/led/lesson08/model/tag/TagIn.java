package com.javarush.led.lesson08.model.tag;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagIn {
    @Positive
    Long id;

    @Size(min = 2, max = 32)
    String name;
}
