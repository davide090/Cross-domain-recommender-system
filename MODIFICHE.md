#TODO

Per effettuare il login su facebook, è stato necessario registrare un'applicazione su facebook. Nel caso si voglia cambiare l'applicazione:
  - nel file src/main/resources/static/index.html modificare la riga n. 48, e inserire l'id della nuova app
  - alla riga 51 dello stesso file è possibile modificare la versione delle api facebook (potrebbero sorgere problemi di         incompatibilità)

Il servizio è impostato per utilizzare la porta 5000. È possibile impostarne una a piacere, modificando il file src/main/resources/application.yml     
(N.B. bisogna aggiornare l'url anche nell'applicazione Facebook)

Il file conf.properties specifica gli indirizzi verso cui vengono fatte le richieste ai modelli per la similarità, è possibile modificarli facendo attenzione a modificare anche le righe della classe RecommenderEngine che se ne occupato, in particolare nel caso in cui l'url delle richieste abbia una sintassi diversa:
Sono i metodi:
  -getSimilarityModel1
  -getExistModel1
  -getSimilarityModel2
  -getExistModel2
