# MongoDB
Darbas Atliktas naudojant Java Spring
Sukuriams failas "docker-compose.yaml", kuriame nurodomi sukuriamo konteinerio nustatymai,
Toliau "application.properties" nurodomi nustatymai, kurie nustatyti "docker-compose.yaml" faile
Sukuriamos dvi klasės, CookBookItem.java ir FastFoodMenuItem.java
CookBookItem yra bazinė klasė, pagal kurią sukuriama FastFoodMenuItem
------------
CookBookItem turi šiuos fieldus: ID, name, type, ingredients, instructions.
ID yra generuojamas automatiškai.
Type yra enum tipo, jis gali būti - VEGAN, VEGETARIAN, REGULAR, NON_DAIRY, GLUTEN_FREE.
Ingredients yra List<String> tipo, kuriame vienas sarašo elementas yra vienas ingridientas.
-------------
FastFoodMenuItem turi visus CookBookItem fieldus, tik turi vieną papildomą - restaurant.
-------------
Toliau sukuriami 2 repozitorijų interfeisai
CookBookItemRepo ir MenuItemRepo
Šie interfeisai extendina MongoRepository interfeisą, kuris duoda šiems visas MongoRepository interfeiso funkcijas, bei leidžia parašyti nuosavas.
-------------
CookBookItemRepo yra @Query funkcijos, kurios leidžia rasti objektą pagal pavadinimą bei instrukcijas.
Taip pat turi ir @Aggregation funkcijas, kurios leidžia rasti visus tipus bei vardus naudojamus kolekcijoje.
-------------
Tolesnės užklausos aprašomos main funkcijoje.
Norint apsirašyti Aggregation ir mapReduce funkcijas, reikėjo aprašyti MongoClient objektą, kuriam paduodamas mano mongo URL, kuris sukuriamas "docker-compose.yaml" dėka
-------------
Aggregation
Norint naudoti aggregation funkciją, teko pasiimti kolekciją, šiuo atvėju cookBookItem, ir aggreguoti kitą kolekciją naudojant tam tikrą filtrą,
šiuo atvėju, filtras pasirinktas Equals("instructions", "biorger"), tai reiškia, kad bus išvedami visi objektai esantys šiose kolekcijose, kurių instrukcijos lygios "biorger".
-------------
MapReduce
MapReduce funkcija susideda iš dviejų funkcijų Map() ir Reduce()
Tai darant per Java, tenka apsirašyti kaip String tipo kintamuosius, kuriuose įrašomos JavaScript funkcijos.

Mano atvėju, Map funkcija atrodo taip;
String map = "function() {if(this.instructions==='biorger'){emit(this, 1)}"
Čia, jei objekto instrukcijos yra lygios "biorger", gražinamas visas objektas.

Reduce funkcija atrodo taip:
String reduce = "function(key, value){return key.name + ' ' + key.instructions + ' ' + key.type + key._class;}"
Funkcija gražina pavadinimą, instrukcijas, bei kokios klasės objektas.

Tačiau, kadangi map funkcijoje pasakoma emit(this, 1)
naujoje kolekcijoje išsaugomų objektų id yra visas objektas
kadangi, bandant tai padaryti su emit(this.id, 1), gražindavo tik ID, ir neberasdavo objekto kitų fieldų.

Taip pat šią funkciją, ne kaip Aggregate, tenka naudoti 2 kartus, kadangi ši vienu metu gali paveikti tik vieną kolekciją.
Taigi funkcija yra panaudojama ir kolekcijai fastFoodMenuItem ir cookBookItem, ir rezultatai sudedami į vieną kolekciją, pavadinimu "AllItems".

