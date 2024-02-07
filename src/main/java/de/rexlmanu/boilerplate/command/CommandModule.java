package de.rexlmanu.boilerplate.command;

import static net.kyori.adventure.text.Component.text;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.paper.PaperCommandManager;

@RequiredArgsConstructor
public class CommandModule extends AbstractModule {
  @Override
  protected void configure() {
    var listener = new AnnotationCommandListener();
    this.requestInjection(listener);
    super.bindListener(Matchers.any(), listener);
  }

  @Provides
  @Singleton
  public CommandManager<CommandSender> provideCommandManager(
      JavaPlugin javaPlugin, Injector injector, MiniMessage miniMessage) {

    PaperCommandManager<CommandSender> commandManager =
        new PaperCommandManager<>(
            javaPlugin, ExecutionCoordinator.asyncCoordinator(), SenderMapper.identity());

    if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
      commandManager.registerBrigadier();
    } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
      commandManager.registerAsynchronousCompletions();
    }

    commandManager
        .parameterInjectorRegistry()
        .registerInjectionService(context -> injector.getInstance(context.injectedClass()));

    MinecraftExceptionHandler.<CommandSender>create(AudienceProvider.nativeAudience())
        .defaultInvalidSyntaxHandler()
        .defaultInvalidSenderHandler()
        .defaultNoPermissionHandler()
        .defaultArgumentParsingHandler()
        .defaultCommandExecutionHandler()
        .decorator(
            component ->
                text()
                    .append(text("[", NamedTextColor.DARK_GRAY))
                    .append(text("Starter", NamedTextColor.GOLD))
                    .append(text("] ", NamedTextColor.DARK_GRAY))
                    .append(component)
                    .build())
        .registerTo(commandManager);

    return commandManager;
  }

  @Provides
  @Singleton
  @Inject
  public AnnotationParser<CommandSender> provideAnnotationParser(
      CommandManager<CommandSender> commandManager) {
    return new AnnotationParser<>(commandManager, CommandSender.class);
  }
}
