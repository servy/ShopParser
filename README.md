# Сборка проекта

Данный проект использует систему сборки gradle. Вы можете импортировать его в вашу любимую среду разработки
и указать главный класс - `ru.samsung.itschool.shopparser.State`, так как данный проект не имеет никаких
внешних зависимостей. Однако, его можно собрать и запустить и из командной строки. Для этого, вам необходимо
запустить команду `gradlew run` в папке с проектом. Если вы используете \*nix систему, вместо `gradlew.bat`
необходимо выполнить `./gradlew run`.

Чтобы собрать jar файл вы можете выполнить `gradlew jar` (он появится в папке build/libs), а с помощью
команды `gradlew distZip` вы можете собрать zip-файл (он появится в папке build/distributions), в котором
будет jar-файл с собранными классами вашей программы и скрипты для запуска вашей программы.

Более подробную информацию о системе сборки gradle вы можете получить на [официальном сайте](https://www.gradle.org/).