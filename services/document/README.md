Pour tester le service document :

Installer SoapUI, RESTClient ou autre...

-> requêtes REST, on utilise uniquement des POST

Exemples de fichiers Json à fournir dans le body de la requête:

- Ajouter un hotel:

{
    "event":"REGISTER",
    "hotel":
    {
            "name":"Hotel de la prairie",
            "date":"2017-09-30",
            "destination":"Paris",
            "price":"200"
    }
}

- Récupérer une liste des hotels (par nom pour l'instant):

{
    "event":"LIST",
    "filter":"prairie"
}

- Récupérer tous les hotels :

{
    "event":"DUMP"
}