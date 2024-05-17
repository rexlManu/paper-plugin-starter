package de.rexlmanu.paperpluginstarter.command;

import static net.kyori.adventure.text.Component.text;

import com.google.inject.Inject;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.help.result.CommandEntry;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HelpCommand {
  private final CommandManager<CommandSender> commandManager;

  @OnPluginEnable
  public void registerCommands() {
    MinecraftHelp<CommandSender> minecraftHelp =
        MinecraftHelp.create(
            "/starter help", this.commandManager, AudienceProvider.nativeAudience());

    this.commandManager.command(
        this.commandManager
            .commandBuilder("starter", RichDescription.of(text("Show the help menu.")))
            .permission("starter.command.help")
            .literal("help")
            .optional(
                "query",
                StringParser.greedyStringParser(),
                DefaultValue.constant(""),
                SuggestionProvider.blocking(
                    (ctx, in) ->
                        this.commandManager
                            .createHelpHandler()
                            .queryRootIndex(ctx.sender())
                            .entries()
                            .stream()
                            .map(CommandEntry::syntax)
                            .map(Suggestion::suggestion)
                            .toList()))
            .handler(
                context -> minecraftHelp.queryCommands(context.get("query"), context.sender())));
  }
}
