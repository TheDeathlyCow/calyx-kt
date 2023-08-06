# Calyx Kotlin

[![](https://jitpack.io/v/TheDeathlyCow/calyx-kt.svg)](https://jitpack.io/#TheDeathlyCow/calyx-kt)

A simple API for generating text using grammars.

Port of [Calyx](https://github.com/maetl/calyx) for the Kotlin/JVM ecosystem. Can be used in both Kotlin and Java projects!

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


```kotlin
fun main() {
    val grammar = Grammar()
    
    grammar.start("Hello World")
    
    println(grammar.generate())
    // > "Hello World"
}
```

We can add more variety by defining some new 'productions' using the rule delimiter syntax `{}`. Each production defines
a possible substitution that can be made to generate the content of other rules.

```kotlin
fun main() {
    val grammar = Grammar()
    
    grammar.start("{greeting} world")
    
    grammar.rule("greeting", listOf("Hello", "Hey", "Kia Ora"))
}
```

Each time `generate()` runs, the tree is evaluated randomly to select variations of the rules and build the final output.

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

```kotlin
fun main() {
    val grammar = Grammar()
    
    grammar.start("{greeting} world")
    
    grammar.rule("greeting", listOf("Hello", "Hey", "Kia Ora"))
    
    println(grammar.generate("greeting"))
    // > Hello
}
```

## Registration Callback

Rather than having to define rules one at a time, Rules can instead be defined when a grammar is initialized using the 
registration callback and dot-chaining:

```kotlin
fun main() {
    val grammar = Grammar {
        it.start("{greeting} world")
            .rule("greeting", listOf("Hello", "Hey", "Kia Ora"))
    }
    
    println(grammar.generate())
    // > Hey World
}
```

## Template Expressions

Basic rule substitution uses single curly brackets as delimiters for template expressions:
```kotlin
fun main() {
    val grammar = Grammar {
        it.start("{colour} {fruit}")
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

## Nesting and Substitution

Rules are recursive. They can be arbitrarily nested and connected to generate larger and more complex texts.

```kotlin
fun main() {
    val grammar = Grammar {
        it.start(listOf("{greeting}", "{world_phrase}."))
            .rule("greeting", listOf("Hello", "Hi", "Hey", "Yo"))
            .rule("world_phrase", listOf("{happy_adj} world", "{sad_adj} world", "world"))
            .rule("happy_adj", listOf("amazing", "bright", "beautiful"))
            .rule("sad_adj", listOf("cruel", "miserable"))
    }
}
```





