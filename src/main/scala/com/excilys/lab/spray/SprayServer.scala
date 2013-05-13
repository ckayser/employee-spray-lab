package com.excilys.lab.spray

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import com.excilys.lab.spray.routing.EmployeeResourceServiceActor

object SprayServer extends App with SprayCanHttpServerApp {

  val service = system.actorOf(Props[EmployeeResourceServiceActor])

  newHttpServer(service) ! Bind("localhost", 8080)

}
