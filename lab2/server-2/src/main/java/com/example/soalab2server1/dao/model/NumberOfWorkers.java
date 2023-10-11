package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "NumberOfWorkers")
public class NumberOfWorkers {
        private Integer number;
        public NumberOfWorkers(Integer num){
            this.number = num;
        }
}
