package thexu.functionparticle.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import thexu.functionparticle.partical.item.menuItem;

import java.util.function.Supplier;

import static thexu.functionparticle.FunctionParticle.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> MAIN_MENU = registerItem("main_menu",
        ()->new menuItem(new Item.Properties().stacksTo(1)));


    public static DeferredItem<Item> registerItem(String name, Supplier<Item> itemSupplier){

        return ITEMS.register(name,itemSupplier);
    }





    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> NEO_TERRA =
            CREATIVE_TABS.register("functionparticle", ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.functionparticle"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(()-> Items.ENDER_PEARL.asItem().getDefaultInstance())
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.MAIN_MENU.get());
                    }).build());

}
