import model.Question;
import model.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuizTest {
e
    Quiz quiz;

    @BeforeEach
    void setup(){
        quiz = new Quiz("Math");
        quiz.addQuestion(new Question("2+2=?", List.of("3","4","5","6"),1));
        quiz.addQuestion(new Question("3*3=?", List.of("6", "7", "8", "9"), 3));
    }

    @Test
    @DisplayName("Score is correct when all answers are correct.")
    void testFullScore(){
        assertEquals(2,quiz.calculateScore(List.of(1,3)));
    }

    @Test
    @DisplayName("Throws an exception if answer list size mismatches")
    void testMismatchedAnswers(){
        assertThrows(IllegalArgumentException.class, ()-> quiz.calculateScore(List.of(1)));
    }

    @Test
    @DisplayName("Empty quiz handled gracefully.")
    void testEmptyQuiz(){
        Quiz emptyQuiz = new Quiz("Empty");
        assertEquals(0,emptyQuiz.calculateScore(List.of()));
    }
}
