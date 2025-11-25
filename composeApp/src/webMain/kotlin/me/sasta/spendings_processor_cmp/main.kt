package me.sasta.spendings_processor_cmp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App()
    }
}


// TODO: give which date when having an 'invalid date format' error
// TODO: when giving invalid month error, point to which line the error is located in like the year error message
// TODO: program don't process last day when it is not padded with an empty line
/* TODO: sometime when 2 individual comments are on consecutive lines, it gives invalid date error
(prob because it is thinking that is an empty line and the next day has started)
 */
// TODO: sample disappears when typing, so i should have a permanently displaying sample

// TODO: duplicate day check
// TODO: daily total and monthly cumulative display decimals need limits (2 digits)