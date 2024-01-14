package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "NumberOfWorkers")
@AllArgsConstructor
public class NumberOfWorkers {
        private Integer number;
}
