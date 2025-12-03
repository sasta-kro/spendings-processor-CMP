package me.sasta.spendings_processor_cmp.rawTextProcessors

import me.sasta.spendings_processor_cmp.classes.Entry

/**
Uses a string split function and trim the 2 parts to get the name and cost,
The cost value is Double,
Returns the Entry object
*/
fun processEntry(entry: String): Entry {
    if ("-" !in entry) throw IllegalArgumentException("Invalid entry line format: \" $entry \"")

    val parts = entry.split("-") .toMutableList()

    // Process the 'cost' part to see if there is any extra chars or numbers
    val rawCostString = parts.last().trim()
    val rawCostStringSplit: List<String> = rawCostString.split(" ")
    if (rawCostStringSplit.size > 1) {
        throw IllegalArgumentException("Invalid format or extra info near the 'cost' number: \" $entry \"")
    }
    // choosing the first number after the `-` split if it has multiple
    val cleanedCostString: String = rawCostStringSplit.first()

    parts[1] = cleanedCostString

    // Ensure that the entry has only two parts: name and cost
    if (parts.size != 2) throw IllegalArgumentException("Invalid entry line format: \" $entry \"")

    val name: String  = parts[0].trim()
    val costStr: String = parts[1].trim()



    // Safe conversion from string to double
    val cost = if (costStr.first() == '+') {
        // make the value negative if the cost has a "+" (e.g. food - +100)
        costStr.toDoubleOrNull()?.times(-1)
    } else{
        costStr.toDoubleOrNull()
    }


    // return error if cost cannot be converted to double
    if (cost == null)   throw IllegalArgumentException("Invalid cost format: \" $costStr \"")

    return Entry(name, cost)
}
