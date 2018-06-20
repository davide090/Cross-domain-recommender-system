# Progetto Sistemi Intelligenti per Internet
 


<h4>Sistema di raccomandazione cross-dominio</h4>

Modificare il file conf.properties inserendo in resource.path il path di una cartella in locale, che verrà utilizzata per salvare i file temporanei relativi a likes e punti di interesse, oltre che vari file per calcolare le raccomandazioni.

Per avviare il servizio:
  - java -jar target/Progetto_SII-0.0.1-SNAPSHOT.jar

oppure da eclipse: 
  - run as spring boot app



Una volta avviato, all'utente verrà chiesto di effettuare il login su Facebook e consentire al servizio di utilizzare alcune informazioni personali (id utente, likes, email), in modo tale da fornire raccomandazioni sulla base del suo profilo.
