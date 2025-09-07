package net.shawdy.themostcompletefoodmod.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shawdy.themostcompletefoodmod.TheMostCompleteFoodMod;
import net.shawdy.themostcompletefoodmod.init.ModItems;

@Mod.EventBusSubscriber(
        modid = TheMostCompleteFoodMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class WaterBowlHandler {

    @SubscribeEvent
    public static void onUseBowl(PlayerInteractEvent.RightClickItem pEvent) {
        ItemStack stack = pEvent.getItemStack();
        if (stack.getItem() != Items.BOWL) return;
        Level level = pEvent.getLevel();
        Player player = pEvent.getEntity();

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return;
        } else {
            if (blockhitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = blockhitresult.getBlockPos();
                if (!level.mayInteract(player, blockpos)) return;
                ItemStack waterBowl = new ItemStack(ModItems.WATER_BOWL.get());

                if (!(level.getFluidState(blockpos).is(FluidTags.WATER) || level.getBlockState(blockpos).is(Blocks.WATER_CAULDRON))) return;

                if (player.isCreative() ) {
                    player.getInventory().add(waterBowl);
                } else
                if (stack.getCount() == 1) {
                    player.setItemInHand(pEvent.getHand(), waterBowl);
                } else {
                    stack.shrink(1);
                    if (!player.getInventory().add(waterBowl)) {
                        player.drop(waterBowl, false);
                    }
                }

                if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 0.8F);
                    level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                    player.swing(pEvent.getHand());
                }

                if (level.getBlockState(blockpos).is(Blocks.WATER_CAULDRON)) {
                    BlockState block = level.getBlockState(blockpos);
                    int waterLevel = block.getValue(LayeredCauldronBlock.LEVEL);
                    if (waterLevel > 1) {
                        level.setBlock(blockpos, block.setValue(LayeredCauldronBlock.LEVEL, waterLevel - 1), 3);
                    } else {
                        level.setBlock(blockpos, Blocks.CAULDRON.defaultBlockState(), 3);
                    }
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 0.8F);
                    level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                    player.swing(pEvent.getHand());
                }
            }
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
        double d0 = pPlayer.getBlockReach();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pPlayer));
    }

}
