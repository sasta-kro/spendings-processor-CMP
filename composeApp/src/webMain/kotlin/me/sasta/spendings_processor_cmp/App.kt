package me.sasta.spendings_processor_cmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Typography.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import me.sasta.spendings_processor_cmp.rawTextProcessors.processMonthBlock
import org.jetbrains.compose.resources.painterResource

import spendings_processor_cmp.composeapp.generated.resources.Res



@Composable
fun App() {

//   DefaultGeneratedView()
    PlaceholderByGemini()

}

@Composable
fun PlaceholderByGemini() {
    MaterialTheme {
        // State variables to hold the text input and output
        var rawInput by remember { mutableStateOf("") }
        var resultOutput by remember { mutableStateOf("Result will appear here...") }
        var isError by remember { mutableStateOf(false) }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState()), // Allows scrolling if output is long


            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text("Spending Records Processor by SASTA", style = MaterialTheme.typography.headlineMedium)


            Spacer(Modifier.height(16.dp))

            // 1. INPUT FIELD
            OutlinedTextField(
                value = rawInput,
                onValueChange = { rawInput = it },
                label = { Text("Paste Raw Month Block Here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),

                placeholder = {
                    Text(
                        "Format/Syntax Example:\n" +
                                "1.1.2025\n" +
                                "Food - 100\n" +
                                "Taxi - 35\n" +
                                "{{{ this is a comment }}}\n" +
                                "Rent - 10000\n\n" +
                                "2.1.2025\n" +
                                "lunch - 60"
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

            // 2. PROCESS BUTTON
            Button(
                onClick = {
                    try {
                        // Call your migrated logic directly!
                        val record = processMonthBlock(rawInput)
                        resultOutput = record.toString()
                        isError = false
                    } catch (e: Exception) {
                        resultOutput = "ERROR:\n${e.message}"
                        isError = true
                    }
                }
            ) {
                Text("Process Text")
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(Modifier.height(16.dp))

            // 3. OUTPUT DISPLAY
            Text(
                "Output:",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            // SelectionContainer allows you to copy the result text
            SelectionContainer {
                Text(
                    text = resultOutput,
                    color = if (isError) Color.Red else Color.Black,
                    fontFamily = FontFamily.Monospace, // Monospace looks better for your formatted tables
                    modifier = Modifier
//                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1f) // Takes up remaining space
                )
            }
        }
    }
}




@Composable
fun DefaultGeneratedView() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}