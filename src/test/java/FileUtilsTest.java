
import model.Question;
import model.Quiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Utility.FileUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    @DisplayName("Save and load single Quiz object successfully")
    void testSaveAndLoadSingleQuiz() throws Exception {
        Quiz quiz = new Quiz("Sample");
        quiz.addQuestion(new Question("A?", List.of("1", "2", "3", "4"), 2));

        File file = new File("testQuiz_single.ser");
        FileUtils.saveObjectToFile(quiz, file);

        assertTrue(file.exists(), "Serialized file should exist after saving.");

        Object loadedObj = FileUtils.loadObjectFromFile(file);
        assertInstanceOf(Quiz.class, loadedObj, "Loaded object should be a Quiz.");
        Quiz loadedQuiz = (Quiz) loadedObj;

        assertEquals("Sample", loadedQuiz.getTitle());
        assertEquals(1, loadedQuiz.getQuestions().size());

        file.delete();
    }

    @Test
    @DisplayName("Save and load list of quizzes successfully")
    void testSaveAndLoadQuizList() throws Exception {
        Quiz q1 = new Quiz("Math");
        Quiz q2 = new Quiz("Science");
        List<Quiz> quizList = List.of(q1, q2);

        File file = new File("testQuiz_list.ser");
        FileUtils.saveObjectToFile(quizList, file);

        Object loadedObj = FileUtils.loadObjectFromFile(file);
        assertInstanceOf(List.class, loadedObj);
        List<?> loadedList = (List<?>) loadedObj;

        assertEquals(2, loadedList.size());
        assertEquals("Math", ((Quiz) loadedList.get(0)).getTitle());
        file.delete();
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when saving null object")
    void testSaveNullObject() {
        assertThrows(IllegalArgumentException.class, () ->
                FileUtils.saveObjectToFile(null, new File("x.ser")));
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when loading non-existing file")
    void testLoadNonExistingFile() {
        File file = new File("does_not_exist.ser");
        assertThrows(IllegalArgumentException.class, () ->
                FileUtils.loadObjectFromFile(file));
    }
}
