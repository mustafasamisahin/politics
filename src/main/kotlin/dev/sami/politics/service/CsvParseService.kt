package dev.sami.politics.service

import dev.sami.politics.model.PoliticalSpeech
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.io.FileReader
import java.io.Reader
import java.nio.file.Paths


@Service
class CsvParseService {

    fun fetchAndParseCsv(url: Pair<String, String>): Pair<String, List<PoliticalSpeech>> {
        val politicalSpeeches = mutableListOf<PoliticalSpeech>()
        val urlNo = url.first

        try {
            val csvContent = fetchCsvContent(url.second)
            val csvMap = CSVParser.parse(
                csvContent, CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setDelimiter(";")
                    .setHeader("Speaker", "Topic", "Date", "Words")
                    .setIgnoreSurroundingSpaces(true)
                    .build()
            )

            politicalSpeeches.addAll(csvMap.records.map {
                PoliticalSpeech(
                    speaker = it["Speaker"]?.trim() ?: "",
                    topic = it["Topic"]?.trim() ?: "",
                    date = it["Date"]?.trim() ?: "",
                    words = it["Words"]?.trim()?.toIntOrNull() ?: 0
                )
            })

        } catch (e: Exception) {
            println("Error processing CSV from ${url.second}: Exception= $e")
            throw e
        }

        return urlNo to politicalSpeeches
    }

    private fun fetchCsvContent(url: String): String {
        return if (url.startsWith("http")) {
            val restTemplate = RestTemplate()
            restTemplate.getForObject(url, String::class.java)
                ?: throw RestClientException("Failed to fetch CSV content from $url")
        } else {
            val fileReader: Reader = FileReader(Paths.get("src/main/resources", url).toString())
            val csvParser = CSVParser(
                fileReader, CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setDelimiter(";")
                    .setHeader("Speaker", "Topic", "Date", "Words")
                    .setIgnoreSurroundingSpaces(true)
                    .setSkipHeaderRecord(true)
                    .build()
            )
            val csvContent = csvParser.records.joinToString("\n") { it.joinToString(";") }
            fileReader.close()
            csvParser.close()
            csvContent
        }
    }
}