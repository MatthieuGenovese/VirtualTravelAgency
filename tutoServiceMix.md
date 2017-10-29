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


