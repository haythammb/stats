package com.hmohtasseb.stats.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hmohtasseb.stats.util.LocalTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {

    @JsonProperty( value = "amount", required = true)
    private double amount;

    @JsonProperty( value = "time" , required = true)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

    public Transaction(double amount, LocalTime time) {
        this.amount = amount;
        this.time = time;
    }

    public long getDurationInSec() {
        return SECONDS.between(time, LocalTime.now());
    }


}
