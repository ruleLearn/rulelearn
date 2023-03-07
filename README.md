# ruleLearn
Open source library for learning rule models from decision examples and applying these models to classify or rank new examples. 

Rule models are induced according to VC-DomLEM sequential covering algorithm presented in [[1]](#VCDomLEM). The learning is preceded by analysis of consistency of data, which is based on rough set theory. More precisely, this library implements dominance-based rough et approaches: original one (DRSA) [[2]](#DRSA) and its variable consistency extensions (VC-DRSA) [[3]](#VCDRSA). Rule models can be used to classify new examples, using VC-DRSA classifier [[4](#VCDRSA-Classifier)], or MODE classifier [[5](#Mode-Classifier)]. During data analysis, missing attribute values are handled [[6](#DRSA-MV)].

ruleLearn also allows to validate constructed rule models in stratified cross-validation.

## Quick start guide
Data sets analyzed by ruleLearn are represented as decision tables, which are composed of objects described by attributes. Data sets should be provided in `CSV` or `JSON` format []. 

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

$RL_VERSION = '0.24.2' //e.g. '0.24.2'

dependencies {
    compile("com.github.ruleLearn:rulelearn:${RL_VERSION}")
}
```

### Importing into pom.xml
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ruleLearn</groupId>
        <artifactId>rulelearn</artifactId>
        <version>0.24.2</version>
    </dependency>
</dependencies>
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

## References

<a name="VCDomLEM">[1]</a>: Błaszczyński, J., Słowiński, R. , Szeląg, M., Sequential Covering Rule Induction Algorithm for Variable Consistency Rough Set Approaches. Information Sciences, 181, 2011, pp. 987-1002.

<a name="DRSA">[2]</a>: Greco, S., Matarazzo, B., Słowiński, R., Rough Sets Theory for Multicriteria Decision Analysis. European Journal of Operational Research, 129(1), 2001, pp. 1-47.

<a name="VCDRSA">[3]</a>: Błaszczyński, J., Greco, S., Słowiński, R., Szeląg, M., Monotonic Variable Consistency Rough Set Approaches. International Journal of Approximate Reasoning, 50(7), 2009, pp. 979-999.

<a name="VCDRSA-Classifier">[4]</a>: Błaszczyński, J., Greco, S., Słowiński, R., Multi-criteria classification - A new scheme for application of dominance-based decision rules. European Journal of Operational Research, 181(3), 2007, pp. 1030-1044.

<a name="Mode-Classifier">[5]</a>: Szeląg, M., Słowiński, R., Dominance-based Rough Set Approach to Bank Customer Satisfaction Analysis. [In]: P. Jędrzejowicz, I. Czarnowski, A. Skakovski, M. Forkiewicz, M. Szarmach, P. Wolski (Eds.), PP-RAI'2022, Proceedings of the 3rd Polish Conference on Artificial Intelligence, April 25-27, 2022, Gdynia, Poland, Publishing House of Gdynia Maritime University, Gdynia, Poland, pp. 147-150.

<a name="DRSA-MV">[6]</a>: Szeląg, M., Błaszczyński, J., Słowiński, R., Rough Set Analysis of Classification Data with Missing Values. [In]: L. Polkowski et al. (Eds.): Rough Sets, International Joint Conference, IJCRS 2017, Olsztyn, Poland, July 3–7, 2017, Proceedings, Part I. Lecture Notes in Artificial Intelligence, vol. 10313, Springer, 2017, pp. 552–565.
