@file:Repository("https://packages.jetbrains.team/maven/p/ktls/maven")
@file:Repository("https://repo.maven.apache.org/maven2/")

@file:DependsOn("org.springframework.boot:spring-boot-starter-web:2.7.9")
@file:DependsOn("io.kotless:kotless-lang:0.2.0")
@file:DependsOn("io.kotless:spring-boot-lang:0.2.0")
@file:DependsOn("io.kotless:spring-boot-lang-local:0.2.0")
@file:DependsOn("io.kotless:spring-lang-parser:0.2.0")

import io.kotless.dsl.spring.Kotless
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


@RestController
object Pages {
    @GetMapping("/")
    fun main() = "Hello World!"
}

@SpringBootApplication
open class Application : Kotless() {
    override val bootKlass: KClass<*> = this::class
}

fun main() {
    val port = 8080
    val classToStart = Application::class.java.name
    println("Starting $classToStart on port $port")
//    val port = System.getenv("SERVER_PORT").toInt()
//    val classToStart = System.getenv("CLASS_TO_START")

    val ktClass = ::main::class.java.classLoader.loadClass(classToStart).kotlin
    val instance = (ktClass.primaryConstructor?.call() ?: ktClass.objectInstance) as? Kotless

    val kotless = instance ?: error("The entry point $classToStart does not inherit from ${Kotless::class.qualifiedName}!")

    val app = SpringApplication(kotless.bootKlass.java)
    app.setDefaultProperties(mapOf("server.port" to port.toString()))
    app.run()
}

main()