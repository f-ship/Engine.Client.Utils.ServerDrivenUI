package ship.f.engine.client.utils.serverdrivenui

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.client.Client
import ship.f.engine.shared.utils.serverdrivenui.state.State

/**
 * Not currently in use because there are no platform-specific behaviors yet
 */
class AndroidClient : Client() {
    override fun postReactiveUpdate(element: ScreenConfig.Element<out State>) {
        TODO("Not yet implemented")
    }
}