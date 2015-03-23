# TIRTproject
Generowanie planu z html' a dla PWR

<b>http://planpwr.unicloud.pl/schedule.html#</b>

# Dostęp do repo - TortoiseGit

Instalacja tak jak tutaj: http://www.mediawiki.org/wiki/Gerrit/TortoiseGit_tutorial.
Tutaj filmik: https://www.youtube.com/watch?v=pp2S2lHjzZI, ale ten klucz nie jest nam raczej potrzebny, więc można to pominąć.
Jak już jest zainstalowane to:
1) Jak w filmiku wyżej tworzymy folder
2) Prawym myszki i Git Clone
3) W URL wkleić https://github.com/19booob92/TIRTproject.git
4) Zaznaczyć branch i wpisać nazwę brancha, który nas interesuje, czyli android.
5) Dajemy OK, podajemy login i hasło do GitHuba i się ściąga.
W przypadku brancha androidowego ściąga się folder, w którym jest projekt Android Studio, tutorial importowania niżej.

Po tym jak już coś porobimy i będziemy chcieli przesłać zmiany na serwer.
1) W folderze, który stworzyliśmy powinien być folder TIRTproject
2) Klikamy prawym, dajemy Git Commit -> "android"
3) Wpisujemy opis zmian, wybieramy pliki i dajemy OK
4) W okientu, w którym będzie pokazywał się progres, jak już się wszystko zakończy, dajemy Push
5) W nowym oknie upewlniamy się, że w Ref w Remote jest wybrany dobry branch (tu android) i OK
6) Login, hasło i gotowe

Ściąganie w przyszłoci:
1) Prawym na folderze TIRTproject -> TortoiseGit -> Pull
2) Upewniamy się, że w Remote Branch jest właściwy branch i dajemy OK

# Android Studio - import projektu
1) Odpalamy Android Studio
2) File -> Import project
3) Szukamy folderu ze ściągniętym kodem i wybieramy build.gradle
4) Jak będzie się pytał o coś z językiem to dać No, albo ogarnąć o co mu chodzi i napisać tu :)
