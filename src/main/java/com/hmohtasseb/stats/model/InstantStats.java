package com.hmohtasseb.stats.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InstantStats {
    private final Double min;
    private final Double max;
    private final Double sum;
    private final Integer count;

    public Double avg() {
        if (count == 0)
            return 0.0;

        return sum / (double)count;
    }
}
