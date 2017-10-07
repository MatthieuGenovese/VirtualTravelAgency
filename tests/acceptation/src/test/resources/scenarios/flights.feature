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
      And with a boolean isDirect set to true
    When the REGISTER message is sent
    Then the flight is registered
      And there are 3 flights in the registry
      And the destination of type string is equals to Paris
      And the price of type string is equals to 230
      And the date of type string is equals to 2017-10-10
      And the isDirect of type boolean is equals to true
      And the stops of type array is equals to ["Marseille","Toulon"]