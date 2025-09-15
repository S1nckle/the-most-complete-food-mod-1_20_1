package net.shawdy.alacarte.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.alacarte.block.custom.FryingPanBlock;
import net.shawdy.alacarte.block.entity.FryingPanBlockEntity;

public class FryingPanBlockRenderer implements BlockEntityRenderer<FryingPanBlockEntity> {
    private final ItemRenderer pItemRenderer;


    public FryingPanBlockRenderer(BlockEntityRendererProvider.Context pContext) {
        this.pItemRenderer = Minecraft.getInstance().getItemRenderer();
    }
    @Override
    public void render(FryingPanBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemStackHandler pInventory = pBlockEntity.getInventory();
        ItemStack pStack = pInventory.getStackInSlot(0);

        if (pStack.isEmpty()) return;

        pPoseStack.pushPose();

        pPoseStack.translate(0.5d, 0.05d, 0.5d);
        pPoseStack.scale(0.6f, 0.6f, 0.6f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));


        Direction facing = pBlockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();

        if (pStack.getCount() < 2) return;
        pPoseStack.pushPose();

        pPoseStack.translate(0.51d, 0.1d, 0.49d);
        pPoseStack.scale(0.6f, 0.6f, 0.6f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));


        facing = pBlockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();

        if (pStack.getCount() < 17) return;
        pPoseStack.pushPose();

        pPoseStack.translate(0.52d, 0.15d, 0.50d);
        pPoseStack.scale(0.6f, 0.6f, 0.6f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));


        facing = pBlockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();

        if (pStack.getCount() < 33) return;
        pPoseStack.pushPose();

        pPoseStack.translate(0.5d, 0.2d, 0.49d);
        pPoseStack.scale(0.6f, 0.6f, 0.6f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));


        facing = pBlockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();

        if (pStack.getCount() < 49) return;
        pPoseStack.pushPose();

        pPoseStack.translate(0.49d, 0.25d, 0.48d);
        pPoseStack.scale(0.6f, 0.6f, 0.6f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));


        facing = pBlockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(FryingPanBlockEntity pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(FryingPanBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }
}
