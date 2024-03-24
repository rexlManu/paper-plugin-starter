# paper-plugin-starter

This is a boilerplate for creating a Paper plugin.

## Features

- Gradle Kotlin DSL
- GitHub Actions
- `paper-plugin.yml` dynamically generated via gradle
- Dependencies are downloaded while runtime instead of shading them
- Dependency Injection via Guice
- Automatic discovery of Components
- Typed Configurations that support reloading
- Incendo Cloud v2 Command Framework
- Listeners and annotation based commands are registered when discovered
- Annotations for Lifecycle Events like `@OnPluginEnable`, `@OnPluginDisable` and `@OnPluginReload`
- Lifecycle Hooks `@RunBefore` and `@RunAfter` for precise declaration when methods are executed
- Define methods as `@TimedTask` to run them on an async repeating schedule
- Extensive Task Scheduler that also supports Folia
- Gradle Version Catalogs

## Libraries

- [Paper](https://papermc.io/)
- [Guice](https://github.com/google/guice)
- [ConfigLib](https://github.com/Exlll/ConfigLib)
- [Cloud](https://github.com/Incendo/cloud)
- [Classgraph](https://github.com/classgraph/classgraph)
- [Annotations](https://github.com/JetBrains/java-annotations)
- [InvUI](https://github.com/NichtStudioCode/InvUI)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

- [Emmanuel Lampe](https://github.com/rexlManu)
