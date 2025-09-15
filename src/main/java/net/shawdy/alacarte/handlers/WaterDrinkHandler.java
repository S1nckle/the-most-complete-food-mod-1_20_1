package net.shawdy.alacarte.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.client.ModKeyMappings;
import net.shawdy.alacarte.network.C2SSendDrinkPacket;
import net.shawdy.alacarte.network.NetworkHandler;

@Mod.EventBusSubscriber(modid = ALaCarte.MOD_ID, value = Dist.CLIENT)
public class WaterDrinkHandler {
    private static boolean wasPressed = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key pEvent) {
        drinkWater();
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton pEvent) {
        drinkWater();
    }

    private static void drinkWater() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (!mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.AIR) || mc.player.isCreative()) return;

        if (ModKeyMappings.DRINK_WATER.isDown()) {
            if (!wasPressed) {
                wasPressed = true;
                Level level = mc.level;
                Player player = mc.player;
                BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
                if(blockhitresult.getType() == HitResult.Type.BLOCK) {
                    BlockState block = mc.level.getBlockState(blockhitresult.getBlockPos());
                    if (level.getFluidState(blockhitresult.getBlockPos()).is(FluidTags.WATER) || block.is(Blocks.WATER_CAULDRON)) {
                        mc.player.startUsingItem(InteractionHand.MAIN_HAND);
                        mc.player.swing(InteractionHand.MAIN_HAND);
                        NetworkHandler.sendToServer(new C2SSendDrinkPacket(blockhitresult.getBlockPos()));
                    }
                }
            }
        } else {
            wasPressed = false;
        }
    }

    private static BlockHitResult getPlayerPOVHitResult(Level pLevel, Player pPlayer, ClipContext.Fluid pFluidMode) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 1.7d;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pPlayer));
    }

}
