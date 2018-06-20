# Progetto Sistemi Intelligenti per Internet
 


<h4>Sistema di raccomandazione cross-dominio</h4>

All'utente verranno raccomandati punti di interesse di vario tipo sulla base del suo profilo Facebook.



<h5>Per avviare il servizio:</h5>

Modificare il file conf.properties inserendo in resource.path il path di una cartella in locale, che verrà utilizzata per salvare i file temporanei relativi a likes e punti di interesse, oltre che vari file per calcolare le raccomandazioni.

da terminale:
  - java -jar target/Progetto_SII-0.0.1-SNAPSHOT.jar

oppure da eclipse: 
  - run as spring boot app
  
  Una volta avviato, il servizio sarà disponibile sulla porta 5000.




<h5>Descrizione: </h5>

All'utente verrà chiesto di effettuare il login su Facebook e consentire al servizio di utilizzare alcune informazioni personali (id utente, likes, email), in modo tale da fornire raccomandazioni sulla base del suo profilo.

Una volta calcolato il profilo utente, vengono richieste città e paese di interesse. Al fine di migliorare la raccomandazione, all'utente verrà chiesto il permesso di accedere alla sua posizione e di inserire un raggio entro il quale verranno ricercati i punti di interesse (la posizione verrà utilizzata solo nel caso in cui l'utente cerca p.o.i. nella città in cui risiede al momento della richiesta)

Successivamente, bisogna selezionare la tipologia di p.o.i. verso i quali si è interessati, e la tipologia di profilazione (tutti i likes, o una categoria specifica)


