package yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks

import net.minecraft.entity.Entity
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksEvent

fun setAnglesPost(entity: Entity) {
    YC.eventDispatcher(TweaksEvent.SetAnglesPost(entity))
}
