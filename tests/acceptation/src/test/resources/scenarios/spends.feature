Feature: Spends to manager

  Background:
    Given an empty spends registry deployed on localhost:8080
      And a spend with id 1 added to the database
      And a spend with id 2 added to the database

      
    Scenario: Registering a spend request

        Given A mock spend is created
        When the submit message spends service is sent
        Then the spend is registered

    Scenario: Retrieve a spend request
        Given an id spend identified as 1
        When the retrieve message spends service is sent
        Then check totalSpend of request

    Scenario: Add spend
        Given an id spend identified as 1
        When the addSpend message spends service is sent
        Then check totalSpend and add of request