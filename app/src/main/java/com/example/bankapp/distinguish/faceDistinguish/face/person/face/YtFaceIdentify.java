package com.example.bankapp.distinguish.faceDistinguish.face.person.face;


import com.example.bankapp.distinguish.faceDistinguish.face.YtPerson;

import java.util.ArrayList;

public class YtFaceIdentify extends YtPerson {

    public String session_id;
    public ArrayList<IdentifyItem> candidates;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public ArrayList<IdentifyItem> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<IdentifyItem> candidates) {
        this.candidates = candidates;
    }
}
