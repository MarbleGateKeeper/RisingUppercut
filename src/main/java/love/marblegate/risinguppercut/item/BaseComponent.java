package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.misc.ModCreativeTab;
import net.minecraft.world.item.Item;

public class BaseComponent extends Item {
    public BaseComponent() {
        super(new Properties()
                .tab(ModCreativeTab.INSTANCE)
                .stacksTo(1));
    }
}
