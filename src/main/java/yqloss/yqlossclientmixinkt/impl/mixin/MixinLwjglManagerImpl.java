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

import cc.polyfrost.oneconfig.internal.renderer.LwjglManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.YqlossClientKt;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;

@Mixin(LwjglManagerImpl.class)
public abstract class MixinLwjglManagerImpl extends URLClassLoader {
    @Unique
    private static final String YQLOSS_PACKAGE = "yqloss.yqlossclientmixinkt.impl.oneconfiginternal.lwjglmanagerimplloaded.";

    @Shadow(remap = false)
    @Final
    private Set<String> classLoaderInclude;

    @Shadow(remap = false)
    @Final
    private Map<String, Class<?>> classCache;

    public MixinLwjglManagerImpl(URL[] urls) {
        super(urls);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void yc$initPost(CallbackInfo ci) throws Exception {
        addURL(YqlossClientKt.getCLASS_ROOT());
        classLoaderInclude.add(YQLOSS_PACKAGE);
        Class.forName(YQLOSS_PACKAGE + "NanoVGAccessorImpl", true, this);
    }

    @Inject(method = "findClass", at = @At("HEAD"), remap = false, cancellable = true)
    private void yc$findClassPre(String name, CallbackInfoReturnable<Class<?>> cir) throws Exception {
        if (name.startsWith(YQLOSS_PACKAGE)) {
            Class<?> clazz;
            if (classCache.containsKey(name)) {
                clazz = classCache.get(name);
            } else {
                clazz = super.findClass(name);
                classCache.put(name, clazz);
            }
            cir.setReturnValue(clazz);
        }
    }
}
