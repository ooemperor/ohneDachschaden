CREATE SCHEMA IF NOT EXISTS stage;

CREATE TABLE stage.admin_codes
(
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

comment
on column stage.admin_codes.CECODID is 'Code Identifikator';
comment
on column stage.admin_codes.CMERKM is 'Code Merkmal';
comment
on column stage.admin_codes.CODTXTLD is 'Code Text kurz Deutsch';
comment
on column stage.admin_codes.CODTXTKD is 'Code Text lang Deutsch';
comment
on column stage.admin_codes.CODTXTLF is 'Code Text kurz Franzoesisch';
comment
on column stage.admin_codes.CODTXTKF is 'Code Text lang Franzoesisch';
comment
on column stage.admin_codes.CODTXTLI is 'Code Text kurz Italienisch';
comment
on column stage.admin_codes.CODTXTKI is 'Code Text lang Italienisch';
comment
on column stage.admin_codes.CEXPDAT is 'Datum des Exports';



CREATE TABLE
    stage.admin_building
(
    EGID       VARCHAR(128),
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
    GEXPDAT    VARCHAR(128)
);

comment
on column stage.admin_building.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column stage.admin_building.gdekt is 'Kantonskuerzel';
comment
on column stage.admin_building.ggdenr is 'BFS-Gemeindenummer';
comment
on column stage.admin_building.ggdename is 'Gemeindename';
comment
on column stage.admin_building.egrid is 'Eidgenoessischer Grundstuecksidentifikator';
comment
on column stage.admin_building.lgbkr is 'Grundbuchkreisnummer';
comment
on column stage.admin_building.lparz is 'Grundstuecksnummer';
comment
on column stage.admin_building.lparzsx is 'Suffix der Grundstuecksnummer';
comment
on column stage.admin_building.ltyp is 'Typ des Grundstuecks';
comment
on column stage.admin_building.gebnr is 'Amtliche Gebaeudenummer';
comment
on column stage.admin_building.gbez is 'Name des Gebaeudes';
comment
on column stage.admin_building.gkode is 'E-Gebaeudekoordinate';
comment
on column stage.admin_building.gkodn is 'N-Gebaeudekoordinate';
comment
on column stage.admin_building.gksce is 'Koordinatenherkunft';
comment
on column stage.admin_building.gstat is 'Gebaeudestatus';
comment
on column stage.admin_building.gkat is 'Gebaeudekategorie';
comment
on column stage.admin_building.gklas is 'Gebaeudeklasse';
comment
on column stage.admin_building.gbauj is 'Baujahr des Gebaeudes YYYY';
comment
on column stage.admin_building.gbaum is 'Baumonat des Gebaeudes MM ';
comment
on column stage.admin_building.gbaup is 'Bauperiode';
comment
on column stage.admin_building.gabbj is 'Abbruchjahr des Gebaeudes';
comment
on column stage.admin_building.garea is 'Gebaeudeflaeche';
comment
on column stage.admin_building.gvol is 'Gebaeudevolumen';
comment
on column stage.admin_building.gvolnorm is 'Gebaeudevolumen: Norm';
comment
on column stage.admin_building.gvolsce is 'Informationsquelle zum Gebaeudevolumen';
comment
on column stage.admin_building.gastw is 'Anzahl Geschosse';
comment
on column stage.admin_building.ganzwhg is 'Anzahl Wohnungen';
comment
on column stage.admin_building.gazzi is ' Anzahl separate Wohnraeume';
comment
on column stage.admin_building.gschutzr is 'Zivilschutzraum';
comment
on column stage.admin_building.gebf is 'Energiebezugsflaeche';
comment
on column stage.admin_building.gwaerzh1 is 'Waermeerzeuger Heizung 1 ';
comment
on column stage.admin_building.genh1 is 'Energie-/Waermequelle Heizung 1';
comment
on column stage.admin_building.gwaersceh1 is 'Informationsquelle Heizung 1 ';
comment
on column stage.admin_building.gwaerdath1 is 'Aktualisierungsdatum Heizung 1';
comment
on column stage.admin_building.gwaerzh2 is 'Waermeerzeuger Heizung 2 ';
comment
on column stage.admin_building.genh2 is 'Energie-/Waermequelle Heizung 2 ';
comment
on column stage.admin_building.gwaersceh2 is 'Informationsquelle Heizung 2';
comment
on column stage.admin_building.gwaerdath2 is 'Aktualisierungsdatum Heizung 2 ';
comment
on column stage.admin_building.gwaerzw1 is 'Waermeerzeuger Warmwasser 1 ';
comment
on column stage.admin_building.genw1 is 'Energie-/Waermequelle Warmwasser 1 ';
comment
on column stage.admin_building.gwaerscew1 is 'Informationsquelle Warmwasser 1 ';
comment
on column stage.admin_building.gwaerdatw1 is ' Aktualisierungsdatum Warmwasser 1 ';
comment
on column stage.admin_building.gwaerzw2 is 'Waermeerzeuger Warmwasser 2 ';
comment
on column stage.admin_building.genw2 is 'Energie-/Waermequelle Warmwasser 2';
comment
on column stage.admin_building.gwaerscew2 is 'Informationsquelle Warmwasser 2 ';
comment
on column stage.admin_building.gwaerdatw2 is 'Aktualisierungsdatum Warmwasser 2';
comment
on column stage.admin_building.gexpdat is 'Datum des Exports ';

CREATE TABLE
    stage.admin_apartment
(
    EGID    varchar(128),
    EWID    varchar(128),
    EDID    varchar(128),
    WHGNR   varchar(128),
    WEINR   varchar(128),
    WSTWK   varchar(128),
    WBEZ    varchar(128),
    WMEHRG  varchar(128),
    WBAUJ   varchar(128),
    WABBJ   varchar(128),
    WSTAT   varchar(128),
    WAREA   varchar(128),
    WAZIM   varchar(128),
    WKCHE   varchar(128),
    WEXPDAT varchar(128)
);

comment
on column stage.admin_apartment.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column stage.admin_apartment.ewid is 'Eidgenoessischer Wohnungsidentifikator';
comment
on column stage.admin_apartment.edid is 'Eidgenoessischer Eingangsidentifikator';
comment
on column stage.admin_apartment.whgnr is ' Administrative Wohnungsnummer';
comment
on column stage.admin_apartment.weinr is 'Physische Wohnungsnummer';
comment
on column stage.admin_apartment.wstwk is 'Stockwerk';
comment
on column stage.admin_apartment.wbez is 'Lage auf dem Stockwerk';
comment
on column stage.admin_apartment.wmehrg is 'Mehrgeschossige Wohnung';
comment
on column stage.admin_apartment.wbauj is 'Baujahr Wohnung';
comment
on column stage.admin_apartment.wabbj is 'Abbruchjahr Wohnung';
comment
on column stage.admin_apartment.wstat is 'Wohnungsstatus ';
comment
on column stage.admin_apartment.warea is 'Wohnungsflaeche';
comment
on column stage.admin_apartment.wazim is 'Anzahl Zimmer';
comment
on column stage.admin_apartment.wkche is 'Kocheinrichtung';
comment
on column stage.admin_apartment.wexpdat is 'Datum des Exports';


CREATE TABLE
    stage.admin_entry
(
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
    DEXPDAT     VARCHAR(128)
);



comment
on column stage.admin_entry.egid is 'Eidgenoessischer Gebaeudeidentifikator';
comment
on column stage.admin_entry.edid is 'Eidgenoessischer Eingangsidentifikator';
comment
on column stage.admin_entry.egaid is 'Eidgenoessischer Gebaeudeadressidentifikator';
comment
on column stage.admin_entry.deinr is 'Eingangsnummer Gebaeude';
comment
on column stage.admin_entry.esid is 'Eidgenoessischer Strassenidentifikator';
comment
on column stage.admin_entry.strname is 'Strassenbezeichnung';
comment
on column stage.admin_entry.strnamk is 'Strassenbezeichnung kurz ';
comment
on column stage.admin_entry.strindx is 'Strassenbezeichnung Index';
comment
on column stage.admin_entry.strsp is 'Strassenbezeichnung Sprache';
comment
on column stage.admin_entry.stroffiziel is 'Strassenbezeichnung offiziell ';
comment
on column stage.admin_entry.dplz4 is 'Postleitzahl';
comment
on column stage.admin_entry.dplzz is 'PLZ-Zusatzziffer';
comment
on column stage.admin_entry.dplzname is 'Ortsbezeichnung (Ortschaft)';
comment
on column stage.admin_entry.dkode is ' E-Eingangskoordinate ';
comment
on column stage.admin_entry.dkodn is 'N-Eingangskoordinate';
comment
on column stage.admin_entry.doffadr is 'Offizielle Adresse';
comment
on column stage.admin_entry.dexpdat is 'Datum des Exports';


CREATE TABLE
    stage.gvb_dangers
(
    OBJECTID                    BIGINT,
    GWR_EGID                    BIGINT,
    BEGIG                       BIGINT,
    OBERFLAECHENABFLUSS         VARCHAR(128),
    HOCHWASSER_SEEN             VARCHAR(128),
    HOCHWASSER_FLIESSGEWAESSER  VARCHAR(128),
    HAGEL                       BIGINT,
    STURM                       float,
    HAUSNUMMER                  VARCHAR(128),
    STRNAME                     VARCHAR(128),
    PLZ                         VARCHAR(128),
    ORTSCHAFT                   VARCHAR(128),
    ADRESSE                     VARCHAR(128),
    ADRESSE_POPUP               VARCHAR(128),
    OBERFLAECHENABFLUSS_TEXT_DE VARCHAR(128),
    OBERFLAECHENABFLUSS_TEXT_FR VARCHAR(128),
    FLIESSGEWAESSER_TEXT_DE     VARCHAR(128),
    FLIESSGEWAESSER_TEXT_FR     VARCHAR(128),
    SEEN_TEXT_DE                VARCHAR(128),
    SEEN_TEXT_FR                VARCHAR(128),
    HAGEL_TEXT                  VARCHAR(128),
    STURM_TEXT                  VARCHAR(128)
);