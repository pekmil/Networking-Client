# Networking-Client

A Networking-Client projekt egy egyszerű socket illetve nio kliens mintakódot tartalmaz. A kliens futtatásához szükséges a fájlok fordítása és csomagolása. A fordítás során használni kell a Networking-Messages.jar csomagot. A csomagolás során pedig a MANIFEST.MF állományt.

1. Töltsük le a forrásfájlokat tartalmazó mappát (src) egy gyökérkönyvtárba (<i>root</i>)
2. Hozzuk létre a <i>root</i>/classes mappát
3. A <i>root</i> mappából adjuk ki a következő parancssori parancsot:<br /><code>javac src/client/nio/\*.java src/client/socket/\*.java -d classes -classpath <i>path/to/</i>Networking-Messages.jar</code>
4. Lépjünk be a <i>root</i>/classes mappába és adjuk ki a következő parancssori parancsot:<br /><code>jar cfm Networking-Client.jar MANIFEST.MF client/nio/\*.class client/socket/\*.class</code>

Az előállt Networking-Client.jar csomag futtatása az alábbi parancssori paranccsal lehetséges:<br /><code>java -jar Networking-Client.jar</code><br />A futtatáshoz szükséges, hogy a Networking-Messages.jar csomagot a Networking-Client.jar csomaggal közös mappában helyezzük el. Jelenlegi konfigurációval a socket kliens indul el.
