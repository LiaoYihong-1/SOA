package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "SortedWorkersResponse")  // 定义XML根元素的名称
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    @JacksonXmlElementWrapper(localName = "content")
    @JacksonXmlProperty(localName = "WorkerFullInfo")
    private List<T> content;
    private int pagenumber;
    private int numberOfElements;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;
    private long totalElements;
    private boolean hasContent;
    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return Page.<T>builder()
                .pagenumber(page.getNumber())
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
