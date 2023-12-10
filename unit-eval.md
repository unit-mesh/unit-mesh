# 开源 UnitEval —— 构建 AI 辅助编程的高质量数据集与模型微调自动评估

太长不读：

Unit Eval 是一个针对于构建高质量代码微调的开源工具箱。其三个核心设计原则：

- 统一提示词（Prompt）。统一工具-微调-评估底层的提示词。
- 代码质量管道。诸如于代码复杂性、代码坏味道、测试坏味道、API 设计味道等。
- 可扩展的质量阈。自定义规则、自定义阈值、自定义质量类型等。

总的来说，UnitEval 所要解决的是生成高质量的数据集。

 GitHub：https://github.com/unit-mesh/unit-eval，文档：[https://eval.unitmesh.cc](https://eval.unitmesh.cc/) 。

PS：由于我最近没有算力资源，暂时只是让输入和输出满足所有设计。

## 为什么是 Unit Eval？

Unit Mesh 是我们认为的下一代 AI 自组织的的下一代软件架构，而 UnitEval 是 Unit Mesh 架构范式的一个新成员。

### 高质量微调数据集是刚需

年初（2023 年 4 月），我们做了一系列的代码微调探索，在那篇《**[AI 研发提效的正确姿势：开源 LLM + LoRA](https://www.phodal.com/blog/llm-lora-for-engineering-effectiveness-solution/)**》里，我们企业应该开始着力于：

- **规范与流程标准化**
- **工程化的数据准备**
- **高质量的脱敏数据**

回到今天来看，我们觉得这个观点依旧成立。

作为，国内最早的围绕于 AI + 开源 LLM 微调的探索者，我和我的 Thoughtworks 同事（@tianweiliu）发起了 Unit Mesh （GitHub：https://github.com/unit-mesh） 上发起/开源了一系列数据汲取代码、微调数据集、训练代码、视频教程等。

随后，我们发现只有微调是不够的，模型需要与工具紧密相结合。

### 工具-微调-评测一体化

围绕于这个体系，我和诸多 GitHub x友们（@iptton, @CGQAQ, @zhengxs2018）一起开发了 AI 辅助编程 IDE 插件 [AutoDev](https://github.com/unit-mesh/auto-dev)、AI 辅助文本编辑器 [Studio B3](https://github.com/unit-mesh/b3)、架构师 Copilot 工具 [Co-mate](https://github.com/unit-mesh/co-mate) 等，以及作为它们基础设施的智能应用 JVM SDK：[Chocolate Factory](https://github.com/unit-mesh/chocolate-factory)、边缘侧 SDK：[Edge-Infer](https://github.com/unit-mesh/edge-infer) 等。

而在有了足够的 Co-pilot 型应用开发经验之后，我们就能解决先前的微调问题 —— 即凭空微调是没有意义的。于是，我们开始构建**工具-微调-评测**一体化开源方案，即围绕于开发人员如何更好使用 AutoDev、Studio B3 等而构建的底层模型支持。

在设计 UnitEval 时，由于我们已经有了丰富的 AI IDE 经验，并且我们已经和大量的企业有了相关的交流。所以，我们想解决的主要痛点是：

- 高质量的代码数据集
- 真实项目作为测试数据集

事实上，与我们先前构建 Unit Minions、DevTi 时，并没有太大的区别。

## UnitEval：用调评一体化，构建高质量代码数据集

再让我们介绍一下 Unit Eval，它是一个针对于构建高质量代码微调的开源工具箱。其三个核心设计原则：

- 统一提示词（Prompt）。统一工具-微调-评估底层的提示词。
- 代码质量管道（Pipeline）。诸如于代码复杂性、代码坏味道、测试坏味道、API 设计味道等。
- 可扩展的质量阈。自定义规则、自定义阈值、自定义质量类型等。

总的来说，UnitEval 所要解决的是生成高质量的数据集。

### 设计原则 1 ：统一提示词

在 AutoDev 插件中，我们针对于解决 GitHub Copilot 在上下文上的能力不足，所以围绕于静态代码分析构建了 AutoDev 上下文体系。即在生成上下文里，会分析函数的输入和输出数据构建等，进而生成真正可用的构造函数等。

这就意味着，我们的 prompt 不能只是简单的 code complete，而是带丰富的上下文，它直接推翻了我们原先的微调体系。如下是简化后的 Prompt：

```jsx
Complete ${context.language} code, return rest code, no explaining

${context.framework}

```${context.language}
${context.relatedCode}
```

Code:
```${context.language}
${beforeCursor}
```
```

为此，我们需要在生成微调数据集和评测数据集时，生成一致的结构。为此，在 10 月，我们将 prompt 模板的能力下沉到底层的通用 SDK，并重构了 AutoDev 使之直接支持模板方式（Apache Veloctiy Engine）的 prompt。所以，在 UnitPicker 和 UnitEval 也直接使用类似的方式来构建 instruction。

### 设计原则 2：代码质量管道

对于代码微调来说，如何选择合适的代码是一件头疼的事？并且还要让选择的规则是可扩展的？？一种最简单的方式是直接采用静态代码分析，根据一些代码坏味道等来进行分析，以选择合适的代码质量。

基于 Thoughtworks 在软件工程的丰富经验，以及 Thoughtworks 的架构治理开源工具 ArchGuard （https://archguard.org/）作为基础设施。在 UnitEval 中，我们也将代码质量的筛选构建成 pipeline 的方式：

- 代码复杂度。在当前的版本设计里，可以直接通过**代码复杂度**来决定是否放**代码文件**进入数据库。
- 不同的坏味道检查类型。诸如于代码坏味道、测试坏味道等。
- 特定的规则检查。Controller 的 API 设计、Repository 的 SQL 设计 等。

而基于 ArchGuard 中所提供的丰富代码质量和架构质量分析能力，诸如 OpenAPI、 SCA（软件依赖分析）能力，我们也在思考未来是否也加入相关的设计。

### 设计原则 3：可扩展的质量阈

在现有的设计里，我们将 code-quality 作为一个独立的包发布到  Maven 仓库中。你可以根据不同的场景和能力，结合不同的规则来进行。诸如于上述的质量规则：

```jsx
enum class CodeQualityType {
    BadSmell,
    TestBadSmell,
    JavaController,
    JavaRepository,
    JavaService,
}
```

配置不同的质量标准（默认阈值选自我开源的遗留系统分析工具 Coca（https://github.com/phodal/coca）：

```jsx
data class BsThresholds(
    val bsLongParasLength: Int = 5,
    val bsIfSwitchLength: Int = 8,
    val bsLargeLength: Int = 20,
    val bsMethodLength: Int = 30,
    val bsIfLinesLength: Int = 3,
)
```

最后，还可以根据自己对于 API 设计、SQL 设计等的需要，添加自定义规则：

```jsx
val ruleset = RuleSet(
    RuleType.SQL_SMELL,
    "normal",
    UnknownColumnSizeRule(),
    LimitTableNameLengthRule()
    // more rules
)
```

基于上述的方式，来更多灵活的挑战自己的代码。当然了，现在 UnitEval 还没有达到如此的灵活的方式。

## How it works？

事实上，上面的内容已经讲了 UnitEval 的核心逻辑了。

### Unit Eval 主要逻辑

从过程来说，UnitEval 会分为 Picker 和 Eval 两个阶段。

在 Picker 阶段：

1. 读取配置信息。会通过读取 yml 配置文件，获得所有的项目信息，即从哪里 clone 代码。
2. 拉取代码。即执行 Git Clone 代码，获得远端的代码。
3. 选择语言的 worker。根据不同的语言选择对应的 worker，当前支持 Java 和 TypeScript。
4. 执行对应语言的质量检查。
5. 输出与 prompt 模板相关联的数据集。
6. 输出用于微调的数据集。

在 Eval 阶段：

1. 读取配置信息。读取 yml 配置文件，获得 LLM 模型的信息，以及 prompt 模板等。
2. 执行 PromptScript。即调用 Chocolate Factory 来执行 PromptScript 的内容，以输出最后的结果。
3. 进行对应的检查。结合  Chocolate Factory  的 ValidateRule。

### 如何使用 Unit Eval

UnitEval 作为一个开源项目，你可以

1. 直接根据自己的需要修改代码：https://github.com/unit-mesh/unit-eval 。
2. 根据文档，从 Release 中直接下载 `.jar` 包运行。
3. 直接使用 Maven 仓库提供的依赖，编写你的代码。

```jsx
dependencies {
    implementation("cc.unitmesh:unit-picker:0.1.5")
    implementation("cc.unitmesh:code-quality:0.1.5")
}
```

当然了，作为第一个版本，会有很多 bug，欢迎来提 issue：https://github.com/unit-mesh/unit-eval/issues 。

## 总结

挖坑不是一件容易的事，欢迎一起来探索 Unit Mesh 架构范式：https://github.com/unit-mesh 。
