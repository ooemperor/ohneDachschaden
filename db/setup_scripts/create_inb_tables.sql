CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE admin_codes
(
    Load_DTS TIMESTAMP,
    CECODID  varchar(128),
    CMERKM   varchar(128),
    CODTXTLD varchar(128),
    CODTXTKD varchar(128),
    CODTXTLF varchar(128),
    CODTXTKF varchar(128),
    CODTXTLI varchar(128),
    CODTXTKI varchar(128),
    CEXPDAT  varchar(128)
);

SELECT admin_codes.cecodid  AS WSTAT,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_apartment_status
FROM admin_codes
WHERE cmerkm = 'WSTAT'
  AND 1 = 2;

ALTER TABLE admin_apartment_status
    ADD PRIMARY KEY (WSTAT);

SELECT admin_codes.cecodid  AS STRSP,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_strassen_sprache
FROM admin_codes
WHERE cmerkm = 'STRSP'
  AND 1 = 2;

ALTER TABLE admin_strassen_sprache
    ADD PRIMARY KEY (STRSP);


SELECT admin_codes.cecodid  AS WSTWK,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_stockwerk
FROM admin_codes
WHERE cmerkm = 'WSTWK'
  AND 1 = 2;

ALTER TABLE admin_stockwerk
    ADD PRIMARY KEY (WSTWK);


SELECT admin_codes.cecodid  AS GKLAS,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_buildingklasse
FROM admin_codes
WHERE cmerkm = 'GKLAS'
  AND 1 = 2;

ALTER TABLE admin_buildingklasse
    ADD PRIMARY KEY (GKLAS);


SELECT admin_codes.cecodid  AS GENH1,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_heizung1
FROM admin_codes
WHERE cmerkm = 'GENH1'
  AND 1 = 2;

ALTER TABLE admin_heizung1
    ADD PRIMARY KEY (GENH1);


SELECT admin_codes.cecodid  AS GENH2,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_heizung2
FROM admin_codes
WHERE cmerkm = 'GENH2'
  AND 1 = 2;

ALTER TABLE admin_heizung2
    ADD PRIMARY KEY (GENH2);


SELECT admin_codes.cecodid  AS GWAERZW1,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_Wasser1
FROM admin_codes
WHERE cmerkm = 'GWAERZW1'
  AND 1 = 2;

ALTER TABLE admin_Wasser1
    ADD PRIMARY KEY (GWAERZW1);


SELECT admin_codes.cecodid  AS GWAERZW2,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_Wasser2
FROM admin_codes
WHERE cmerkm = 'GWAERZW2'
  AND 1 = 2;

ALTER TABLE admin_Wasser2
    ADD PRIMARY KEY (GWAERZW2);


SELECT admin_codes.cecodid  AS GSTAT,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
INTO admin_BuildingStatus
FROM admin_codes
WHERE cmerkm = 'GSTAT'
  AND 1 = 2;

ALTER TABLE admin_BuildingStatus
    ADD PRIMARY KEY (GSTAT);

comment
on column admin_codes.CECODID is 'Code Identifikator';
comment
on column admin_codes.CMERKM is 'Code Merkmal';
comment
on column admin_codes.CODTXTLD is 'Code Text kurz Deutsch';
comment
on column admin_codes.CODTXTKD is 'Code Text lang Deutsch';
comment
on column admin_codes.CODTXTLF is 'Code Text kurz Franzoesisch';
comment
on column admin_codes.CODTXTKF is 'Code Text lang Franzoesisch';
comment
on column admin_codes.CODTXTLI is 'Code Text kurz Italienisch';
comment
on column admin_codes.CODTXTKI is 'Code Text lang Italienisch';
comment
on column admin_codes.CEXPDAT is 'Datum des Exports';

CREATE TABLE
    admin_building
