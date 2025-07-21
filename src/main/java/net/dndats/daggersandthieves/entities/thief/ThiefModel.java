package net.dndats.daggersandthieves.entities.thief;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;

public class ThiefModel extends GeoModel<ThiefEntity> {

    @Override
    public ResourceLocation getModelResource(ThiefEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DaggersAndThieves.MODID, "geo/thief.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThiefEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DaggersAndThieves.MODID, "textures/entity/thief_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThiefEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DaggersAndThieves.MODID, "animations/thief.animation.json");
    }

    @Override
    public void setCustomAnimations(ThiefEntity animatable, long instanceId, AnimationState<ThiefEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var headData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        float headRot = 0;
        float headYaw = 0;

        if (headData != null) {
            headRot = headData.headPitch();
            headYaw = headData.netHeadYaw();
        }

        var headBone = getAnimationProcessor().getBone("head");

        if (headBone != null) {
            headBone.setRotX(headRot * ((float)Math.PI / 180F));
            headBone.setRotY(headYaw * ((float)Math.PI / 180F));
        }
    }

}
