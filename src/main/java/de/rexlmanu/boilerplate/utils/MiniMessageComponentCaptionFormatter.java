package de.rexlmanu.boilerplate.utils;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.minecraft.extras.caption.ComponentCaptionFormatter;
import org.incendo.cloud.minecraft.extras.caption.RichVariable;
import org.intellij.lang.annotations.Subst;

public class MiniMessageComponentCaptionFormatter<C> implements ComponentCaptionFormatter<C> {

  private final MiniMessage miniMessage;
  private final List<TagResolver> extraResolvers;

  public MiniMessageComponentCaptionFormatter(
      final @NonNull MiniMessage miniMessage, final @NonNull List<TagResolver> extraResolvers) {
    this.miniMessage = miniMessage;
    this.extraResolvers = extraResolvers;
  }

  @Override
  public @NonNull Component formatCaption(
      final @NonNull Caption captionKey,
      final @NonNull C recipient,
      final @NonNull String caption,
      final @NonNull List<@NonNull CaptionVariable> variables) {
    final TagResolver.Builder builder = TagResolver.builder();
    builder.resolvers(this.extraResolvers);
    for (final CaptionVariable variable : variables) {
      @Subst("key")
      final String key = variable.key();
      if (variable instanceof RichVariable) {
        builder.resolver(Placeholder.component(key, ((RichVariable) variable).component()));
      } else {
        builder.resolver(Placeholder.parsed(key, variable.value()));
      }
    }
    return this.miniMessage.deserialize(caption, builder.build());
  }
}
