

package simulations

import java.util.UUID

import scala.language.postfixOps

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._


class AccessSimulationAddSpends extends Simulation {

  val httpConf =
    http
      .baseURL("http://localhost:8080/vtg-spends/")
      .acceptHeader("application/json")
      .header("Content-Type", "application/json")

  val stressSample =
    scenario("Submit Spends")
      .repeat(10)
      {
        exec(session =>
          session.set("id", UUID.randomUUID().toString)
        )
          .exec(
            http("submit bills")
              .post("spends")
              .body(StringBody(session => buildRegister(session)))
              .check(status.is(200))
          )
          .pause(1 seconds)
          .exec(
            http("Add Spends")
              .post("spends")
              .body(StringBody(session => buildAddSpends(session)))
              .check(status.is(200))
          )


      }

  def buildRegister(session: Session): String = {
    val id = session("id").as[String]
    raw"""{
           	"type":"submit",
           	"bills":
           	 {
           		"id":6,
           		"identity":
           		{
           			"firstName":"mohamed",
           			"lastName":"chennouf",
           			"email":"mohamed.chennouf@etu.unice"
           		},
           		"spends":
           		[
           			{
           			"id":"0",
           			"reason":"Restaurant",
           			"date":"05/02/2018",
           			"country": "AM" ,
           			"prix" :
           			    {
           				"price":400,
           				"currency":"EUR"
           				}
           			},
           			{
           			"id":"1",
           			"reason":"Restaurant",
           			"date":"01/02/2005",
           			"country": "AM" ,
           			"prix" :
           			    {
           				"price":150,
           				"currency":"EUR"
           				}
           			}
           		]
           	}
          }""""
  }


  def buildAddSpends(session: Session): String = {
    raw"""{
            "type":"addSpend",
            "id":6,
            "spends":
              {
              "id":"80",
              "reason":"Taxi",
              "date":"05/02/2018",
              "country": "AM" ,
              "prix" :
                {
                "price":44,
                "currency":"EUR"
                }
              }
          }""""
  }



  setUp(stressSample.inject(rampUsers(20) over (10 seconds)).protocols(httpConf))

}
