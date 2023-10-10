# Calyx Kotlin

[![](https://jitpack.io/v/TheDeathlyCow/calyx-kt.svg)](https://jitpack.io/#TheDeathlyCow/calyx-kt)

A simple API for generating text using grammars.

Port of [Calyx](https://github.com/maetl/calyx) for the Kotlin/JVM ecosystem. Can be used in both Kotlin and Java
projects!

# Building and testing

CalyxKt is built using Gradle. Gradle is not required to build and test Calyx.

Build CalyxKt using the following command

```bash
./gradlew build
```

And to test...

```bash
./gradlew test
```

# Including in your project

CalyxKt is distributed using [Jitpack](https://jitpack.io).

## Gradle

Add the Jitpack repository to your project:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add Calyx as a dependency:

```groovy
dependencies {
    implementation 'com.github.TheDeathlyCow:calyx-kt:VERSION'
}
```

Replace `VERSION` with the version you wish to use.

## Maven

Add the Jitpack repository to your project:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add Calyx as a dependency:

```xml

<dependency>
    <groupId>com.github.TheDeathlyCow</groupId>
    <artifactId>calyx-kt</artifactId>
    <version>VERSION</version>
</dependency>
```

Replace `VERSION` with the version you wish to use.

# Usage

Start by instantiating a `Grammar` and invoking `start()` on it. Then, to generate some actual text, call the
`generate()` method.

Kotlin:

```kotlin
fun main() {
    val grammar = Grammar()

    grammar.start("Hello World")

    println(grammar.generate())
    // > "Hello World"
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.start("Hello World");
        System.out.println(grammar.generate());
        // > "Hello World"
    }
}
```

</details>

We can add more variety by defining some new 'productions' using the rule delimiter syntax `{}`. Each production defines
a possible substitution that can be made to generate the content of other rules.

Kotlin:
```kotlin
fun main() {
    val grammar = Grammar()

    grammar.start("{greeting} world")

    grammar.rule("greeting", listOf("Hello", "Hey", "Kia Ora"))
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.start("{greeting} world");
        grammar.rule("greeting", List.of("Hello", "Hey", "Kia Ora"));
    }
}
```

</details>



Each time `generate()` runs, the tree is evaluated randomly to select variations of the rules and build the final
output.

Kotlin:
```kotlin
println(grammar.generate())
// > Hello world
println(grammar.generate())
// > Hey world
println(grammar.generate())
// > Kia Ora world
```

By default, the `start` rule defines the starting point for generating the final text. However, grammars can start
generating from any rule by passing the rule explicitly to `generate()`.

Kotlin:
```kotlin
fun main() {
    val grammar = Grammar()

    grammar.start("{greeting} world")

    grammar.rule("greeting", listOf("Hello", "Hey", "Kia Ora"))

    println(grammar.generate("greeting"))
    // > Hello
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.start("{greeting} world");
        grammar.rule("greeting", List.of("Hello", "Hey", "Kia Ora"));
    }
}
```

</details>

## Registration Callback

Rather than having to define rules one at a time, Rules can instead be defined when a grammar is initialized using the
registration callback and dot-chaining:

Kotlin:
```kotlin
fun main() {
    val grammar = Grammar { // Uses a Receiver type, so a `this` passed implicitly instead of `it` 
        start("{greeting} world")
            .rule("greeting", listOf("Hello", "Hey", "Kia Ora"))
    }

    println(grammar.generate())
    // > Hey World
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {

        Grammar grammar = new Grammar(Options.defaultStrict, g -> {
            g.start("{greeting} world")
                    .rule("greeting", List.of("Hello", "Hey", "Kia Ora"));
            return null;
        });
        System.out.println(grammar.generate());
        // > Hey World
    }
}
```

Special notes for the Java version here: Java does not have optional arguments, so arguments that are optional in Kotlin
are required in Java. Also, the lambda type in Kotlin returns `Unit`, which is equivalent to `Void` (yes with a capital
-- not `void`), and the only valid value for `Void` in Java is `null`, so you must ALWAYS return `null` in the
registration
callback. Weird Java interoperability stuff is weird!

</details>

## Template Expressions

Basic rule substitution uses single curly brackets as delimiters for template expressions:

Kotlin:
```kotlin
fun main() {
    val grammar = Grammar {
        start("{colour} {fruit}")
            .rule("colour", listOf("red", "green", "yellow"))
            .rule("fruit", listOf("apple", "pear", "tomato"))
    }

    for (i in 0..6) {
        println(grammar.generate())
    }
    // > "yellow pear"
    // > "red apple"
    // > "green tomato"
    // > "red pear"
    // > "yellow tomato"
    // > "green apple"
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {

        Grammar grammar = new Grammar(Options.defaultStrict, g -> {
            g.start("{colour} {fruit}")
                    .rule("colour", List.of("red", "green", "yellow"))
                    .rule("fruit", List.of("apple", "pear", "tomato"));
            return null;
        });

        for (int i = 0; i < 6; i++) {
            System.out.println(grammar.generate());
        }
        // > "yellow pear"
        // > "red apple"
        // > "green tomato"
        // > "red pear"
        // > "yellow tomato"
        // > "green apple"
    }
}
```

</details>

## Nesting and Substitution

Rules are recursive. They can be arbitrarily nested and connected to generate larger and more complex texts.

Kotlin:
```kotlin
fun main() {
    val grammar = Grammar {
        start(listOf("{greeting}", "{world_phrase}."))
            .rule("greeting", listOf("Hello", "Hi", "Hey", "Yo"))
            .rule("world_phrase", listOf("{happy_adj} world", "{sad_adj} world", "world"))
            .rule("happy_adj", listOf("amazing", "bright", "beautiful"))
            .rule("sad_adj", listOf("cruel", "miserable"))
    }
}
```

<details>
<summary>Java</summary>

```java
class Example {
    public static void main(String[] args) {
        Grammar grammar = new Grammar(Options.defaultStrict, g -> {
            g.start(List.of("{greeting}", "{world_phrase}."))
                    .rule("greeting", List.of("Hello", "Hi", "Hey", "Yo"))
                    .rule("world_phrase", List.of("{happy_adj} world", "{sad_adj} world", "world"))
                    .rule("happy_adj", List.of("amazing", "bright", "beautiful"))
                    .rule("sad_adj", List.of("cruel", "miserable"));
            return null;
        });
    }
}
```

</details>





