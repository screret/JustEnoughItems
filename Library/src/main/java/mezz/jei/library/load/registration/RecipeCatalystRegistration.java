package mezz.jei.library.load.registration;

import com.google.common.collect.ImmutableListMultimap;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.core.collect.ListMultiMap;
import mezz.jei.library.ingredients.TypedIngredient;

public class RecipeCatalystRegistration implements IRecipeCatalystRegistration {
	private final ListMultiMap<RecipeType<?>, ITypedIngredient<?>> recipeCatalysts = new ListMultiMap<>();
	private final IIngredientManager ingredientManager;
	private final IJeiHelpers jeiHelpers;

	public RecipeCatalystRegistration(IIngredientManager ingredientManager, IJeiHelpers jeiHelpers) {
		this.ingredientManager = ingredientManager;
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return ingredientManager;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public <T> void addRecipeCatalyst(IIngredientType<T> ingredientType, T ingredient, RecipeType<?>... recipeTypes) {
		ErrorUtil.checkNotEmpty(recipeTypes, "recipeTypes");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");

		for (RecipeType<?> recipeType : recipeTypes) {
			ErrorUtil.checkNotNull(recipeType, "recipeType");
			ITypedIngredient<T> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredientType, ingredient, true)
				.orElseThrow(() -> new IllegalArgumentException("Recipe catalyst must be valid"));
			this.recipeCatalysts.put(recipeType, typedIngredient);
		}
	}

	public ImmutableListMultimap<RecipeType<?>, ITypedIngredient<?>> getRecipeCatalysts() {
		return recipeCatalysts.toImmutable();
	}
}
