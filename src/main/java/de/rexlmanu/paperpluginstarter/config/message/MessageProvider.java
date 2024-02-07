package de.rexlmanu.paperpluginstarter.config.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.boilerplate.config.ConfigProvider;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessageProvider {
  private final ConfigProvider configProvider;
  private final MiniMessage miniMessage;

  public void send(
      Audience audience, Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    audience.sendMessage(this.getTranslationComponent(messageMapper, resolvers));
  }

  public String getTranslation(Function<MessageConfig, String> messageMapper) {
    return messageMapper.apply(this.configProvider.get(MessageConfig.class));
  }

  public Component getTranslationComponent(
      Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    return this.miniMessage.deserialize(this.getTranslation(messageMapper), resolvers);
  }
}
