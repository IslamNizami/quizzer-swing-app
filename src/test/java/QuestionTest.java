import model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    @DisplayName("Correct answer returns true, incorrect answer returns false.")
    void testIsCorrect(){
        Question question = new Question("2+2=?", List.of("3","4","5","6"),1);
        assertTrue(question.isCorrect(1));
        assertFalse(question.isCorrect(0));
    }

    @Test
    @DisplayName("Invalid correct index throws exception")
    void testInvalidIndex(){
        assertThrows(IllegalArgumentException.class, () -> new Question("Bad",List.of("A","B"),5));
    }

    @Test
    @DisplayName("Question stores options correctly.")
    void testOptionsStoredProperly(){
        Question  question = new Question("Capital of France?",List.of("Paris","Berlin","Rome","Madrid"),0);
        assertEquals("Paris",question.getOptions().get(0));
        assertEquals(4,question.getOptions().size());
    }
}
