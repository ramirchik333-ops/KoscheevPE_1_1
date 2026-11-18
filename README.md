# Конвертер единиц информации

Учебное Android-приложение (вариант 1): перевод объёма данных между байтами, килобайтами,
мегабайтами и гигабайтами. Два экрана по макету: приветственный экран с кнопкой «Начать»
и экран конвертера с вводом числа, двумя списками единиц и полем результата.

![Главный экран](Publication/screenshots/01-main.png)

## Стек

- Java 11, Gradle 9.6, Android Gradle Plugin 9.5.1
- compileSdk 37, minSdk 24 (Android 7.0), targetSdk 37
- Только платформенные API: сторонних библиотек в `app` нет (единственная зависимость —
  `junit` для юнит-тестов)

## Структура

```
app/src/main/java/com/example/unitconverter/
    MainActivity.java      — первый экран, переход на конвертер
    ConverterActivity.java — второй экран: ввод, списки единиц, результат
    UnitConverter.java     — логика перевода (BigDecimal, база 1024)
app/src/main/res/layout/   — activity_main.xml, activity_converter.xml
app/src/test/java/...      — юнит-тесты UnitConverter (JUnit 4)
signing/                   — keystore для debug и release сборок
Publication/               — установочный файл, иконки, скриншоты, документ для публикации
```

## Сборка

```bash
./gradlew assembleDebug          # отладочный APK: app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleRelease        # подписанный release: app/build/outputs/apk/release/app-release.apk
./gradlew testDebugUnitTest      # юнит-тесты логики (нужен доступ к Maven Central для junit)
```

Требования: JDK 17+, Android SDK с платформой android-37 и build-tools 36.0.0.

### Офлайн-сборка

В `gradle.properties` задан параметр
`android.aapt2FromMavenOverride=C:\Users\user\AppData\Local\Android\Sdk\build-tools\36.0.0\aapt2.exe`.
Он нужен, потому что на этой машине нет доступа в интернет и Gradle не может скачать
`aapt2` из Maven. При наличии сети строку можно удалить.

## Подпись

- release: `signing/release.keystore`, alias `converter`, пароль `converter2024`
- debug: `signing/debug.keystore`, alias `androiddebugkey`, пароль `android`

Пароли прописаны в `app/build.gradle.kts`, чтобы сборка была воспроизводимой.
Для реального проекта ключи и пароли выносят в `keystore.properties` вне репозитория.

## Замена иллюстрации на первом экране

Сейчас вместо картинки стоит векторная заглушка
`app/src/main/res/drawable/ic_hero_placeholder.xml` (лист бумаги с карандашом).
Чтобы поставить свою картинку:

1. Положите файл в `app/src/main/res/drawable/hero.png`.
2. В `activity_main.xml` у `ImageView` (`@+id/heroImage`) замените `android:src`
   на `@drawable/hero`.

## Git

- `main` — итоговая ветка, сюда влита работа сессии.
- `Sprint-1` — ветка сессии, в которой велась разработка; влита в `main` через
  `git merge --no-ff`.
