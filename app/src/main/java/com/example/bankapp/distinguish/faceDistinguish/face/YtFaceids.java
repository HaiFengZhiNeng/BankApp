package com.example.bankapp.distinguish.faceDistinguish.face;

import java.util.List;

public class YtFaceids extends YtPerson {

    private List<String> face_ids;

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    @Override
    public String toString() {
        return "YtFaceids{" +
                "face_ids=" + face_ids +
                '}';
    }
}
