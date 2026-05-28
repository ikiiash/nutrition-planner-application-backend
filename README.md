# Nutrition Planner Application

This application will allow users to plan their daily or weekly meals and track nutritional values of the foods they consume. Users will be able to add food products, define portion sizes and automatically calculate calories and macronutrients.

The system will provide tools for organizing meals and viewing a summary of nutritional intake over time. It will help users maintain a balanced diet according to their personal goals, such as weight loss, maintaining weight or gaining muscle.

The application will also include a shopping list feature based on planned meals and allow users to record food prices to track their spending.

Additionally, a chat assistant will provide basic recommendations related to nutrition and meal planning.

The goal of the application is to simplify meal planning, support healthier eating habits and help users manage their food-related expenses.

# Authentication and roles

The backend uses Keycloak as the identity provider and acts only as an OAuth2 resource server.

- `ADMIN` for administration and catalog management
- `USER` for standard authenticated users
- `PREMIUM_USER` for subscription-gated premium features

Local JWT issuing has been removed from the backend.

# Run

Application:

- local workshop mode: `./mvnw -pl application/springboot -am spring-boot:run`
- keycloak profile: `./mvnw -pl application/springboot -am spring-boot:run -Dspring-boot.run.profiles=keycloak`

Database:

- PostgreSQL: `docker compose up -d db`

Keycloak:

- start Keycloak: `docker compose up -d my-keycloak`
- bootstrap realm, client, roles and demo users on Windows: `powershell -ExecutionPolicy Bypass -File .scripts\keycloak\bootstrap-nutrition-realm.ps1`
- bootstrap realm, client, roles and demo users on bash: `bash .scripts/keycloak/bootstrap-nutrition-realm.sh`
- run backend with Keycloak profile: `./mvnw clean -pl application/springboot -am spring-boot:run -Dspring-boot.run.profiles=keycloak`

Keycloak defaults:

- realm: `NUTRITION`
- client id: `nutrition-planner-client`
- client secret: `nutrition-planner-client-secret`
- users:
  - `admin@nutrition.local / admin123 / ADMIN`
  - `user@nutrition.local / user123 / USER`
  - `planner@nutrition.local / planner123 / USER`
  - `premium@nutrition.local / premium123 / PREMIUM_USER`

Token endpoint:

- `http://localhost:8081/realms/NUTRITION/protocol/openid-connect/token`

# Current implemented backend scope

- authenticated users can create, read, update and delete their own `food-products`
- `food-products` now support `category`, `grams` and optional `photoUrl`
- `food-products` support full micronutrient tracking: sodium, potassium, magnesium, iron, calcium, zinc, vitamins A/C/D/E/K/B1/B2/B6/B9/B12
- authenticated users can set fridge inventory for each food product (`/food-products/{id}/fridge`)
- authenticated users can read and update `/user-profile/me`
- the user profile stores personal data and calculates target calories and macros based on the selected goal
  - BMR calculated with Mifflin–St Jeor formula
  - TDEE multiplied by activity level factor (1.2 – 1.9)
  - target calories adjusted for goal: cut −15%, maintain 0%, bulk +10%
  - macro targets: protein 1.8–2.0 g/kg, fat 0.8–1.0 g/kg, carbs from remaining calories
- authenticated users can create, read, update and delete `meals` composed of food product ingredients
- authenticated users can create, read, update and delete `meal-plans` with a configurable number of days
- meal plans support `activate` / `deactivate` — only one plan can be active at a time
- authenticated users can add and remove `plan entries` (meal or food product) per day and meal type (BREAKFAST / LUNCH / DINNER / SNACK)
- nutritional totals per day and per plan are computed automatically from entries
- authenticated users can trigger `POST /meal-plans/{id}/deduct-fridge` to automatically subtract consumed food from fridge inventory based on days elapsed since plan activation
- authenticated users can manage a `shopping-list`: add, update, delete individual items, and clear the entire list
- `PREMIUM_USER` and `ADMIN` can use AI autofill (`POST /ai/autofill`) to populate nutrition values by product name
- `PREMIUM_USER` and `ADMIN` can create and manage persistent chat sessions (`/ai/chats`) and send messages to the AI assistant

# Zber požiadaviek

