package models

import play.api.libs.json.Json

case class Customer(name: String , age:Option[Int]) {

}

object Customer {

  implicit val customerFormat = Json.format[Customer]

}