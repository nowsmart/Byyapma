package com.zaheer.byyapma

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.zaheer.byyapma.ui.theme.BüyüYapmaTheme
import androidx.compose.material.icons.filled.Share


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BüyüYapmaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // SharedPreferences کا ہینڈل
    val sharedPref = remember {
        context.getSharedPreferences("ByyapmaPrefs", Context.MODE_PRIVATE)
    }

    var talibName by remember {
        mutableStateOf(
            sharedPref.getString("saved_talib_name", "جمیل") ?: "جمیل"
        )
    }
    var talibMotherName by remember {
        mutableStateOf(
            sharedPref.getString(
                "saved_talib_mother_name",
                "زینب"
            ) ?: "زینب"
        )
    }
    val ebarat = remember {
        mutableStateListOf<String>().apply {
            val saved = sharedPref.getString("saved_ebarat", null)

            if (!saved.isNullOrEmpty()) {
                addAll(saved.split("|"))
            } else {
                addAll(listOf("عزت", "الو", "جلب"))
            }
        }
    }
    var result by remember { mutableStateOf("") }
    var resultDoubled by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = talibName,
            onValueChange = {
                talibName = it
                sharedPref.edit { putString("saved_talib_name", talibName) }
                result = ""
            },
            trailingIcon = {
                if (talibName.isNotEmpty()) {
                    IconButton(onClick = { talibName = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            label = { Text("İsim girin") },//Enter name
            placeholder = { Text("İsmi buraya yazın...") }//Type name here
        )
        OutlinedTextField(
            value = talibMotherName,
            onValueChange = {
                talibMotherName = it
                sharedPref.edit { putString("saved_talib_mother_name", talibMotherName) }
                result = ""
            },
            trailingIcon = {
                if (talibMotherName.isNotEmpty()) {
                    IconButton(onClick = { talibMotherName = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            label = { Text("Anne adını girin") },//Enter mother name
            placeholder = { Text("Annenizin adını buraya yazın...") }//Type mother name here
        )
        ebarat.forEachIndexed { index, text ->
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    ebarat[index] = newText
                    sharedPref.edit {
                        putString(
                            "saved_ebarat",
                            ebarat.joinToString("|")
                        )
                    }
                    result = ""
                    // Create another field when the current field gets text
                    if (newText.isNotBlank() && index == ebarat.lastIndex) {
                        ebarat.add("")
                    }
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { ebarat.removeAt(index) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text"
                            )
                        }
                    }
                },
                label = { Text("Metin girin") },
                placeholder = { Text("Metni buraya girin...") }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                val (newResult, newResultDoubled) = calculateResult(ebarat)
                result = newResult
                resultDoubled = newResultDoubled
            }
        ) {
            Text("Göster")//Show
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            if (result.isNotEmpty()) {
                // Define a shared Modifier for clean alignment and padding
                val sharedTextModifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )

// Primary Result Text (Prominent Heading Style)
                Text(
                    text = result,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.15.sp,
                        lineHeight = 72.sp,
                        fontSize = 36.sp,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = sharedTextModifier
                )

// Subtitle / Extended Spell Text (Secondary Subdued Style)
                Surface(
                    modifier = sharedTextModifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    tonalElevation = 2.dp,
                    shadowElevation = 1.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        val context = LocalContext.current
                        val combinedText =
                            "$resultDoubled ${createSpell(talibName, talibMotherName)}"

                        // Copy Button (Positioned Top-End)
                        IconButton(
                            onClick = {
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Copied Text", combinedText)
                                clipboard.setPrimaryClip(clip)
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Result",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Main Display Text
                        Text(
                            text = combinedText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 36.sp,
                                fontSize = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 36.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        )
                    }
                }

            }
        }
    }
}

/**
 * Interleaves characters from a list of strings index-by-index,
 * chunks the interleaved result into triplets, attaches [yoosh],
 * and joins them with spaces.
 */
fun calculateResult(ebarat: List<String>): Pair<String, String> {
    if (ebarat.isEmpty()) return Pair("", "")

    val maxLength = ebarat.maxOf { it.length }
    val interleaved = buildString {
        for (i in 0 until maxLength) {
            for (text in ebarat) {
                val text1 = text.replace(" ", "")
                if (i < text1.length) {
                    append(text1[i])
                }
            }
        }
    }
    val remainder = interleaved.length % 3
    val paddingNeeded = if (remainder == 0) 0 else 3 - remainder
    val padded = interleaved + interleaved.take(paddingNeeded)
    val chunks = padded.chunked(3)

    val result = chunks.joinToString(separator = " ") { chunk ->
        "$chunk$yoosh"
    }
//حب الو
    val resultDoubled = chunks.joinToString(separator = " ") { chunk ->
        "$chunk$yoosh $chunk$yoosh"
    }
    return Pair(result, resultDoubled)
}

