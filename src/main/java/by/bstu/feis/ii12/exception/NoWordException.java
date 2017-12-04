package by.bstu.feis.ii12.exception;

public class NoWordException extends RuntimeException {

    private String word;

    public NoWordException(String message, String word) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
