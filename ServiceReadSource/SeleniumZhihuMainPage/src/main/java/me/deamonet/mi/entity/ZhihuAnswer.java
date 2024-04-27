package me.deamonet.mi.entity;

public class ZhihuAnswer extends Article{
    private long questionId;
    private long answerId;
    public ZhihuAnswer(String answerUrl, String questionTxt, String answerTxt, String votesFigureTxt){
        super();
        this.setTitle(questionTxt);
        this.setContent(answerTxt  + "\n" + votesFigureTxt);
        this.setLink(answerUrl);
    }
}
