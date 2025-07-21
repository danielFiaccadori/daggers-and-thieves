package net.dndats.daggersandthieves.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dndats.daggersandthieves.entities.thief.ThiefEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ThiefHeldItemLayer extends GeoRenderLayer<ThiefEntity> {

    private final EntityRendererProvider.Context context;

    public ThiefHeldItemLayer(GeoEntityRenderer<ThiefEntity> entityRenderer, EntityRendererProvider.Context context) {
        super(entityRenderer);
        this.context = context;
    }

    @Override
    public void renderForBone(PoseStack poseStack, ThiefEntity animatable, GeoBone bone, RenderType renderType,
                              MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        String targetBoneName = "right_arm";

        if (!bone.getName().equals(targetBoneName)) {
            return;
        }

        ItemStack heldItem = animatable.getItemBySlot(EquipmentSlot.MAINHAND);

        if (heldItem.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        poseStack.translate(0.35F, 0.75, -0.20F);

        poseStack.mulPose(new Quaternionf().setAngleAxis((float) Math.toRadians(-90f), 1f, 0f, 0f));

        context.getItemRenderer().renderStatic(
                animatable,
                heldItem,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                false,
                poseStack,
                bufferSource,
                animatable.level(),
                packedLight,
                packedOverlay,
                animatable.getId()
        );

        poseStack.popPose();
    }
}
