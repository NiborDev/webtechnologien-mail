***VL: 3***  
**1\. What is the responsibility of the API layer. Give one example.**  
Der API Layer ist dafür da um sich um die HTTP Anfragen und Antworten zu kümmern. Dieser sorgt dafür das die anderen Layer nichts mit den HTTP anfragen zu tun haben. Er nimmt entsprechend HTTP requests an und parst / Mapped die entsprechenden daten, sodass diese von den anderen Layers verwendet werden können und eine konsistente Kommunikation zwischen Client und Server stattfinden kann. Die Daten werden an die Service Klasse weiter geleitet.  
Beispiel: Die UserController (API) Klasse ist in der API-Schicht. Wenn ein Client einen POST /users Request ausführt, kümmert sich diese Klasse darum, diese Anfrage entgegenzunehmen. Der RequestBody wird in ein UserCreate DTO umgewandelt und anschließend an die Service klasse weiter gegeben. Die Service Klasse gibt anschließend einen User Objekt zurück, welche von dem UserContoller zu einer Response umgewandelt und an den Client zurück gesendet wird.

**2\. What is the responsibility of the Business layer? Give one example.**  
Der Business-Layer ist für die Umsetzung der Geschäftslogik zuständig. Er setzt ausschließlich solche interne Logik, wie z.B. die Prüfung ob eine Anfrage bestimmten Vorgaben entspricht oder die durchführung interner Berechnungen um, und ruft die entsprechende Funktion im Repository-Layer auf.

Bsp: Es soll ein neuer Nutzer angelegt werden. Durch die UserController-Klasse (API) wird die relevante Funktion im User Service aufgerufen, welche ein UserCreate DTO entgegennimmt. Diese prüft nun ob es bereits einen Nutzer mit dieser Email gibt, wenn ja wirft sie einen Fehler. Existiert noch kein solcher Nutzer, so erstellt die Funktion den Passworthash und ruft mit den so erhaltenen Daten die Funktion zum speichern des Nutzers im UserRepository auf und gibt den durch diese gespeicherten Nutzer zurück.

**3\. What is the responsibility of the Repository layer? Give one example.**  
Der Repository-Layer ist für die Datenpersistenz verantwortlich. Er stellt die Verbindung zwischen Backend und Datenbank her, indem er die Datenbankaufrufe durchführt.

Bsp: Es soll ein neuer Nutzer angelegt werden. Durch den UserService wird die relevante Funktion im UserRepository aufgerufen, diese speichert den User in der Datenbank und gibt den erstellten Nutzer zurück.

**4\. Draw a 3 tier application architecture of your current email system**

**![architecture](./diagram.png)**

---

***VL4:***  
**1\. If an admin loads a list of all users and a user is indirectly connected to all objects inside the**  
**database, does the application has to load the whole database? Argue why or why not\!**  
Nein, die Anwendung muss die Datenbank nicht komplett laden. Durch die verwendung von ORM werden Beziehungen wie ManyToMany mit LazyLoading (FetchType.LAZY) geladen, wodurch diese Daten erst geladen werden, wenn auf diese Ressource zugegriffen wird. Eine weitere möglichkeit ist auch die Paginierung, welche wir mit Pageable umsetzen wenn wir zum Beispiel die Emails oder Alle Users ausgebeben lassen wollen.

**2\. One goal in a layered application architecture is avoid coupling the business logic to a database**  
**technology. Is your domain model coupled to a database technology?**  
**Check: Can you change your database from an SQL based to a no-sql database without**  
**adjusting the model classes? Argue why or why not\!**  
Unsere Domain-Modelle, wie beispielsweise die Klasse `Mail`, sind durch den Einsatz von JPA-Annotationen (`@Entity`, `@Id`, `@CollectionTable` etc.) direkt an die relationale SQL-Datenbanktechnologie gekoppelt. Da wir diese Abhängigkeit haben, können wir nicht ohne die Anpassung der model klassen zu einer no-sql Datenbank wechseln. Wir brächten dafür saubere Domainen Models, welche keine JPA-Annotation besitzen. Zudem würden eigene Datenbank Entity Klassen und entsprechende Mapper benötigt.

**3\. Research what Lazy Loading in JPA is, and where and how it can be configured?**  


Lazy Loading in JPA ist eine Möglichkeit, welche Daten aus Beziehungen aus der Datenbank erst laden, wenn explizit auf das jeweilige Feld zugegriffen wird. Die Daten werden also nicht direkt mit den anderen Daten geladen sondern zu einem späteren zeitpunkt. Die Umsetzung erfolgt dabei direkt in der Beziehungs-Annotation.

Beispiel:   
@OneToMany(fetch \= FetchType.LAZY)  
var attachments: MutableList\<Attachment\> \= mutableListOf()

**4\. Assume we want to use DTOs between the persistence layer and the business layer to decouple**  
**our domain model from JPA.**
**Will lazy loading still work or will the translation from repository DTO objects to model object**  
**trigger the loading anyway? Which benefits and drawbacks will we get when using DTO objects**  
**between business layer and persistence layer?**