- RQ01 Systém umožní vytvoriť a spravovať účet.
- RQ02 Systém umožní zadať a upravovať osobné údaje (váha, výška, vek, aktivita, cieľ).
- RQ03 Systém automaticky vypočíta odporúčaný denný príjem kalórií a makroživín na základe údajov používateľa.
- RQ04 Systém umožní spravovať nastavenia aplikácie.
- RQ05 Systém umožní evidovať potraviny s nutričnými hodnotami a cenou.
- RQ06 Systém umožní vytvárať jedlá z viacerých potravín.
- RQ07 Systém umožní vyhľadávať potraviny a jedlá podľa názvu.
- RQ08 Systém umožní upravovať a odstraňovať potraviny a jedlá.
- RQ09 Systém umožní vytvárať a spravovať plán stravovania pre rôzne obdobia.
- RQ010 Systém umožní pridávať jedlá do plánu stravovania.
- RQ011 Systém automaticky vypočíta kalórie a makroživiny pre plán.
- RQ012 Systém umožní zobraziť prehľad nutričných hodnôt plánu.
- RQ013 Systém umožní evidovať ceny potravín.
- RQ014 Systém umožní vytvoriť nákupný zoznam.
- RQ015 Systém umožní automaticky vytvoriť nákupný zoznam na základe vybraných jedál.
- RQ016 Systém umožní sledovať výdavky na potraviny.
- RQ017 Systém umožní porovnať rozpočet s výdavkami.
- RQ018 Systém bude obsahovať chat asistenta na odporúčania stravovania.
- RQ019 Systém umožní automaticky doplniť nutričné hodnoty potraviny na základe názvu.
- RQ020 Systém umožní využívať rôzne úrovne predplatného (Free, Premium).
- RQ021 Systém obmedzí prístup k vybraným funkciám podľa typu predplatného.

# Slovník pojmov

| Pojem | Anglický názov | Definícia |
|------|---------------|----------|
| Používateľ | User | Osoba, ktorá používa aplikáciu na plánovanie stravy a sledovanie výživy. |
| Profil | Profile | Súbor osobných údajov používateľa, ako sú váha, výška, vek, aktivita a cieľ. |
| Potravina | Food Product | Základná jednotka stravy s definovanými nutričnými hodnotami a cenou. |
| Jedlo | Meal | Kombinácia potravín, ktorá tvorí konkrétny pokrm a môže byť súčasťou príjmu jedla. |
| Plán stravovania | Meal Plan | Zoznam jedál usporiadaných pre určité časové obdobie. |
| Deň plánu | Plan Day | Časť plánu stravovania reprezentujúca jeden deň s priradenými jedlami. |
| Príjem jedla | Meal Intake | Časť dňa, počas ktorej sa konzumuje jedlo (napr. raňajky, obed, večera), obsahujúca jedno alebo viac jedál. |
| Porcia | Portion | Množstvo potraviny alebo jedla určené na konzumáciu. |
| Kalórie | Calories | Energetická hodnota potraviny alebo jedla. |
| Makroživiny | Macronutrients | Základné živiny, najmä bielkoviny, tuky a sacharidy. |
| Cena | Price | Finančná hodnota potraviny alebo jedla. |
| Výdavky | Expenses | Záznam finančných prostriedkov vynaložených na potraviny. |
| Rozpočet | Budget | Plánovaná suma určená na výdavky za potraviny. |
| Nákupný zoznam | Shopping List | Zoznam potravín určených na nákup. |
| Položka zoznamu | Shopping List Item | Jedna položka nákupného zoznamu obsahujúca potravinu a množstvo. |
| Chat asistent | Chat Assistant | Systémová komponenta poskytujúca odporúčania pre stravovanie. |
| Odporúčanie | Recommendation | Návrh alebo rada generovaná systémom na zlepšenie stravy. |
| Predplatné | Subscription | Typ prístupu používateľa k funkciám systému (napr. Free alebo Premium). |

# Prípady použitia

- UC-01 Vytvorenie účtu
- UC-02 Úprava profilu používateľa
- UC-03 Nastavenie cieľov používateľa
- UC-04 Pridanie novej potraviny
- UC-05 Úprava potraviny
- UC-06 Vyhľadávanie potravín
- UC-07 Vytvorenie jedla z potravín
- UC-08 Úprava jedla
- UC-09 Vytvorenie plánu stravovania
- UC-10 Pridanie jedla do plánu
- UC-11 Zobrazenie plánu stravovania
- UC-12 Výpočet nutričných hodnôt plánu
- UC-13 Vytvorenie nákupného zoznamu
- UC-14 Automatické vytvorenie nákupného zoznamu z plánu
- UC-15 Evidencia ceny potraviny
- UC-16 Zobrazenie výdavkov
- UC-17 Porovnanie rozpočtu a výdavkov
- UC-18 Získanie odporúčania od asistenta
- UC-19 Automatické doplnenie nutričných hodnôt potraviny
- UC-20 Aktivácia predplatného
- UC-21 Prístup k premium funkciám

