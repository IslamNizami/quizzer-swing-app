package model;

import Utility.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final  List<Quiz> quizzes = new ArrayList<Quiz>();


    public void addQuiz(Quiz quiz) {
        if(quiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null.");
        }
        quizzes.add(quiz);
    }

    public void removeQuiz(String title) {
        quizzes.removeIf(q->q.getTitle().equalsIgnoreCase(title));
    }

    public List<Quiz> getQuizzes(){
        return new ArrayList<>(quizzes);
    }

    public void clear(){
        quizzes.clear();
    }

    public void saveToFile(File file) throws IOException {
        FileUtils.saveObjectToFile(quizzes, file);
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(File file) throws IOException ,ClassNotFoundException{
        quizzes.clear();
        quizzes.addAll((List<Quiz>) FileUtils.loadObjectFromFile(file));
    }


    @Override
    public String toString() {
        return "QuizManager{" +
                "quizzes=" + quizzes.size() +
                '}';
    }
}
