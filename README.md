🎓 Quizzer — AI-Enhanced Java Quiz Application

A modular, extensible Java application for creating, managing, and taking quizzes — powered by AI-generated questions, clean architecture, JSON storage, and full JUnit test coverage.

This project is ideal for learning Java OOP, file handling, API integration, testing, and UML documentation.

🚀 Features

📝 Create custom quizzes

🤖 Generate questions automatically using the OpenAI API

💾 Save & load quizzes using JSON

🎯 Auto-evaluate quiz results

📦 Clean architecture (Model / Manager / Utility)

🧪 Fully tested with JUnit 5

📐 Includes UML Class & Sequence Diagrams

🛠️ Tech Stack
Component	Technology
Language	Java 17
Build Tool	Maven
Storage	JSON (Gson)
AI	OpenAI API
Testing	JUnit 5

🧩 How It Works
📘 Model Layer

Question
Stores the question text, four options, and the correct option index.

Quiz
Contains a list of questions and score calculation logic.

🧠 AI Generator

AiQuestionGenerator sends a prompt to the OpenAI API and receives:

question text

4 options

correct answer index

Results are converted into Question objects.

📂 Persistence

FileUtils handles JSON storage using Gson.

📋 QuizManager

Central controller that:

Creates quizzes

Adds questions

Requests AI-generated questions

Saves/loads quizzes

🧪 Unit Tests

All major components are tested:

✔ QuestionTest

Validates answer correctness, invalid index handling, and option storage.

✔ QuizTest

Ensures:

correct scoring

mismatched answer lists are rejected

empty quizzes are handled safely

Uses @BeforeEach to rebuild a fresh Quiz object before every test.

✔ FileUtilsTest

Confirms correct JSON serialization/deserialization and error handling.

▶️ Running the Project
Build
mvn clean package

Run
java -jar target/Quizzer-1.0-SNAPSHOT.jar

📜 License

This project is open-source and free to modify.

Made by Islam Nizami.
