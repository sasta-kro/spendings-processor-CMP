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


// TODO: sample disappears when typing, so i should have a permanently displaying sample

// TODO: duplicate day check
// TODO: daily total and monthly cumulative display decimals need limits (2 digits)

// TODO: feature: button to clear the output view
// TODO: 'copy text' button
// TODO: feature to add a json export button
