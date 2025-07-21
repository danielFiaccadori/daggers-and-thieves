package net.dndats.daggersandthieves.entities.thief;

import net.dndats.daggersandthieves.geckolib.renderer.ThiefHeldItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ThiefRenderer extends GeoEntityRenderer<ThiefEntity> {

    public ThiefRenderer(EntityRendererProvider.Context context) {
        super(context, new ThiefModel());
        addRenderLayer(new ThiefHeldItemLayer(this, context));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.withScale(0.9f, 0.9f);
    }

}
