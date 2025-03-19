
```kotlin
import com.google.ai.client.generativeai.GenerativeModel

val model = GenerativeModel(
  "gemma-3-27b-it",
  // Retrieve API key as an environmental variable defined in a Build Configuration
  // see https://github.com/google/secrets-gradle-plugin for further instructions
  BuildConfig.geminiApiKey,
  generationConfig = generationConfig {
    temperature = 1f
    topK = 64
    topP = 0.95f
    maxOutputTokens = 8192
    responseMimeType = "text/plain"
  },
)

val chatHistory = listOf(
)

val chat = model.startChat(chatHistory)

// Note that sendMessage() is a suspend function and should be called from
// a coroutine scope or another suspend function
val response = chat.sendMessage("INSERT_INPUT_HERE")

// Get the first text part of the first candidate
println(response.text)
// Alternatively
println(response.candidates.first().content.parts.first().asTextOrNull())
```

