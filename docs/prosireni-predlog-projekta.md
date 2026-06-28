# Sistem za raspodelu nastave

## Clanovi tima

Jovana Panic SV64/2022

## Motivacija

Katedra za informatiku ima oko 100 predmeta i oko 40 asistenata. Raspodela termina vezbi na asistente je problem koji se svake godine rucno resava. Sistem za automatsku raspodelu bi znacajno olaksao taj posao.


Pored same automatizacije raspodele, cilj sistema je i da odluke budu objasnjive. To znaci da korisnik ne dobija samo konacan rezultat, vec moze da vidi na osnovu kojih pravila, preferenci, prethodnih angazovanja i specificnih zahteva je sistem dosao do raspodele. Zbog toga sistem prikazuje kandidate, njihove score vrednosti, pozitivne razloge i kazne.

## Pregled problema

Zadatak sistema je da raspodeli nastavu na asistente. Nastava se sastoji od niza predmeta, sa razlicitim brojem grupa i casova po terminu vezbi, podeljene u dva semestra.

Opterecenje asistenta predstavlja ukupan broj casova koje asistent drzi tokom jedne godine. Sistem bi trebalo da rasporedi nastavu ravnopravno, tako da opterecenje asistenata bude ujednaceno.

Asistenti se izjasnjavaju o svojim preferencama za svaki predmet ocenom 1-5. Sistem bi trebalo da maksimizuje prosecnu ocenu predmeta dodeljenih asistentima.

Zahtevi da odredjeni asistent drzi odredjeni predmet mogu doci od asistenata i profesora, i biti razlicitih prioriteta.


U implementaciji se ovaj problem modeluje kroz klase `Assistant`, `Subject`, `Preference`, `PreviousAssignment`, `SpecificRequest`, `Candidate` i `AssignmentResult`. `Assistant` i `Subject` nisu direktno povezani, vec se povezuju preko veznih klasa koje nose dodatni kontekst: ocenu preference, prethodni broj casova, prioritet zahteva, score kandidata ili konacno dodeljene casove.

## Metodologija rada

## Ulaz u sistem

Ulaz u sistem podrazumeva:

- raspodelu nastave od prethodne godine,
- preference asistenata u vidu ocene 1-5 za svaki predmet,
- specificne zahteve da odredjeni asistent drzi odredjeni predmet, razlicitih prioriteta.


U trenutnoj aplikaciji testni ulaz se nalazi u klasi `DemoDataFactory`. Frontend moze da ucita te podatke preko endpointa `/api/demo-input`. Korisnik zatim moze da promeni JSON u editoru i posalje izmenjeni ulaz na endpoint `/api/allocate`.

## Raspodela nastave od prethodne godine

Tabela gde su redovi predmeti, a kolone asistenti. Format je isti kao izlaz iz sistema.

Informacije o predmetu:

- naziv,
- semestar,
- broj grupa,
- broj casova u terminu vezbi.

Informacije o asistentu:

- ime i prezime,
- ukupan broj casova u zimskom semestru,
- ukupan broj casova u letnjem semestru,
- ciljano opterecenje, po semestru ili ukupno.

Raspodela:

- u polju koje odgovara predmetu i asistentu stoji broj casova koje asistent drzi,
- broj predstavlja broj grupa pomnozen sa brojem casova u terminu.


U kodu je prethodna raspodela predstavljena klasom `PreviousAssignment`. Ona povezuje jednog asistenta i jedan predmet i cuva broj casova koje je asistent prethodno drzao. Ovo se koristi kao pozitivan faktor u pravilima, jer sistem daje prednost asistentima koji su vec drzali odredjeni predmet.

## Preference asistenata

Tabela gde redovi predstavljaju asistente, a kolone predmete.

U polju koje odgovara predmetu i asistentu stoji preferenca asistenta ka predmetu.

 
U implementaciji je preferenca predstavljena klasom `Preference`, koja sadrzi asistenta, predmet i ocenu `grade`. Visoka preferenca 5 pravi jakog kandidata, srednje preference 3 i 4 prave slabijeg kandidata, dok se niska preferenca 2 koristi samo kao fallback kada nema boljih kandidata.

## Specificni zahtevi

Tabela sadrzi:

- ime i prezime asistenta,
- predmet,
- broj casova, opciono,
- prioritet.


Specificni zahtevi su predstavljeni klasom `SpecificRequest`. Oni se ne obradjuju samo rucno u Java kodu, vec se od njih generisu Drools pravila preko template fajla `request-template.drt`. Ako zahtev ima visok prioritet, kandidat dobija veci bonus. Ako zahtev ima unet broj casova, sistem moze direktno da dodeli taj broj casova trazenom asistentu.

