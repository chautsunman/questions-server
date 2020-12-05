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
        val questions = questionsService.getQuestions()

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(@RequestBody question: Question): ApiResult {
        val res = questionsService.addQuestion(question)

        return ApiResult(true, res)
    }
}