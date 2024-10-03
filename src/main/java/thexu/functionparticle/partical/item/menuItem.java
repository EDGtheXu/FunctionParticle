package thexu.functionparticle.partical.item;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thexu.functionparticle.partical.emitter.expEmitter;
import thexu.functionparticle.partical.emitter.expHelper;

import static thexu.functionparticle.partical.emitter.expHelper.*;
import static thexu.functionparticle.partical.emitter.expHelper.EXP_RANDOM;

public class menuItem extends Item {

    static int index =0;
    public menuItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level pLevel, Player pPlayer, InteractionHand pUsedHand) {


        if (!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND) {
            expHelper helper = new expHelper();
            var exp =helper.exps.get(index);
            index = (index+1)%helper.exps.size();
            Vec3 initPos = pPlayer.position().add(0,2,0);
            expEmitter expGen = new expEmitter(pLevel,
                    initPos,
                    pPlayer.getRotationVector().add(new Vec2(pPlayer.getXRot(), 0)),
                    exp
            );
            expGen.setPos(initPos);
            pLevel.addFreshEntity(expGen);

        }
        if (!pLevel.isClientSide && pUsedHand == InteractionHand.OFF_HAND) {
            /*
            expHelper helper = new expHelper();

            var exp =helper.exps.get(index);
            index = (index+1)%helper.exps.size();
*/
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

            Vec3 initPos = pPlayer.position().add(0,2,0);
            expEmitter expGen = new expEmitter(pLevel,
                    initPos,
                    pPlayer.getRotationVector().add(new Vec2(pPlayer.getXRot(), 0)),
                    EXP_MOVE_SPHERE
                    );
            expGen.setPos(initPos);
            pLevel.addFreshEntity(expGen);
        }



        return super.use(pLevel, pPlayer, pUsedHand);
    }

}
