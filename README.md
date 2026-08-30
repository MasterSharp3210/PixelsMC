# PixelsMC
An App to convert any pixels from your image in Minecraft's textures pixelated picture

# Come usare
Innanzitutto è necessario installare Java 8 o una versione successiva. Dopo, puoi tranquillamente aprire un terminale nella directory del file .jar e si qualsiasi sistema operativo si vuole eseguire il file basta fare il seguente comando '''java - jar PixelsMC '''.

# Come funziona
Come molti sanno, i computer non calcolano come gli umani la luce e il colore. il cosiddetto "RGB" o "ARGB" può essere interpretato come un punto nello spazio e per calcolare la media del punto luce in un pixel è necessario implementare un algoritmo chiamato **"Norma Euclidea"**. Questa sfrutta il teorema di Pitagora per trattare il pixel come uno spazio cubo di cui è necessario calcolare il punto centrale. Per calcolarlo si ricorre alla seguente formula: √ r² + b² + g² . 

Infine, vengono presi in considerazione soltanto i pixels con un alpha > 128. L'alpha è l'unità di misura della trasparenza (**A**l'ha**R**ed**G**reen**B**lue) e chiaramente i pixel completamente trasparenti o opachi non possono contribuire alla media del colore intermedio e renderebbero il colore finale poco fedele, per questo vengono eliminati.

Dopo aver calcolato il colore in sequenza RGB di ogni pixel ne viene fatta la somma totale e quindi alla fine si otterrebbe la somma di R, la somma di G e la somma di B per tutta la griglia di pixel nella texture. Infine ne viene fatta la media con un divisione (ex. totaleR = 200, totaleG = 300, totaleB = 400, totaleNumeroDiPixelNellaGriglia16x16 = 10 == colore intermedio = 20; 30; 40) */ Questo è un esempio e non esistono valori R, G o B che possano andare fino a 300 o 400 come scritto. il valore massimo del colore è 255*

# Consigli 
Per l'utilizzo di questo programma consiglio vivamente di rimuovere alcune texture Minecraft di blocchi più piccoli o incompleti come funghi, erba, fiori ed altro. Nella versione pre-compilata disponibile in Release ho già aggiunto le textures base di Minecraft 1.8.9 ma ovviamente si possono aggiungere ancora più textures compresi gli assets delle versioni più moderne di Minecraft. **IMPORTANTE: LE TEXTURE DEVONO ESSERE PERÒ SEMPRE IN RISOLUZIONE 16x16**