package me.sasta.spendings_processor_cmp.rawTextProcessors

import me.sasta.spendings_processor_cmp.classes.DailyRecord
import me.sasta.spendings_processor_cmp.classes.Entry
import me.sasta.spendings_processor_cmp.classes.UtilFuncs.extractDate

/**
Gets the date by making a helper function, which splits the string twice to get the date, returns LocalDate.
Uses a function chain to process the raw entry Strings to Entry objects with processEntry function,
and map them to a list.
Returns DailyRecord object.
 */
fun processDayBlock(rawDailyRecord: String): DailyRecord {
    // splitting the raw text into individual lines
    val splitRawDailyRecord: List<String> = rawDailyRecord.lines()

    // cleaning up the empty lines (if there is any)
    val cleanedRawDailyRecord = splitRawDailyRecord.filter { it.isNotBlank() }


    val date = extractDate(cleanedRawDailyRecord[0])     // extract the date using the first line

    val removedDateLineDailyRecord = cleanedRawDailyRecord.drop(1)  // skips the first line which is the date line



    // An empty mutable list of Entries
    val entries = mutableListOf<Entry>()

    for (line in removedDateLineDailyRecord) {
        try {
            val entry = processEntry(line)
            entries.add(entry)
        }
        catch (e: IllegalArgumentException) {
            throw Exception("Error processing entry. Date of entry = $date \n Error Message: ${e.message}")
        }
    }


    /* Replaced with simpler, easily debuggable code
    // An empty mutable list of Entries
    val entries = removedDateLineDailyRecord
        // passed my process_entry function (with "it" argument) to the .map function
        // which iterates through the list and returns a list of my process_entry function's return values
        // which, in this case, returns a list of Entry objects
        .map { processEntry(it) }
    */
    
    return DailyRecord(date, entries)
}

