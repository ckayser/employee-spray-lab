package com.excilys.lab.spray.model

import spray.json._

case class Employee(id:Option[Int], name:String, age:Int)

object EmployeeProtocol extends DefaultJsonProtocol {
  implicit val EmployeeFormat = jsonFormat3(Employee)
}

object EmployeeProtocolWithoutReflection extends DefaultJsonProtocol {
  implicit val EmployeeFormat = new RootJsonFormat[com.excilys.lab.spray.model.Employee] {
    def write(emp: com.excilys.lab.spray.model.Employee) = JsObject(
      "name" -> JsString(emp.name),
      "age" -> JsNumber(emp.age)
    )

    def read(json: JsValue) = json.asJsObject.getFields(/*"id",*/"name", "age") match {
      case Seq(JsString(name), JsNumber(age)) =>
        com.excilys.lab.spray.model.Employee(None, name, age.toInt)
    }
  }
}
