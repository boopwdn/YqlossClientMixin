package yqloss.yqlossclientmixinkt.impl.mixin.tweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks.CallbackMinecraftKt;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Redirect(
        method = "rightClickMouse",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;isAirBlock(Lnet/minecraft/util/BlockPos;)Z")
    )
    private boolean yc$tweaks$rightClickMouseClickBlockPre(WorldClient instance, BlockPos blockPos) {
        return instance.isAirBlock(blockPos) || CallbackMinecraftKt.rightClickMouseClickBlockPre(instance, blockPos);
    }
}
