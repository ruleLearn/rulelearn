# ruleLearn
Open source library for learning rule models from decision examples and applying these models to classify or rank new examples. 

Rule models are induced according to sequential covering algorithm presented in []. The learning is preceded by analysis of consistency of data, whcih is based on rough set theory. More precisely, this library implements dominance-based rough et approaches: original one (DRSA) [] and its variable consistency extensions (VC-DRSA) []. Rule models can be used to classify new examples [].

ruleLearn also allows to validate constructed rule models in stratified cross-validation.

## Quick start guide
Data sets analysed by ruleLearn are represented as decision tables, which are composed of objects described by attributes. Data sets should be provided in `CSV` or `JSON` format []. 

We consider the following use cases, which are typical forms of use of ruleLearn.

### Use cases

#### Checking consistency of data
In this case we will have to calculate lower and upper approximations of unions od ordered decision classes represented in data. 

#### Induction of a rule model


#### Validation of a rule model

### Importing into build.gradle
```
repositories {
    maven { url 'https://jitpack.io' }
}

$RL_VERSION = '0.14.2' //e.g. '0.14.2'

dependencies {
    compile("com.github.ruleLearn:rulelearn:${RL_VERSION}")
}
```

## Developer guide

### Documentation
[API Javadoc documentation](https://javadoc.jitpack.io/com/github/rulelearn/ruleLearn/latest/javadoc/)

### Configuration of Gradle build:
Developers should either extend/supply `gradle.properties` file (in the main directory of the project
or in the `USER_HOME/.gradle` directory), specifying inside this local path to the installed Java JDK,
e.g., on a Windows machine:

`org.gradle.java.home=C:\\Program Files\\Java\\jdk-11.0.1`

or set `JAVA_HOME` environmental variable to local path to the installed Java JDK.

### Other settings
When importing ruleLearn into an IDE (e.g., Eclipse, IntelliJ), one should specify the following settings: `UTF-8` encoding, and `LF` (i.e., line feed) line endings.
