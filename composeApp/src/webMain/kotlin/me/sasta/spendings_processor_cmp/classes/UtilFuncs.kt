package me.sasta.spendings_processor_cmp.classes

// NOTE, changed the imports to be web compatible
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

// "object" in kotlin is like classes in Java with static methods
// A singleton class
object UtilFuncs {

    /** Function to remove a useless decimal in Doubles (e.g., removing ".0" in Doubles) */
    fun checkIfUselessDecimal(number: Double): String {

        // checking if this has a decimal place or not
        return if (number % 1 == 0.0) {
            // returns the value with no decimal + 3 spaces

            /* NOTE, WEB FIX: Formatting Fix - String.format() is Java-only.
             * We use simple string manipulation for the web.
             */
            number.toLong().toString()
        } else if (number == 0.0) {
            "-"
        }
        else {
            "$number"
        }
    }

    /** Helper function for "processDayBlock" function to extract date from entries
     */
    fun extractDate(dateLine: String): LocalDate {

        // splitting the line to get the date section, and splitting it again to get the individual dd, mm, yy
        val dateStr: List<String> = dateLine
            // remove trailing spaces
            .trim()

            // splitting by empty space character and getting the [0] element (first item) which is the date string
            .split(" ")[0]

            // splitting the date string by "." to get the individual day, month, and year
            .split(".")
        // thus the dateStr variable becomes a list of strings

        // Formatting Error Handling
        val isDateCharactersNotNumber =
            // using predicate function operators (For All, There Exists) that returns a boolean after checking a collection
            dateStr.any {  // checking the individual strings in dateStr (e.g. "14", "2", "2025)
                it.any {    // iterating through each character in the string
                        char -> !char.isDigit()     // checking each character
                }
            }
        val has3partsDDMMYY = dateStr.size == 3

        if (isDateCharactersNotNumber || !has3partsDDMMYY) {
            throw IllegalArgumentException("Invalid date format: \" $dateLine \"")
        }

        return LocalDate(// NOTE, WEB FIX: cannot use `LocalDate.of()` syntax anymore cuz it's kotlinx lib on web and not java lib

            // LocalDate object takes in (year, month, day) as constructors, so the elements in dateStr are passed
            // in reverse order and then cast/converted into int

            year = dateStr[2].toInt(),
            month = dateStr[1].toInt(),
            day = dateStr[0].toInt()
        )
    }



}

