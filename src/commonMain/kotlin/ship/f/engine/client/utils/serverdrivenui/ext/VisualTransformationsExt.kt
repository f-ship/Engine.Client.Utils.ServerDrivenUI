package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
            // Keep the original cursor position
            offsetMapping = OffsetMapping.Identity
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PasswordVisualTransformation) return false
        if (mask != other.mask) return false
        return true
    }

    override fun hashCode(): Int = mask.hashCode()
}