package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.Customer
import play.api.data.Form
import play.api.data.Forms._
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object CustomerController extends Controller with MongoController {


  def collection: JSONCollection = db.collection[JSONCollection]("customer")
  def add = Action.async(parse.json) { implicit request =>
      request.body.validate[Customer].map { customer =>
      collection.insert(customer).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
   
   }

  def list = Action.async { 
    val cursor = collection.find(Json.obj()).cursor[Customer]
    val futureList =  cursor.collect[List]()
    futureList.map{customers=> Ok(Json.toJson(customers))}
    
    }

}