package de.rexlmanu.boilerplate.command;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import de.rexlmanu.boilerplate.message.MessageProvider;
import de.rexlmanu.boilerplate.utils.MiniMessageComponentCaptionFormatter;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
      JavaPlugin javaPlugin,
      Injector injector,
      @Named("message") MiniMessage miniMessage,
      MessageProvider messageProvider) {

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
        .registerInjectionService(
            request -> {
              if (injector.findBindingsByType(TypeLiteral.get(request.injectedClass())).isEmpty()) {
                return null;
              }

              return injector.getInstance(request.injectedClass());
            });

    MinecraftExceptionHandler.<CommandSender>create(AudienceProvider.nativeAudience())
        .defaultInvalidSyntaxHandler()
        .defaultInvalidSenderHandler()
        .defaultNoPermissionHandler()
        .defaultArgumentParsingHandler()
        .defaultCommandExecutionHandler()
        .captionFormatter(new MiniMessageComponentCaptionFormatter<>(miniMessage, List.of()))
        .decorator(
            component ->
                messageProvider.getTranslationComponent(MessageConfig::prefix).append(component))
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