## UC-01 Vytvorenie účtu

**Účel**  
Zaregistrovať nového používateľa v systéme.

**Používateľ**  
Neprihlásený návštevník

**Vstupné podmienky**  
Používateľ má prístup k aplikácii.

**Výstup**  
V systéme existuje nový účet. Používateľ sa môže prihlásiť.

**Postup**

1. Používateľ otvorí prihlasovaciu obrazovku.
   Systém zobrazí možnosť prihlásenia cez Keycloak.

2. Používateľ zvolí možnosť „Register".
   Systém presmeruje používateľa na registračný formulár Keycloak.

3. Používateľ zadá prihlasovacie meno, e-mail a heslo.

4. Používateľ potvrdí registráciu.

5. Systém overí zadané údaje a vytvorí nový účet s rolou `USER`.

6. Používateľ je automaticky prihlásený a presmerovaný do aplikácie.

**Alternatívny scenár**

3a. Používateľ zadá e-mail, ktorý už existuje v systéme.  
Systém zobrazí chybové hlásenie a vyžiada iný e-mail.

3b. Heslo nespĺňa požiadavky na bezpečnosť.  
Systém zobrazí chybové hlásenie s požiadavkami na heslo.

## UC-02 Úprava profilu používateľa

**Účel**  
Aktualizovať osobné údaje používateľa v systéme.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
Osobné údaje používateľa sú aktualizované. Systém prepočítal odporúčaný príjem na základe nových hodnôt.

**Postup**

1. Používateľ otvorí sekciu profilu.
   Systém zobrazí aktuálne uložené osobné údaje.

2. Používateľ upraví požadované údaje: prezývku, meno, pohlavie, vek, výšku alebo váhu.

3. Používateľ vyberie úroveň fyzickej aktivity (sedavý životný štýl, ľahká aktivita, stredná aktivita, vysoká aktivita, extrémna aktivita).

4. Používateľ potvrdí uloženie zmien.

5. Systém overí správnosť zadaných hodnôt.

6. Systém uloží aktualizovaný profil a prepočíta BMR, TDEE a cieľové makroživiny.

7. Systém zobrazí aktualizované hodnoty v sekcii denných cieľov.

**Alternatívny scenár**

5a. Používateľ zadal neplatnú hodnotu (napr. záporný vek).  
Systém zobrazí chybové hlásenie a neuloží zmeny.

4a. Používateľ zruší úpravu.  
Systém nevykoná žiadne zmeny a používateľ zostáva na stránke profilu.

## UC-03 Nastavenie cieľov používateľa

**Účel**  
Nastaviť fitnes cieľ, podľa ktorého systém vypočíta odporúčaný kalorický príjem.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Používateľ má vyplnené základné biometrické údaje (vek, výška, váha, pohlavie, aktivita).

**Výstup**  
Systém prepočítal cieľový kalorický príjem a makroživiny na základe zvoleného cieľa.

**Postup**

1. Používateľ otvorí sekciu profilu.
   Systém zobrazí aktuálny profil vrátane sekcie výberu cieľa.

2. Používateľ vyberie jeden z dostupných cieľov:
   - Schudnúť (kalorický deficit −15 %)
   - Udržať hmotnosť (žiadna úprava)
   - Nabrať svalovú hmotu (kalorický prebytok +10 %)

3. Používateľ potvrdí uloženie profilu.

4. Systém prepočíta cieľové kalórie na základe TDEE a zvoleného cieľa.

5. Systém prepočíta denné ciele pre bielkoviny, tuky a sacharidy.

6. Systém zobrazí aktualizované cieľové hodnoty.

**Alternatívny scenár**

2a. Používateľ nemá vyplnené biometrické údaje.  
Systém napriek tomu uloží cieľ, ale zobrazí upozornenie, že výpočty nie sú dostupné bez kompletného profilu.

## UC-04 Pridanie novej potraviny

**Účel**  
Pridať novú potravinu do systému.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
V systéme pribudla nová potravina s názvom, nutričnými hodnotami a cenou.

**Postup**

1. Používateľ otvorí sekciu potravín.
   Systém zobrazí zoznam evidovaných potravín a možnosť pridať novú potravinu.

2. Používateľ zvolí možnosť „Pridať potravinu".
   Systém zobrazí formulár na zadanie údajov o potravine.

3. Používateľ zadá názov potraviny.

