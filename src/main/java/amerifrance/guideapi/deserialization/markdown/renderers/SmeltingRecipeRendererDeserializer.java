package amerifrance.guideapi.deserialization.markdown.renderers;

import amerifrance.guideapi.api.RegisterDeserializer;
import amerifrance.guideapi.deserialization.markdown.ItemStackDeserializer;
import amerifrance.guideapi.renderers.CookingRecipeRenderer;
import amerifrance.guideapi.utils.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;

import java.util.regex.Matcher;

@RegisterDeserializer("SMELTING_RECIPE-MARKDOWN")
public class SmeltingRecipeRendererDeserializer implements ItemStackDeserializer {

    @Override
    public Object deserialize(String value, Object... initParameters) {
        Matcher matcher = PATTERN.matcher(value);

        if (matcher.find()) {
            ItemStack itemStack = ItemStackHelper.itemStackFromString(matcher.group(1));

            return new CookingRecipeRenderer(itemStack.getItem(), RecipeType.SMELTING);
        }

        return null;
    }
}
