package com.example.bankapp.distinguish.faceDistinguish.face.person.face;


import com.example.bankapp.distinguish.faceDistinguish.face.YtPerson;

import java.util.List;

public class YtDelface extends YtPerson {

    private String session_id;
    private int deleted;
    private List<String> face_ids;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }
}
