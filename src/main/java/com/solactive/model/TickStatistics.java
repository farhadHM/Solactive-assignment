package com.solactive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Data
@NoArgsConstructor
public class TickStatistics {
    private Double avg;
    private Double min;
    private Double max;
    private long count = 0;

    @JsonIgnore
    private double sum = 0;

    public TickStatistics(double avg, double min, double max, long count) {
        this.avg = avg;
        this.min = min;
        this.max = max;
        this.count = count;
    }

    private void addMin(double price) {
        if (this.min == null)
            this.min = price;
        else if (this.min > price)
            min = price;
    }

    private void addMax(double price) {
        if (this.max == null)
            this.max = price;
        else if (max < price)
            max = price;
    }

    private void addAvg(double price) {
        sum += price;
        count++;
        avg = sum / count;
    }

    public void updateStatistics(Tick tick) {
        addMin(tick.getPrice());
        addMax(tick.getPrice());
        addAvg(tick.getPrice());
    }

    private void refreshMin(Tick tick) {
        if (this.min.equals(tick.getPrice())) {
            min = tick.getNext().getPrice();
        }
    }

    private void refreshMax(Tick tick) {
        if (max.equals(tick.getPrice()))
            max = tick.getPrev().getPrice();
    }

    private void refreshAvg(double price) {
        sum -= price;
        count--;
        avg = sum / count;
    }

    public void refreshStatistics(Tick tick) {
        refreshMin(tick);
        refreshMax(tick);
        refreshAvg(tick.getPrice());
    }


}
