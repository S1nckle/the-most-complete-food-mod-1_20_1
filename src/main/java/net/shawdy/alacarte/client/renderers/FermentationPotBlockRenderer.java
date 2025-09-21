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
import net.shawdy.alacarte.block.entity.FermentationPotBlockEntity;
import net.shawdy.alacarte.block.entity.FryingPanBlockEntity;

public class FermentationPotBlockRenderer implements BlockEntityRenderer<FermentationPotBlockEntity> {
    private final ItemRenderer pItemRenderer;

    public FermentationPotBlockRenderer(BlockEntityRendererProvider.Context pContext) {
        this.pItemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(FermentationPotBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemStackHandler pInventory = pBlockEntity.getInventory();

        for (int i = 0; i < pInventory.getSlots(); i++) {
            ItemStack stack = pInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                renderItem(stack, i, pBlockEntity, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
            }
        }
    }

    private void renderItem(ItemStack stack, int slot, FermentationPotBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        // Перемещаем в центр блока
        poseStack.translate(0.5d, 0.5d, 0.5d);

        // Поворачиваем согласно направлению блока
        Direction facing = blockEntity.getBlockState().getValue(FryingPanBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        poseStack.translate(-0.5d, -0.5d, -0.5d);

        // Разные позиции для разных слотов (теперь они будут поворачиваться с блоком)
        switch (slot) {
            case 0 -> poseStack.translate(0.3d, 0.32d, 0.3d);
            case 1 -> poseStack.translate(0.5d, 0.25d, 0.75d);
            case 2 -> poseStack.translate(0.7d, 0.1d, 0.5d);
        }

        // Локальные повороты для каждого предмета
        switch (slot) {
            case 0 -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(-45));
                poseStack.mulPose(Axis.YP.rotationDegrees(50));
            }
            case 1 -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(25));
                poseStack.mulPose(Axis.XP.rotationDegrees(25));
                poseStack.mulPose(Axis.YP.rotationDegrees(-10));
            }
            case 2 -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
            }
        }

        poseStack.scale(0.5f, 0.5f, 0.5f);

        pItemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack,
                buffer, blockEntity.getLevel(), 0);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(FermentationPotBlockEntity pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(FermentationPotBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }
}
