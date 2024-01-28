package dev.sami.politics.service

import dev.sami.politics.model.PoliticalSpeech
import dev.sami.politics.model.Response
import org.springframework.stereotype.Service

@Service
class EvaluationService(private val csvParseService: CsvParseService) {
    fun evaluateSpeech(url: Pair<String, String>): Response {
        val speechMap = csvParseService.fetchAndParseCsv(url)

        val urlNo = speechMap.first
        val speechList = speechMap.second

        return Response(
            urlNo,
            findMostSpeeches(speechList),
            findMostSecurity(speechList),
            findLeastWordy(speechList)
        )
    }

    private fun findMostSpeeches(speechList: List<PoliticalSpeech>): String? {
        val speechesInYear = speechList.filter { it.date.startsWith("2013-") }
        return if (speechesInYear.isNotEmpty()) {
            speechesInYear.groupingBy { it.speaker }.eachCount().maxByOrNull { it.value }?.key
        } else {
            null
        }
    }

    private fun findMostSecurity(speechList: List<PoliticalSpeech>): String? {
        val securitySpeeches = speechList.filter { it.topic.contains("security") }
        return if (securitySpeeches.isNotEmpty()) {
            securitySpeeches.groupingBy { it.speaker }
                .eachCount()
                .maxByOrNull { it.value }?.key
        } else {
            null
        }
    }

    private fun findLeastWordy(speechList: List<PoliticalSpeech>): String? {
        return speechList.groupBy { it.speaker }
            .mapValues { entry -> entry.value.sumOf { it.words } }
            .minByOrNull { it.value }?.key
    }
}