package com.solactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickStatistics {
    private double avg = 0;
    private double min = 0;
    private double max = 0;
    private long count = 0;
}