4. Používateľ zadá nutričné hodnoty potraviny.
   Používateľ môže zadať kalórie a makroživiny ručne.

5. Používateľ zadá cenu potraviny.

6. Používateľ potvrdí uloženie potraviny.

7. Systém overí vyplnenie povinných údajov.

8. Systém uloží potravinu do databázy.

9. Systém zobrazí novú potravinu v zozname potravín.

**Alternatívny scenár**

4a. Používateľ zvolí možnosť automatického doplnenia nutričných hodnôt.  
Systém na základe názvu potraviny doplní kalórie a makroživiny do formulára.

7a. Používateľ nevyplní povinný údaj.  
Systém zobrazí chybové hlásenie a neuloží potravinu.

6a. Používateľ zruší vytváranie potraviny.  
Systém zavrie formulár a používateľ zostáva v sekcii potravín.

## UC-05 Úprava potraviny

**Účel**  
Upraviť existujúcu potravinu v systéme.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Potravina existuje v systéme a patrí prihlásenom používateľovi.

**Výstup**  
Potravina je aktualizovaná s novými hodnotami.

**Postup**

1. Používateľ otvorí sekciu potravín.
   Systém zobrazí zoznam evidovaných potravín.

2. Používateľ vyberie potravinu zo zoznamu.
   Systém zobrazí detail potraviny s formulárom na úpravu.

3. Používateľ zmení požadované hodnoty (názov, kategória, gramáž, cena, nutričné hodnoty, mikroživiny).

4. Používateľ potvrdí uloženie zmien.

5. Systém overí správnosť zadaných hodnôt.

6. Systém aktualizuje potravinu v databáze.

7. Systém zobrazí aktualizovaný detail potraviny.

**Alternatívny scenár**

5a. Používateľ zadal neplatnú hodnotu (napr. záporná gramáž).  
Systém zobrazí chybové hlásenie a neuloží zmeny.

4a. Používateľ zruší úpravu.  
Systém nevykoná žiadne zmeny.

3a. Používateľ zvolí možnosť automatického doplnenia nutričných hodnôt.  
Systém na základe aktuálneho názvu potraviny doplní nutričné hodnoty (len pre PREMIUM_USER).

## UC-06 Vyhľadávanie potravín

**Účel**  
Nájsť potravinu v zozname podľa názvu alebo kategórie.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
Systém zobrazí zoznam potravín zodpovedajúcich zadaným kritériám.

**Postup**

1. Používateľ otvorí sekciu potravín.
   Systém zobrazí zoznam všetkých potravín.

2. Používateľ zadá hľadaný výraz do vyhľadávacieho poľa.
   Systém priebežne filtruje zoznam potravín podľa názvu.

3. Voliteľne používateľ vyberie kategóriu z rozbaľovacieho zoznamu.
   Systém aplikuje filter kategórie na výsledky.

4. Voliteľne používateľ zvolí zoradenie (A–Z alebo Z–A).

5. Systém zobrazí filtrovaný a zoradený zoznam potravín.

**Alternatívny scenár**

2a. Žiadna potravina nezodpovedá zadaným kritériám.  
Systém zobrazí informáciu, že neboli nájdené žiadne potraviny.

## UC-07 Vytvorenie jedla z potravín

**Účel**  
Vytvoriť jedlo zložené z viacerých potravín s definovanými gramážami.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
V systéme existuje aspoň jedna potravina.

**Výstup**  
V systéme pribudlo nové jedlo s vypočítanými nutričnými hodnotami a cenou.

**Postup**

1. Používateľ otvorí záložku „Meals" v sekcii Food.
   Systém zobrazí zoznam existujúcich jedál a možnosť vytvoriť nové.

2. Používateľ zvolí možnosť „New meal".
   Systém zobrazí formulár na vytvorenie jedla.

3. Používateľ zadá názov jedla a počet porcií.

4. Používateľ pridá ingredienciu: vyberie potravinu zo zoznamu a zadá gramáž.
   Systém zobrazí aktualizované nutričné hodnoty jedla.

5. Používateľ opakuje krok 4 pre každú ďalšiu ingredienciu.

6. Používateľ potvrdí uloženie jedla.

7. Systém vypočíta celkové nutričné hodnoty a cenu jedla, ako aj hodnoty na jednu porciu.

8. Systém uloží jedlo do databázy.

9. Systém zobrazí nové jedlo v zozname jedál.

**Alternatívny scenár**

4a. Požadovaná potravina neexistuje v zozname.  
Používateľ musí najprv pridať potravinu cez UC-04.

