package gg.moonflower.pollen.core.mixin.fabric.data;

import com.google.gson.JsonObject;
import gg.moonflower.pollen.api.datagen.provider.ConditionalDataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(TagsProvider.class)
public class TagsProviderMixin<T> {

    @Unique
    private ResourceLocation captureId;

    @ModifyVariable(method = "method_27046", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", shift = At.Shift.BEFORE), ordinal = 0)
    public List<Tag.BuilderEntry> modifyList(List<Tag.BuilderEntry> list) {
        list.clear();
        return list;
    }

    @Inject(method = "method_27046", at = @At("HEAD"))
    public void captureId(Function<ResourceLocation, Tag<T>> function, Function<ResourceLocation, T> function2, HashCache hashCache, ResourceLocation id, Tag.Builder builder, CallbackInfo ci) {
        this.captureId = id;
    }

    @ModifyVariable(method = "method_27046", ordinal = 0, at = @At("STORE"))
    public JsonObject modifyJson(JsonObject value) {
        if (this instanceof ConditionalDataProvider)
            ((ConditionalDataProvider) this).injectConditions(this.captureId, value);
        return value;
    }
}