## Baza znanja

Pravila su definisana tako da obezbede ravnomernu raspodelu opterecenja, maksimalno zadovoljstvo asistenata i ispunjenje specificnih zahteva.

## Osnovna pravila

Postoje specificni, osnovni zahtevi i kazne koje odredjuju raspodelu nastave.

## Specificni zahtevi

Ukoliko postoji zahtev da odredjeni asistent drzi nastavu na odredjenom predmetu, onda dodeljujemo nastavu tom asistentu bez obzira na ostale faktore.

Nagraditi da asistent drzi vise casova predmeta za koji postoji specificni zahtev.
 
U implementaciji se specificni zahtev realizuje kroz bonus `priority * 15`. Na primer, zahtev prioriteta 5 daje bonus 75. Ako kandidat vec postoji zbog preference, pravilo mu dodaje bonus. Ako kandidat ne postoji, template pravilo ga kreira.

## Osnovni zahtevi

Ako je asistent vec drzao odredjeni predmet i ima ocenu 5, onda ostavljamo predmet tom asistentu sa najvecim prioritetom.

Ako je asistent imao visu ocenu za odredjen predmet od nekog drugog asistenta, teziti da se njemu dodeli predmet.

Ako je asistent drzao prethodne godine odredjeni predmet, onda preferiramo da ga i dalje drzi.

Preferirati da se predmeti sa 10+ ukupnog opterecenja podele na barem 2 asistenta.

Ako asistent ima ocenu 3 ili 4 za predmet, predmet mu se moze dodeliti ukoliko to doprinosi ravnomernijem opterecenju sistema.

Asistenti sa ocenom 2 ili 3 imaju manji prioritet u odnosu na asistente sa ocenom 5, ali ostaju validni kandidati za raspodelu.

Sistem tezi da maksimalizuje zadovoljstvo preferenci, ali ne iskljucuje asistente sa nizim ocenama ukoliko je to potrebno za kompletnu raspodelu nastave.

 
Ova pravila su implementirana u `assignment-rules.drl`. Kandidati se formiraju kroz pravila za visoku, srednju i nisku preferencu. Prethodno angazovanje dodaje kandidatu dodatni score. Veliki predmeti se prepoznaju metodom `isLargeSubject()`, gde je predmet veliki ako ima 10 ili vise ukupnih casova.

## Kazne

Ako asistent drzi vise od 2 predmeta u semestru dodeli kaznu, a ukoliko drzi vise od 3 dodeli veliku kaznu.

Ako asistent ima ocenu 1 za odredjeni predmet, gledamo da izbegnemo ponovnu dodelu, osim ako nema drugih opcija.

Ako bi dodela predmeta dovela do toga da asistent ima vise od 30 termina, takva dodela se kaznjava.

Ako je opterecenje asistenta vece od njegovog ciljanog opterecenja, sistem dodeljuje kaznu proporcionalnu odstupanju. Ukoliko je manje, sistem povecava prioritet za dobijanje novih termina.

Niska preferenca u kombinaciji sa velikim opterecenjem dovodi do negativnog faktora i velike kazne.

 
U implementaciji postoje kazne za prekoracenje ciljanog opterecenja, prekoracenje granice od 30 casova i preveliki broj predmeta u semestru. Za broj predmeta po semestru koristi se Drools `accumulate`, koji broji koliko dodela asistent vec ima u tom semestru.

## Raspodela predmeta

Ako predmet ima n grupa i odredjen h broj casova, onda ukupan broj casova mora biti raspodeljen u potpunosti i dodeljen asistentima.

Ako predmet ima mali broj casova, raspodeliti ga na maksimalno 2 asistenta.

Sistem tezi da se termini podele, tako da opterecenja asistenata budu ujednacena.

U slucaju konflikta izmedju vise pravila:

- prioritet imaju tvrda ogranicenja sistema,
- nakon toga se razmatraju specificni zahtevi visokog prioriteta,
- zatim se uzimaju u obzir preferencije asistenata i ravnomernost opterecenja,
- ukoliko vise kandidata ima isti score, prednost ima kandidat sa manjim trenutnim opterecenjem.

  
Konacna raspodela je predstavljena klasom `AssignmentResult`. Mali predmeti se dodeljuju najboljem kandidatu, dok se veliki predmeti dele na prvi i drugi deo kada postoje odgovarajuci kandidati. Nakon raspodele se pokrece validacija da nijedan predmet ne ostane nerasporedjen i da zbir dodeljenih casova odgovara ukupnom broju casova predmeta.

