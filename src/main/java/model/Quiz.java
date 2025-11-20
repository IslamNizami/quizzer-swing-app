package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {

    private String title;
    private List<Question> questions;

    public Quiz(String title){
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title of quiz  cannot be null or empty");
        }

        this.title = title;
        this.questions = new ArrayList<Question>();
    }

    public void addQuestion(Question question){
        if(question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        questions.add(question);
    }

    public void removeQuestion(int index){
        if(index < 0 || index >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid question index");
        }
        questions.remove(index);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title of quiz  cannot be null or empty");
        }

        this.title = title;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public int size(){
        return questions.size();
    }

    public int calculateScore(List<Integer> answers){
        if(answers == null || answers.size() != questions.size()) {
            throw new IllegalArgumentException("Answer list size must match number of questions.");
        }

        int score = 0;
        for(int i = 0; i < questions.size(); i++){
            if(questions.get(i).isCorrect(answers.get(i))) {
                score++;
            }
        }
        return score;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "title='" + title + '\'' +
                ", questions=" + questions.size() +
                '}';
    }
}

