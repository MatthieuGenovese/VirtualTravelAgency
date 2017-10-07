Feature: Car reservation

  Scenario: Acceder aux resas de voiture (toutes)
    Given my service exists
    When la methode getCarsWithParam est appele
    Then le resultat renvoi toutes les voitures