6a. Používateľ nepridal žiadnu ingredienciu.  
Systém zobrazí chybové hlásenie a neuloží jedlo.

6b. Používateľ zruší vytváranie jedla.  
Systém nevytvorí jedlo a používateľ zostáva v sekcii jedál.

## UC-08 Úprava jedla

**Účel**  
Upraviť existujúce jedlo – zmeniť názov, počet porcií alebo ingrediencie.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Jedlo existuje v systéme a patrí prihlásenom používateľovi.

**Výstup**  
Jedlo je aktualizované s novými hodnotami. Nutričné hodnoty sú prepočítané.

**Postup**

1. Používateľ otvorí záložku „Meals" v sekcii Food.
   Systém zobrazí zoznam existujúcich jedál.

2. Používateľ vyberie jedlo zo zoznamu.
   Systém zobrazí detail jedla so zoznamom ingrediencií a formulárom na úpravu.

3. Používateľ zmení názov alebo počet porcií jedla.

4. Voliteľne používateľ pridá novú ingredienciu alebo odstráni existujúcu.

5. Používateľ potvrdí uloženie zmien.

6. Systém prepočíta nutričné hodnoty a cenu jedla.

7. Systém aktualizuje jedlo v databáze.

8. Systém zobrazí aktualizovaný detail jedla.

**Alternatívny scenár**

5a. Používateľ zruší úpravu.  
Systém nevykoná žiadne zmeny.

4a. Jedlo po úprave neobsahuje žiadne ingrediencie.  
Systém zobrazí chybové hlásenie a neuloží zmeny.

## UC-09 Vytvorenie plánu stravovania

**Účel**  
Vytvoriť nový plán stravovania pre zvolené obdobie.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
V systéme pribudol nový plán stravovania pre zvolené obdobie.

**Postup**

1. Používateľ otvorí sekciu plánovania stravy.
   Systém zobrazí existujúce plány stravovania a možnosť vytvoriť nový plán.

2. Používateľ zvolí možnosť „Vytvoriť plán stravovania".

3. Používateľ vyberie obdobie plánu.
   Používateľ môže zvoliť deň, týždeň, mesiac alebo vlastný interval.

4. Systém pripraví nový plán pre zvolené obdobie.

5. Používateľ zadá základné informácie o pláne.

6. Používateľ potvrdí vytvorenie plánu.

7. Systém uloží plán stravovania do databázy.

8. Systém zobrazí vytvorený plán používateľovi.

**Alternatívny scenár**

3a. Používateľ zvolí vlastný interval.  
Systém umožní zadať počiatočný a koncový dátum plánu.

6a. Používateľ zruší vytváranie plánu.  
Systém nevytvorí plán a používateľ zostáva v sekcii plánovania.

3b. Používateľ zadá neplatné obdobie.  
Systém zobrazí chybové hlásenie a vyžiada opravu údajov.

## UC-10 Pridanie jedla do plánu

**Účel**  
Priradiť jedlo alebo potravinu do konkrétneho dňa a časovej časti plánu stravovania.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Plán stravovania existuje v systéme.  
V systéme existuje aspoň jedno jedlo alebo potravina.

**Výstup**  
K vybranému dňu plánu pribudla nová položka. Denné nutričné hodnoty sú prepočítané.

**Postup**

1. Používateľ otvorí plán stravovania a vyberie deň.
   Systém zobrazí prehľad dňa s časovými časťami (raňajky, obed, večera, desiata).

2. Používateľ zvolí časovú časť a typ záznamu (jedlo alebo potravina).

3. Používateľ vyberie konkrétne jedlo alebo potravinu zo zoznamu.

4. Používateľ zadá počet porcií (pri jedle) alebo gramáž (pri potravine).

5. Používateľ potvrdí pridanie záznamu.

6. Systém uloží záznam a prepočíta nutričné hodnoty pre daný deň.

7. Systém zobrazí aktualizovaný prehľad dňa vrátane nového záznamu.

**Alternatívny scenár**

3a. Požadované jedlo alebo potravina neexistuje v systéme.  
Používateľ musí najprv vytvoriť jedlo (UC-07) alebo potravinu (UC-04).

5a. Používateľ zruší pridávanie záznamu.  
Systém nevykoná žiadne zmeny.

## UC-11 Zobrazenie plánu stravovania

**Účel**  
Zobraziť detail plánu stravovania vrátane všetkých dní, jedál a nutričných hodnôt.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Aspoň jeden plán stravovania existuje v systéme.

**Výstup**  
Používateľ vidí prehľad plánu s rozpisom jedál pre každý deň a súhrnnými nutričnými hodnotami.

