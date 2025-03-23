package net.shawdy.themostcompletefoodmod.mixin;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    private Player self() {
        return (Player) (Object) this;
    }
    /**
     * @author me
     * @reason because
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        self().displayClientMessage(Component.literal("TICK TOCK").withStyle(ChatFormatting.AQUA), false);
    }
}
