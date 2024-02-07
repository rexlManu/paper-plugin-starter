import os


project_name = input("Enter the new project name (as example 'WorldEdit'): ")


def replace_token_in_file(file, token, value):
    with open(file, "r") as f:
        content = f.read()
    with open(file, "w") as f:
        f.write(content.replace(token, value))


project_name_lower = project_name.lower()
plugin_name = project_name + "Plugin"
loader_name = project_name + "Loader"

replace_token_in_file("settings.gradle.kts", "paper-plugin-starter", project_name_lower)
replace_token_in_file("lombok.config", "paperpluginstarter", project_name_lower)
replace_token_in_file("build.gradle.kts", "paperpluginstarter", project_name_lower)
replace_token_in_file("build.gradle.kts", "StarterPlugin", plugin_name)
replace_token_in_file("build.gradle.kts", "StarterLoader", loader_name)

plugin_directory = "src/main/java/de/rexlmanu/paperpluginstarter"

# rename the package
os.rename(
    plugin_directory, plugin_directory.replace("paperpluginstarter", project_name_lower)
)

plugin_directory = plugin_directory.replace("paperpluginstarter", project_name_lower)

main_class_path = os.path.join(plugin_directory, "StarterPlugin.java")
loader_class_path = os.path.join(plugin_directory, "StarterLoader.java")

# rename the main class
os.rename(main_class_path, main_class_path.replace("StarterPlugin", plugin_name))
os.rename(loader_class_path, loader_class_path.replace("StarterLoader", loader_name))

main_class_path = main_class_path.replace("StarterPlugin", plugin_name)
loader_class_path = loader_class_path.replace("StarterLoader", loader_name)

replace_token_in_file(main_class_path, "StarterPlugin", plugin_name)
replace_token_in_file(loader_class_path, "StarterLoader", loader_name)

# go though all files and replace the package name
for root, dirs, files in os.walk(plugin_directory):
    for file in files:
        if file.endswith(".java"):
            file_path = os.path.join(root, file)
            replace_token_in_file(
                file_path,
                "de.rexlmanu.paperpluginstarter",
                f"de.rexlmanu.{project_name_lower}",
            )

print("Project renamed to", project_name)

# remove the script
os.remove(__file__)
