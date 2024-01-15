package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "Error")
public class Error implements Serializable {
    public Error(String message,Integer code){
        this.code = code;
        this.message = message;
    }
    public String message;
    public Integer code;
}
