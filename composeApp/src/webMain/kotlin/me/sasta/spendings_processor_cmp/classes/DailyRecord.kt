package me.sasta.spendings_processor_cmp.classes

import me.sasta.spendings_processor_cmp.classes.UtilFuncs.checkIfUselessDecimal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

data class DailyRecord(
    val date: LocalDate,
    val entries: List<Entry>,

    /**
    This value will be null by default since a DailyRecord object cannot know
    the dailyTotal value of the previous dailyRecords, nor its location inside the list of MonthlyRecord.
    This is also the reason why the currentMonthlyTotal value cannot be derived inside the DailyRecord class.

    Thus, the best method is to copy and construct new DailyRecord objects in the month-block processor, and
    calculate and inject the currentlyMonthlyTotal value into the newly made copies.
    And finally, use the data injected copies to construct the monthlyRecord object.
     */
    // for calculation purposes
    var trueCurrentMonthlyTotal: Double? = null,

    // For display purposes.
    // This value excludes rent and other outlier values to better visualize the daily spending
    var displayCurrentMonthlyTotal: Double? = null,

    ) {
    // Computed Values
    val dailyTotal: Double
        get() = entries.sumOf { it.cost }


    override fun toString(): String {
        var result = ""

        // Formatting: spaces between date, daily total, and current monthly total (currently 3 spaces)
        val paddingSpaces = "   "

        // Appends date: formats the LocalDate from "2025-2-14" to "14.2.2025"
        // NOTE - WEB FIX: `day` instead of `dayOfMonth`, `month.number` instead of `monthValue`, also `monthNumber` is deprecated
        result += "${date.day}.${date.month.number}.${date.year}" + paddingSpaces

        // Appends the daily total
        result += "(${checkIfUselessDecimal(dailyTotal)})" + paddingSpaces

        // Appends the current monthly total
        result += "[${checkIfUselessDecimal(displayCurrentMonthlyTotal?: 0.0)}]\n"     // using elvis operator to give default value when null

        entries.forEach { result += "${it}\n"}      // appends every Entry as a new line

        return result
    }

}