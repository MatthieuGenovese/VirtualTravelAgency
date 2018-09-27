Feature: Validation des reservations de voyages

  Scenario: Demande de validation de nos reservations
    Given my validation service
    And i have choose all my tickets
    And i send them to my superior
    Then i receive a mail response saying yes