package ship.f.engine.client.utils.serverdrivenui3

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ServerToastEvent(
    val message: String,
    val durationMs: Long? = 10000, // null for indefinite
    val actionText: String? = null,
    val toastType: ToastType = ToastType.Warning,
) {
    @Serializable
    @SerialName("ToastType")
    sealed class ToastType {
        @Serializable
        @SerialName("Success")
        object Success : ToastType()
        @Serializable
        @SerialName("Warning")
        object Warning : ToastType()
        @Serializable
        @SerialName("Error")
        object Error : ToastType()
    }
}