/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.impl.mixin;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackRenderGlobal;

import java.util.Map;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
    @Final
    @Shadow
    private Map<Integer, DestroyBlockProgress> damagedBlocks;

    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void yc$renderEntitiesPost(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobal.YqlossClient.INSTANCE.renderEntitiesPost();
    }

    @Inject(method = "drawBlockDamageTexture", at = @At("HEAD"))
    private void yc$miningprediction$drawBlockDamageTexturePre(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobal.MiningPrediction.INSTANCE.drawBlockDamageTexturePre(damagedBlocks);
    }

    @Inject(method = "drawBlockDamageTexture", at = @At("RETURN"))
    private void yc$miningprediction$drawBlockDamageTexturePost(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobal.MiningPrediction.INSTANCE.drawBlockDamageTexturePost(damagedBlocks);
    }
}
