package de.rexlmanu.boilerplate.message;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MessageModule extends AbstractModule {
  @Provides
  @Singleton
  @Named("message")
  public MiniMessage provideMiniMessage(Provider<MessageProvider> messageProvider) {
    return MiniMessage.builder()
        .tags(
            TagResolver.resolver(
                TagResolver.standard(),
                TagResolver.resolver(
                    "prefix",
                    (argumentQueue, context) ->
                        Tag.inserting(
                            context.deserialize(
                                messageProvider.get().getTranslation(MessageConfig::prefix))))))
        .strict(false)
        .build();
  }
}
