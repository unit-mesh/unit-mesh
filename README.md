# Unit Mesh: Asymptotic AI programming architecture pattern

> Unit Mesh是一种基于人工智能生成的分布式架构，与传统的分布式架构不同，Unit Mesh 中的服务单元 (Unit) 是由 AI 生成的，应用程序中的服务和数据抽象为一个个独立的单元，并通过统一的控制平面进行管理和部署。

[简体中文版](README.zh_CN.md)

![Unit Mesh](images/unit-mesh-processes.png)

> Unlike traditional distributed architectures, the service units (Units) in Unit Mesh are generated by AI, and the services and data in the application are abstracted into individual units that are managed and deployed through a unified control plane.

PS: The reason why it is called Unit Mesh is because we wrote an underlying service called [UnitServer](https://github.com/prompt-engineering/unit-server), and there are references to Service Mesh and Data Mesh architecture concepts, so AI takes We suggest we call it ****Unit Mesh**** .

## TLDR Version

Our initial definition of this version (0.1, called UnitGenius) has three core features:

- **Language and Framework DSL** (Domain Specific Language) Abstraction: abstracts non-programming language and framework features to simplify the possibility of errors.
- **REPL as a Service**: runs AI-generated code and provides corresponding API services.
- **AI Designed Adaptive Architecture**: Self-adaptive API service architecture to automatically adapt and optimize in different environments.

Developers can generate a certain level of DSL abstracted code by interacting with AI, and then run and test this code on REPL, the Serverless service. Developers can also submit this code to the AI for automated operations and maintenance, and the AI will optimize and tune the code to further improve the performance and reliability of the API service.

Start with the nonsense version of the text.

## Unit Mesh Preliminary Demo: DSL + REPL = Unit Server

For a detailed process, see the second half of this article.

Front-end page: [https://prompt.phodal.com/zh-CN/click-flow/unit-mesh-unit-server/](https://prompt.phodal.com/zh-CN/click-flow/unit-mesh-unit-server/)

First, you need to clone the code of Unit Server: [https://github.com/prompt-engineering/unit-server](https://github.com/prompt-engineering/unit-server) , then Then, select kotlin-repl or typescript-repl corresponding to both Kotlin and TypeScript languages.

Then, run your Unit Server according to the corresponding README.

Next, have ChatGPT generate the following code in ChatFlow, and click the ``Run`` button:

```jsx
%spring

@RestController
object Pages {
   @GetMapping("/")
   fun main() = "It works!"
}
```

Finally, you get a running service (the feature is still under development): [http://localhost:8080/](http://localhost:8080/hello) and after visiting the service, if's should be It works.

---

PS: Here there is a manual code to call the Application class and call the main method, because we need to do static analysis to determine the framework used, for the time being not written in the Unit Server code.

# Unit Mesh Architecture

Repeat the definition again:

> Unit Mesh is a distributed architecture based on artificial intelligence generation. Unlike traditional distributed architectures, the service units (Units) in Unit Mesh are generated by AI, and the services and data in the application are abstracted into individual units and managed and deployed through a unified control plane.
># Unit Mesh

## Unit Mesh core idea: AI-generated code is Unit

Unit Mesh is an architectural pattern centered around units.

- AI generates Units, i.e., the code that AI should generate should be runnable **Unit**, up to React components and down to backend services.
- Unit verification: Humans check and verify the units, and if there is a problem with the AI-generated code, then humans just need to fix it.
- Unit adaptive deployment architecture. When deployed, Units can form Serverless architectures, microservices architectures, monolithic architectures, and mesh architectures, without human intervention.

Carbon base is suitable as a Verifier.

## Core Elements of Unit Mesh Architecture

Combined with the Unit Server we designed, the Unit Mesh architecture we designed consists of the following three elements:

![Unit Mesh Elements](images/unit-mesh-elements.png)

### DSL abstraction for languages and frameworks: encapsulating unstable abstractions

Since AI-generated code has various problems, such as inability to interface with internal cloud platforms, erroneous imports, etc., we need to design domain-specific languages to solve this problem and encapsulate abstractions.

In short: we need to **abstract all unstable elements** to build stable elements.

The detailed design will be developed later in the Unit Server section.

---

PS: Since the large language model has a contextual capacity limit, like me, I can't get the recharge only with 4k. therefore, I design the Unit to be called 4k Unit Mesh, I design the DSL to be called 4k Unit DSL, some people may be 99k DSL.

### REPL as a Service: AI Code Fixer's

After the DSL, we also need a REPL (Read-Eval-Print Loop) service that can directly run up the AI-generated units and then let humans test whether the generated code is correct. If there are errors in the generated AI, an AI code fixer is needed to fix the code.

For a service, if we are an API, it needs to be a Serverless service, which is why we call it in the diagram: REPL i.e. **Serverless** service. Details can be found later in the design of Unit Server.

### Adaptive Architecture for AI Design

One of the drawbacks of designing systems by humans is that if the units are not the same at design time, development time, and runtime, then there are all kinds of doubts. So, we would prefer to design into a tri-state consistent architectural pattern, which itself is a problem for the adaptive optimization of the architecture.

Since all code is a Unit, it can be designed as a microservice, developed as a Serverless, or online as a monolith. As Google's Service Waver does, we don't decide on the runtime architecture, we let you choose.

So, let AI decide how to run our Units.

![Adaptive Architecture](images/adaptive-architecture.png)

---

PS: Originally, the title should have been Adaptive Architecture, but I thought about just the code structure and such, and reconsidered it.

# Unit Mesh Design Insights: Counterintuitive is the way to go

At the end of last year, when researching low-latency architectures, I was struck by various **counterintuitive** architectural patterns in this field, such as: GC is the problem, then don't GC.

So when designing Unit Mesh, our question remains the same: **How to open your mind**. So our main challenge is how to **expand your mind** and open your mind**.

### Point 1: If layered architecture is the bottleneck, then don't layered architecture

In that "Future-proof AI programming" layered architecture is our biggest challenge, so the ideal way to propose is the Serverless + FaaS approach, which is based on the existing armament and is too idealistic.

And as we wrote UnitServer, we found that we could also Class as a Service approach (manual dog head).

Since our code runs in the cloud and is generated by AI, do humans have to look at the code? When do humans have to look at the code? It's just when they check in, and when they review the architecture, so they only need to generate the architecture when they **review** it.

Example: I want to analyze the calls to xx services and the corresponding code, **please** pull it out for me.

### Point 2: If dependencies are the problem, then don't depend on them

The second challenge we encounter is the dependency problem, and dependencies are two problems:

- Library dependencies for the project. That is, library dependencies similar to Gradle, Maven, NPM at this level
- Code dependencies. I.e. `import` of the code source file

Repeater ChatGPT does not solve the problem very well, so you have to let GPT forget about it. The ideal programming experience should be that if I want to use Spring, the intelligence will automatically analyze the dependencies, such as Intelij IDEA. so we use `% spring` like Jupyter magic syntax in UnitServer to automatically solve these two types of problems.

### Point 3: If Serverless deployment is the problem, then don't use Serverless deployment

Initially in Unit Server, we designed Unit Server as a Serverless-like architecture, so we ran into a problem: the cost of a Serverless architecture is not acceptable to all. So, we just need to use Serverless as a development when testing Unit and merge it into a monolithic or microservice architecture online, then we can solve this problem perfectly.

At this point, we also need to break through the hierarchical architecture, since each time the code is generated, we only need a package name, such as: `org.clickprompt.unitmesh`, all the code is under this package; or, we can further divide the business into different packages, combined with tools to categorize the code.

### Unit Mesh Discovery Path: From REPL to UnitServer

The above is too theoretical, let's take a look at our exploration path, which is divided into four steps:

1. start with a minimal Hello, world optimization
2. build a REPL environment
3. abstract and simplify the design ← repeat. 4.
4. connect to the real-world Prompt.

See Unit Server and ChatFlow commit records for details.

### Start with the smallest Hello, world

First, let's look at a Hello, World for Spring written in Kotlin Script.

```kotlin
@file:DependsOn("org.springframework.boot:spring-boot-starter-web:2.7.9")

import ...
import java.util.*

@Controller
class HelloController {
    @GetMapping("/hello")
    fun helloKotlin(): String {
        return "hello world"
    }
}

@SpringBootApplication
open class ReplApplication

fun main(args: Array<String>) {
    ...
}

main(arrayOf("--server.port=8083"))
```
In this example, you will find a series of useless code, dependency information, import information, main function. And as the creator of a 4k Unit Mesh, I had to get rid of these unstable useless messages in order to run it correctly, so it became:

```kotlin
%use spring

@Controller
class HelloController {
    @GetMapping("/hello")
    fun helloKotlin(): String {
        return "hello world"
    }
}
```

This way, I just need to get ChatGPT to return the Controller and that's it.

### Building the REPL environment: WebSocket + %magic

Now that we have a simplified DSL, the next step is to bring in Kotlin Script to build a Unit Serverless server, which is what we have: [https://github.com/prompt-engineering/unit-server](https://github.com/prompt-engineering/unit-server).

The source code of Unit Server is built on the Kotlin Jupyter API, which encapsulates the Kotlin REPL environment. The main reason for basing this on Kotlin Jupyter instead of Kotlin REPL is that magic and DSL can be used to abstract the details, such as in

```kotlin
"spring" to Json.encodeToString(
    SimpleLibraryDefinition(
        imports = listOf(
            "org.springframework.boot.*".
            "org.springframework.boot.autoconfigure.*".
            "org.springframework.web.bind.annotation.*".
            "ComponentScan".
            Configuration", "org.springframework.context.annotation.
        Configuration").
        dependencies = listOf(
            "org.springframework.boot:spring-boot-starter-web:2.7.9"
        )
    )
)
```

That is, you can automatically add Spring's dependencies and Import information, and you can support the Hello, World approach to the steps. In addition to Spring, we also need magic for other libraries.

Finally, the interface is then exposed using WebSocket to make it available to ChatFlow.

### Abstraction, simplified design ← Loop

Of course, just having a hello, world isn't enough, so we need more examples, such as access to a database. And because of Spring's scanning mechanism, and we don't want to (mostly not) do too much specialization for Spring, we switched to the Kotr framework in Kotlin.

PS: It is worth noting that we still need to abstract the framework, but Ktor is a little better than we expected. So, here comes our second version of

```kotlin
%use kotless
%use exposed

data class User(val id: Int, val username: String)

class Server : KotlessAWS() {
    override fun prepare(app: Application) {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.create(Users)
        }

        app.routing {
            post("/register") {
                val user = call.receive<User>()
                val id = transaction {
                    // Insert the new user into the database
                    users.insert {
                        it[username] = user.username
                    } get Users.id
                }

                val newUser = User(id, user.username)
                call.respond(newUser)
            }
        }
    }
}

object Users : org.jetbrains.exposed.sql.Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}
```

In this version, we use Exposed as the ORM for the database, using H2 as the database. Of course, it's still 10% away from taking this code as a Unit, but basically it can solve most of the CRUD scenarios.

PS 1: KotlessAWS here is just an AWS Serverless abstraction, does not affect our operation, we can directly encapsulate a UnitMesh class, is lazy.

PS 2: We just need to take out the code in routing by static analysis, and then optimize it. More code for exploring the process can be found at: [_samples]([https://github.com/prompt-engineering/unit-server/tree/master/kotlin-repl/_samples](https://github.com/prompt-engineering/unit-server/tree/master/kotlin-repl/_samples)).

### A real-world Prompt

Now, let's run it in conjunction with AI: the

``kotlin
Please help me implement a RESTful API for user registration using Ktor + Kotlin + Exposed, with the following requirements:

- Where database is involved, please use Database.connect directly.
- Only return the core logic and write it in Server class, I want to deploy it in Serverless server.
- Please use Kotlin DSL to write the code.
- Do not return any other extraneous code such as comments, dependencies, imports, etc.

Finally, you return only the code of the class, in the following format:
```

```kotlin
class Server : KotlessAWS() {
    override fun prepare(app: Application) {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            SchemaUtils.create(Users)
        }

        app.routing {
            { {{{}}}
        }
    }
}
```

Life is short, welcome to join our Watchlist and discuss the future together.

## Join Waitlist

Right now **Waitlist engineers**, you can join Unit Mesh's Watchlist at:

[https://github.com/prompt-engineering/unit-mesh](https://github.com/prompt-engineering/unit-mesh)

