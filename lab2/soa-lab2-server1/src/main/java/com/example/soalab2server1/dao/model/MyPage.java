package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@JacksonXmlRootElement(localName = "SortedWorkersResponse")  // 定义XML根元素的名称
@Data
@NoArgsConstructor
public class MyPage<T> {
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
    private boolean hasContenet;

    public void buildWithPage(Page<T> p){
        content = p.getContent();
        pagenumber = p.getNumber();
        numberOfElements = p.getNumberOfElements();
        first = p.isFirst();
        last = p.isLast();
        hasContenet = p.hasContent();
        hasNext = p.hasNext();
        hasPrevious = p.hasPrevious();
        totalElements = p.getTotalElements();
        totalPages = p.getTotalPages();
    }
}
