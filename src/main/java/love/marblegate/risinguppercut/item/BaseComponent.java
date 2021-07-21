package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.util.ModGroup;
import net.minecraft.item.Item;

public class BaseComponent extends Item {
    public BaseComponent() {
        super(new Properties()
                .group(ModGroup.INSTANCE)
                .maxStackSize(1));
    }
}