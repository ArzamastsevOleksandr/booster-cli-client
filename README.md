# booster-cli-client

The command line application interacting with the [booster-server](https://github.com/ArzamastsevOleksandr/booster-server).

Currently, only the interactive commands are supported, meaning that to add a vocabulary entry, for example, one must separately pass the word, description, synonyms, antonyms etc, until the whole command arguments are passed.

Every command can be interrupted with the `EXIT` command (`e`). Shutting down the app is also done with the `EXIT` command.

To start the project run it from the IDEA or execute the following command from the project root:
```bash
./mvnw clean install && java -jar target/cli-client-0.0.1-SNAPSHOT.jar
```
