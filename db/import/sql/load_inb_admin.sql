TRUNCATE TABLE public.admin_entry CASCADE;
TRUNCATE TABLE public.admin_apartment CASCADE;
TRUNCATE TABLE public.admin_apartment_status CASCADE;
TRUNCATE TABLE public.admin_building CASCADE;
TRUNCATE TABLE public.admin_codes CASCADE;
TRUNCATE TABLE public.admin_stockwerk CASCADE;
TRUNCATE TABLE public.admin_buildingklasse CASCADE;
TRUNCATE TABLE public.admin_heizung1 CASCADE;
TRUNCATE TABLE public.admin_heizung2 CASCADE;
TRUNCATE TABLE public.admin_Wasser1 CASCADE;
TRUNCATE TABLE public.admin_Wasser2 CASCADE;
TRUNCATE TABLE public.admin_BuildingStatus CASCADE;

INSERT INTO public.admin_codes
SELECT NOW() AS Load_DTS,
       *
FROM stage.admin_codes;

INSERT INTO public.admin_apartment_status
SELECT admin_codes.cecodid  AS WSTAT,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'WSTAT';

INSERT INTO public.admin_strassen_sprache
SELECT admin_codes.cecodid  AS STRSP,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'STRSP';

INSERT INTO public.admin_stockwerk
SELECT admin_codes.cecodid  AS WSTWK,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'WSTWK';

INSERT INTO public.admin_buildingklasse
SELECT admin_codes.cecodid  AS GKLAS,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'GKLAS';

INSERT INTO public.admin_heizung1
SELECT admin_codes.cecodid  AS GENH1,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'GENH1';

INSERT INTO public.admin_heizung2
SELECT admin_codes.cecodid  AS GENH2,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'GENH2';

INSERT INTO public.admin_Wasser1
SELECT admin_codes.cecodid  AS GWAERZW1,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'GWAERZW1';

INSERT INTO public.admin_Wasser2
SELECT admin_codes.cecodid  AS GWAERZW2,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long

FROM public.admin_codes
WHERE cmerkm = 'GWAERZW2';

INSERT INTO public.admin_BuildingStatus
SELECT admin_codes.cecodid  AS GSTAT,
       admin_codes.codtxtkd AS description,
       admin_codes.codtxtld as description_long
FROM public.admin_codes
WHERE cmerkm = 'GSTAT';


INSERT INTO public.admin_building
SELECT NOW() AS Load_DTS,
       *
FROM stage.admin_building;

INSERT INTO public.admin_apartment
SELECT NOW() AS Load_DTS,
       *
FROM stage.admin_apartment;

INSERT INTO public.admin_entry
SELECT NOW() AS Load_DTS,
       *
FROM stage.admin_entry;
