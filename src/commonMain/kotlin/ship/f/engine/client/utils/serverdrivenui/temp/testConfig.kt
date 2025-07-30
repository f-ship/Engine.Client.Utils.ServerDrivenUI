package ship.f.engine.client.utils.serverdrivenui.temp

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Component
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.action.Action.UpdateState
import ship.f.engine.shared.utils.serverdrivenui.action.Action.UpdateValue
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnFieldUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnStateUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.ext.id
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
                                    id = id(value = "TestConfig-TextId"),
                                    state = TextState(
                                        value = "",
                                    ),
                                    triggers = listOf(
                                        OnStateUpdateTrigger(
                                            action = UpdateValue(
                                                publisherId = id(value = "TestConfig-TextFieldId"),
                                            )
                                        ),
                                    )
                                ),
                                Component(
                                    id = id(value = "TestConfig-TextFieldId"),
                                    state = FieldState(
                                        value = "",
                                        initialValue = "",
                                        placeholder = "",
                                        label = "",
                                    ),
                                    triggers = listOf(
                                        OnFieldUpdateTrigger(
                                            action = UpdateState,
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
                        id = id(value = "TestConfig-TextFieldId2"),
                        state = FieldState(
                            value = "",
                            initialValue = "",
                            placeholder = "",
                            label = "",
                        ),
                        triggers = listOf(
                            OnFieldUpdateTrigger(
                                action = UpdateState,
                            )
                        )
                    )
                ),
                arrangement = Arrange.Flex,
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