package com.meboo.birthcontrol;

import java.io.Serializable;

/**
 * Created by ngocbeo1121 on 3/11/16.
 */
public class NextRemind implements Serializable {

    public long timestamp;
    public boolean isPlaceboPill;

    public NextRemind(long timestamp, boolean isPlaceboPill) {
        this.timestamp = timestamp;
        this.isPlaceboPill = isPlaceboPill;
    }

}
