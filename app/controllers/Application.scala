package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

import play.api.db._

import scala.collection.mutable.ArrayBuffer



object Application extends Controller {

  def hotel1 = Action {
    Ok(views.html.hotel1(null))
  }

  def resData = Action { request =>
    val resRaw = request.body.asText.toString()
    val res = resRaw.replaceAll("Some", "")
    val resNoIzq = res.replace("(", "")
    val newRes = resNoIzq.replace(")", "")
    println("Esto es: " + newRes)
    val arrayRes = newRes.split(",")

    var str = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      str = "INSERT INTO reservation( adults, dayin, dayout, email, fcfm, floor, gym, monthin, monthout, name, other, phone, spa, total) VALUES ('"+ arrayRes(0) + "','"+arrayRes(1)+"','"+arrayRes(2)+"','"+arrayRes(3)+"','"+arrayRes(4)+"','"+arrayRes(5)+"','"+arrayRes(6)+"',''"+arrayRes(7)+"','"+arrayRes(8)+"','"+arrayRes(9)+"','"+arrayRes(10)+"','"+arrayRes(11)+"','"+arrayRes(12)+"','"+arrayRes(13)+"')"
      print("STR: " + str)
      stmt.executeUpdate(str)


    } finally {
      conn.close()
    }
    Ok(views.html.index(null)) // Automatic Redirection doesnt work

  }

  def index = Action {
    Ok(views.html.index(null))
  }

  def db = Action {
    var out = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")

      //val rs = stmt.executeQuery("SELECT tick FROM ticks")
      val hotels = stmt.executeQuery("SELECT name FROM hotels")

      while (hotels.next) {
        out += "Read from DB: " + hotels.getString("name") + "\n"
      }
    } finally {
      conn.close()
    }
    Ok(out)
  }

}