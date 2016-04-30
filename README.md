# akka-http-jackson-support
Addition to Akka HTTP which provides (un)marshalling via Jackson library

# Usage
```scala
object MarshallingServer extends JacksonJsonSupport {
  val jacksonObjectMapper = new ObjectMapper()
  //Jackson scala module is required for case classes
  jacksonObjectMapper.registerModule(DefaultScalaModule)

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      post {
        pathSingleSlash {
          entity(as[Person]) { person =>
            complete(Result("ok"))
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }
}
```

# Dependencies
The library is built based on Akka HTTP 2.4.4 and Jackson Module Scala 2.7.3
```
    "com.typesafe.akka" %% "akka-http-core" % "2.4.4"    
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4"
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.3"
```

