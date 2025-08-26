package ship.f.engine.client.utils.serverdrivenui2.ext

import ship.f.engine.client.utils.serverdrivenui2.CommonClient2
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2

val C
    get() = ClientHolder2.get<CommonClient2>()