package ru.marimax.crossroadsbackend.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    @Schema(description = "Номер текущей страницы")
    private int number;

    @Schema(description = "Количество элементов на текущей странице")
    private int numberOfElements;

    @Schema(description = "Является ли данная страница первой")
    private boolean first;

    @Schema(description = "Является ли данная страница последней")
    private boolean last;

    @Schema(description = "Имеется ли следующая страница")
    private boolean hasNext;

    @Schema(description = "Имеется ли предыдущая страница")
    private boolean hasPrevious;

    @Schema(description = "Количество страниц")
    private int totalPages;

    @Schema(description = "Общее количество элементов")
    private long totalElements;

    @Schema(description = "Имеется ли содержимое")
    private boolean hasContent;

    @Schema(description = "Содержимое в виде списка элементов")
    private List<T> content;


    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return Page.<T>builder()
                .number(page.getNumber())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasContent(page.hasContent())
                .content(page.getContent())
                .build();
    }

}


