package com.excilys.lab.spray.routing

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.duration._
import spray.http.HttpHeaders.Location
import spray.http.HttpResponse
import spray.http.StatusCodes._
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._

import com.excilys.lab.spray.model._
import com.excilys.lab.spray.model.EmployeeProtocol._
import com.excilys.lab.spray.dao._

class EmployeeResourceServiceActor extends Actor with EmployeeResourceService {
  def actorRefFactory = context

  def receive = runRoute(employeeResourceRoute)
}

trait EmployeeResourceService extends HttpService {

  val employeeResourceRoute =
    get {
      path("employee" / IntNumber) { employeeId =>
        complete {
          retrieveEmployeeNameById(employeeId)
        }
      } ~
      path("employee") {
        complete {
          retrieveEmployees()
        }
      }
    } ~
    put {
      path("employee") {
        entity(as[Employee]) { employee =>
          complete {
            persistNewEmployee(employee).map { newId =>
              HttpResponse(
                status = Created,
                headers = Location(s"/employee/$newId") :: Nil
              )
            }
          }
        }
      }
    }

  implicit val timeout = Timeout(5 seconds)

  val employeeDaoActor = actorRefFactory.actorOf(Props[EmployeeDaoActor])

  def retrieveEmployeeNameById(id: Int): Future[String] =
    (employeeDaoActor ? Retrieve(id)).mapTo[Employee].map(_.name)

  def persistNewEmployee(employee: Employee) =
    (employeeDaoActor ? Persist(employee)).mapTo[Int]

  def retrieveEmployees(): Future[Iterable[Employee]] =
    (employeeDaoActor ? RetrieveAll()).mapTo[Iterable[Employee]]
}
