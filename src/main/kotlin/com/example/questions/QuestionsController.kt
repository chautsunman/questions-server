package com.example.questions

import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/questions")
class QuestionsController {
    @GetMapping("/questions")
    fun questions(): ApiResult {
        var questions = mutableListOf(
                Question("1", "Question 1"),
                Question("2", "Question 2"),
                Question("3", "Question 3"),
                Question("4", "Question 4"),
                Question("5", "Question 5")
        )

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(@RequestBody question: Question): ApiResult {
        print("Add question $question")

        return ApiResult(true, null)
    }
}