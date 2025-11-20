
import controller.QuizController;
import model.Quiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizControllerTest {

    @Test
    @DisplayName("Controller creates and stores quizzes")
    void testCreateQuiz() {
        QuizController controller = new QuizController();
        Quiz quiz = controller.createQuiz("Math Test");
        assertNotNull(quiz);
        assertEquals("Math Test", quiz.getTitle());
        assertEquals(1, controller.getAllQuizzes().size());
    }

    @Test
    @DisplayName("Get all quizzes returns unmodifiable list copy")
    void testGetAllQuizzes() {
        QuizController controller = new QuizController();
        controller.createQuiz("History");
        List<Quiz> list = controller.getAllQuizzes();
        assertEquals(1, list.size());
    }
}
