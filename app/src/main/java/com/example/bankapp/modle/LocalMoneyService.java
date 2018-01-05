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
    private String introduceType;//类型
    private String introduceQuestion;//问题
    private String introduceAnswer;//答案
    private String introduceAction;//动作
    private String introduceActionData;//动作
    private String introduceExpression;//表情
    private String introduceExpressionData;//表情

    @Generated(hash = 1714746742)
    public LocalMoneyService(Long id, String introduceType,
                             String introduceQuestion, String introduceAnswer,
                             String introduceAction, String introduceActionData,
                             String introduceExpression, String introduceExpressionData) {
        this.id = id;
        this.introduceType = introduceType;
        this.introduceQuestion = introduceQuestion;
        this.introduceAnswer = introduceAnswer;
        this.introduceAction = introduceAction;
        this.introduceActionData = introduceActionData;
        this.introduceExpression = introduceExpression;
        this.introduceExpressionData = introduceExpressionData;
    }

    public LocalMoneyService(String introduceType,
                             String introduceQuestion, String introduceAnswer,
                             String introduceAction, String introduceActionData,
                             String introduceExpression, String introduceExpressionData) {
        this.introduceType = introduceType;
        this.introduceQuestion = introduceQuestion;
        this.introduceAnswer = introduceAnswer;
        this.introduceAction = introduceAction;
        this.introduceActionData = introduceActionData;
        this.introduceExpression = introduceExpression;
        this.introduceExpressionData = introduceExpressionData;
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

    public String getIntroduceType() {
        return this.introduceType;
    }

    public void setIntroduceType(String introduceType) {
        this.introduceType = introduceType;
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

    public String getIntroduceAction() {
        return this.introduceAction;
    }

    public void setIntroduceAction(String introduceAction) {
        this.introduceAction = introduceAction;
    }

    public String getIntroduceActionData() {
        return this.introduceActionData;
    }

    public void setIntroduceActionData(String introduceActionData) {
        this.introduceActionData = introduceActionData;
    }

    public String getIntroduceExpression() {
        return this.introduceExpression;
    }

    public void setIntroduceExpression(String introduceExpression) {
        this.introduceExpression = introduceExpression;
    }

    public String getIntroduceExpressionData() {
        return this.introduceExpressionData;
    }

    public void setIntroduceExpressionData(String introduceExpressionData) {
        this.introduceExpressionData = introduceExpressionData;
    }


}