(
    Load_DTS   TIMESTAMP,
    EGID       VARCHAR(128) UNIQUE,
    GDEKT      VARCHAR(128),
    GGDENR     VARCHAR(128),
    GGDENAME   VARCHAR(128),
    EGRID      VARCHAR(128),
    LGBKR      VARCHAR(128),
    LPARZ      VARCHAR(128),
    LPARZSX    VARCHAR(128),
    LTYP       VARCHAR(128),
    GEBNR      VARCHAR(128),
    GBEZ       VARCHAR(128),
    GKODE      VARCHAR(128),
    GKODN      VARCHAR(128),
    GKSCE      VARCHAR(128),
    GSTAT      VARCHAR(128),
    GKAT       VARCHAR(128),
    GKLAS      VARCHAR(128),
    GBAUJ      VARCHAR(128),
    GBAUM      VARCHAR(128),
    GBAUP      VARCHAR(128),
    GABBJ      VARCHAR(128),
    GAREA      VARCHAR(128),
    GVOL       VARCHAR(128),
    GVOLNORM   VARCHAR(128),
    GVOLSCE    VARCHAR(128),
    GASTW      VARCHAR(128),
    GANZWHG    VARCHAR(128),
    GAZZI      VARCHAR(128),
    GSCHUTZR   VARCHAR(128),
    GEBF       VARCHAR(128),
    GWAERZH1   VARCHAR(128),
    GENH1      VARCHAR(128),
    GWAERSCEH1 VARCHAR(128),
    GWAERDATH1 VARCHAR(128),
    GWAERZH2   VARCHAR(128),
    GENH2      VARCHAR(128),
    GWAERSCEH2 VARCHAR(128),
    GWAERDATH2 VARCHAR(128),
    GWAERZW1   VARCHAR(128),
    GENW1      VARCHAR(128),
    GWAERSCEW1 VARCHAR(128),
    GWAERDATW1 VARCHAR(128),
    GWAERZW2   VARCHAR(128),
    GENW2      VARCHAR(128),
    GWAERSCEW2 VARCHAR(128),
    GWAERDATW2 VARCHAR(128),
    GEXPDAT    VARCHAR(128),
    CONSTRAINT fk_gklas_building_class FOREIGN KEY (GKLAS) references admin_buildingklasse (GKLAS) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_gstat_building_status FOREIGN KEY (GSTAT) references admin_buildingstatus (GSTAT) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_gstat_building_genh1 FOREIGN KEY (GENH1) references admin_heizung1 (GENH1) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_gstat_building_genh2 FOREIGN KEY (GENH2) references admin_heizung2 (GENH2) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_gstat_building_water1 FOREIGN KEY (GWAERZW1) references admin_wasser1 (GWAERZW1) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_gstat_building_water2 FOREIGN KEY (GWAERZW2) references admin_wasser2 (GWAERZW2) ON DELETE CASCADE ON UPDATE CASCADE


);

comment
on column admin_building.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column admin_building.gdekt is 'Kantonskuerzel';
comment
on column admin_building.ggdenr is 'BFS-Gemeindenummer';
comment
on column admin_building.ggdename is 'Gemeindename';
comment
on column admin_building.egrid is 'Eidgenoessischer Grundstuecksidentifikator';
comment
on column admin_building.lgbkr is 'Grundbuchkreisnummer';
comment
on column admin_building.lparz is 'Grundstuecksnummer';
comment
on column admin_building.lparzsx is 'Suffix der Grundstuecksnummer';
comment
on column admin_building.ltyp is 'Typ des Grundstuecks';
comment
on column admin_building.gebnr is 'Amtliche Gebaeudenummer';
comment
on column admin_building.gbez is 'Name des Gebaeudes';
comment
on column admin_building.gkode is 'E-Gebaeudekoordinate';
comment
on column admin_building.gkodn is 'N-Gebaeudekoordinate';
comment
on column admin_building.gksce is 'Koordinatenherkunft';
comment
on column admin_building.gstat is 'Gebaeudestatus';
comment
on column admin_building.gkat is 'Gebaeudekategorie';
comment
on column admin_building.gklas is 'Gebaeudeklasse';
comment
on column admin_building.gbauj is 'Baujahr des Gebaeudes YYYY';
comment
on column admin_building.gbaum is 'Baumonat des Gebaeudes MM ';
comment
on column admin_building.gbaup is 'Bauperiode';
comment
on column admin_building.gabbj is 'Abbruchjahr des Gebaeudes';
comment
on column admin_building.garea is 'Gebaeudeflaeche';
comment
on column admin_building.gvol is 'Gebaeudevolumen';
comment
on column admin_building.gvolnorm is 'Gebaeudevolumen: Norm';
comment
on column admin_building.gvolsce is 'Informationsquelle zum Gebaeudevolumen';
comment
on column admin_building.gastw is 'Anzahl Geschosse';
comment
on column admin_building.ganzwhg is 'Anzahl Wohnungen';
comment
on column admin_building.gazzi is ' Anzahl separate Wohnraeume';
comment
on column admin_building.gschutzr is 'Zivilschutzraum';
comment
on column admin_building.gebf is 'Energiebezugsflaeche';
comment
on column admin_building.gwaerzh1 is 'Waermeerzeuger Heizung 1 ';
comment
on column admin_building.genh1 is 'Energie-/Waermequelle Heizung 1';
comment
on column admin_building.gwaersceh1 is 'Informationsquelle Heizung 1 ';
comment
on column admin_building.gwaerdath1 is 'Aktualisierungsdatum Heizung 1';
comment
on column admin_building.gwaerzh2 is 'Waermeerzeuger Heizung 2 ';
comment
on column admin_building.genh2 is 'Energie-/Waermequelle Heizung 2 ';
comment
on column admin_building.gwaersceh2 is 'Informationsquelle Heizung 2';
comment
on column admin_building.gwaerdath2 is 'Aktualisierungsdatum Heizung 2 ';
comment
on column admin_building.gwaerzw1 is 'Waermeerzeuger Warmwasser 1 ';
comment
on column admin_building.genw1 is 'Energie-/Waermequelle Warmwasser 1 ';
comment
on column admin_building.gwaerscew1 is 'Informationsquelle Warmwasser 1 ';
comment
on column admin_building.gwaerdatw1 is ' Aktualisierungsdatum Warmwasser 1 ';
comment
on column admin_building.gwaerzw2 is 'Waermeerzeuger Warmwasser 2 ';
comment
on column admin_building.genw2 is 'Energie-/Waermequelle Warmwasser 2';
comment
on column admin_building.gwaerscew2 is 'Informationsquelle Warmwasser 2 ';
comment
on column admin_building.gwaerdatw2 is 'Aktualisierungsdatum Warmwasser 2';
comment
on column admin_building.gexpdat is 'Datum des Exports ';


