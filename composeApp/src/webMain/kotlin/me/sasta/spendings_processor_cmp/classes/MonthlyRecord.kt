package me.sasta.spendings_processor_cmp.classes

// NOTE, changed the `YearMonth` import to be web compatible
import kotlinx.datetime.YearMonth
import me.sasta.spendings_processor_cmp.classes.UtilFuncs.checkIfUselessDecimal


data class MonthlyRecord(
    val dateMonthYear: YearMonth,
    val dailyRecords: List<DailyRecord>,
    val rentFee: Double? = null,
) {
    // Computed values
    val monthlyTotal: Double
        get() = dailyRecords.sumOf { it.dailyTotal }


    override fun toString(): String {
        val monthTitle = dateMonthYear.month.toString().lowercase().replaceFirstChar { it.uppercase() } // converting from all uppercase
        var result = "Financial Expenditure Record ($monthTitle ${dateMonthYear.year}) \n"


        result += dailyRecords.joinToString(separator = "\n") { it.toString() }

        // displays True monthly total
        result += "___________________________________________ \n"
        val monthlyTotalWithoutRent = checkIfUselessDecimal(monthlyTotal - (rentFee ?: -99999.9))

        result += "Monthly Total without Rent = $monthlyTotalWithoutRent \n"

        if (rentFee == null) throw IllegalArgumentException("rent fee not found in this month block: $dateMonthYear")

        result += "Rent Fee = ${checkIfUselessDecimal(rentFee)} \n" +
                "Final Monthly Total = ${checkIfUselessDecimal(monthlyTotal)} \n"
        result += "__________________________________"

        return result
    }


}