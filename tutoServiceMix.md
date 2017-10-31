TUTO SERVICE MIX POUR BONOBO

lancer service mix :
télécharger et dézipper apache-service mix : http://www.apache.org/dyn/closer.lua/servicemix/servicemix-7/7.0.1/apache-servicemix-7.0.1.zip
se rendre dans le dossier apache-servicemix-7.0.1 puis lancer la commande ./bin/servicemix

installer dep camel :

dans le terminal ServiceMix :feature:install -u camel-csv camel-http camel-saxon camel-spring-ws camel-servlet camel-jackson

Utiliser uniquement l'esb :
Il faut d'abord lancer le start.sh (pas besoin de build avant)
utiliser le script cpServiceMix.sh et regarder les resultats de vos tests sur la console de serviceMix
Si vous remodifiez du code dans le camel, il suffit de refaire cpServiceMix.sh (pas besoin de refaire le start)

pour copier le test dans le bon endroit, utilisez le script cp.sh


Quelques commandes serviceMix utile :

log:display  (affiche les log)
log:clear (nettoie l'historique des logs)

bundle:list (affiche la liste de tous les process installer (le notre est tout en bas et s'appelle Integration Technical Flows)
bundle:start id (demarre le process numero "id" (l'id est le nombre de la premiere colonne de bundle:list)))
bundle:stop id (pas besoin d'explications j'imagine)
bundle:restart id (la m?me)


serviceMix sous docker :
pour acceder a notre apacheServiceMix sous docker il faut executer le script runServiceMixDocker.sh (avoir fait start.sh avant evidemment)
une fois lancé, le terminal est maintenant dans le service mix du docker.

on a accès a toutes les commandes servicemix classique mais il faut rajouter ./bin/client devant.
Si après avoir rentrée une commande vous obtenez "Failed to login session" ou un truc du genre, c est que vous êtes allez trop vite.
Attendez quelques secondes le temps que servicemix soit vraiment lancé

Exemple :

"log:display" deviendra "./bin/client log:display"
Pour tester les routes, simplement lancer le script cp.sh (comme avant) il copiera les fichiers de test dans l'entrée de docker



