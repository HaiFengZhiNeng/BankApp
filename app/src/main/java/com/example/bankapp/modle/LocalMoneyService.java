package com.example.bankapp.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * 理财产品 -- 理财介绍
 *
 * @author Guanluocang
 *         created at 2017/12/21 9:43
 */
@Entity
public class LocalMoneyService {
    @Id
    private Long id;
    private String introduceQuestion;
    private String introduceAnswer;

    @Generated(hash = 1351244576)
    public LocalMoneyService(Long id, String introduceQuestion,
                             String introduceAnswer) {
        this.id = id;
        this.introduceQuestion = introduceQuestion;
        this.introduceAnswer = introduceAnswer;
    }

    public LocalMoneyService(String introduceQuestion,
                             String introduceAnswer) {
        this.introduceQuestion = introduceQuestion;
        this.introduceAnswer = introduceAnswer;
    }

    @Generated(hash = 1947646929)
    public LocalMoneyService() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntroduceQuestion() {
        return this.introduceQuestion;
    }

    public void setIntroduceQuestion(String introduceQuestion) {
        this.introduceQuestion = introduceQuestion;
    }

    public String getIntroduceAnswer() {
        return this.introduceAnswer;
    }

    public void setIntroduceAnswer(String introduceAnswer) {
        this.introduceAnswer = introduceAnswer;
    }


}
