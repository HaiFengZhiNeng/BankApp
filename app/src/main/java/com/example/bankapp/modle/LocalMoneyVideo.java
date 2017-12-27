package com.example.bankapp.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dell on 2017/12/21.
 */

@Entity
public class LocalMoneyVideo {

    @Id
    private Long id;
    private String videoName;
    private String videoAddress;
    @Generated(hash = 739421474)
    public LocalMoneyVideo(Long id, String videoName, String videoAddress) {
        this.id = id;
        this.videoName = videoName;
        this.videoAddress = videoAddress;
    }
    public LocalMoneyVideo( String videoName, String videoAddress) {
        this.videoName = videoName;
        this.videoAddress = videoAddress;
    }
    @Generated(hash = 1000076068)
    public LocalMoneyVideo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getVideoName() {
        return this.videoName;
    }
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
    public String getVideoAddress() {
        return this.videoAddress;
    }
    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    
}
