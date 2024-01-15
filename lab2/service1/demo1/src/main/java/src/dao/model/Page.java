package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@JacksonXmlRootElement(localName = "SortedWorkersResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Page<T> implements Serializable {
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
}
