Feature: Car/Hotel reservation

  Scenario: Acceder aux reservations de voiture (toutes)
    Given my service exists
    When la methode getCarsWithParam est appele
    Then le resultat renvoi toutes les voitures

  Scenario: Acceder a la reservation de voiture pour Lyon
    Given my service exists
    When la methode getCarsWithParam est appele avec Lyon
    Then le resultat renvoi toutes les voitures de Lyon

  Scenario: Acceder a la reservation de voiture pour Paris à une certaine date
    Given my service exists
    When la methode getCarsWithParam est appele avec Paris le 28/11/2017
    Then le resultat renvoi les voitures disponible a Paris le 28/11/2017