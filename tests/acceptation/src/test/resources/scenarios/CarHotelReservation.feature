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

  Scenario: Acceder aux reservations d'hotels (toutes)
    Given my service exists
    When la methode getHotelWithParam est appele
    Then le resultat renvoi toutes les resas d'hotels

  Scenario: Acceder aux reservationd d'hotel pour Paris, du - cher au + cher
    Given my service exists
    When la methode getHotelWithParam est appele avec Paris
    Then le resultat renvoi toutes les resas d'hotel triées par prix

  Scenario: Acceder a la reservation d'un hotel pour un lieu et une date précise
    Given my service exists
    When la methode getHotelWithParam est appele avec Paris pour le 28/12/2017
    Then le resultat renvoi toutes les resas d'hotels disponibles pour Paris le 28/12/2017