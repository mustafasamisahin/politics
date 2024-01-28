package dev.sami.politics.controller

import dev.sami.politics.model.Response
import dev.sami.politics.service.EvaluationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException

@RestController
@RequestMapping("/evaluation")
class EvaluationController(
    private val evaluationService: EvaluationService
) {

    @GetMapping
    fun evaluateSpeeches(@RequestParam urls: Map<String, String>): ResponseEntity<Any> {
        val responseList = mutableListOf<Response>()
        for (url in urls.entries) {
            try {
                val response = evaluationService.evaluateSpeech(url.toPair())
                responseList.add(response)
            } catch (e: FileNotFoundException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't fetch from ${url.key}: ${e.message}")
            } catch (e: Exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: ${e.message}")
            }
        }

        return if (responseList.isNotEmpty()) {
            ResponseEntity.ok(responseList)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}