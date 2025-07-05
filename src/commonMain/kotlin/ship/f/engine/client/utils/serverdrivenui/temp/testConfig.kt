package ship.f.engine.client.utils.serverdrivenui.temp

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.action.Action.UpdateState
import ship.f.engine.shared.utils.serverdrivenui.action.Action.UpdateValue
import ship.f.engine.shared.utils.serverdrivenui.action.Target
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnFieldUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnStateUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.state.*

/**
 * Temporary configuration for the test screen
 */
val testConfig = ScreenConfig(
    children = listOf(
        Widget(
            state = ColumnState(
                children = listOf(
                    Widget(
                        state = CardState(
                            children = listOf(
                                Component(
                                    state = UnknownComponentState(
                                        value = ""
                                    ),
                                ),
                                Component(
                                    state = TextState(
                                        value = "",
                                    ),
                                ),
                                Component(
                                    id = ID(id = "TestConfig-TextId", scope = ""),
                                    state = TextState(
                                        value = "",
                                    ),
                                    triggers = listOf(
                                        OnStateUpdateTrigger(
                                            action = UpdateValue(
                                                targetIds = listOf(
                                                    Target(
                                                        id = ID(id = "TestConfig-TextFieldId", scope = ""),
                                                    )
                                                )
                                            )
                                        ),
                                    )
                                ),
                                Component(
                                    id = ID(id = "TestConfig-TextFieldId", scope = ""),
                                    state = FieldState(
                                        value = "",
                                        initialValue = "",
                                        placeholder = "",
                                        label = "",
                                    ),
                                    triggers = listOf(
                                        OnFieldUpdateTrigger(
                                            action = UpdateState(),
                                        )
                                    )
                                ),
                            )
                        ),
                    ),
                )
            ),
        ),
        Widget(
            state = FlexColumnState(),
        ),
        Widget(
            state = RowState(
                children = listOf(
                    Component(
                        id = ID(id = "TestConfig-TextFieldId2", scope = ""),
                        state = FieldState(
                            value = "",
                            initialValue = "",
                            placeholder = "",
                            label = "",
                        ),
                        triggers = listOf(
                            OnFieldUpdateTrigger(
                                action = UpdateState(),
                            )
                        )
                    )
                ),
                arrangement = Arrangement.Flex,
            ),
        ),
        Widget(
            state = FlexRowState(),
        ),
        Widget(
            state = CardState(),
        ),
        Widget(
            state = BottomSheetState(),
        ),
        Widget(
            state = GridState(),
        ),
        Widget(
            state = FlexGridState(),
        ),
        Widget(
            state = FlexGridState(),
        )
    )
)