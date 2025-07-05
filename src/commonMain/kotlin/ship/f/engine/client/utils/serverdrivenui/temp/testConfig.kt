package ship.f.engine.client.utils.serverdrivenui.components

import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnFieldUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnStateUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.action.Action
import ship.f.engine.shared.utils.serverdrivenui.action.Action.UpdateState
import ship.f.engine.shared.utils.serverdrivenui.action.Target
import ship.f.engine.shared.utils.serverdrivenui.state.*

val testConfig = ScreenConfig(
    state = listOf(
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
//                                        type = "UnknownStateText",
                                    ),
                                ),
                                Component(
                                    id = ID(id = "TextId", scope = ""),
                                    state = TextState(
                                        value = "",
//                                        type = "TextState",
                                    ),
                                    triggers = listOf(
                                        OnStateUpdateTrigger(
                                            action = Action.UpdateValue(
                                                targetIds = listOf(
                                                    Target(
                                                        id = ID(id = "TextFieldId", scope = ""),
                                                    )
                                                )
                                            )
                                        ),
                                    )
                                ),
                                Component(
                                    id = ID(id = "TextFieldId", scope = ""),
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
                        id = ID(id = "TextFieldId2", scope = ""),
                        state = FieldState(
                            value = "",
                            initialValue = "",
                            placeholder = "",
                            label = "",
                        ),
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
            state = FlexGridState(
//                type = "UnknownStateGrid"
            ),
        )
    )
)