package com.example.bankapp.modle;

/**
 * Created by dell on 2017/12/8.
 */
/**
 * 取号 排队
 *@author Guanluocang
 *created at 2017/12/11 15:59
*/
public class RegisterLineUp {
    private String type ;
    private String lineNum;

    public RegisterLineUp(String type, String lineNum) {
        this.type = type;
        this.lineNum = lineNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }
}
