package de.rexlmanu.boilerplate.item;

import de.exlll.configlib.Configuration;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ConfigItem {
  @Nullable String name;
  @Nullable Material material;
  @Nullable List<String> lore;
  @Nullable Integer amount;
  @Nullable String texture;
  @Nullable String owner;
  @Nullable Integer customModelData;
}
