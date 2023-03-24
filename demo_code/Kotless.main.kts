@file:Repository("https://packages.jetbrains.team/maven/p/ktls/maven")
@file:Repository("https://repo.maven.apache.org/maven2/")

@file:DependsOn("io.kotless:kotless-lang:0.2.0")
@file:DependsOn("io.kotless:kotless-lang-local:0.2.0")

import io.kotless.dsl.lang.http.Get

@Get("/")
fun main() = "Hello world!"

main()
