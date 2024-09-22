package thexu.functionparticle.partical.gener;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class emitRenderer extends EntityRenderer<Entity> {
    protected ResourceLocation TEXTURE_LOCATION;
    protected RenderType RENDER_TYPE;

    public emitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(@NotNull Entity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Entity pEntity) {
        return TEXTURE_LOCATION;
    }

}
