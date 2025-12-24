package anya.pizza.pizzasoat.item.custom;

import anya.pizza.pizzasoat.PizzasOAT;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class DrillTemplateItem extends SmithingTemplateItem {
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.RED;
    private static final Formatting TITLE_FORMATTING = Formatting.BLUE;
    private static final Text DRILL_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("upgrade", Identifier.of(PizzasOAT.MOD_ID,"drill_upgrade"))).formatted(TITLE_FORMATTING);
    private static final Text DRILL_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(PizzasOAT.MOD_ID, "smithing_template.drill_upgrade.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text DRILL_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(PizzasOAT.MOD_ID,"smithing_template.drill_upgrade.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text DRILL_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(PizzasOAT.MOD_ID,"smithing_template.drill_upgrade.base_slot_description")));
    private static final Text DRILL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(PizzasOAT.MOD_ID,"smithing_template.drill_upgrade.additions_slot_description")));

    public DrillTemplateItem(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, FeatureFlag... requiredFeatures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, requiredFeatures);
    }

    private static final Identifier EMPTY_SLOT_DRILL_TEXTURE = Identifier.of(PizzasOAT.MOD_ID, "item/drill_slot");
    private static final Identifier EMPTY_SLOT_DRILLBIT_TEXTURE = Identifier.of(PizzasOAT.MOD_ID, "item/drillbit_slot");
    private static final Identifier EMPTY_SLOT_CSTEEL_BLOCK_TEXTURE = Identifier.of(PizzasOAT.MOD_ID, "item/csteel_block_slot");

    public static SmithingTemplateItem createDrillUpgrade() {
        return new SmithingTemplateItem(
                DRILL_UPGRADE_APPLIES_TO_TEXT,
                DRILL_UPGRADE_INGREDIENTS_TEXT,
                DRILL_UPGRADE_TEXT,
                DRILL_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
                DRILL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getDrillUpgradeEmptyBaseSlotTextures(),
                getDrillUpgradeEmptyAdditionsSlotTextures()
        );
    }

    private static List<Identifier> getDrillUpgradeEmptyBaseSlotTextures() {
        return List.of(
                EMPTY_SLOT_DRILL_TEXTURE,
                EMPTY_SLOT_DRILLBIT_TEXTURE
        );
    }

    private static List<Identifier> getDrillUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_CSTEEL_BLOCK_TEXTURE);
    }
}