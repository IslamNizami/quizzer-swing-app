import controller.QuizController;
import model.Question;
import model.Quiz;

import java.io.File;
import java.util.List;

public class QuizzerApplication {
    public static void main(String[] args) {
        try {
            QuizController controller = new QuizController();

            Quiz mathQuiz = controller.createQuiz("Math Basics");
            controller.addQuestionToQuiz(mathQuiz,
                    new Question("2+2=?", List.of("3", "4", "5", "6"), 1));
            controller.addQuestionToQuiz(mathQuiz,
                    new Question("3*3=?", List.of("6", "7", "8", "9"), 3));

            System.out.println("Created quizzes: " + controller.getAllQuizzes());
            int score = mathQuiz.calculateScore(List.of(1, 3));
            System.out.println("Score: " + score);

            // Optional save/load demonstration
            File file = new File("quizzes.ser");
            controller.saveToFile(file);
            System.out.println("Saved quizzes to " + file.getAbsolutePath());
            controller.clearAll();
            controller.loadFromFile(file);
            System.out.println("Reloaded quizzes: " + controller.getAllQuizzes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