## Template

Omoguciti da se specificni zahtevi pretvore u pravila u obliku:

Ako postoji zahtev da asistent X drzi predmet Y sa prioritetom Z, onda sistem povecava prioritet dodele tog asistenta na tom predmetu proporcionalno prioritetu zahteva.

Na osnovu ovog template-a generisu se konkretna pravila, na primer:

Ako postoji zahtev da asistent Marko Markovic drzi predmet Programiranje 1 sa prioritetom 5, onda se taj zahtev tretira kao visoki prioritet.

Ako postoji zahtev prioriteta 5, kandidat se automatski prihvata osim u slucaju konflikta sa tvrdim ogranicenjima sistema.

Ako postoji zahtev nizeg prioriteta, sistem ga uzima u obzir zajedno sa ostalim faktorima evaluacije.

## Forward chaining

## Nivo 1 - generisanje kandidata

U prvom nivou sistem identifikuje potencijalne kandidate za dodelu predmeta. Kandidati se formiraju na osnovu preferenci i prethodnog iskustva asistenata.

Ako asistent ima visoku preferencu, na primer ocenu 5, za predmet, smatra se kandidatom.

Ako je asistent prethodne godine drzao predmet, povecava se njegov prioritet.

Ako asistent ima srednju preferencu, na primer ocenu 3 ili 4, takodje se ukljucuje u skup kandidata, ali sa manjim prioritetom.

Asistenti sa nizim preferencama ne eliminisu se automatski iz procesa raspodele, vec mogu dobiti predmet ukoliko je potrebno obezbediti ravnomernu raspodelu opterecenja ili ne postoje kandidati sa visim preferencama.

Ako postoji specifican zahtev visokog prioriteta, kandidat se automatski prihvata.

Rezultat: kandidat, odnosno par asistent-predmet-prioritet.

 
U kodu je rezultat ovog nivoa objekat `Candidate`. Kandidat sadrzi asistenta, predmet, score, obrazlozenje i oznake koja pravila su vec primenjena. Pravila imaju salience vrednosti, pa se prvo izvrsavaju pravila za template zahteve i preference, a zatim pravila za dodatne bonuse i kazne.

## Nivo 2 - evaluacija kandidata

U drugom nivou vrsi se evaluacija svakog kandidata kroz izracunavanje kvaliteta potencijalne dodele. U obzir se uzimaju:

- preferencija asistenta,
- trenutno opterecenje u odnosu na ciljano,
- broj predmeta koje asistent vec drzi.

U ovom koraku bih koristila accumulate funkciju za agregaciju podataka:

- ukupno opterecenje asistenta, zbir casova,
- broj predmeta po semestru,
- prosecna ocena zadovoljstva.

Na osnovu ovih vrednosti formira se evaluacija, odnosno asistent-predmet-score, gde score predstavlja kombinaciju pozitivnih faktora, kao sto su preferenca i iskustvo, i kazni, kao sto su preopterecenje i preveliki broj predmeta.

 
Score se racuna postepenim izmenama kandidata kroz Drools `modify`. Na primer, visoka preferenca daje pocetni score 50, srednja 25, prethodna raspodela dodaje 20, specificni zahtev dodaje `priority * 15`, a kazne smanjuju score. Svaka izmena dodaje tekst u `explanation`, pa se kasnije moze objasniti zasto je kandidat dobio odredjenu vrednost.

## Nivo 3 - donosenje odluke

Sistem bira najboljeg kandidata za svaki predmet na osnovu izracunate evaluacije. Dodela se vrsi tako da:

- svi casovi predmeta budu rasporedjeni,
- opterecenje asistenata ostane ujednaceno,
- ukupno zadovoljstvo, na osnovu preferenci, ostane zadovoljeno, odnosno maksimalno.

Rezultat: dodeljen asistent, predmet i broj casova.


Odluka se cuva kao `AssignmentResult`. Frontend prikazuje za svaku odluku asistenta, predmet, broj casova, score i obrazlozenje. Ako kandidat nije izabran, sistem generise negativno objasnjenje gde navodi koji kandidat je izabran umesto njega i kolika je bila razlika u score-u.

## Backward chaining

Sistem krece od konkretne odluke, odnosno cilja, na primer dodele asistenta odredjenom predmetu, i unazad proverava pravila i uslove koji su doveli do te odluke. Cilj je objasnjavanje vec donetih odluka o raspodeli asistenata na predmete.

