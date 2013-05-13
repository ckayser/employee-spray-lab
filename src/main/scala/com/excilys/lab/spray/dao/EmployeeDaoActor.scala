package com.excilys.lab.spray.dao

import akka.actor.Actor
import com.excilys.lab.spray.model.Employee

class EmployeeDaoActor extends Actor {
  private[this] var employeesInMem = Map.empty[Int, Employee]

  def receive = {
    case Retrieve(id) => {
      employeesInMem.get(id).map(sender ! _)
    }
    case Persist(employee) => {
      val newId = employeesInMem.size
      employeesInMem += (newId -> employee.copy(id = Some(newId)))
      sender ! newId
    }
    case RetrieveAll() => {
      sender ! employeesInMem.values
    }
    case RemoveAll() => {
      println("remove all")
      employeesInMem = employeesInMem.empty
    }
    case _ => throw new IllegalAccessException()
  }
}



case class RetrieveAll()

case class RemoveAll()

case class Retrieve(id:Int)

case class Persist(employee:Employee)