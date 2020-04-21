package com.gistMED.gistmd.Classes;

public class CaseQuestion {
    private String labelID;
    private String answerType;
    private String questionID;

    public CaseQuestion(String labelID, String answerType,String questionID) {
        this.labelID = labelID;
        this.answerType = answerType;
        this.questionID = questionID;
    }

    public String getLabelID() {
        return labelID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }
}
