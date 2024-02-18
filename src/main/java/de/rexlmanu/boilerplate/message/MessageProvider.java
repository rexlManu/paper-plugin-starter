package de.rexlmanu.boilerplate.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.rexlmanu.boilerplate.config.ConfigProvider;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessageProvider {
  private static final MessageConfig DEFAULT_MESSAGE_CONFIG = new MessageConfig();

  private final ConfigProvider configProvider;

  @Named("message")
  private final MiniMessage miniMessage;

  public void send(
      Audience audience, Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    audience.sendMessage(this.getTranslationComponent(messageMapper, resolvers));
  }

  public String getTranslation(Function<MessageConfig, String> messageMapper) {
    return messageMapper.apply(
        this.configProvider.getOrNull(MessageConfig.class).orElse(DEFAULT_MESSAGE_CONFIG));
  }

  public Component getTranslationComponent(
      Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    return this.miniMessage.deserialize(this.getTranslation(messageMapper), resolvers);
  }
}
