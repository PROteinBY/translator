package by.bstu.feis.ii12.model;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LangFunc {

    private Integer funcId;
    private String fTxtRus;
    private String fTxtEng;
    private List<String> rusPlaceHolders;
    private List<String> engPlaceHolders;
    private HashMap<String, String> suitableWords;

    public LangFunc() {
    }

    public LangFunc(Integer funcId, String fTxtRus, String fTxtEng, List<String> rusPlaceHolders, List<String> engPlaceHolders, HashMap<String, String> suitableWords) {
        this.funcId = funcId;
        this.fTxtRus = fTxtRus;
        this.fTxtEng = fTxtEng;
        this.rusPlaceHolders = rusPlaceHolders;
        this.engPlaceHolders = engPlaceHolders;
        this.suitableWords = suitableWords;
    }

    public String getRegexString() {
        String regex = fTxtRus;

        for (String rusPlaceHolder : rusPlaceHolders) {
            regex.replaceAll(rusPlaceHolder, "(?<" + rusPlaceHolder + ">[а-яА-Я\\-]*)");
        }

        return regex;
    }

    public void addWord(String rus, String eng) {
        suitableWords.put(rus, eng);
    }

    public Integer getFuncId() {
        return funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public String getfTxtRus() {
        return fTxtRus;
    }

    public void setfTxtRus(String fTxtRus) {
        this.fTxtRus = fTxtRus;
    }

    public String getfTxtEng() {
        return fTxtEng;
    }

    public void setfTxtEng(String fTxtEng) {
        this.fTxtEng = fTxtEng;
    }

    public List<String> getRusPlaceHolders() {
        return rusPlaceHolders;
    }

    public void setRusPlaceHolders(List<String> rusPlaceHolders) {
        this.rusPlaceHolders = rusPlaceHolders;
    }

    public List<String> getEngPlaceHolders() {
        return engPlaceHolders;
    }

    public void setEngPlaceHolders(List<String> engPlaceHolders) {
        this.engPlaceHolders = engPlaceHolders;
    }

    public HashMap<String, String> getSuitableWords() {
        return suitableWords;
    }

    public void setSuitableWords(HashMap<String, String> suitableWords) {
        this.suitableWords = suitableWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LangFunc langFunc = (LangFunc) o;
        return Objects.equals(funcId, langFunc.funcId) &&
                Objects.equals(fTxtRus, langFunc.fTxtRus) &&
                Objects.equals(fTxtEng, langFunc.fTxtEng) &&
                Objects.equals(rusPlaceHolders, langFunc.rusPlaceHolders) &&
                Objects.equals(engPlaceHolders, langFunc.engPlaceHolders) &&
                Objects.equals(suitableWords, langFunc.suitableWords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(funcId, fTxtRus, fTxtEng, rusPlaceHolders, engPlaceHolders, suitableWords);
    }

    @Override
    public String toString() {
        return "LangFunc{" +
                "funcId=" + funcId +
                ", fTxtRus='" + fTxtRus + '\'' +
                ", fTxtEng='" + fTxtEng + '\'' +
                ", rusPlaceHolders=" + rusPlaceHolders +
                ", engPlaceHolders=" + engPlaceHolders +
                ", suitableWords=" + suitableWords +
                '}';
    }
}
