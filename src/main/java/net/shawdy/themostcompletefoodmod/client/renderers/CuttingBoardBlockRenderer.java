package net.shawdy.themostcompletefoodmod.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.shawdy.themostcompletefoodmod.block.custom.CuttingBoardBlock;
import net.shawdy.themostcompletefoodmod.block.custom.FryingPanBlock;
import net.shawdy.themostcompletefoodmod.block.entity.CuttingBoardBlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.FryingPanBlockEntity;

public class CuttingBoardBlockRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {
    private final ItemRenderer pItemRenderer;

    public CuttingBoardBlockRenderer(BlockEntityRendererProvider.Context pContext) {
        this.pItemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(CuttingBoardBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        SimpleContainer pInventory = pBlockEntity.getInventory();
        ItemStack pStack = pInventory.getItem(0);

        if (pStack.isEmpty()) return;

        pPoseStack.pushPose();



        pPoseStack.translate(0.5d, 0.08d, 0.5d);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180));

        pPoseStack.scale(0.6f, 0.6f, 0.6f);



        Direction facing = pBlockEntity.getBlockState().getValue(CuttingBoardBlock.FACING);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        pItemRenderer.renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 0);

        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CuttingBoardBlockEntity pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(CuttingBoardBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }
}
