package ship.f.engine.client.utils.serverdrivenui2.ext

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2.Direction2.Backward2
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2.Direction2.Forward2

fun defaultTransitionSpec(direction: BackStackEntry2.Direction2) = when (direction) {
    is Forward2 ->     // The new screen enters from the right; old slides left out.
        (slideInHorizontally(
            initialOffsetX = { it },          // start just off the right edge
            animationSpec = tween(300)
        ) + fadeIn(tween(150))) togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { -it / 4 },      // slight parallax
                    animationSpec = tween(300)
                ) + fadeOut(tween(150)))

    is Backward2 ->     // The old screen enters from the left; current slides right out.
        (slideInHorizontally(
            initialOffsetX = { -it },          // start just off the right edge
            animationSpec = tween(300)
        ) + fadeIn(tween(150))) togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { it / 4 },      // slight parallax
                    animationSpec = tween(300)
                ) + fadeOut(tween(150)))
}