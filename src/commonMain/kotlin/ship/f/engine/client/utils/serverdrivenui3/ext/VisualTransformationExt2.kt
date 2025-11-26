package ship.f.engine.client.utils.serverdrivenui3.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PasswordVisualTransformation(private val mask: Char = '•') : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = buildAnnotatedString {
                repeat(text.length) {
                    append(mask)
                }
            },
            offsetMapping = OffsetMapping.Identity
        )
    }
}