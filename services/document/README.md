Pour utiliser le service de réservation de vols :

Header nécéssaire :

Content-Type : application/json

Requêtes POST

Exemples de fichiers Json à fournir dans le body de la requête:

- Ajouter un vol:

{
    "event": "REGISTER",
    "flightreservation":
        {
            "id":"1",
            "destination":"Paris", 
            "date":"2017-09-30", 
            "isDirect":"false", 
            "price":"200", 
            "stops":["Marseille", "Toulouse]
        }
}


- Récupérer une liste des vols :

{
    "event": "LIST", 
    "filter":
    {
        "destination":"Paris", 
        "date":"2017-09-30",
        "stops":[Marseille, Toulouse]
    }
}

- Récupérer un vol :

{
    "event":"RETRIEVE",
    "id":"1"
}

- Récupérer tous les vols :

{
    "event":"DUMP"
}

- Supprimer un vol :

{
    "event":"DELETE",
    "id":"1"
}

- Supprimer tous les champs de la base de données :

{
    "event":"PURGE",
    "use_with":"caution"
}
