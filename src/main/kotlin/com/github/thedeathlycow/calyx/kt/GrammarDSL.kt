package com.github.thedeathlycow.calyx.kt

class RuleBuildContext(
    private val registry: Registry
) {

    fun uniformBranch(builder: UniformRuleBuilderContext.() -> Unit): RuleBuilder {
        val uniformBranchBuilder = UniformRuleBuilderContext()
        uniformBranchBuilder.builder()
        return uniformBranchBuilder
    }

    fun weightedBranch(builder: WeightedRuleBuilderContext.() -> Unit): RuleBuilder {
        val uniformBranchBuilder = WeightedRuleBuilderContext()
        uniformBranchBuilder.builder()
        return uniformBranchBuilder
    }

}

abstract class RuleBuilder {

    abstract fun build(name: String, registry: Registry): Rule

}

class UniformRuleBuilderContext : RuleBuilder() {

    private val choices: MutableList<String> = mutableListOf()

    override fun build(name: String, registry: Registry): Rule {
        return Rule.build(name, choices, registry)
    }

    fun item(choice: String) {
        choices.add(choice)
    }

    fun items(vararg choices: String) {
        this.choices.addAll(choices)
    }

}

class WeightedRuleBuilderContext : RuleBuilder() {

    private val choices: MutableMap<String, Double> = mutableMapOf()

    override fun build(name: String, registry: Registry): Rule {
        return Rule.build(name, choices, registry)
    }

    fun item(choice: String, weight: Double) {
        choices[choice] = weight
    }

    fun items(vararg choices: Pair<String, Double>) {
        this.choices.putAll(choices)
    }

}

fun Grammar.start(createBuilder: RuleBuildContext.() -> RuleBuilder) {
    rule("start", createBuilder)
}

fun Grammar.rule(name: String, createBuilder: RuleBuildContext.() -> RuleBuilder) {
    val ctx = RuleBuildContext(this.registry)
    val builder = ctx.createBuilder()
    this.registry.defineRule(name, builder.build(name, this.registry))
}