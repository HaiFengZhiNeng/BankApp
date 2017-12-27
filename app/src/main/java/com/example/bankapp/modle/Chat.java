package com.example.bankapp.modle;

/**
 * 聊天实体
 *
 * @author Guanluocang
 *         created at 2017/12/7 15:29
 */
public class Chat {
    private String massage;//文本内容
    private int type;//消息类型

    public Chat(String massage, int type) {
        this.massage = massage;
        this.type = type;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
