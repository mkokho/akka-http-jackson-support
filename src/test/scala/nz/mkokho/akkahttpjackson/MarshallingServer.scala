/*
 * Copyright 2016  Mikhail Kokho
 *
 * Licensed under the MIT License (MIT)
 */

package nz.mkokho.akkahttpjackson

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class Person(name: String)
case class Result(status: String)

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



