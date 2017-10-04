Pour tester le service document :

Installer SoapUI, RESTClient ou autre...

-> requêtes REST, on utilise uniquement des POST

Exemples de fichiers Json à fournir dans le body de la requête:

- Ajouter un vol:

{"event": "REGISTER", "flyreservation":{"destination":"Paris", "date":"2017-09-30", "isDirect":"true", "price":"200", "stops":[{"stop":"abc"}, {"stop":"def"}]}}}

- Récupérer une liste des vols (les champs doivent être stockés en string pour l'instant):

{"event": "LIST", "filter":{"destination":"Paris", "date":"2017-09-30"}}

- Récupérer tous les vols :

{
    "event":"DUMP"
}

Enjoy !
