package com.example.questions

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/questions")
class QuestionsController {
    @GetMapping("/questions")
    fun questions(): MutableList<Question> {
        var questions = mutableListOf(
                Question("1", "Question 1"),
                Question("2", "Question 2"),
                Question("3", "Question 3"),
                Question("4", "Question 4"),
                Question("5", "Question 5")
        )

        return questions
    }
}