Lazy Loading funktioniert bei Verwendung von DTOs zwischen Business-Layer und Persistenz-Layer nicht, insofern hierbei
auf Felder zugegriffen wird welche über Lazy-Loading verfügen. In diesem Fall wird durch das laden dieser Felder das Lazy Loading ausgelöst und ist damit Zwecklos.

Vorteile der Verwendung von DTOs:
1. Man kann festlegen, dass nur die Felder übertragen werden, welche wirklich benötigt werden.
2. Die Verwendung von DTOs sorgt für eine bessere Entkopplung zwischen Business- und Persistenz-Layer, da Änderungen an der Entity-Klasse den Business-Layer nicht berühren.

Nachteile der Verwendung von DTOs:
1. Der Code wird durch die Verwendung von DTOs komplexer, da z.B. zusätzliche Logik für die Umwandlung benötigt wird.
2. Die Verwendung von DTOs kann zu Problemen mit Lazy-Loading in JPA führen.

**Consider places where lazy loading is reasonable to use and implement lazy loading\!**

Es kann sinnvoll sein Lazy Loading einzusetzen, wenn durch das fetchen eines Objekt sehr große Abhängigkeiten geladen werden würden.
Bsp: Eine Bestellung enthält Referenzen zu den Detailinformationen für jedes enthaltene Produkt. Diese Information werden nicht zwingend bei jedem Laden der Bestellung benötigt.

----

***VL5***

**What is the difference between symmetric and asymmetric signing?**  
Bei symmetrischer Signierung gibt es nur einen Schlüssel, dieser existiert als geteiltes Geheimnis (Shared Secret) zwischen den beteiligten Systemen. Der Schlüssel kann sowohl zum Signieren als auch zum Überprüfen einer Signatur verwendet werden.

Die asymmetrische Signierung hingegen basiert auf asymmetrischen Verschlüsselungsverfahren. Das heißt, dass es ein Schlüsselpaar aus einem privaten und einem öffentlichen Schlüssel gibt. Bei der asymmetrischen Verschlüsselung wird der private Schlüssel zum Signieren und der öffentliche zum Überprüfen der Signatur verwendet.

Dies hat den Vorteil, dass sichergestellt werden kann, dass nur das System, das über den privaten Schlüssel verfügt, Signaturen erstellen kann. Dies sorgt außerdem dafür, dass der Austausch der Schlüssel zwischen zwei Systemen weniger kritisch ist, da nur der öffentliche Schlüssel weitergegeben werden muss, mit dem es nicht möglich ist, Signaturen zu erstellen. Der private Schlüssel verbleibt ausschließlich bei dem signierenden System.

**Describe how the server can use a signature to verify that the information was not manipulated.**

Um die Integrität der Daten sicherzustellen, kann aus den Daten ein Hashwert erstellt werden. Dieser wird mit dem (privaten) Schlüssel signiert und dieser Wert als Signatur mitgesendet. Der Empfänger erstellt mit dem gleichen Verfahren wie der Absender einen Hash aus den empfangenen Daten. Anschließend überprüft er anhand des (öffentlichen) Schlüssels und des erstellten Hashes die Gültigkeit der Signatur. Ist dies der Fall, kann man davon ausgehen, dass die Signatur mit dem gleichen Hash und dem zugehörigen (privaten) Schlüssel erstellt worden sein muss. Man kann daraus schlussfolgern, dass die Daten vollständig und korrekt sind. Unterscheiden sie sich, kann das z.B. auf eine Manipulation oder einen Übertragungsfehler zurückgeführt werden.

Hierdurch kann eine Manipulation der Daten erkannt werden, da im Falle von veränderten Daten der vom Empfänger erstellte Hashwert sich verändert und somit nicht mehr mit dem zur Signatur verwendeten Hash übereinstimmt.  
Einem Angreifer ist es ohne im Besitz des (privaten) Schlüssels zu sein nicht möglich, eine korrekte Signatur für die manipulierten Daten zu erstellen.

**What information must be shared between trusted servers in symmetric and what in asymmetric signing?**

In symmetrischen Verfahren muss der Schlüssel als geteiltes Geheimnis zwischen den Servern bestehen.

Bei asymmetrischen Verfahren muss jeder Server, der eine Signatur prüfen soll, über den öffentlichen Schlüssel verfügen, welcher das Gegenstück zum privaten Schlüssel des signierenden Servers ist.

Zudem müssen bei beiden Varianten die verwendeten Verfahren bekannt sein. (z.B. welche Hashfunktion verwendet wurde)

Check if the node\_modules folder is part of the distribution files.  
**Is the node\_modules folder required in production?**  
Der node\_modules ordner ist in der Produktion nicht erforderlich, da die Anwendung als statische Datei (HTML, JS und CSS geladen wird. Der ordner wird jedoch benötigt um mit npm run build den dist ordner zu bauen.  
If yes, then you also need to add it to the distribution folder.  
Check if the node\_modules folder contains dev dependencies.  
If yes, then find a way to exclude them.  
Wir entfernen die devDependencies indem wir im ersten Schritt mit npm run build die fertigen Statischen Dateien (HTML, JS und CSS) in einem dist ordner im Docker (/app/dist/mail\_client/browser/  
) speichern. Im zweiten Schritt laden wir dann diesen Dist ordner in einem anderen Container und stellen diesen als Webserver zu verfügung. 