**Postup**

1. Používateľ otvorí sekciu plánovania stravy.
   Systém zobrazí zoznam plánov stravovania. Aktívny plán je označený.

2. Používateľ vyberie konkrétny plán.
   Systém zobrazí detail plánu s výberom dní.

3. Používateľ vyberie deň.
   Systém zobrazí záznamy pre daný deň rozdelené podľa časovej časti (raňajky, obed, večera, desiata).

4. Systém zobrazí nutričné hodnoty pre daný deň a porovná ich s cieľovými hodnotami z profilu používateľa.

5. Systém zobrazí súhrnné hodnoty za celý plán (priemer na deň, celkové kalórie, makroživiny).

**Alternatívny scenár**

1a. Používateľ nemá vytvorený žiadny plán.  
Systém zobrazí výzvu na vytvorenie prvého plánu.

## UC-12 Výpočet nutričných hodnôt plánu

**Účel**  
Automaticky vypočítať a zobraziť nutričné hodnoty pre vybraný deň a celý plán stravovania.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Plán stravovania obsahuje aspoň jeden záznam (jedlo alebo potravinu).

**Výstup**  
Systém zobrazí súhrnné nutričné hodnoty (kalórie, bielkoviny, tuky, sacharidy) pre deň aj celý plán. Hodnoty sú porovnané s cieľmi z profilu používateľa.

**Postup**

1. Systém automaticky vypočíta nutričné hodnoty pri každej zmene záznamu v pláne.

2. Pre každý deň systém sčíta kalórie, bielkoviny, tuky a sacharidy zo všetkých záznamov.

3. Systém porovná denné hodnoty s cieľovými hodnotami z profilu a zobrazí percentuálne plnenie.

4. Systém prepočíta priemer na deň a celkové hodnoty za celý plán.

5. Systém zobrazí upozornenie, ak používateľ prekročí cieľový príjem kalórií.

**Alternatívny scenár**

3a. Používateľ nemá vyplnený profil s cieľovými hodnotami.  
Systém zobrazí nutričné hodnoty bez porovnania a ponúkne odkaz na vyplnenie profilu.

## UC-13 Vytvorenie nákupného zoznamu

**Účel**  
Vytvoriť alebo doplniť nákupný zoznam manuálnym pridaním produktov.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
Nákupný zoznam obsahuje nové položky s požadovanými množstvami.

**Postup**

1. Používateľ otvorí sekciu financií a zvolí záložku „Shopping list".
   Systém zobrazí aktuálny obsah nákupného zoznamu.

2. Používateľ vyberie zdroj pre pridanie položiek (potravina, jedlo alebo plán stravovania).

3. Používateľ vyberie konkrétnu potravinu alebo jedlo a zadá množstvo (gramy alebo porcie).

4. Voliteľne používateľ zapne možnosť „Consider fridge" — systém od požadovaného množstva odpočíta zásoby z chladničky.

5. Používateľ potvrdí pridanie položky.

6. Systém pridá položku do nákupného zoznamu s vypočítanými nutričnými hodnotami a cenou.

7. Systém zobrazí aktualizovaný nákupný zoznam s celkovými hodnotami.

**Alternatívny scenár**

3a. Požadovaná potravina neexistuje v systéme.  
Používateľ musí najprv pridať potravinu (UC-04).

5a. Používateľ zruší pridávanie položky.  
Systém nevykoná žiadne zmeny.

## UC-14 Automatické vytvorenie nákupného zoznamu z plánu

**Účel**  
Automaticky vytvoriť nákupný zoznam na základe vybraného plánu stravovania.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Plán stravovania existuje v systéme.  
Plán obsahuje jedlá alebo potraviny.

**Výstup**  
V systéme pribudol nový nákupný zoznam vytvorený na základe plánu stravovania.

**Postup**

1. Používateľ otvorí sekciu plánovania stravy.
   Systém zobrazí zoznam existujúcich plánov stravovania.

2. Používateľ zvolí konkrétny plán stravovania.
   Systém zobrazí detail plánu vrátane dní a priradených jedál.

3. Používateľ zvolí možnosť „Vytvoriť nákupný zoznam".

4. Systém načíta všetky jedlá a potraviny priradené k vybranému plánu.

5. Systém identifikuje potraviny potrebné na prípravu všetkých jedál v pláne.

6. Systém spojí rovnaké potraviny do jednej položky a sčíta ich množstvá.

7. Systém vytvorí nový nákupný zoznam.

8. Systém zobrazí nákupný zoznam používateľovi.

**Alternatívny scenár**

