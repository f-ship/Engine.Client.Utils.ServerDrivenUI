package ship.f.engine.client.utils.serverdrivenui

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.State

class AndroidClient : Client {
    override val stateMap: MutableMap<ScreenConfig.ID, StateHolder<State>> = mutableMapOf()
    override val elementMap: MutableMap<ScreenConfig.ID, ScreenConfig.Element> = mutableMapOf()
    override fun postUpdateHook(
        id: ScreenConfig.ID,
        stateHolder: StateHolder<State>,
    ) {
        TODO("Not yet implemented")
    }
}