CREATE TABLE
    admin_apartment
(
    Load_DTS TIMESTAMP,
    EGID     varchar(128),
    EWID     varchar(128),
    EDID     varchar(128),
    WHGNR    varchar(128),
    WEINR    varchar(128),
    WSTWK    varchar(128),
    WBEZ     varchar(128),
    WMEHRG   varchar(128),
    WBAUJ    varchar(128),
    WABBJ    varchar(128),
    WSTAT    varchar(128),
    WAREA    varchar(128),
    WAZIM    varchar(128),
    WKCHE    varchar(128),
    WEXPDAT  varchar(128),
    CONSTRAINT fk_egid_apartment_building FOREIGN KEY (EGID) references admin_building (EGID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_wstat_apartment_status FOREIGN KEY (WSTAT) references admin_apartment_status (WSTAT) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_wstwk_apartment_stockwerk FOREIGN KEY (WSTWK) references admin_stockwerk (WSTWK) ON DELETE CASCADE ON UPDATE CASCADE

);

comment
on column admin_apartment.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column admin_apartment.ewid is 'Eidgenoessischer Wohnungsidentifikator';
comment
on column admin_apartment.edid is 'Eidgenoessischer Eingangsidentifikator';
comment
on column admin_apartment.whgnr is ' Administrative Wohnungsnummer';
comment
on column admin_apartment.weinr is 'Physische Wohnungsnummer';
comment
on column admin_apartment.wstwk is 'Stockwerk';
comment
on column admin_apartment.wbez is 'Lage auf dem Stockwerk';
comment
on column admin_apartment.wmehrg is 'Mehrgeschossige Wohnung';
comment
on column admin_apartment.wbauj is 'Baujahr Wohnung';
comment
on column admin_apartment.wabbj is 'Abbruchjahr Wohnung';
comment
on column admin_apartment.wstat is 'Wohnungsstatus ';
comment
on column admin_apartment.warea is 'Wohnungsflaeche';
comment
on column admin_apartment.wazim is 'Anzahl Zimmer';
comment
on column admin_apartment.wkche is 'Kocheinrichtung';
comment
on column admin_apartment.wexpdat is 'Datum des Exports';

CREATE TABLE
    admin_entry
(
    Load_DTS    TIMESTAMP,
    EGID        VARCHAR(128),
    EDID        VARCHAR(128),
    EGAID       VARCHAR(128),
    DEINR       VARCHAR(128),
    ESID        VARCHAR(128),
    STRNAME     VARCHAR(128),
    STRNAMK     VARCHAR(128),
    STRINDX     VARCHAR(128),
    STRSP       VARCHAR(128),
    STROFFIZIEL VARCHAR(128),
    DPLZ4       VARCHAR(128),
    DPLZZ       VARCHAR(128),
    DPLZNAME    VARCHAR(128),
    DKODE       VARCHAR(128),
    DKODN       VARCHAR(128),
    DOFFADR     VARCHAR(128),
    DEXPDAT     VARCHAR(128),
    CONSTRAINT fk_egid_entry_building FOREIGN KEY (EGID) references admin_building (EGID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_strsp_entry FOREIGN KEY (STRSP) references admin_strassen_sprache (STRSP) ON DELETE CASCADE ON UPDATE CASCADE
);


comment
on column admin_entry.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column admin_entry.edid is 'Eidgenoessischer Eingangsidentifikator';
comment
on column admin_entry.egaid is 'Eidgenoessischer Gebaeudeadressidentifikator';
comment
on column admin_entry.deinr is 'Eingangsnummer Gebaeude';
comment
on column admin_entry.esid is 'Eidgenoessischer Strassenidentifikator';
comment
on column admin_entry.strname is 'Strassenbezeichnung';
comment
on column admin_entry.strnamk is 'Strassenbezeichnung kurz ';
comment
on column admin_entry.strindx is 'Strassenbezeichnung Index';
comment
on column admin_entry.strsp is 'Strassenbezeichnung Sprache';
comment
on column admin_entry.stroffiziel is 'Strassenbezeichnung offiziell ';
comment
on column admin_entry.dplz4 is 'Postleitzahl';
comment
on column admin_entry.dplzz is 'PLZ-Zusatzziffer';
comment
on column admin_entry.dplzname is 'Ortsbezeichnung (Ortschaft)';
comment
on column admin_entry.dkode is ' E-Eingangskoordinate ';
comment
on column admin_entry.dkodn is 'N-Eingangskoordinate';
comment
on column admin_entry.doffadr is 'Offizielle Adresse';
comment
on column admin_entry.dexpdat is 'Datum des Exports';