U sistemu su sva pravila prosirena dodatnim atributom obrazlozenje, koji opisuje razlog zbog kog se pravilo aktivira. Kada se neko pravilo primeni, na primer dodela na osnovu visoke preference ili specificnog zahteva, uz zakljucak se cuva i odgovarajuce obrazlozenje. Na taj nacin se formira lanac zakljucivanja koji sistem moze kasnije da prati unazad.

Sistem ce imati mogucnost da odgovori na upite poput: "Zasto je asistent X dodeljen predmetu Y?", tako sto backward chaining pristupom prolazi kroz aktivirana pravila i prikuplja njihova obrazlozenja. Kao rezultat, korisniku se vraca skup razloga koji su najvise uticali na konacnu odluku, na primer visok prioritet zahteva, velika preferenca, prethodno angazovanje ili optimalno opterecenje.

Takodje, sistem omogucava i objasnjenje negativnih odluka, odnosno zasto neki asistent nije dodeljen predmetu. U tom slucaju backward chaining identifikuje pravila koja nisu bila zadovoljena ili su bila nadjacana drugim pravilima, i vraca njihova obrazlozenja, na primer niska preferenca, preopterecenje ili postojanje kandidata sa boljim uslovima.

Preciznije funkcionisanje algoritma bi izgledalo ovako:

"Zasto je asistent Marko Markovic dodeljen predmetu Programiranje 1?". Na osnovu toga formira se cilj oblika dodeljen(MarkoMarkovic, Programiranje1), nakon cega sistem rekurzivno proverava pravila koja mogu dovesti do tog zakljucka. Sistem najpre pronalazi pravila povezana sa dodelom predmeta, a zatim proverava njihove uslove, kao sto su visoka preferenca asistenta za predmet, prethodno angazovanje na predmetu ili trenutno opterecenje asistenta. Svaki od tih uslova moze predstavljati novi podcilj koji se dodatno proverava kroz nova pravila ili cinjenice iz baze znanja. Na primer, uslov ima_visoku_preferencu(MarkoMarkovic, Programiranje1) moze se dokazati cinjenicom ocena(MarkoMarkovic, Programiranje1, 5), dok se uslov nije_preopterecen(MarkoMarkovic) proverava analizom trenutnog i ciljanog opterecenja asistenta. Proces se nastavlja sve dok svi podciljevi ne budu dokazani ili dok sistem ne utvrdi da ne postoji pravilo koje moze potvrditi odredjeni uslov. Na taj nacin formira se lanac zakljucivanja koji omogucava sistemu da objasni zbog cega je odredjena odluka doneta, ali i zasto neki kandidat nije izabran za dati predmet.

**Dopuna:**  
U trenutnoj implementaciji objasnjenja su realizovana prakticno kroz `explanation` polja u klasama `Candidate` i `AssignmentResult`, kao i kroz listu negativnih objasnjenja u `AllocationOutput`. To predstavlja implementacioni oblik objasnjavanja odluka: sistem ne izvrsava poseban backward chaining engine, ali cuva trag primenjenih pravila i preko tog traga korisniku objasnjava zasto je odluka doneta ili zasto kandidat nije izabran.

## Klijentska aplikacija


Frontend prikazuje:

- testni ulaz,
- konacnu raspodelu,
- kandidate,
- validacione poruke,
- negativna objasnjenja,
- generisana template pravila,
- broj aktiviranih pravila,
- ukupan broj dodeljenih casova,
- prosecnu preferencu.

## Testni podaci
 
Testni podaci su definisani u `DemoDataFactory`. Oni sadrze asistente, predmete iz oba semestra, preference, prethodne raspodele i specificne zahteve. Koriste se za demonstraciju rada sistema i za automatske testove.

## Testiranje

Testovi se nalaze u `SzrnApplicationTests`. Proveravaju da se Spring kontekst podize, da demo raspodela radi, da JSON ulaz sa frontenda moze da se obradi i da fallback pravilo za nisku preferencu funkcionise. Testovi se pokrecu komandom:

```powershell
.\mvnw.cmd test
```

## Klasni dijagram

Klasni dijagram
  
Klasni dijagram je dodat kao SVG fajl `docs/class-diagram.svg`. Na njemu je prikazano da `Assistant` i `Subject` nisu direktno povezani, vec se povezuju preko veznih klasa:

- `Preference`,
- `PreviousAssignment`,
- `SpecificRequest`,
- `Candidate`,
- `AssignmentResult`.

Svaka od ovih klasa ima vezu ka jednom asistentu i jednom predmetu, dok jedan asistent i jedan predmet mogu ucestvovati u vise takvih veza.
