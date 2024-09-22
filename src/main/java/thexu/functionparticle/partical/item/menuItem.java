package thexu.functionparticle.partical.item;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import thexu.functionparticle.partical.expParticle.expEncoder;
import thexu.functionparticle.partical.CoordinateSystem;
import thexu.functionparticle.partical.gener.ParticleGener;
import thexu.functionparticle.partical.util.ParticleHelper;

import java.awt.*;
import java.util.List;

import static thexu.functionparticle.partical.util.ParticleHelper.*;

public class menuItem extends Item {

    static int index =0;
    public menuItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level pLevel, Player pPlayer, InteractionHand pUsedHand) {


        if ( pUsedHand == InteractionHand.MAIN_HAND) {

/*
            ParticleGener gener = new ParticleGener(pPlayer.position().add(0,1,0),pPlayer.getRotationVector().add(new Vec2(pPlayer.getXRot(), 0)),
                    ParticleGener.CoordinateSystem.WORLD,
                    new  ParticleGener.Build()
                            .genLine(0.1f,5).addToList()
                            .build());
            gener.genParticle(pLevel, ParticleRegistry.DRAGON_FIRE_PARTICLE.get());
*/

        }
        if (!pLevel.isClientSide && pUsedHand == InteractionHand.OFF_HAND) {
            ParticleHelper helper = new ParticleHelper();
            var exp =helper.exps.get(index);
            index = (index+1)%helper.exps.size();

            //加载图片
            /*
            ParticleGener gener = new ParticleGener(
                    pPlayer.position().add(0,1.5,0),
                    pPlayer.getRotationVector().add(new Vec2(pPlayer.getXRot(), 0)),
                    ParticleGener.CoordinateSystem.LOCAL,
                    "testimg.png");
            gener.genParticle(pLevel, ParticleHelper.BASE_TEST,
                    pos->new Vector3f(pPlayer.getForward().toVector3f().normalize().mul(0.1f))
                    );
*/
            //sendFunctionParticle(pLevel,"hello world!");


            expEncoder expGen = new expEncoder(pLevel,
                    pPlayer.position().add(0,2,0),
                    pPlayer.getRotationVector().add(new Vec2(pPlayer.getXRot(), 0)),
                    CoordinateSystem.LOCAL,
                    exp
                    );

            pLevel.addFreshEntity(expGen);
        }



        return super.use(pLevel, pPlayer, pUsedHand);
    }

}
