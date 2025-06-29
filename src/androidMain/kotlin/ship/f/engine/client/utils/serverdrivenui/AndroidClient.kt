package ship.f.engine.client.utils.serverdrivenui

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.State

/**
 * Not currently in use because there are no platform-specific behaviors yet
 */
class AndroidClient : Client {
    override val stateMap: MutableMap<ScreenConfig.ID, StateHolder<State>> = mutableMapOf()
    override val elementMap: MutableMap<ScreenConfig.ID, ScreenConfig.Element> = mutableMapOf()
    override var banner: ScreenConfig.Fallback? = null
    override fun postUpdateHook(
        id: ScreenConfig.ID,
        stateHolder: StateHolder<State>,
    ) {
        TODO("Not yet implemented")
    }
}