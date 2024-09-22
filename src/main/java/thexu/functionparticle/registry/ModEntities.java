package thexu.functionparticle.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thexu.functionparticle.partical.expParticle.expEncoder;

import java.util.function.Supplier;

import static thexu.functionparticle.FunctionParticle.MODID;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);



    //emitter
    public static final Supplier<EntityType<expEncoder>> PARTICLE_EMITTER =
            ENTITY_TYPES.register("emitter",
                    () ->EntityType.Builder.<expEncoder>of(expEncoder::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .setTrackingRange(4)
                            .setUpdateInterval(10)
                            .setShouldReceiveVelocityUpdates(true)
                            .build(ResourceLocation.fromNamespaceAndPath(MODID,"emitter").toString())
            );







    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
    private static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));}

}
