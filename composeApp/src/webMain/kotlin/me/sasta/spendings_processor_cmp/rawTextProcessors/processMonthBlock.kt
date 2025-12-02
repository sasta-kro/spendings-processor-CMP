package me.sasta.spendings_processor_cmp.rawTextProcessors

import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import me.sasta.spendings_processor_cmp.classes.DailyRecord
import me.sasta.spendings_processor_cmp.classes.MonthlyRecord

/**
 * Remove comments, Splits the raw string file to individual lines,
 * Separate and builds day-blocks with a temp day-builder string variable,
 * Check the end of the file by checking if the next line exists,
 * Process the day blocks
 */
fun processMonthBlock(rawMonthlyRecord: String): MonthlyRecord {
    val removedCommentsMonthlyRecord = rawMonthlyRecord.replace(
        /*  NOTE, had to fix regex. `.DOT_MATCHES_ALL` is not supported
        OLD: `Regex("""\{\{\{.*?\}\}\}\n?""", RegexOption.DOT_MATCHES_ALL), replacement=""`
        by default, `.` (dot) in regex does not match newline characters (\n, \r) so we have to use "Dot matches all" flag.
        Fix: replacing `.` with `[\s\S]` (Matches anything including newlines)

         */
        // NEW FIX: Added [ \t]* to consume spaces/tabs before the newline
        Regex(pattern = """\{\{\{[\s\S]*?\}\}\}[ \t]*\n?"""),
        replacement = ""
    )

    // splitting the raw text file to individual lines
    val splitRawMonthlyRecord = removedCommentsMonthlyRecord.lines()


    // Get processed daily records using helper function
    val dailyRecords = processAndGetDailyRecords(splitRawMonthlyRecord)

    // Check if there are any inconsistencies or errors (using helper) in dates and
    // Get month and year values as YearMonth
    val dateMonthYear = validateYearMonthValuesInDailyRecords(dailyRecords)



    // Inject cumulativeMonthlyTotal values to copy of DailyRecord in dailyRecords list
    var cumulativeMonthlyTotal = 0.0
    var cumulativeMonthlyTotalWithExclusions = 0.0
    val updatedDailyRecords = mutableListOf<DailyRecord>()
    val excludedKeywords = listOf("rent")
    var rentFee: Double? = null

    for (index in dailyRecords.indices) {
        val dailyRecord = dailyRecords[index]

        // Getting the true currentMonthlyTotal
        cumulativeMonthlyTotal += dailyRecord.dailyTotal

        // Getting currentMonthlyTotal without rent and other exclusions
        // getting filtered dailyTotal for each day (if there is no "rent", then the totaling process goes as normal)

        var dailyTotalWithExclusions = 0.0
        for (entry in dailyRecord.entries) {
            // if "rent" is in the entry name, don't add it to the dailyTotalWithExclusions
            if (excludedKeywords.any { keyword -> entry.name.lowercase().contains(keyword) }){
                rentFee = entry.cost
                continue
            }

            dailyTotalWithExclusions += entry.cost

        }
        // add the temp dailyTotalWithExclusion value to cMTWE
        cumulativeMonthlyTotalWithExclusions += dailyTotalWithExclusions


        // copying the days in dailyRecords and recreating them with the 2 currentMonthlyTotal values
        val updatedDailyRecord = dailyRecord.copy(
            trueCurrentMonthlyTotal = cumulativeMonthlyTotal,
            displayCurrentMonthlyTotal = cumulativeMonthlyTotalWithExclusions
        )
        updatedDailyRecords.add(updatedDailyRecord)
    }

    return MonthlyRecord(dateMonthYear, updatedDailyRecords, rentFee)
}





private fun processAndGetDailyRecords(splitRawMonthlyRecord: List<String>) :List<DailyRecord> {
    val dailyRecords: MutableList<DailyRecord> = mutableListOf<DailyRecord>()
    var tempDailyRecordBuilder = ""
    for (index in splitRawMonthlyRecord.indices) {
        // Explicit variable declarations for ease of debugging
        val line = splitRawMonthlyRecord[index]
        val isCurrentLineBlank = line.isBlank()

        // Checking if the next line exists in the raw text file (to prevent out of bounds errors)
        // (index starts at 0, so index+1 turns it into irl line number. If line num < splitRMR size, next line doesn't exist)
        val doesNextLineExist = index + 1 < splitRawMonthlyRecord.size

        // Stop the line-processing for-loop if there are no more lines to process
        /* TODO: fix bug where the processing stops when there are no more lines even though the current day hasn't finished processing
        This bug occurs when there is no trailing empty line at the end of every file.
         */
        if (!doesNextLineExist) {
            break
        }

        // Continue on with the line-processing for-loop if the next line exists
        val nextLine = splitRawMonthlyRecord[index + 1]
        val isNextLineBlank = nextLine.isBlank()

        // Append the current line into the day-builder
        tempDailyRecordBuilder += "$line\n"


        // Process day when a full day is finished building (if the current line is not blank && the next line is blank)
        if (!isCurrentLineBlank && isNextLineBlank) {
            val day = processDayBlock(tempDailyRecordBuilder)

            // add the DailyRecord object into the list
            dailyRecords.add(day)

            // reset day-builder
            tempDailyRecordBuilder = ""

        }


    }
    return dailyRecords
}


/**
 * Process and find inconsistencies in months and years values in a month-block and return a YearMonth value
 * to later use it in the creation of MonthlyRecord
 */
private fun validateYearMonthValuesInDailyRecords(dailyRecords: List<DailyRecord>) : YearMonth {

    // Check if content is empty first
    if (dailyRecords.isEmpty()) throw IndexOutOfBoundsException(
        "The Monthly Record is empty or not formatted right. YearMonth value cannot be found."
    )

    // Set an initial value as the first value in the list as a placeholder
    // NOTE - WEB FIX: `.month.number` instead of `.monthValue`
    var currentMonthValue = dailyRecords[0].date.month.number
    var monthValueOfLastDailyRecord = currentMonthValue

    var currentYearValue = dailyRecords[0].date.year
    var yearValueOfLastDailyRecord = currentYearValue


    // Going through each day-block in the list and comparing it to the last day-block's monthValue & yearValue
    for (dailyRecord in dailyRecords) {

        // NOTE - WEB FIX: `.month.number` instead of `.monthValue`
        currentMonthValue = dailyRecord.date.month.number
        currentYearValue = dailyRecord.date.year

        // Error if month-date is inconsistent
        if (currentMonthValue != monthValueOfLastDailyRecord) {

            throw IllegalArgumentException("Inconsistent month-date in date format: \n" +
                    "The other dailyRecords in the month have monthValue '$monthValueOfLastDailyRecord', " +
                    "but this specific day block has a monthValue of '$currentMonthValue' "+
                    "\n\" $dailyRecord \"")
        }

        // Error message if year-date is inconsistent
        if (currentYearValue != yearValueOfLastDailyRecord) {       // not else-if on purpose

            throw IllegalArgumentException("Inconsistent year-date in date format: \n" +
                    "The other dailyRecords in the month have yearValue '$yearValueOfLastDailyRecord', " +
                    "but this specific day block has a yearValue of '$currentYearValue' "+
                    "\n\" $dailyRecord \"")
        }

        // set the current monthValue as this variable to compare it to the next day-block
        monthValueOfLastDailyRecord = currentMonthValue
        yearValueOfLastDailyRecord = currentYearValue
    }

    // NOTE, `YearMonth` construction is different now without `.of` construction
    return YearMonth(currentYearValue, currentMonthValue)
}

