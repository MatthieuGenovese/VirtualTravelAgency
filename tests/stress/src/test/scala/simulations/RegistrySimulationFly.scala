package simulations

import java.util.UUID

import scala.language.postfixOps

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._


class RegistrySimulationFly extends Simulation {

  val httpConf =
    http
      .baseURL("http://localhost:9080/flyreservation-service-document/")
      .acceptHeader("application/json")
      .header("Content-Type", "application/json")

  val stressSample =
    scenario("Registering Fly")
        .repeat(10)
        {
          exec(session =>
            session.set("id", UUID.randomUUID().toString)
          )
            .exec(
              http("registering a fly")
                .post("registry")
                .body(StringBody(session => buildRegister(session)))
                .check(status.is(200))
            )
            .pause(1 seconds)
            .exec(
              http("retrieving a fly")
                .post("registry")
                .body(StringBody(session => buildRetrieve(session)))
                .check(status.is(200))
            )
        }

  def buildRegister(session: Session): String = {
    val id = session("id").as[String]
    raw"""{
      "event": "REGISTER",
      "flyreservation": {
        "id": "$id",
        "date": "2017-09-30",
        "isDirect": "false",
        "destination": "Paris",
        "price": "543",
        "stops": [{"stop":"Marseille"},{"stop":"Toulouse"}]
      }
    }""""
  }


  def buildRetrieve(session: Session): String = {
    val id = session("id").as[String]
    raw"""{
      "event": "RETRIEVE",
      "id": "$id"
    }""""
  }

  setUp(stressSample.inject(rampUsers(20) over (10 seconds)).protocols(httpConf))
}