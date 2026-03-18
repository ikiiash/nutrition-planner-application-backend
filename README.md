# Nutrition Planner Application

This application will allow users to plan their daily or weekly meals and track nutritional values of the foods they consume. Users will be able to add food products, define portion sizes and automatically calculate calories and macronutrients.

The system will provide tools for organizing meals and viewing a summary of nutritional intake over time. It will help users maintain a balanced diet according to their personal goals, such as weight loss, maintaining weight or gaining muscle.

The application will also include a shopping list feature based on planned meals and allow users to record food prices to track their spending.

Additionally, a chat assistant will provide basic recommendations related to nutrition and meal planning.

The goal of the application is to simplify meal planning, support healthier eating habits and help users manage their food-related expenses.

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

2. Používateľ zvolí možnosť „Pridať potravinu“.
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

2. Používateľ zvolí možnosť „Vytvoriť plán stravovania“.

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

3. Používateľ zvolí možnosť „Vytvoriť nákupný zoznam“.

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
