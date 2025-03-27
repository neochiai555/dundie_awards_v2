package com.ninjaone.dundie_awards;

import org.springframework.stereotype.Component;

/**
 * Total dundie awards cache
 */
@Component
public class AwardsCache {
    private int totalAwards;

    public void setTotalAwards(int totalAwards) {
        this.totalAwards = totalAwards;
    }

    public int getTotalAwards(){
        return totalAwards;
    }

    public void addOneAward(){
        this.totalAwards += 1;
    }
    
    public void subtractOneAward(){
        this.totalAwards -= 1;
    }
}
