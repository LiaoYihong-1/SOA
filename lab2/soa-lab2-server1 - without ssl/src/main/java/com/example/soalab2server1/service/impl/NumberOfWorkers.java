package com.example.soalab2server1.service.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NumberOfWorkers {
        private Integer number;
        public NumberOfWorkers(Integer num){
            this.number = num;
        }
}
