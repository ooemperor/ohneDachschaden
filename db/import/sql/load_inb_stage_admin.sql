TRUNCATE TABLE stage.admin_entry;
TRUNCATE TABLE stage.admin_apartment;
TRUNCATE TABLE stage.admin_building;
TRUNCATE TABLE stage.admin_codes;


\copy stage.admin_apartment FROM './data/ch/wohnung_logement_abitazione.csv' WITH DELIMITER E'\t' NULL AS 'null' CSV HEADER;
\copy stage.admin_building FROM './data/ch/gebaeude_batiment_edificio.csv' WITH DELIMITER E'\t' NULL AS 'null' CSV HEADER;
\copy stage.admin_entry FROM './data/ch/eingang_entree_entrata.csv' WITH DELIMITER E'\t' NULL AS 'null' CSV HEADER;
\copy stage.admin_codes FROM './data/ch/kodes_codes_codici.csv' WITH DELIMITER E'\t' NULL AS 'null' CSV HEADER;

UPDATE stage.admin_building SET gklas = NULL where gklas = '';
UPDATE stage.admin_building SET genh1 = NULL where genh1 = '';
UPDATE stage.admin_building SET genh2 = NULL where genh2 = '';
UPDATE stage.admin_building SET gwaerzw1 = NULL where gwaerzw1 = '';
UPDATE stage.admin_building SET gwaerzw2 = NULL where gwaerzw2 = '';

UPDATE stage.admin_entry SET strsp = NULL where strsp = '';
