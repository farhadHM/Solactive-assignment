package com.solactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tick implements Comparable<Tick> {
    @NotBlank(message = "instrument is mandatory")
    private String instrument;

    @NotNull(message = "price is mandatory")
    private Double price;

    @NotNull(message = "timestamp is mandatory")
    private Long timestamp;

    @Override
    public int compareTo(Tick tick) {
        if (this.timestamp < tick.timestamp)
            return -1;
        if (this.timestamp.equals(tick.timestamp))
            return 0;
        return 1;
    }
}
