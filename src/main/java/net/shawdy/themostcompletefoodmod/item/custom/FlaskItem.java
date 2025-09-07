package net.shawdy.themostcompletefoodmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlaskItem extends Item {
    private final FlaskTiers tier;

    public FlaskItem(FlaskTiers pTier) {
        super(new Properties().setNoRepair().durability(pTier.getMaxUses()));
        tier = pTier;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            if (!((Player) pLivingEntity).isCreative()) {
                pStack.hurt(1, pLevel.random, null);
            }
        }
        return pStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
        if (getFullness(pStack) < tier.getMaxUses()) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                if (pLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
                    pStack.setDamageValue(0);
                    pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 0.8F);
                    return InteractionResultHolder.success(pStack);
                }
            }
        }
        if (getFullness(pStack) > 0) {
            return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
        }
        return InteractionResultHolder.pass(pStack);
    }

    private int getFullness(ItemStack pStack) {
        return getMaxDamage(pStack) - getDamage(pStack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        if (tier == FlaskTiers.NETHERITE) return true;
        return super.isFoil(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (tier == FlaskTiers.NETHERITE)
            pTooltipComponents.add(Component.translatable("tooltip.tmcfm.netherite_flask").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack pStack = new ItemStack(this);
        pStack.setDamageValue(pStack.getMaxDamage());
        return pStack;
    }
}
