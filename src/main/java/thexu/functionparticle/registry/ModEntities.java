package thexu.functionparticle.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thexu.functionparticle.FunctionParticle;
import thexu.functionparticle.partical.emitter.expEmitter;
import thexu.functionparticle.partical.emitter.photoEmitter;

import java.util.function.Supplier;

import static thexu.functionparticle.FunctionParticle.MODID;

@EventBusSubscriber(modid = FunctionParticle.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);



    //emitter
    public static final Supplier<EntityType<expEmitter>> PARTICLE_EMITTER =
            ENTITY_TYPES.register("emitter",
                    () ->EntityType.Builder.<expEmitter>of(expEmitter::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(200)
                            .setTrackingRange(200)
                            .setUpdateInterval(1)
                            .updateInterval(1)
                            .setShouldReceiveVelocityUpdates(true)
                            .build(ResourceLocation.fromNamespaceAndPath(MODID,"emitter").toString())
            );
    public static final DeferredHolder<EntityType<?>, EntityType<photoEmitter>> PHOTO_EMITTER =
            ENTITY_TYPES.register("photo_emitter",
                    () ->EntityType.Builder.<photoEmitter>of(photoEmitter::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(200)
                            .setTrackingRange(200)
                            .setUpdateInterval(1)
                            .updateInterval(1)
                            .setShouldReceiveVelocityUpdates(true)
                            .build(ResourceLocation.fromNamespaceAndPath(MODID,"photo_emitter").toString())
            );



    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {

        AttributeSupplier.Builder kulouwangHandAttribs = Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20000)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000);


        event.put(ModEntities.PARTICLE_EMITTER.get(), kulouwangHandAttribs.build());



    }




    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
    private static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));}

}
