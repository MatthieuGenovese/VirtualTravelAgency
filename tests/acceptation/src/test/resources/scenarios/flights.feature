Feature: Flight Registry

  Background:
    Given an empty registry deployed on localhost:9080
      And a flight with id 1 added to the registry
      And a flight with id 2 added to the registry

  Scenario: Registering a flight

    Given A flight identified as 1
      And with a string destination set to Paris
      And with a string price set to 230
      And with a string date set to 2017-10-10
      And with an array stops set to ["Marseille","Toulon"]
      And with a boolean isDirect set to false
    When the REGISTER message is sent
    Then the flight is registered
      And there are 3 flights in the registry
      And the destination is equals to Paris
      And the price is equals to 230
      And the date is equals to 2017-10-10
      And the isDirect is equals to false
      And the stops is equals to ["Marseille","Toulon"]

  Scenario: Removing a flight
    Given an id identified as 1
      And the DELETE message is sent
    Then the flight is removed
      And there is 1 flight in the registry

  Scenario: Purging the database
    Given the caution safe word
    When the PURGE message is sent
    Then there is 0 flight in the registry

   Scenario: Retrieving a flight
     Given an id identified as 1
     When the RETRIEVE message is sent
     Then the flight exists
      And the destination is equals to Paris
      And the price is equals to 200
      And the date is equals to 2017-10-10
      And the isDirect is equals to true
      And the stops is equals to []

  Scenario: Looking for flights #1
    Given a filter set to {"destination":"Paris", "date":"2017-10-10"}
    When the LIST message is sent
    Then the answer contains 2 results

  Scenario: Looking for flights #2
    Given a filter set to {"id":"1"}
    When the LIST message is sent
    Then the answer contains 1 result

  Scenario: Looking for flights #3
    Given a filter set to {"date":"2018-10-10"}
    When the LIST message is sent
    Then the answer contains 0 results

  Scenario: Looking for flights #4
    Given a filter set to {"id":"3"}
    When the LIST message is sent
    Then the answer contains 0 results

  Scenario: Getting all the citizens
    When the DUMP message is sent
    Then the answer contains 2 results