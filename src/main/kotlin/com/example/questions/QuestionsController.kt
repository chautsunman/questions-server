package com.example.questions

import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/questions")
class QuestionsController(
        private val questionsService: QuestionsService
) {
    @GetMapping("/questions")
    fun questions(): ApiResult {
        var questions = questionsService.getQuestions()

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(@RequestBody question: Question): ApiResult {
        print("Add question $question")

        return ApiResult(true, null)
    }
}