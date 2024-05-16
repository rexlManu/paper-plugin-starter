package de.rexlmanu.boilerplate.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class ItemFactory {
  private final MiniMessage miniMessage;
  private final Server server;

  public ItemStack make(ConfigItem configItem, TagResolver... tagResolver) {
    if (configItem == null) {
      log.warn("ConfigItem is null.");
      return new ItemStack(Material.BARRIER);
    }
    Material material = configItem.material;

    if (material == null || material.isAir()) {
      log.warn("Material {} is not valid.", material);
      material = Material.BARRIER;
    }

    ItemStack itemStack = new ItemStack(material);
    if (configItem.amount != null) {
      itemStack.setAmount(configItem.amount);
    }

    ItemMeta itemMeta = itemStack.getItemMeta();

    if (!isEmpty(configItem.name)) {
      itemMeta.displayName(this.miniMessage.deserialize(configItem.name, tagResolver));
    }

    if (configItem.customModelData != null) itemMeta.setCustomModelData(configItem.customModelData);

    if (configItem.lore != null && !configItem.lore.isEmpty()) {
      itemMeta.lore(
          configItem.lore.stream()
              .map(lore -> this.miniMessage.deserialize(lore, tagResolver))
              .toList());
    }

    if (!isEmpty(configItem.texture) && itemMeta instanceof SkullMeta skullMeta) {
      PlayerProfile profile =
          this.server.createProfile(createUUIDBasedOnTexture(configItem.texture));
      profile.setProperty(new ProfileProperty("textures", configItem.texture));
      skullMeta.setPlayerProfile(profile);
    }

    if (!isEmpty(configItem.owner) && itemMeta instanceof SkullMeta skullMeta) {
      skullMeta.setOwningPlayer(this.server.getOfflinePlayer(configItem.owner));
    }

    for (ItemFlag value : ItemFlag.values()) {
      itemMeta.addItemFlags(value);
    }

    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  private static boolean isEmpty(String string) {
    return string == null || string.isEmpty();
  }

  private static UUID createUUIDBasedOnTexture(String texture) {

    return new UUID(texture.hashCode(), texture.hashCode());
  }
}
