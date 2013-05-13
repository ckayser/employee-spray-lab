package com.excilys.lab.spray.routing

import org.specs2.mutable.{After, Specification}
import spray.http._
import spray.http.HttpHeaders.Location
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._
import spray.http.MediaTypes._
import com.excilys.lab.spray.model._
import com.excilys.lab.spray.dao._
import com.excilys.lab.spray.model.EmployeeProtocol._
import spray.httpx.SprayJsonSupport._


class EmployeeResourceSpec extends Specification with Specs2RouteTest with EmployeeResourceService {
  sequential

  implicit def actorRefFactory = system

  "The employee resource service" should {

    "return the name of the employee for GET requests with an id" in new employees {
      Get("/employee/2") ~> employeeResourceRoute ~> check {
        status === OK
        mediaType === `text/plain`
        entityAs[String] === "name 3"
      }
    }
    "create an employee for PUT requests to the employee path" in new employees {
      Put("/employee").withEntity(HttpBody(`application/json`, """{ "name" : "kayser", "age"  : 27 }""")) ~>
        employeeResourceRoute ~> check {
          status === Created
          header[Location] must beSome[Location].which(_.value.matches("/employee/[0-9]+"))
      }
    }

    "return the list of employees for GET requests to the employee path" in new employees {

      Get("/employee") ~> employeeResourceRoute ~> check {
        status === OK
        mediaType === `application/json`
        val employees = entityAs[Seq[Employee]]
        employees must haveAllElementsLike { case employee => employee.name must beMatching("name [1|2|3]") }
      }
    }

    "return a NotAcceptable error for GET requests with text/plain Accept header to th employee path" in {
      Get("/employee") ~> addHeader(HttpHeaders.Accept(`text/plain`)) ~> sealRoute(employeeResourceRoute) ~> check {
        status === NotAcceptable
      }
    }
  }

  trait employees extends After {
    employeeDaoActor ! Persist(Employee(None, "name 1", 25))
    employeeDaoActor ! Persist(Employee(None, "name 2", 26))
    employeeDaoActor ! Persist(Employee(None, "name 3", 27))

    def after = employeeDaoActor ! RemoveAll()
  }
}
