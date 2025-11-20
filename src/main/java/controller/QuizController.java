package controller;

import Utility.FileUtils;
import model.Question;
import model.Quiz;
import model.QuizManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class QuizController {

    private final QuizManager manager = new QuizManager();

    public Quiz createQuiz(String title){
        Quiz quiz = new Quiz(title);
        manager.addQuiz(quiz);
        return quiz;
    }

   public void removeQuiz(String title){
        manager.removeQuiz(title);
   }

   public List<Quiz> getAllQuizzes(){
        return manager.getQuizzes();
   }

   public void addQuestionToQuiz(Quiz quiz, Question question){
        if(quiz == null || question == null ){
            throw new IllegalArgumentException("Quiz and Question cannot be null");
        }

        quiz.addQuestion(question);
   }

   public void saveToFile(File file) throws IOException {
        manager.saveToFile(file);
   }

   public void loadFromFile(File file) throws IOException,ClassNotFoundException{
        manager.loadFromFile(file);
   }

   public void clearAll(){
        manager.clear();
   }

    @Override
    public String toString() {
        return "QuizController{" +
                "quizzes=" + manager.getQuizzes().size() +
                '}';
    }



}