4a. Plán neobsahuje žiadne jedlá ani potraviny.  
Systém zobrazí informáciu, že nákupný zoznam nie je možné vytvoriť.

3a. Používateľ zruší vytváranie nákupného zoznamu.  
Systém nevytvorí nákupný zoznam a používateľ zostáva na detaile plánu.

## UC-15 Evidencia ceny potraviny

**Účel**  
Zaznamenať cenu potraviny pri jej vytváraní alebo úprave.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.

**Výstup**  
Potravina má uloženú cenu, ktorá sa využíva pri výpočtoch nákladov jedál, plánov a nákupného zoznamu.

**Postup**

1. Používateľ pridá novú potravinu (UC-04) alebo upraví existujúcu (UC-05).

2. Používateľ zadá cenu potraviny v poli „Price (€)".

3. Používateľ potvrdí uloženie.

4. Systém uloží cenu spolu s ostatnými údajmi potraviny.

5. Systém využíva cenu pri výpočte celkových nákladov jedál, plánov a položiek nákupného zoznamu.

**Alternatívny scenár**

2a. Používateľ nezadá cenu.  
Systém uloží potravinu s cenou 0. Náklady v súvisiacich výpočtoch budú nulové.

## UC-16 Zobrazenie výdavkov

**Účel**  
Zobraziť prehľad výdavkov súvisiacich s nákupným zoznamom a plánmi stravovania.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Nákupný zoznam obsahuje aspoň jednu položku alebo plán stravovania obsahuje potraviny s cenami.

**Výstup**  
Používateľ vidí celkové náklady nákupného zoznamu, ceny plánov stravovania a štatistiky výdavkov.

**Postup**

1. Používateľ otvorí sekciu financií.
   Systém zobrazí nákupný zoznam s celkovou cenou.

2. Používateľ prepne na záložku „Summary".
   Systém zobrazí štatistiky výdavkov: celkovo utratené, celkové kalórie, priemer kalórií na deň.

3. Systém zobrazí prehľad plánov stravovania s celkovými nákladmi a cenou na deň.

4. Systém zobrazí analýzu hodnoty (kcal/€, bielkoviny/€, tuky/€, sacharidy/€) pre produkty, jedlá a plány.

**Alternatívny scenár**

1a. Nákupný zoznam je prázdny a plány neobsahujú ceny.  
Systém zobrazí nulové hodnoty výdavkov.

## UC-17 Porovnanie rozpočtu a výdavkov

**Účel**  
Porovnať nutričnú hodnotu potravín a jedál voči ich cene a identifikovať najvýhodnejšie možnosti.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
V systéme existujú potraviny alebo jedlá s vyplnenými cenami.

**Výstup**  
Systém zobrazí zoradený zoznam potravín/jedál podľa vybranej metriky efektivity (kcal/€, bielkoviny/€, tuky/€ alebo sacharidy/€).

**Postup**

1. Používateľ otvorí sekciu financií a prepne na záložku „Summary".

2. Používateľ vyberie metriku porovnania (kcal/€, bielkoviny/€, tuky/€ alebo sacharidy/€).

3. Používateľ vyberie zdroj porovnania (potraviny, jedlá alebo plány).

4. Systém zoradí záznamy podľa vybranej metriky od najvýhodnejšieho po najmenej výhodné.

5. Systém zobrazí poradie s grafickými ukazovateľmi efektivity a makrohodnotami.

6. Systém zobrazí zoznam lídrov mikroživín — pre každú mikroživinu zobrazí najlepší produkt z hľadiska obsahu na euro.

**Alternatívny scenár**

2a. Žiadny záznam nemá vyplnenú cenu.  
Systém zobrazí informáciu, že nie je možné porovnať záznamy bez ceny.

## UC-18 Získanie odporúčania od asistenta

**Účel**  
Získať odporúčanie pre stravovanie pomocou chat asistenta.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému.  
Používateľ má aktívne predplatné, ktoré umožňuje používanie asistenta.

**Výstup**  
Používateľ dostane odporúčanie súvisiace so stravovaním, jedlami alebo plánom stravovania.

**Postup**

1. Používateľ otvorí sekciu chat asistenta.
   Systém zobrazí rozhranie pre komunikáciu s asistentom.

2. Používateľ zadá požiadavku.
   Požiadavka môže byť napríklad návrh jedla, odporúčanie k plánu alebo úprava stravy podľa cieľa.

3. Používateľ odošle správu asistentovi.

4. Systém spracuje požiadavku používateľa.

