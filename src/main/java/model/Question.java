package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setText(String text) {
        if(text == null || text.isBlank()){
            throw new IllegalArgumentException("Questiont text cannot be null or blank");
        }
        this.text = text;
    }

    public void setOption(int index, String value) {
        if(index < 0 || index >= 4){
            throw new IllegalArgumentException("Option index must be between 0 and 3");
        }
        if(value == null){
            value = "";
        }
        options.set(index, value);
    }

    public void setCorrectIndex(int index) {
        if (index < 0 || index >= 4){
            throw new IllegalArgumentException("Correct index must be between 0 and 3");
        }
        this.correctIndex = index;
    }

    public Question(String text, List<String> options, int correctIndex){
        if(text == null || text.isBlank()){
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if(options == null || options.size() != 4){
            throw new IllegalArgumentException("Exactly 4 options are required.");
        }
        if(correctIndex < 0 || correctIndex > 3){
            throw new IllegalArgumentException("Correct index must be between 0 and 3.");
        }

        this.text = text;
        this.options = new ArrayList<>(options);
        this.correctIndex = correctIndex;
    }

   public boolean isCorrect(int chosenIndex){
        return chosenIndex == correctIndex;
   }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", correctIndex=" + correctIndex +
                ", options=" + options +
                '}';
    }

    private String text;
    private List<String> options;
    private int correctIndex;


}