5. Systém zohľadní dostupné údaje používateľa, napríklad cieľ, plán stravovania alebo potraviny.

6. Systém vygeneruje odporúčanie.

7. Systém zobrazí odpoveď asistenta používateľovi.

**Alternatívny scenár**

2a. Používateľ nemá aktívne predplatné.  
Systém zobrazí informáciu, že funkcia je dostupná len pre premium používateľov.

3a. Používateľ zruší odoslanie správy.  
Systém neodošle požiadavku a používateľ zostáva v chate.

6a. Systém nedokáže vytvoriť odporúčanie.  
Systém zobrazí informáciu, že odporúčanie nie je momentálne dostupné.

## UC-19 Automatické doplnenie nutričných hodnôt potraviny

**Účel**  
Automaticky vyplniť nutričné hodnoty potraviny na základe jej názvu pomocou AI.

**Používateľ**  
PREMIUM_USER, ADMIN

**Vstupné podmienky**  
Používateľ je prihlásený do systému s rolou PREMIUM_USER alebo ADMIN.  
Používateľ je v procese vytvárania alebo úpravy potraviny a má vyplnený názov.

**Výstup**  
Formulár potraviny je vyplnený odporúčanými nutričnými hodnotami (kalórie, bielkoviny, tuky, sacharidy, mikroživiny).

**Postup**

1. Používateľ otvorí formulár na vytvorenie alebo úpravu potraviny.

2. Používateľ zadá názov potraviny.

3. Používateľ zvolí možnosť „AI Fill".
   Systém odošle názov potraviny do AI služby.

4. Systém prijme odporúčané nutričné hodnoty od AI.

5. Systém vyplní hodnoty kalórií, bielkovín, tukov, sacharidov a dostupných mikroživín do formulára.

6. Používateľ skontroluje a prípadne upraví doplnené hodnoty.

7. Používateľ potvrdí uloženie potraviny.

**Alternatívny scenár**

3a. AI služba nie je dostupná.  
Systém zobrazí chybové hlásenie a používateľ musí vyplniť hodnoty ručne.

2a. Názov potraviny je príliš všeobecný alebo neznámy.  
Systém vráti približné hodnoty, ktoré používateľ musí skontrolovať a potvrdiť.

1a. Používateľ nemá rolu PREMIUM_USER ani ADMIN.  
Tlačidlo „AI Fill" je zobrazené ale deaktivované s informáciou, že ide o prémiovú funkciu.

## UC-20 Aktivácia predplatného

**Účel**  
Získať prístup k prémiovým funkciám aplikácie.

**Používateľ**  
Používateľ

**Vstupné podmienky**  
Používateľ je prihlásený do systému s rolou USER.

**Výstup**  
Používateľovi je priradená rola PREMIUM_USER. Prémiové funkcie sú dostupné.

**Postup**

1. Používateľ si všimne prémiovú funkciu označenú ako „PRO" alebo „Premium".

2. Správca systému priradí rolu PREMIUM_USER danému používateľovi v Keycloak.

3. Používateľ sa znovu prihlási alebo obnoví token.

4. Systém rozpozná rolu PREMIUM_USER v JWT tokene.

5. Prémiové funkcie (AI asistent, AI autofill) sú odblokované.

**Alternatívny scenár**

2a. Správca nepriradil rolu.  
Používateľ zostáva s rolou USER a prémiové funkcie zostávajú neprístupné.

## UC-21 Prístup k premium funkciám

**Účel**  
Využívať funkcie dostupné len pre prémiových používateľov (AI asistent, AI autofill).

**Používateľ**  
PREMIUM_USER, ADMIN

**Vstupné podmienky**  
Používateľ je prihlásený do systému s rolou PREMIUM_USER alebo ADMIN.

**Výstup**  
Používateľ môže plnohodnotne využívať prémiové funkcie.

**Postup**

1. Používateľ otvorí sekciu AI asistenta.
   Systém rozpozná rolu PREMIUM_USER a zobrazí plné rozhranie chatu.

2. Používateľ vytvorí nové chatové sedenie.
   Systém uloží sedenie a sprístupní históriu konverzácií.

3. Používateľ odosiela správy asistentovi a dostáva odpovede.
   Systém ukladá históriu správ v rámci sedenia.

4. Používateľ môže pri tvorbe potravín použiť funkciu AI autofill (UC-19).

**Alternatívny scenár**

1a. Používateľ nemá rolu PREMIUM_USER ani ADMIN.  
Systém zobrazí uzamknutú obrazovku s informáciou o prémiovom predplatnom namiesto rozhrania chatu.
